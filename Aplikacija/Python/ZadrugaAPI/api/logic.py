from enum import Enum
import os
import requests
from datetime import datetime, timedelta

from django.http.response import HttpResponse
from django.shortcuts import get_object_or_404

from api.models import Ad, Applied, Badge, Earned, Notification, User, UserFCM, UserNotification
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


class Badges:
    FINISHED10 = 1
    FINISHED20 = 2
    FINISHED50 = 3
    FIRST_WEEK_JOB = 4
    MULTI_CATEGORY = 5
    CLOSED10 = 6
    CLOSED20 = 7
    CLOSED50 = 8
    FIRST_10DAYS_5EMPLOYEES = 9
    POPULAR_AD = 10

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

    def sendChatNotification(username, adId, message, userQbIds):
        userFcms = [u.fcmToken for u in UserFCM.objects.filter(user__userQbId__in=userQbIds)]
        
        try:
            adTitle = Ad.objects.get(pk=adId).title
        except Ad.DoesNotExist:
            adTitle = None

        data = {
            'type': 'chat',
            'username': username,
            'message': message,
            'adId': adId,
            'adTitle': adTitle
        }
        NotificationLogic.sendPushNotifications(data, userFcms)

    
    def sendNewBadgeNotification(user, badgeId):
        userFcms = [u.fcmToken for u in UserFCM.objects.filter(user=user)]
        data = {
            'type': 'badge',
            'badgeId': badgeId
        }
        NotificationLogic.sendPushNotifications(data, userFcms)


class EarnedBadgeLogic:
    
    def saveBadge(user, badgeId):
        newBadge = Earned()
        newBadge.user = user
        newBadge.badge = Badge.objects.get(pk=badgeId)
        newBadge.save()

        NotificationLogic.sendNewBadgeNotification(user, badgeId)

    def onAdClose(ad, acceptedUsers):
        employer = ad.employer

        closed_ads = Ad.objects.filter(isClosed=True, employer=employer)
        if len(closed_ads) == 10:
            EarnedBadgeLogic.saveBadge(employer, Badges.CLOSED10)
        if len(closed_ads) == 20:
            EarnedBadgeLogic.saveBadge(employer, Badges.CLOSED20)
        if len(closed_ads) == 50:
            EarnedBadgeLogic.saveBadge(employer, Badges.CLOSED50)

        if datetime.utcnow()-employer.registrationDate.replace(tzinfo=None) < timedelta(days=10):
            chosen_employees = Applied.objects.filter(chosen=True, ad__employer=employer)
            if len(chosen_employees) > 5:
                exists = len(Earned.objects.filter(user=employer, badge__badgeId=Badges.FIRST_10DAYS_5EMPLOYEES)) > 0
                if not exists:
                    EarnedBadgeLogic.saveBadge(employer, Badges.FIRST_10DAYS_5EMPLOYEES)

        appliedUsers = Applied.objects.filter(ad=ad)
        if len(appliedUsers) >= 5*ad.numberOfEmployees:
            exists = len(Earned.objects.filter(user=employer, badge__badgeId=Badges.POPULAR_AD)) > 0
            if not exists:
                EarnedBadgeLogic.saveBadge(employer, Badges.POPULAR_AD)

        for employee in acceptedUsers:
            jobs = Ad.objects.filter(isClosed=True, applicants__user=employee)
            if len(jobs) == 10:
                EarnedBadgeLogic.saveBadge(employee, Badges.FINISHED10)
            if len(jobs) == 20:
                EarnedBadgeLogic.saveBadge(employee, Badges.FINISHED20)
            if len(jobs) == 50:
                EarnedBadgeLogic.saveBadge(employee, Badges.FINISHED50)
            if len(jobs) == 1 and datetime.utcnow()-employee.registrationDate.replace(tzinfo=None) < timedelta(weeks=1):
                EarnedBadgeLogic.saveBadge(employee, Badges.FIRST_WEEK_JOB)

            distinctTags = set()
            for job in jobs:
                for tag in job.tags:
                    distinctTags.add(tag.tagId)
            if len(distinctTags) >= 4:
                exists = len(Earned.objects.filter(user=employee, badge__badgeId=Badges.MULTI_CATEGORY)) > 0
                if not exists:
                    EarnedBadgeLogic.saveBadge(employee, Badges.MULTI_CATEGORY)
