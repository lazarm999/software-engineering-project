from django.db import models


# Create your models here.
class University(models.Model):
    universityId = models.BigIntegerField(primary_key=True)
    name = models.CharField(max_length=50)


class Faculty(models.Model):
    facultyId = models.BigIntegerField(primary_key=True)
    name = models.CharField(max_length=50)
    university = models.ForeignKey('University', related_name='faculties', on_delete=models.CASCADE)


class Tag(models.Model):
    tagId = models.BigIntegerField(primary_key=True)
    name = models.CharField(max_length=50)


class Location(models.Model):
    locId = models.BigIntegerField(primary_key=True)
    cityName = models.CharField(max_length=50)


class Badge(models.Model):
    badgeId = models.BigIntegerField(primary_key=True)
    description = models.CharField(max_length=100)


class User(models.Model):
    userId = models.BigAutoField(primary_key=True)
    firstName = models.CharField(max_length=50)
    lastName = models.CharField(max_length=50)
    email = models.EmailField(unique=True)
    username = models.CharField(max_length=50, unique=True)
    password = models.CharField(max_length=256)
    bio = models.TextField(null=True)
    imageName = models.CharField(max_length=100, null=True)
    phoneNumber = models.CharField(max_length=20, null=True)
    isAdmin = models.BooleanField(default=False)
    isEmployer = models.BooleanField(default=False)
    companyName = models.CharField(max_length=100, null=True)
    faculty = models.ForeignKey('Faculty', related_name='students', on_delete=models.CASCADE, null=True)
    banAdmin = models.ForeignKey('User', on_delete=models.DO_NOTHING, null=True)
    banExplanation = models.TextField(null=True)
    badges = models.ManyToManyField(Badge, through='Earned')
    userQbId = models.IntegerField()

class Earned(models.Model):
    user = models.ForeignKey('User', on_delete=models.CASCADE)
    badge = models.ForeignKey('Badge', on_delete=models.CASCADE)

    class Meta:
        constraints = [models.UniqueConstraint(fields=['user', 'badge'], name='earned_pk')]


class Rating(models.Model):
    rater = models.ForeignKey('User', related_name='given_ratings', on_delete=models.CASCADE)
    ratee = models.ForeignKey('User', related_name='received_ratings', on_delete=models.CASCADE)
    rating = models.IntegerField()
    comment = models.TextField(null=True)
    postTime = models.DateTimeField(auto_now=True)

    class Meta:
        constraints = [models.UniqueConstraint(fields=['rater', 'ratee'], name='rating_pk')]


class Ad(models.Model):
    adId = models.BigAutoField(primary_key=True)
    title = models.CharField(max_length=200)
    description = models.TextField(null=True)
    numberOfEmployees = models.IntegerField(null=True)
    compensationMin = models.IntegerField()
    compensationMax = models.IntegerField(null=True)
    employer = models.ForeignKey('User', related_name='ads', on_delete=models.CASCADE)
    location = models.ForeignKey('Location', on_delete=models.CASCADE)
    isClosed = models.BooleanField(default=False)
    postTime = models.DateTimeField(auto_now_add=True)
    tags = models.ManyToManyField(Tag, through='RelatedTo')
    qbChatId = models.CharField(max_length=40, null=True)

    def numberOfApplied(self):
        return Applied.objects.filter(ad__adId=self.adId).count()


class RelatedTo(models.Model):
    ad = models.ForeignKey('Ad', on_delete=models.CASCADE)
    tag = models.ForeignKey('Tag', on_delete=models.CASCADE)

    class Meta:
        constraints = [models.UniqueConstraint(fields=['ad', 'tag'], name='relatedTo_pk')]


class Comment(models.Model):
    user = models.ForeignKey('User', on_delete=models.CASCADE)
    ad = models.ForeignKey('Ad', related_name='comments', on_delete=models.CASCADE)
    comment = models.TextField()
    postTime = models.DateTimeField(auto_now_add=True)
    

class Applied(models.Model):
    user = models.ForeignKey('User', on_delete=models.CASCADE)
    ad = models.ForeignKey('Ad', related_name='applicants', on_delete=models.CASCADE)
    chosen = models.BooleanField(default=False)

    class Meta:
        constraints = [models.UniqueConstraint(fields=['ad', 'user'], name='applied_pk')]
    

# TODO: Notifications
class Notification(models.Model):
    pass