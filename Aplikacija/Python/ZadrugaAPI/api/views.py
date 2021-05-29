import os
import uuid

import jwt
from PIL import Image

from django.http import HttpResponse
from django.shortcuts import get_object_or_404
from django.contrib.auth.hashers import make_password, check_password
from django.db import transaction

from rest_framework import generics, mixins
from rest_framework.views import APIView
from rest_framework.parsers import FileUploadParser
from rest_framework.response import Response

from api.models import *
from api.serializers import *
from api.permissions import *
from api.responses import *

# Create your views here.
class Register(generics.GenericAPIView, mixins.CreateModelMixin):
    queryset = User.objects.all()
    serializer_class = UserSerializer

    def post(self, request, *args, **kwargs):
        password = request.data.get('password')
        print(password)
        if not (password and len(password) >= 8):
            return r400('Password must be at least 8 characters long')
        return self.create(request, *args, **kwargs)

    def perform_create(self, serializer):
        inst = serializer.save()
        inst.password = make_password(inst.password)
        inst.save()


class Login(APIView):
    def post(self, request):
        email = request.data.get('email')
        password = request.data.get('password')

        user = get_object_or_404(User, email=email)
        try:
            if check_password(password, user.password) and user.banAdmin is None:
                return Response({'id': user.userId, 
                    'token': jwt.encode({'id' : user.userId}, 
                    os.environ.get('SECRET_KEY'), algorithm="HS256")})
            else:
                return r401('Unauthorized')
        except Exception as e:
            return r500(e)


class UserDetail(generics.GenericAPIView, mixins.UpdateModelMixin, mixins.DestroyModelMixin, mixins.RetrieveModelMixin):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [IsLoggedIn, IsOwner]

    def get(self, request, pk, *args, **kwargs):
        return self.retrieve(request, args, kwargs)

    def patch(self, request, pk, *args, **kwargs):
        user = get_object_or_404(User, pk=pk)
        firstName = request.data.get('firstName')
        lastName = request.data.get('lastName')
        bio = request.data.get('bio')
        phoneNumber = request.data.get('phoneNumber')
        companyName = request.data.get('companyName')
        facultyId = request.data.get('facultyId')

        if facultyId:
            if user.isEmployer:
                return r400('Cannot set faculty for employer')
            user.faculty = get_object_or_404(Faculty, pk=facultyId)
        if companyName:
            if not user.isEmployer:
                return r400('Cannot set company for employee')
            user.companyName = companyName
        user.firstName = firstName or user.firstName
        user.lastName = lastName or user.lastName
        user.bio = bio or user.bio
        user.phoneNumber = phoneNumber or user.phoneNumber
        try:
            user.save()
        except:
            return r500('Failed saving user')
        return r204()

    def delete(self, request, *args, **kwargs):
        return self.destroy(request, *args, **kwargs)


class PasswordChange(APIView):
    permission_classes = [IsLoggedIn]

    def put(self, request):
        oldPwd = request.data.get('oldPassword')
        newPwd = request.data.get('newPassword')

        user = get_object_or_404(User, pk=request._auth)
        if not (newPwd and len(newPwd) >= 8):
            return r401('Password must be at least 8 characters long')
        if oldPwd and check_password(oldPwd, user.password):
            user.password = make_password(newPwd)
            user.save()
        else:
            return r401('Wrong password')
        return r204()
    

class ProfilePicture(APIView):
    parser_classes = [FileUploadParser]
    imgsDir = os.environ.get('IMGS_DIR')
    permission_classes = [IsLoggedIn, IsOwner]

    def get(self, request, pk):
        imgName = get_object_or_404(User, pk=pk).imageName
        if imgName:
            imgPath = os.path.join(ProfilePicture.imgsDir, imgName)
        else:
            return r404()
        with open(imgPath, 'rb') as f:
            if f:
                return HttpResponse(f.read(), content_type='image/png')
            else:
                return r404()

    def post(self, request, pk):
        '''
            Requires that you set the following headers:
            Content-Type: image/png (or the corresponding image format)
            Content-Disposition: attachment; filename=<filename> 
        '''
        user = get_object_or_404(User, pk=pk)
        user.imageName = str(uuid.uuid4())+'.png'
        image = Image.open(request.data.get('file'))
        # TODO: png + format
        try:
            image.save(os.path.join(ProfilePicture.imgsDir, user.imageName))
            user.save()
        except:
            return r500('Failed saving profile picture')
        return r201()


