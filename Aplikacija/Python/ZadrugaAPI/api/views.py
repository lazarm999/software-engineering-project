import os
import uuid

import jwt
from PIL import Image

from django.http import Http404, HttpResponse
from django.shortcuts import get_object_or_404
from django.contrib.auth.hashers import make_password, check_password

from rest_framework import generics, status, mixins
from rest_framework.views import APIView
from rest_framework.parsers import FileUploadParser
from rest_framework.response import Response

from api.models import *
from api.serializers import *
from api.permissions import *


# Create your views here.
class Register(generics.GenericAPIView, mixins.CreateModelMixin):
    queryset = User.objects.all()
    serializer_class = UserSerializer

    def post(self, request, *args, **kwargs):
        return self.create(request, *args, **kwargs)

    def perform_create(self, serializer):
        inst = serializer.save()
        inst.password = make_password(inst.password)
        inst.save()


class Login(APIView):
    def post(self, request):
        email = request.data['email']
        password = request.data['password']

        try:
            user = User.objects.get(email=email)
            if check_password(password, user.password) and user.banAdmin is None:
                return Response({'token' : jwt.encode({'id' : user.userId}, os.environ.get('SECRET_KEY'), algorithm="HS256")})
            else:
                return Response('Unauthorized', status=status.HTTP_401_UNAUTHORIZED)
        except Exception as e:
            print(e)
            return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class UserDetail(generics.GenericAPIView, mixins.UpdateModelMixin, mixins.DestroyModelMixin, mixins.RetrieveModelMixin):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [IsLoggedIn, IsOwner]

    def get(self, request, *args, **kwargs):
        return self.retrieve(request, args, kwargs)

    def patch(self, request, pk, *args, **kwargs):
        '''
            Only expose PATCH method, because of separate password update.
        '''
        user = get_object_or_404(User, pk=pk)
        if request.data['facultyId']:
            user.faculty = get_object_or_404(Faculty, pk=request.data['facultyId'])
            try:
                user.save()
            except:
                pass
        self.partial_update(request, *args, **kwargs)
        return Response(status=status.HTTP_204_NO_CONTENT)

    def delete(self, request, *args, **kwargs):
        return self.destroy(request, *args, **kwargs)


class PasswordChange(APIView):
    permission_classes = [IsLoggedIn, IsOwner]

    def put(self, request, pk):
        oldPwd = request.data['oldPassword']
        newPwd = request.data['newPassword']

        user = get_object_or_404(User, pk=pk)
        if check_password(oldPwd, user.password):
            user.password = make_password(newPwd)
            user.save()
            return Response(status=status.HTTP_204_NO_CONTENT)
        else:
            return Response(status=status.HTTP_401_UNAUTHORIZED)
    

