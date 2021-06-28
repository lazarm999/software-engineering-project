from api.models import User
from django.core.exceptions import PermissionDenied
from rest_framework import permissions
import os
import jwt

class IsLoggedIn(permissions.BasePermission):
    def has_permission(self, request, view):
        token = request.headers.get('Authorization')
        if not token:
            return False
        try:
            obj = jwt.decode(token, os.environ.get('SECRET_KEY'), algorithms=["HS256"])
            request._auth = obj['id']
            return User.objects.get(pk=obj['id']).banAdmin is None
        except:
            return False

class IsOwner(permissions.BasePermission):
    def has_permission(self, request, view):
        if request.method in permissions.SAFE_METHODS:
            return True
        pk = view.kwargs.get('pk')
        if pk is not None and pk == request._auth:
            return True
        raise PermissionDenied


class IsAdmin(permissions.BasePermission):
    def has_permission(self, request, view):
        return User.objects.get(pk=request._auth).isAdmin