class BanUser(APIView):
    permission_classes = [IsLoggedIn, IsAdmin]

    def post(self, request, pk):
        user = get_object_or_404(User, pk=pk)
        user.banAdmin = User.objects.get(pk=request._auth)
        banExplanation = request.data.get('banExplanation')
        if banExplanation:
            user.banExplanation = banExplanation
        try:
            user.save()
        except:
            return r500('Failed banning user')
        return r204()


class UnbanUser(APIView):
    permission_classes = [IsLoggedIn, IsAdmin]

    def post(self, request, pk):
        user = get_object_or_404(User, pk=pk)
        user.banAdmin = None
        user.banExplanation = None
        try:
            user.save()
        except:
            return r500('Failed unbanning user')
        return r204()


class RateUser(APIView):
    permission_classes = [IsLoggedIn]
    
    def post(self, request, pk, *args, **kwargs):
        raterId = request._auth
        rateeId = pk
        rating = request.data.get('rating')
        comment = request.data.get('comment')

        rater = get_object_or_404(User, pk=raterId)
        ratee = get_object_or_404(User, pk=rateeId)
        if not (rater.isEmployer != ratee.isEmployer and isinstance(rating, int) \
            and rating > 0 and rating < 6):
            return r400('Invalid parameters')
        r = Rating()
        r.rater = rater
        r.ratee = ratee
        r.rating = rating
        r.comment = comment
        try:
            r.save()
        except:
            return r500('Failed saving rating')
        serialized = RatingSerializer(r)
        return r201(serialized.data)

    def delete(self, request, pk, *args, **kwargs):
        raterId = request._auth
        rateeId = pk

        rating = get_object_or_404(Rating, rater_id=raterId, ratee_id=rateeId)
        try:
            rating.delete()
        except:
            return r500('Failed deleting rating')
        return r204()


    def put(self, request, pk, *args, **kwargs):
        raterId = request._auth
        rateeId = pk
        rating = request.data.get('rating')
        comment = request.data.get('comment')

        ratingObj = get_object_or_404(Rating, rater_id=raterId, ratee_id=rateeId)
        if not (isinstance(rating, int) and rating > 0 and rating < 6):
            return r400('Invalid parameters')
        ratingObj.rating = rating
        ratingObj.comment = comment
        try:
            ratingObj.save()
        except:
            return r500('Failed saving rating')
        return r204()


class UserRatings(APIView):
    permission_classes = [IsLoggedIn]

    def get(self, request, pk, *args, **kwargs):
        ratingList = Rating.objects.filter(ratee_id=pk) \
            .order_by('-postTime')
        serialized = RatingSerializer(ratingList, many=True)
        return Response(serialized.data)