class ProfilePicture(APIView):
    parser_classes = [FileUploadParser]
    imgsDir = os.environ.get('IMGS_DIR')
    permission_classes = [IsLoggedIn, IsOwner]

    def get(self, request, pk):
        try:
            imgName = get_object_or_404(User, pk=pk).imageName
            if imgName:
                imgPath = os.path.join(ProfilePicture.imgsDir, imgName)
            else:
                raise Http404
            with open(imgPath, 'rb') as f:
                if f:
                    return HttpResponse(f.read(), content_type='image/png')
                else:
                    raise Http404
        except User.DoesNotExist:
            raise Http404

    def post(self, request, pk):
        '''
            Requires that you set the following headers:
            Content-Type: image/png (or the corresponding image format)
            Content-Disposition: attachment; filename=<filename> 
        '''
        user = get_object_or_404(User, pk=pk)
        user.imageName = str(uuid.uuid4())+'.png'
        image = Image.open(request.data['file'])
        # TODO: png + format
        try:
            image.save(os.path.join(ProfilePicture.imgsDir, user.imageName))
            user.save()
        except Exception as e:
            return Response(str(e), status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        return Response(status=status.HTTP_201_CREATED)


class BanUser(APIView):
    permission_classes = [IsLoggedIn, IsAdmin]

    def post(self, request, pk):
        print(User.objects.get(pk=request._auth).isAdmin)
        user = get_object_or_404(User, pk=pk)
        user.banAdmin = User.objects.get(pk=request._auth)
        if request.data['banExplanation']:
            user.banExplanation = request.data['banExplanation']
        try:
            user.save()
        except Exception as e:
            return Response(str(e), status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        return Response(status=status.HTTP_204_NO_CONTENT)


class UnbanUser(APIView):
    permission_classes = [IsLoggedIn, IsAdmin]

    def post(self, request, pk):
        user = get_object_or_404(User, pk=pk)
        user.banAdmin = None
        user.banExplanation = None
        try:
            user.save()
        except Exception as e:
            return Response(str(e), status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        return Response(status=status.HTTP_204_NO_CONTENT)


class RateUser(APIView):
    permission_classes = [IsLoggedIn]
    
    def post(self, request, *args, **kwargs):
        rater_id = request.data['rater']
        ratee_id = request.data['ratee']
        rating = request.data['rating']
        comment = request.data['comment']
        if rater_id and ratee_id:
            rater = get_object_or_404(User, pk=rater_id)
            ratee = get_object_or_404(User, pk=ratee_id)
            if rater.isEmployer != ratee.isEmployer and isinstance(rating, int) \
                and rating > 0 and rating < 6 and rater.userId == request._auth:
                r = Rating()
                r.rater = rater
                r.ratee = ratee
                r.rating = rating
                r.comment = comment
                try:
                    r.save()
                except:
                    return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
            else:
                return Response('Invalid parameters', status=status.HTTP_400_BAD_REQUEST)
        else:
            return Response('Please provide rater and ratee', status=status.HTTP_400_BAD_REQUEST)
        return Response(status=status.HTTP_201_CREATED)


class UserRatings(APIView):
    permission_classes = [IsLoggedIn]

    def get(self, request, pk, *args, **kwargs):
        ratingList = Rating.objects.filter(ratee_id=pk)
        serialized = RatingSerializer(ratingList, many=True)
        return Response(serialized.data)


class AdList(generics.ListCreateAPIView):
    queryset = Ad.objects.all()
    serializer_class = AdSerializer
    permission_classes = [IsLoggedIn]

    def post(self, request, *args, **kwargs):
        print(request.data)
        ad = Ad()
        ad.title = request.data.get('title')
        ad.description = request.data.get('description')
        ad.numberOfEmployees = request.data.get('numberOfEmployees')
        ad.compensationMin = request.data.get('compensationMin')
        ad.compensationMax = request.data.get('compensationMax')

        if ad.compensationMin > ad.compensationMax:
            return Response(status=status.HTTP_400_BAD_REQUEST)

        ad.employer = get_object_or_404(User, pk=request.data.get('employerId'))
        ad.location = get_object_or_404(Location, pk=request.data.get('locationId'))
        try:
            ad.save()
        except:
            return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)

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
        return Response(status=status.HTTP_201_CREATED)

    def get(self, request, *args, **kwargs):
        ads = Ad.objects.all()
        serialized = AdSerializer(ads, many=True)
        return Response(serialized.data)


class AdTags(APIView):
    permission_classes = [IsLoggedIn]

    def post(self, request, pk, *args, **kwargs):
        tagId = request.data.get('tagId')
        if tagId:
            tag = get_object_or_404(Tag, pk=tagId)
            ad = get_object_or_404(Ad, pk=pk)
            if ad.employer.userId != request._auth:
                return Response(status=status.HTTP_401_UNAUTHORIZED)
            r = RelatedTo()
            r.tag = tag
            r.ad = ad
            try:
                r.save()
            except:
                return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)
        return Response(status=status.HTTP_201_CREATED)

    def delete(self, request, pk, *args, **kwargs):
        tagId = request.data.get('tagId')
        if tagId:
            r = get_object_or_404(RelatedTo, tag_id=tagId, ad_id=pk)
            if r.ad.employer.userId != request._auth:
                return Response(status=status.HTTP_401_UNAUTHORIZED)
            try:
                r.delete()
            except:
                return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)
        return Response(status=status.HTTP_204_NO_CONTENT)


