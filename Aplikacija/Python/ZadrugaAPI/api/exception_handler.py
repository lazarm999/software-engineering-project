from api.permissions import BannedException
from api.responses import r408
from rest_framework.views import exception_handler

def custom_exception_handler(exc, context):
    response = exception_handler(exc, context)
    
    if isinstance(exc, BannedException):
        return r408()