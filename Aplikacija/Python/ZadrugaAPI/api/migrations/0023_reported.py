# Generated by Django 3.2.3 on 2021-06-29 10:13

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0022_auto_20210626_1253'),
    ]

    operations = [
        migrations.CreateModel(
            name='Reported',
            fields=[
                ('reportId', models.BigAutoField(primary_key=True, serialize=False)),
                ('elaboration', models.TextField()),
                ('ad', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='api.ad')),
                ('comment', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='api.comment')),
                ('reporter', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.user')),
            ],
        ),
    ]
