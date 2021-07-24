from datetime import time, datetime
from flask import Flask, render_template,request, redirect, jsonify, Response
from flask import Blueprint
from model.room import *
from model.company import *
from model.position import *
from model.forbidden_word import *
import uuid
import time
import datetime
import json
from model import wsuser
from dto.room_dto import *
from dto.user_dto import *
from dto.user_last_idx_dto import *
filter_chat = Blueprint('filter_chat', __name__) 


@filter_chat.route('/', methods=['GET', "OPTIONS"])
def get_forbidden_words():
   
    words = ForbiddenWord.get_all()
    word_list = []
    for w in words:
        word_list.append({ "wid": w.wid, "word" : w.word})
    data = {"forbidden_words" : word_list}
    res = jsonify(status=200, message="금지어 목록 로드 성공", data=data)
    return res