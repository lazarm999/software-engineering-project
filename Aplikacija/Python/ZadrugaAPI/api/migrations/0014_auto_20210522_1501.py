# Generated by Django 3.2.3 on 2021-05-22 15:01

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0013_remove_comment_comment_pk'),
    ]

    operations = [
        migrations.AlterField(
            model_name='badge',
            name='badgeId',
            field=models.BigIntegerField(primary_key=True, serialize=False),
        ),
        migrations.AlterField(
            model_name='faculty',
            name='facultyId',
            field=models.BigIntegerField(primary_key=True, serialize=False),
        ),
        migrations.AlterField(
            model_name='location',
            name='locId',
            field=models.BigIntegerField(primary_key=True, serialize=False),
        ),
        migrations.AlterField(
            model_name='tag',
            name='tagId',
            field=models.BigIntegerField(primary_key=True, serialize=False),
        ),
        migrations.AlterField(
            model_name='university',
            name='universityId',
            field=models.BigIntegerField(primary_key=True, serialize=False),
        ),
    ]
