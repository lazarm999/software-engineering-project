from django.urls import path
from api import views

urlpatterns = [
    path('register/', views.Register.as_view()),
    path('user/<int:pk>/', views.UserDetail.as_view()),
    path('profilePicture/<int:pk>/', views.ProfilePicture.as_view()),
    path('login/', views.Login.as_view()),
    path('passwordChange/<int:pk>/', views.PasswordChange.as_view()),
    path('ban/<int:pk>/', views.BanUser.as_view()),
    path('unban/<int:pk>/', views.UnbanUser.as_view()),
    path('rate/<int:pk>/', views.RateUser.as_view()),
    path('ratings/<int:pk>/', views.UserRatings.as_view()),
    path('ad/', views.AdList.as_view()),
    path('ad/<int:pk>/', views.AdDetail.as_view()),
    path('tags/<int:pk>/', views.AdTags.as_view()),
    path('comments/<int:pk>/', views.CommentList.as_view()),
    path('comment/<int:pk>/', views.CommentDetail.as_view()),
    path('apply/<int:pk>/', views.Apply.as_view()),
    path('choose/<int:pk>/', views.Choose.as_view()),
]
