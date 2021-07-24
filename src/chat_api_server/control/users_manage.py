from datetime import time, datetime
from flask import Flask, render_template,request, redirect, jsonify, Response
from flask import Blueprint
from model.room import *
from model.company import *
from model.position import *
import uuid, time, datetime, json
from model import wsuser
from dto.room_dto import *
from dto.user_dto import *
from dto.user_last_idx_dto import *

userlist = Blueprint('users', __name__) 

@userlist.route('/room/<room_id>', methods=['GET'])
def get_member(room_id):
    uuids =  RoomUser.get_uuids_with_ruuid(room_id)
    print(">>",uuids)
    
    users = []
    for uuid in uuids:
        user_result = WSUser.get_user_with_uuid(uuid)
        
        user = UserLastIdxDto()
        user.uid = user_result.uid
        user.uname = user_result.uname
        user.profile = user_result.profile
        user.role = user_result.role
        user.cid = user_result.cid
        user.subroot_cid = user_result.subroot_cid
        user.root_cid = user_result.root_cid
        user.pid = user_result.pid
        user.email = user_result.email
        user.phone = user_result.phone
        user.register_at = user_result.register_at
        user.login_at = user_result.login_at
        user.all_push_notice = user_result.all_push_notice
        user.cname = Company.get_cname_with_cid(user_result.cid)
        user.subroot_cname = Company.get_cname_with_cid(user_result.subroot_cid)
        user.root_cname = Company.get_cname_with_cid(user_result.root_cid)
        user.pname = Position.get_pname_with_pid(user_result.pid) 
        uuid = WSUser.get_current_user_uuid(user.uid)
        user.last_read_idx = RoomUser.get_last_read_idx_with_uuid_ruuid(uuid, room_id)

        users.append(user.__dict__)
    
    data = { "users" : users}
    return jsonify(status=200, message="채팅방 내 멤버 조회 성공", data=data)    
    