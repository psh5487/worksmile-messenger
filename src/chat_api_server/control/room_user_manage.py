from datetime import time, datetime
from flask import Flask, render_template,request, redirect, jsonify, Response
from flask import Blueprint
from model.room import *
from model.company import *
from model.position import *
import uuid, time, datetime, json
from dto.room_dto import *
from dto.user_dto import *

invite_exit = Blueprint('room_user', __name__)

@invite_exit.route('/', methods=['POST','DELETE'])
def invite_user():

    if request.method == 'POST':
        request_body = request.json
        ruuid = request_body["ruuid"]
        room_type = request_body["room_type"]
        user_list = request_body["users"]
        rname = Room.get_rname_with_ruuid(ruuid)

        for uid in user_list:
            uuid = WSUser.get_current_user_uuid(uid)
            RoomUser.create(uuid, ruuid, rname)

        room = Room.set_memcnt_with_ruuid(ruuid,len(user_list))
        if room_type == "":
            Room.set_roomType_with_ruuid(ruuid, "private")
            
        users = []
        user_list = RoomUser.get_users_uuid(ruuid)
        for uid in user_list:
            user_result = WSUser.get_user_with_uid(uid)
            user = UserDto()
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

            user.register_at = user_result.register_at
            if user.login_at != None :
                user.login_at = user_result.login_at
            users.append(user.__dict__)    
        
        data = {"users" : users}
        return jsonify(status=200, message="초대 및 입장 성공", data=data)

    elif request.method == 'DELETE':
        request_body = request.json
        ruuid = request_body["ruuid"]
        uid = request_body["uid"]
        uuid = WSUser.get_current_user_uuid(uid)

        room_user_result = RoomUser.delete_with_ruuid_uuid(ruuid, uuid)

        room_memcnt = Room.set_memcnt_with_ruuid(ruuid,-1) # 퇴장 -1
        if room_memcnt <= 0 :
            Room.delete_with_ruuid(ruuid)

        user_result = WSUser.get_user_with_uid(uid)
        user = UserDto()
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

        user.register_at = user_result.register_at
        if user.login_at != None :
            user.login_at = user_result.login_at

        data =  {"users" : user.__dict__}
        return jsonify(status=200, message="퇴장 성공", data=data)


@invite_exit.route('/eviction', methods=['DELETE'])
def evict():
    if request.method == 'DELETE':
        request_body = request.json
        ruuid = request_body["ruuid"]
        uid = request_body["uid"]
        room_leader = request_body["room_leader"]

        if Room.get_room_leader_uid_with_ruuid(ruuid) == room_leader :
            uuid = WSUser.get_current_user_uuid(uid)
            RoomUser.delete_with_ruuid_uuid(ruuid, uuid)
            Room.set_memcnt_with_ruuid(ruuid,-1)

            user_result = WSUser.get_user_with_uid(uid)
            user = UserDto()
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

            user.register_at = user_result.register_at
            if user.login_at != None :
                user.login_at = user_result.login_at

            data = {"user" : user.__dict__}
            return jsonify(status=200, message="강제퇴장 처리 성공", data=data)
        else:
            data = {"user" : None}
            return jsonify(status=400, message="유효한 방장 id가 아닙니다", data=data)
    else:
        data = {"user" : None}
        return jsonify(status=405, message="유효하지 않은 메소드 접근입니다.", data=data)
