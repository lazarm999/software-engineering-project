import os
import requests
import json

from django.http.response import HttpResponse
from django.shortcuts import get_object_or_404

from api.models import Notification, User, UserFCM, UserNotification
from api.responses import r404


class ProfilePictureLogic:
    imgsDir = os.environ.get('IMGS_DIR')

    @staticmethod
    def retrieveProfilePicture(userId, isQb=False):
        if isQb:
            user = get_object_or_404(User, userQbId=userId)
        else:
            user = get_object_or_404(User, pk=userId)

        imgName = user.imageName
        if imgName:
            imgPath = os.path.join(ProfilePictureLogic.imgsDir, imgName)
        else:
            return r404()
        with open(imgPath, 'rb') as f:
            if f:
                return HttpResponse(f.read(), content_type='image/png')
            else:
                return r404() 


class EarnedBadgeLogic:
    pass


class CommentLogic:

    def extractTaggedUsers(comment, returnUsernames=True):
        ind = 0
        usernames = []
        indices = []
        while ind != -1:
            ind = comment.find('@', ind)
            if ind == 0 or comment[ind-1] == ' ':
                end = comment.find(' ', ind)
                username = comment[ind+1:end] if end != -1 else comment[ind+1:]
                indices.append(ind)
                ind = end
                usernames.append(username)
        return usernames if returnUsernames else indices


class NotificationLogic:

    def sendPushNotifications(data, fcm_tokens):
        headers = {
            'Content-Type': 'application/json',
            'Authorization': os.environ.get('FCM_KEY')
        }

        data = {
            'data': data,
            'registration_ids': fcm_tokens
        }

        requests.post('https://fcm.googleapis.com/fcm/send', headers=headers, json=data)

    
    def sendChosenNotifications(ad, userIds):
        acceptedNotification = Notification()
        acceptedNotification.ad = ad
        acceptedNotification.accepted = True

        declinedNotification = Notification()
        declinedNotification.ad = ad
        declinedNotification.accepted = False

        appliedUsers = User.objects.filter(applies__ad__adId=ad.adId)
        acceptedUsers = User.objects.filter(userId__in=userIds)
        declinedUsers = list(set(appliedUsers) - set(acceptedUsers))

        declinedUsersFcm = [u.fcmToken for u in UserFCM.objects.filter(user__in=declinedUsers)]
        acceptedUsersFcm = [u.fcmToken for u in UserFCM.objects.filter(user__in=acceptedUsers)]

        userNotifs = []
        for user in appliedUsers:
            userNotif = UserNotification()
            userNotif.user = user
            if user in acceptedUsers:
                userNotif.notification = acceptedNotification
            else:
                userNotif.notification = declinedNotification

            userNotifs.append(userNotif)

        acceptedNotification.save()
        declinedNotification.save()
        for notif in userNotifs:
            notif.save()
        NotificationLogic.sendPushNotifications({'type': 'accepted', 'adId': ad.adId, 'adTitle': ad.title}, acceptedUsersFcm)
        NotificationLogic.sendPushNotifications({'type': 'declined', 'adId': ad.adId, 'adTitle': ad.title}, declinedUsersFcm)

    def sendCommentNotification(comment):
        commentOwnerNotification = Notification()
        commentOwnerNotification.comment = comment
        commentOwnerNotification.tagged = False

        usernames = CommentLogic.extractTaggedUsers(comment.comment)

        taggedUsers = User.objects.filter(username__in=usernames)
        taggedUserFcm = [u.fcmToken for u in UserFCM.objects.filter(user__username__in=usernames)]
        adOwnerFcm = [u.fcmToken for u in UserFCM.objects.filter(user__userId=comment.ad.employer.userId)]

        taggedNotification = None
        if len(taggedUsers) > 0:
            taggedNotification = Notification()
            taggedNotification.comment = comment
            taggedNotification.tagged = True

        userNotifs = []
        ownerNotif = UserNotification()
        ownerNotif.user = comment.ad.employer
        ownerNotif.notification = commentOwnerNotification
        for user in taggedUsers:
            userNotif = UserNotification()
            userNotif.user = user
            userNotif.notification = taggedNotification
            userNotifs.append(userNotif)

        commentOwnerNotification.save()
        if taggedNotification:
            taggedNotification.save()
        ownerNotif.save()
        for notif in userNotifs:
            notif.save()
        data = {
            'adId': comment.ad.adId,
            'adTitle': comment.ad.title,
            'comment': comment.comment,
            'username': comment.user.username,
            'type': 'adComment'
        }
        NotificationLogic.sendPushNotifications(data, adOwnerFcm)
        data['type'] = 'tagged'
        NotificationLogic.sendPushNotifications(data, taggedUserFcm)

    def sendRatingNotification(rating):
        ratingNotification = Notification()
        ratingNotification.rating = rating

        notif = UserNotification()
        notif.notification = ratingNotification
        notif.user = rating.ratee

        userFcms = [u.fcmToken for u in UserFCM.objects.filter(user__username=rating.ratee.username)]

        ratingNotification.save()
        notif.save()

        data = {
            'rater': rating.rater.username,
            'rating': rating.rating,
            'type': 'rating'
        }
        NotificationLogic.sendPushNotifications(data, userFcms)

    def sendChatNotification(sender, message, userQbIds):
        userFcms = [u.fcmToken for u in UserFCM.objects.filter(user__userQbId__in=userQbIds)]
        
        NotificationLogic.sendPushNotifications({
            'type': 'chat',
            'sender': sender,
            'message': message
        }, userFcms)

