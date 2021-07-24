# 인증 토큰 JWT  유효성 검사 데코레이터
from flask import request
from functools import wraps
import json, jwt, requests
from datetime import datetime as dtime
import datetime as pydatetime

def check_token(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        access_token = request.headers['X-Auth-Token']
        secret_key = 'Giant-Panda-Aibao-Lebao-Xingbao-Lives-In-Everland-They-Are-Very-Very-Cute-Pandas'
        decoded_token = jwt.decode(access_token, secret_key, algorithms=['HS256'])
        
        expiration_time = decoded_token['expiration']
        current_time = pydatetime.datetime.now().timestamp()

        if current_time >= expiration_time :
            
            data = {"expired-token": access_token } 
            URL = "http://52.198.41.19:8080//api/auth/token/issue"
            headers = {'Content-type':'application/json', 'Accept':'application/json'}
            response = requests.post(URL, data=data, headers=headers)
            result = response.json()

            if result["status"] == 200 :
                new_token = res["data"]["new_token"]
                data = result["data"]
                new_token = data["new_token"]
                kwargs["new_token"] = new_token
                return f(*args, **kwargs)
        
            else:
                return response 
        else :
            kwargs["new_token"] = access_token
            return f(*args, **kwargs)
    return decorated