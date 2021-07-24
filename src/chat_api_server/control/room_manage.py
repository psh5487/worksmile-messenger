from datetime import time, datetime
from flask import Flask, render_template,request, redirect, jsonify, Response
from flask import Blueprint
from model.room import *
from model.company import *
from model.position import *
import uuid, time, datetime, json
from dto.room_dto import *
from dto.user_dto import *
import requests
room_crud = Blueprint('room', __name__) 

@room_crud.route('/', methods=['POST','DELETE'])
def new():

    if request.method == 'POST':
        request_body = request.json
        room_leader = request_body['room_leader']
        company_name = request_body['company_name']
        user_list = request_body['userlist']
        room_type = request_body['room_type']
        room_name = request_body['room_name']
        mem_cnt = request_body['memcnt']

        cid = Company.get_cid(company_name)
        room_result = Room.create(room_leader, user_list, room_type, room_name, mem_cnt,cid)

        for uid in user_list:
            user_uuid = WSUser.get_current_user_uuid(uid)
            RoomUser.create(user_uuid, room_result.ruuid, room_name)

        leader_uuid = WSUser.get_current_user_uuid(room_leader)
        room_user_result = RoomUser.get_room_with_ruuid_uuid(room_result.ruuid, leader_uuid)
        user_result = WSUser.get_uname_with_uuid(leader_uuid)
    
        room = RoomDto()
        room.ruuid = room_result.ruuid
        room.memcnt = room_result.memcnt
        room.register_at = room_result.register_at
        room.update_at = room_result.update_at
        room.room_type = room_result.room_type
        room.leader_id = room_result.leader_id
        room.init_name = room_result.init_name
        room.rname = room_user_result.rname
        room.favorite_type = room_user_result.favorite_type
        room.push_notice = room_user_result.push_notice
        room.last_read_idx = room_user_result.last_read_idx
        room.leader_name = user_result

        uuids = RoomUser.get_users_uuid(room.ruuid)
        if len(uuids) >= 4 :
            uuids = uuids[0:4]
        profiles = WSUser.get_users_profile(uuids)
        room.thumbnails = profiles

        room.register_at = room.register_at.strftime('%Y-%m-%d %H:%M:%S')
        room.update_at = room.update_at.strftime('%Y-%m-%d %H:%M:%S')

        users = []
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
            users.append(user.__dict__)

        msg_server_endpoint = "http://52.198.41.19:8080/api/messages/create/room"
        data_for_msg_server = {"ruuid" : room.ruuid, "rname"  :room.init_name, "users":user_list }
        
        result = json.dumps(data_for_msg_server)
        headers = {'Content-type':'application/json', 'Accept':'application/json'}
        msg_server_response = requests.post(msg_server_endpoint, data=result, headers=headers)
        
        status = msg_server_response.status_code 
        if status == 200 :
            res = msg_server_response.json()
            data = { "room" : room.__dict__ , "users" : users }
            return jsonify(status=200, message="채팅방 생성 성공", data=data)

        else :
            Room.delete_with_ruuid(room.ruuid)
            for x in user_list:
                uuid = WSUser.get_uid_with_uuid(x)
                RoomUser.delete_with_ruuid_uuid(room.ruuid, uuid)
            data = { "room" : None, "users" : None }
            return jsonify(status=450, message="채팅방 생성 실패 - 몽고디비에 저장하는 과정에서 오류", data=data)

    elif request.method == 'DELETE':
        request_body = request.json
        ruuid = request_body['ruuid']
        RoomUser.delete_with_ruuid(ruuid)
        Room.delete_with_ruuid(ruuid)
        data = { "ruuid" : ruuid }
        return jsonify(status=200, message="채팅방 제거 성공", data=data)


@room_crud.route('/<room_id>', methods=['POST'])
def roomInfo(room_id):

    request_body = request.json
    uid = request_body['uid']

    room_result = Room.get_room_with_ruuid(room_id)
    uuid = WSUser.get_current_user_uuid(uid)
    room_user_result = RoomUser.get_room_with_ruuid_uuid(room_id, uuid)

    room = RoomDto()
    room.ruuid = room_result.ruuid
    room.memcnt = room_result.memcnt
    room.register_at = room_result.register_at
    room.update_at = room_result.update_at
    room.room_type = room_result.room_type
    room.leader_id = room_result.leader_id
    room.init_name = room_result.init_name
    room.subroot_cname = Company.get_cname_with_cid(room_result.root_cid)
    room.leader_name = WSUser.get_uname_with_uid(room.leader_id) 
    room.rname = room_user_result.rname
    room.favorite_type = room_user_result.favorite_type
    room.push_notice = room_user_result.push_notice
    room.last_read_idx = room_user_result.last_read_idx
        
    data = { "room" : room.__dict__ }
    return jsonify(status=200, message="채팅방 타입(공개/비공개 여부)출력 성공", data=data)

