from django.urls import path
from api import views

urlpatterns = [
    path('register/', views.Register.as_view()),
    path('user/<int:pk>/', views.UserDetail.as_view()),
    path('profilePicture/<int:pk>/', views.ProfilePicture.as_view()),
    path('profilePictureQb/<int:pk>/', views.ProfilePictureQb.as_view()),
    path('login/', views.Login.as_view()),
    path('passwordChange/', views.PasswordChange.as_view()),
    path('ban/<int:pk>/', views.BanUser.as_view()),
    path('unban/<int:pk>/', views.UnbanUser.as_view()),
    path('rate/<int:pk>/', views.RateUser.as_view()),
    path('ratings/<int:pk>/', views.UserRatings.as_view()),
    path('ad/', views.AdList.as_view()),
    path('ad/<int:pk>/', views.AdDetail.as_view()),
    path('comments/<int:pk>/', views.CommentList.as_view()),
    path('comment/<int:pk>/', views.CommentDetail.as_view()),
    path('apply/<int:pk>/', views.Apply.as_view()),
    path('choose/<int:pk>/', views.Choose.as_view()),
    path('universityList/', views.UniversityList.as_view()),
    path('facultyList/', views.FacultyList.as_view()),
    path('tagList/', views.TagList.as_view()),
    path('locationList/', views.LocationList.as_view()),
    path('badgeList/', views.BadgeList.as_view()),
    path('userByQbUserId/<int:pk>/', views.UserByQbUserId.as_view()),
    path('chatMembers/', views.ChatMembers.as_view()),
    path('adByQbChatId/<str:pk>/', views.AdByQbChatId.as_view()),
    path('userAds/<int:pk>/', views.UserAds.as_view()),
    path('userFcm/', views.UserFCMView.as_view()),
    path('userFcm/<str:pk>/', views.UserFCMDeleteView.as_view())
]
