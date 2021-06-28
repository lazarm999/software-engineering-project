#!/bin/sh
git checkout backend
git pull
pkill -f "python manage.py runserver"
cd ZadrugaAPI
sed -i 's/DEBUG = True/DEBUG = False/g' ./ZadrugaAPI/settings.py
sed -i 's/ALLOWED_HOSTS = \[\]/ALLOWED_HOSTS = \["*"\]/g' ./ZadrugaAPI/settings.py
pipenv install
pipenv run python manage.py migrate
(pipenv run python manage.py runserver 0.0.0.0:8000 &)
