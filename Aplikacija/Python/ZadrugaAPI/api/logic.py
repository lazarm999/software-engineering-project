import os

from django.http.response import HttpResponse
from django.shortcuts import get_object_or_404

from api.models import User
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


class NotificationLogic:
    pass