class AdList(generics.ListCreateAPIView):
    queryset = Ad.objects.all()
    serializer_class = AdSerializer
    permission_classes = [IsLoggedIn]

    def post(self, request, *args, **kwargs):
        title = request.data.get('title')
        description = request.data.get('description')
        numberOfEmployees = request.data.get('numberOfEmployees')
        compensationMin = request.data.get('compensationMin')
        compensationMax = request.data.get('compensationMax')
        locationId = request.data.get('locationId')
        employerId = request._auth

        if not (title and numberOfEmployees and compensationMin and \
            compensationMax and locationId):
            return r400('Please provide needed fields: title, numberOfEmployees \
                compensationMin, compensationMax and locationId')

        if not (compensationMin > 0 and compensationMax > 0 and numberOfEmployees > 0):
            return r400('Compensations and number of employees must be natural numbers')

        if compensationMin > compensationMax:
            return r400('compensationMin must be less or equal to compensationMax')
        
        ad = Ad()
        ad.title = title
        ad.description = description
        ad.numberOfEmployees = numberOfEmployees
        ad.compensationMin = compensationMin
        ad.compensationMax = compensationMax
        ad.employer = get_object_or_404(User, pk=employerId)
        ad.location = get_object_or_404(Location, pk=locationId)
        try:
            ad.save()
        except:
            return r500('Failed saving ad')

        tags = request.data.get('tags')
        if tags:
            for t in tags:
                rt = RelatedTo()
                rt.ad = ad
                tag = Tag.objects.filter(pk=t).first()
                if not tag:
                    print(f"Tag {t} not found")
                    continue
                rt.tag = tag
                try:
                    rt.save()
                except:
                    continue
        serializer = AdSerializer(ad)
        return r201(serializer.data)

    def get(self, request, *args, **kwargs):
        ads = Ad.objects.all().order_by('-postTime')
        serialized = AdSerializer(ads, many=True)
        return Response(serialized.data)


class AdDetail(generics.GenericAPIView, mixins.RetrieveModelMixin, mixins.UpdateModelMixin):
    permission_classes = [IsLoggedIn]
    queryset = Ad.objects.all()
    serializer_class = AdSerializer

    def get(self, request, *args, **kwargs):
        return self.retrieve(request, args, kwargs)

    def delete(self, request, pk, *args, **kwargs):
        ad = get_object_or_404(Ad, pk=pk)
        if ad.employer.userId != request._auth:
            return r401('Unauthorized')
        try:
            ad.delete()
        except:
            return r500('Failed deleting ad')
        return r204()

    def patch(self, request, pk, *args, **kwargs):
        ad = get_object_or_404(Ad, pk=pk)
        if ad.employer.userId != request._auth:
            return r401('Unauthorized')
        title = request.data.get('title', ad.title)
        desc = request.data.get('description', ad.description)
        numberOfEmployees = request.data.get('numberOfEmployees', ad.numberOfEmployees)
        compMin = request.data.get('compensationMin', ad.compensationMin)
        compMax = request.data.get('compensationMax', ad.compensationMax)
        addTagIds = request.data.get('addTags')
        removeTagIds = request.data.get('removeTags')

        removeTags = None
        if removeTagIds:
            for t in removeTagIds:
                removeTags = RelatedTo.objects.filter(ad_id=pk, tag_id__in=removeTagIds)

        addTags = []
        if addTagIds:
            for t in addTagIds:
                rt = RelatedTo()
                rt.ad_id = pk
                rt.tag_id = t
                addTags.append(rt)

        if not (compMin > 0 and compMax > 0 and numberOfEmployees > 0):
            return r400('Compensations and number of employees must be natural numbers')

        if compMin > compMax:
            return r400('compensationMin must be less or equal to compensationMax')

        locationId = request.data.get('locationId')
        if locationId:
            location = Location.objects.filter(pk=locationId).first()
            if location:
                ad.location = location

        ad.title = title
        ad.description = desc
        ad.numberOfEmployees = numberOfEmployees
        ad.compensationMin = compMin
        ad.compensationMax = compMax
        try:
            with transaction.atomic():
                ad.save()
                if removeTags:
                    removeTags.delete()
                if addTags:
                    for rt in addTags:
                        rt.save()
        except:
            return r500('Failed saving ad')          
        return r204()


