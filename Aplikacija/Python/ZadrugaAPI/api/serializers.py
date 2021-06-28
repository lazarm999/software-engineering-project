from api.logic import CommentLogic
from rest_framework import serializers
from rest_framework.relations import PrimaryKeyRelatedField

from api.models import *


class UniversitySerializer(serializers.ModelSerializer):
    class Meta:
        model = University
        fields = ['universityId', 'name']


class FacultySerializer(serializers.ModelSerializer):
    university = UniversitySerializer()

    class Meta:
        model = Faculty
        fields = ['facultyId', 'name', 'university']


class TagSerializer(serializers.ModelSerializer):
    class Meta:
        model = Tag
        fields = ['tagId', 'name']


class LocationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Location
        fields = ['locId', 'cityName', 'latitude', 'longitude']


class BadgeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Badge
        fields = ['badgeId', 'description']


class EarnedSerializer(serializers.ModelSerializer):
    user = PrimaryKeyRelatedField(queryset=User.objects.filter(isAdmin=False))
    badge = BadgeSerializer()

    class Meta:
        model = Earned
        fields = ['user', 'badge']


class UserSerializer(serializers.ModelSerializer):
    faculty = FacultySerializer(required=False)
    banAdmin = PrimaryKeyRelatedField(required=False, queryset=User.objects.filter(isAdmin=True))
    badges = BadgeSerializer(required=False, many=True)

    class Meta:
        model = User
        fields = ['userId', 'firstName', 'lastName', 'email', 'username', 'password', 'bio', 'imageName',
            'phoneNumber', 'isAdmin', 'isEmployer', 'companyName', 'faculty', 'banAdmin', 'banExplanation',
            'badges', 'userQbId']
        extra_kwargs = {'password' : {'write_only' : True}}


class RatingSerializer(serializers.ModelSerializer):
    rater = UserSerializer()
    ratee = PrimaryKeyRelatedField(queryset=User.objects.filter(isAdmin=False))

    class Meta:
        model = Rating
        fields = ['id', 'rater', 'ratee', 'rating', 'comment', 'postTime']


class RelatedToSerializer(serializers.ModelSerializer):
    ad = PrimaryKeyRelatedField(queryset=Ad.objects.all())
    tag = TagSerializer()

    class Meta:
        model = RelatedTo
        fields = ['ad', 'tag']


class AdSerializer(serializers.ModelSerializer):
    employer = UserSerializer()
    location = LocationSerializer()
    tags = TagSerializer(many=True)
    numberOfApplied = serializers.ReadOnlyField()

    class Meta:
        model = Ad
        fields = ['adId', 'title', 'description', 'numberOfEmployees', 'compensationMin', 
            'compensationMax', 'employer', 'location', 'postTime', 'tags', 'numberOfApplied',
            'qbChatId', 'isClosed']


class CommentSerializer(serializers.ModelSerializer):
    user = UserSerializer()
    ad = PrimaryKeyRelatedField(queryset=Ad.objects.all())
    taggedIndices = serializers.ReadOnlyField()

    class Meta:
        model = Comment
        fields = ['id', 'user', 'ad', 'comment', 'postTime', 'taggedIndices']


class AppliedSerializer(serializers.ModelSerializer):
    user = UserSerializer()
    ad = PrimaryKeyRelatedField(queryset=Ad.objects.all())

    class Meta:
        model = Applied
        fields = ['user', 'ad', 'chosen']


class UserFCMSerializer(serializers.ModelSerializer):
    user = UserSerializer()

    class Meta:
        model = UserFCM
        fields = ['user', 'fcmToken']


class NotificationSerializer(serializers.ModelSerializer):
    ad = AdSerializer()
    comment = CommentSerializer()
    rating = RatingSerializer()

    class Meta:
        model = Notification
        fields = ['notificationId', 'ad', 'accepted', 'comment', 'rating', 'tagged']