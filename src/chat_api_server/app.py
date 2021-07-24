from flask import Flask, jsonify, request,render_template, make_response, url_for
from bson.objectid import ObjectId
from control import  search_manage, room_manage, landing_manage, file_manage,users_manage, message_manage, room_user_manage, filter_manage
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime as dtime
import datetime as pydatetime
import pymongo
import pandas as pd
from inspect import getargspec
from model.room import *
from model.wsuser import *
from flask_cors import CORS, cross_origin
from check_token_decorator import *

app = Flask(__name__)
CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'
app.config['JSON_AS_ASCII'] = False

try:
    mongo = pymongo.MongoClient('mongodb://jobjava:1234@10.250.93.124:27017/')
    mongo_db = mongo.worksmile
    mongo.server_info()
    print(dtime.today().strftime("%Y/%m/%d %H:%M:%S"), " : app.py >> MongoDB is connected")
except:
    print(dtime.today().strftime("%Y/%m/%d %H:%M:%S"), " : app.py >> MongoDB is not connected")

HOSTNAME = "onstove-dev-jobjavadb.ch3kdwqk9zub.ap-northeast-1.rds.amazonaws.com"
USERNAME = "admin"
PASSWORD = "tmxhqm123$"
app.config["SQLALCHEMY_DATABASE_URI"] = "mysql+pymysql://"+USERNAME+":"+PASSWORD+"@"+HOSTNAME+":3306/worksmile"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
app.config["SQLALCHEMY_POOL_SIZE"] = 20
app.config["SQLALCHEMY_POOL_TIMEOUT"] = 300
db = SQLAlchemy(app)

app.register_blueprint(room_manage.room_crud, url_prefix='/api/chat/room')
app.register_blueprint(landing_manage.landing, url_prefix='/api/chat/rooms')
app.register_blueprint(search_manage.search_user_rooms, url_prefix='/api/chat/search')
app.register_blueprint(file_manage.s3_file, url_prefix='/api/messages/file')
app.register_blueprint(users_manage.userlist, url_prefix='/api/chat/users')
app.register_blueprint(message_manage.search_messages, url_prefix='/api/messages/msg')
app.register_blueprint(room_user_manage.invite_exit, url_prefix='/api/chat/user')
app.register_blueprint(filter_manage.filter_chat, url_prefix='/api/chat/forbidden-words')

# sample for custom decorator
# todo : put @check_token decorator on functions (in control folder)
@app.route('/')
@check_token
def index(**kwargs):
    out = jsonify(data='success')
    out.headers['X-Auth-Token'] = kwargs['new_token']
    return out

app.run(host='0.0.0.0', port=5000, debug=True)