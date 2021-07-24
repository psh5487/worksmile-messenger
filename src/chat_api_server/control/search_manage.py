from datetime import time, datetime
from flask import Flask, render_template,request, redirect, jsonify, Response
from flask import Blueprint
from model.wsuser import *
from model.room_user import *
from model.room import *
from model.company import *
from model.position import *
import json
from marshmallow import fields
from model import wsuser
from dto.room_dto import *
from dto.user_dto import *
from dto.open_room_dto import *

search_user_rooms = Blueprint('search',__name__)

@search_user_rooms.route('/user/cur-user/<user_id>')
def search_only_user(user_id):
    
    keyword = request.args["keyword"]
    search = "%{}%".format(keyword)
    subroot_cid = WSUser.get_current_user_subroot_id(user_id)
    
    user_result = WSUser.get_users_with_uname_subid_except_uid(search, subroot_cid, user_id)
    user_result = WSUser.get_users_with_uname_subid_except_uid_role(search, subroot_cid, user_id)
    
    users = []
    for user_object in user_result:
        user = UserDto()
        user.uid = user_object.uid
        user.uname = user_object.uname
        user.profile = user_object.profile
        user.role = user_object.role
        user.cid = user_object.cid
        user.subroot_cid = user_object.subroot_cid
        user.root_cid = user_object.root_cid
        user.pid = user_object.pid
        user.email = user_object.email
        user.phone = user_object.phone
        user.register_at = user_object.register_at
        user.login_at = user_object.login_at
        user.all_push_notice = user_object.all_push_notice
        user.cname = Company.get_cname_with_cid(user_object.cid)
        user.subroot_cname = Company.get_cname_with_cid(user_object.subroot_cid)
        user.root_cname = Company.get_cname_with_cid(user_object.root_cid)
        user.pname = Position.get_pname_with_pid(user_object.pid) 
        
        users.append(user.__dict__)
    data = {"users" : users}
    return jsonify(status=200, message="검색 결과로 나온 유저의 정보", data=data)

@search_user_rooms.route('/cur-user/<user_id>')
def search_user_rooms_both(user_id):
    
    keyword = request.args["keyword"]
    search = "%{}%".format(keyword)

    subroot_cid = WSUser.get_current_user_subroot_id(user_id)
    user_result = WSUser.get_users_with_uname_subid_except_uid(search, subroot_cid, user_id)
    user_result = WSUser.get_users_with_uname_subid_except_uid_role(search, subroot_cid, user_id)
    
    users = []
    for user_object in user_result:
        user = UserDto()
        user.uid = user_object.uid
        user.uname = user_object.uname
        user.profile = user_object.profile
        user.role = user_object.role
        user.cid = user_object.cid
        user.subroot_cid = user_object.subroot_cid
        user.root_cid = user_object.root_cid
        user.pid = user_object.pid
        user.email = user_object.email
        user.phone = user_object.phone
        user.register_at = user_object.register_at
        user.login_at = user_object.login_at
        user.all_push_notice = user_object.all_push_notice
        user.cname = Company.get_cname_with_cid(user_object.cid)
        user.subroot_cname = Company.get_cname_with_cid(user_object.subroot_cid)
        user.root_cname = Company.get_cname_with_cid(user_object.root_cid)
        user.pname = Position.get_pname_with_pid(user_object.pid) 
        
        users.append(user.__dict__)
    
    current_user_uuid = WSUser.get_current_user_uuid(user_id)
    
    room_user_result = RoomUser.get_rooms_with_rname_uuid(search, current_user_uuid)
    
    rooms = []
    for room_user_object in room_user_result:
        room = RoomDto()
        room_result = Room.get_room_with_ruuid(room_user_object.ruuid)
        room.ruuid = room_result.ruuid
        room.memcnt = room_result.memcnt
        room.register_at = room_result.register_at
        room.update_at = room_result.update_at
        room.room_type = room_result.room_type
        room.leader_id = room_result.leader_id
        room.init_name = room_result.init_name
        room.subroot_cname = Company.get_cname_with_cid(room_result.root_cid)
        room.leader_name = WSUser.get_uname_with_uid(room_result.leader_id) 
        room.rname = room_user_object.rname
        room.favorite_type = room_user_object.favorite_type
        room.push_notice = room_user_object.push_notice
        room.last_read_idx = room_user_object.last_read_idx
        uuids = RoomUser.get_users_uuid(room.ruuid) 

        if user_id in uuids:
            uuids.remove(user_id)
        if len(uuids) >= 4 :
            uuids = uuids[0:4]
        
        profiles = WSUser.get_users_profile(uuids) 
        room.thumbnails = profiles
        rooms.append(room.__dict__)
    data = { "users" : users, "rooms" : rooms }
    return jsonify(status=200, message="유저, 대화방 통합 검색 성공", data=data)
    
@search_user_rooms.route('/public-room/cur-user/<user_id>')
def search_public_room(user_id):
    rname = request.args["rname"]
    current_user_uuid = WSUser.get_current_user_uuid(user_id)
    wsu = WSUser.get_current_user_subroot_id(user_id)
    search = "%{}%".format(rname)
    
    public_result = Room.get_public_rooms_with_cid_rname(wsu, search)
    
    rooms = []
    for proom in public_result:
        room = OpenRoomDto()
        room.ruuid = proom.ruuid
        room.memcnt = proom.memcnt
        room.register_at = proom.register_at
        room.update_at = proom.update_at
        room.room_type = proom.room_type
        room.leader_id = proom.leader_id
        room.init_name = proom.init_name
        room.leader_name = WSUser.get_uname_with_uid(proom.leader_id) 

        uuids = RoomUser.get_users_uuid(room.ruuid)
        if len(uuids) >= 4 :
            uuids = uuids[0:4]
        profiles = WSUser.get_users_profile(uuids)
        room.thumbnails = profiles
        rooms.append(room.__dict__)
    
    data = {"open_rooms" : rooms}
    return jsonify(status=200, messages="오픈 채팅방 검색 성공", data=data)