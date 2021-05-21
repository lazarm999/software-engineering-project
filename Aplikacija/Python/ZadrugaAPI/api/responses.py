from rest_framework.response import Response
from rest_framework.status import * 

def r500(obj=None):
    return Response(obj, status=HTTP_500_INTERNAL_SERVER_ERROR)

def r401(obj=None):
    return Response(obj, status=HTTP_401_UNAUTHORIZED)

def r204(obj=None):
    return Response(obj, status=HTTP_204_NO_CONTENT)

def r201(obj=None):
    return Response(obj, status=HTTP_201_CREATED)

def r400(obj=None):
    return Response(obj, status=HTTP_400_BAD_REQUEST)