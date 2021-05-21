#!/bin/sh
git checkout backend
git pull
pkill -f "python manage.py runserver"
cd ZadrugaAPI
sed 's/DEBUG = True/DEBUG = False/g' ./ZadrugaAPI/settings.py
sed 's/ALLOWED_HOSTS = []/ALLOWED_HOSTS = ["*"]/g' ./ZadrugaAPI/settings.py
pipenv install
pipenv shell
python manage.py runserver 0.0.0.0:8000