class AdDetail(generics.GenericAPIView, mixins.RetrieveModelMixin, mixins.UpdateModelMixin):
    permission_classes = [IsLoggedIn]
    queryset = Ad.objects.all()
    serializer_class = AdSerializer

    def get(self, request, *args, **kwargs):
        return self.retrieve(request, args, kwargs)

    def delete(self, request, pk, *args, **kwargs):
        ad = get_object_or_404(Ad, pk=pk)
        if ad.employer.userId == request._auth:
            try:
                ad.delete()
            except:
                return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        else:
            return Response(status=status.HTTP_401_UNAUTHORIZED)
        return Response(status=status.HTTP_204_NO_CONTENT)

    def patch(self, request, pk, *args, **kwargs):
        ad = get_object_or_404(Ad, pk=pk)
        if ad.employer.userId == request._auth:
            compMin = request.data.get('compensationMin', ad.compensationMin)
            compMax = request.data.get('compensationMax', ad.compensationMax)
            if compMin > compMax:
                return Response(status=status.HTTP_400_BAD_REQUEST)
            locationId = request.data.get('locationId')
            if locationId:
                location = Location.objects.filter(pk=locationId).first()
                if location:
                    ad.location = location
                    try:
                        ad.save()
                    except:
                        pass           
        else:
            return Response(status=status.HTTP_401_UNAUTHORIZED)
        self.partial_update(request, args, kwargs)
        return Response(status=status.HTTP_204_NO_CONTENT)


class CommentList(APIView):
    permission_classes = [IsLoggedIn]

    def post(self, request, *args, **kwargs):
        adId = request.data.get('adId')
        comment = request.data.get('comment')
        if adId and comment:
            c = Comment()
            c.user = get_object_or_404(User, pk=request._auth)
            c.ad = get_object_or_404(Ad, pk=adId)
            c.comment = comment
            try:
                c.save()
            except:
                return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)
        return Response(status=status.HTTP_201_CREATED)

    def get(self, request, *args, **kwargs):
        adId = request.data.get('adId')
        if not adId:
            return Response(status=status.HTTP_400_BAD_REQUEST)
        comments = Comment.objects.filter(ad_id=adId)
        serialized = CommentSerializer(comments, many=True)
        return Response(serialized.data)


class CommentDetail(APIView):
    permission_classes = [IsLoggedIn]

    def delete(self, request, pk, *args, **kwargs):
        comment = get_object_or_404(Comment, pk=pk)
        if comment.user.userId == request._auth:
            try:
                comment.delete()
            except:
                return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        else:
            return Response(status=status.HTTP_401_UNAUTHORIZED)
        return Response(status=status.HTTP_204_NO_CONTENT)


class Apply(APIView):
    permission_classes = [IsLoggedIn]

    def post(self, request, pk, *args, **kwargs):
        user = get_object_or_404(User, pk=request._auth)
        ad = get_object_or_404(Ad, pk=pk)

        if not user or not ad:
            return Response('Please specify valid ad', status=status.HTTP_400_BAD_REQUEST)
        if user.isEmployer:
            return Response('Only employees can apply for jobs', status=status.HTTP_400_BAD_REQUEST)
        if ad.isClosed:
            return Response('This job is closed', status=status.HTTP_400_BAD_REQUEST)
        
        a = Applied()
        a.user = user
        a.ad = ad
        try:
            a.save()
        except:
            return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        return Response(status=status.HTTP_201_CREATED)

    def delete(self, request, pk, *args, **kwargs):
        a = get_object_or_404(Applied, user_id=request._auth, ad_id=pk)
        if a.ad.isClosed:
            return Response('This job is closed', status=status.HTTP_400_BAD_REQUEST)

        try:
            a.delete()
        except:
            return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        return Response(status=status.HTTP_204_NO_CONTENT)


class Choose(APIView):
    permission_classes = [IsLoggedIn]

    def post(self, request, pk, *args, **kwargs):
        ad = get_object_or_404(Ad, pk=pk)

        if ad.employer.userId != request._auth:
            return Response(status=status.HTTP_401_UNAUTHORIZED)
        if ad.isClosed:
            return Response('This job is closed', status=status.HTTP_400_BAD_REQUEST)

        userIds = request.data.get('userIds')
        users = Applied.objects.filter(ad_id=pk, user__userId__in=userIds)
        if len(users) != len(userIds):
            return Response('Invalid user id', status=status.HTTP_400_BAD_REQUEST)

        try:
            users.update(chosen=True)
            ad.isClosed=True
            ad.save()
        except Exception as e:
            print(e)
            return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        return Response(status=status.HTTP_204_NO_CONTENT)
        

# TODO: refactor, make sure you are consistent + Response functions
# TODO: log exceptions + startserver skripta
# TODO: return object on create
# TODO: paginacija svuda
# TODO: ne svi podaci za svaki upit
# TODO: .get za dictove
# TODO: forgot password, badzevi, notifikacije, recommender system