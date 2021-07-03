from api.models import UserFCM
from api.permissions import BannedException
from api.responses import r408
from rest_framework.views import exception_handler

def custom_exception_handler(exc, context):
    response = exception_handler(exc, context)
    
    if isinstance(exc, BannedException):
        try:
            UserFCM.objects.filter(user__userId=context['request']._auth).delete()
        except:
            pass
        return r408()