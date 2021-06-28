# Generated by Django 3.2.3 on 2021-06-26 12:53

from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0021_notification_tagged'),
    ]

    operations = [
        migrations.AddField(
            model_name='notification',
            name='postTime',
            field=models.DateTimeField(auto_now_add=True, default=django.utils.timezone.now),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='usernotification',
            name='notification',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='notifications', to='api.notification'),
        ),
    ]