class CommentList(APIView):
    permission_classes = [IsLoggedIn]

    def post(self, request, pk, *args, **kwargs):
        adId = pk
        comment = request.data.get('comment')
        if not (adId and comment and comment != ''):
            return r400('Please provide comment')
        c = Comment()
        c.user = get_object_or_404(User, pk=request._auth)
        c.ad = get_object_or_404(Ad, pk=adId)
        c.comment = comment
        try:
            c.save()
        except:
            return r500('Failed to save comment')
        serialized = CommentSerializer(c)
        return r201(serialized.data)

    def get(self, request, pk, *args, **kwargs):
        comments = Comment.objects.filter(ad_id=pk).order_by('postTime')
        serialized = CommentSerializer(comments, many=True)
        return Response(serialized.data)


class CommentDetail(APIView):
    permission_classes = [IsLoggedIn]

    def delete(self, request, pk, *args, **kwargs):
        comment = get_object_or_404(Comment, pk=pk)
        if comment.user.userId != request._auth:
            return r401('Unauthorized')
        try:
            comment.delete()
        except:
            return r500('Failed to delete comment')
        return r204()


class Apply(APIView):
    permission_classes = [IsLoggedIn]

    def get(self, request, pk, *args, **kwargs):
        data = Applied.objects.filter(ad__adId=pk)
        serialized = UserSerializer([d.user for d in data], many=True)
        return Response(serialized.data)

    def post(self, request, pk, *args, **kwargs):
        user = get_object_or_404(User, pk=request._auth)
        ad = get_object_or_404(Ad, pk=pk)

        if not user or not ad:
            return r400('Please specify valid ad')
        if user.isEmployer:
            return r400('Only employees can apply for jobs')
        if ad.isClosed:
            return r400('This job is closed')
        
        a = Applied()
        a.user = user
        a.ad = ad
        try:
            a.save()
        except:
            return r500('Failed to apply for job')
        serialized = AppliedSerializer(a)
        return r201(serialized.data)

    def delete(self, request, pk, *args, **kwargs):
        a = get_object_or_404(Applied, user_id=request._auth, ad_id=pk)
        if a.ad.isClosed:
            return r400('This job is closed')
        try:
            a.delete()
        except:
            return r500('Failed to unapply for job')
        return r204()


class Choose(APIView):
    permission_classes = [IsLoggedIn]

    def post(self, request, pk, *args, **kwargs):
        ad = get_object_or_404(Ad, pk=pk)

        if ad.employer.userId != request._auth:
            return r401('Unauthorized')
        if ad.isClosed:
            return r400('This job is closed')

        userIds = request.data.get('userIds')
        if len(userIds) == 0:
            return r400('Please specify a user to be chosen')

        users = Applied.objects.filter(ad_id=pk, user__userId__in=userIds)
        if len(users) != len(userIds):
            return r400('Invalid user id')

        try:
            with transaction.atomic():
                users.update(chosen=True)
                ad.isClosed=True
                ad.save()
        except:
            return r500('Failed to choose applicants')
        return r204()
        

class UniversityList(generics.ListAPIView):
    permission_classes = [IsLoggedIn]
    queryset = University.objects.all()
    serializer_class = UniversitySerializer


class FacultyList(generics.ListAPIView):
    permission_classes = [IsLoggedIn]
    queryset = Faculty.objects.all()
    serializer_class = FacultySerializer
    

class TagList(generics.ListAPIView):
    permission_classes = [IsLoggedIn]
    queryset = Tag.objects.all()
    serializer_class = TagSerializer
    

class LocationList(generics.ListAPIView):
    permission_classes = [IsLoggedIn]
    queryset = Location.objects.all()
    serializer_class = LocationSerializer
    

class BadgeList(generics.ListAPIView):
    permission_classes = [IsLoggedIn]
    queryset = Badge.objects.all()
    serializer_class = BadgeSerializer
    

# TODO: ocenjivanje samo kad su saradjivali + zavrsen posao
# TODO: filtriranje i sortiranje
# TODO: prijavljivanje neprikladnog sadrzaja
# TODO: paginacija svuda
# TODO: ne svi podaci za svaki upit
# TODO: forgot password, badzevi, notifikacije, recommender system
# TODO: sql skripta za pocetno punjenje
# TODO: Docker
# TODO: Readme za instalaciju