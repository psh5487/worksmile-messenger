from datetime import time, datetime
from flask import Flask, render_template,request, redirect, jsonify, Response
from flask import Blueprint
from model.room import *
from model.company import *
from model.room_user import *
from model.wsuser import *
from datetime import datetime as dtime
import uuid, time, json
import app as mongo
from dto.open_room_dto import *
from dto.user_dto import *
from dto.room_dto import *
from bson.objectid import ObjectId

landing = Blueprint('rooms', __name__)

@landing.route('/cur-user/<user_id>', methods=['GET'])
def show_private_rooms(user_id):

    current_user_uuid = WSUser.get_current_user_uuid(user_id)
    ruuids = RoomUser.get_user_ruuids(current_user_uuid)
    landing_data = list(mongo.mongo_db.messages.find( {"ruuid":{ "$in": ruuids}}, { "msg": { "$slice": -1 } }).sort([ ("msg.created_at", -1)]))

    for x in landing_data:
        x["_id"]=str(x["_id"])

    sorted_ruuids = []
    for x in landing_data:
        sorted_ruuids.append(x["ruuid"])

    room_dto_list = []
    for ruuid in sorted_ruuids:
        room_result = Room.get_room_with_ruuid(ruuid)
        room_dto = RoomDto()
        room_dto.ruuid = ruuid
        room_dto.memcnt = room_result.memcnt
        room_dto.register_at = room_result.register_at
        room_dto.update_at = room_result.update_at
        room_dto.room_type = room_result.room_type
        room_dto.leader_id = room_result.leader_id
        room_dto.init_name = room_result.init_name

        room_dto.subroot_cname = Company.get_cname_with_cid(room_result.root_cid)
        room_dto.leader_name = WSUser.get_uname_with_uid(room_result.leader_id) 
        room_user_result = RoomUser.get_room_with_ruuid_uuid(room_result.ruuid, current_user_uuid)

        room_dto.rname = room_user_result.rname
        room_dto.favorite_type = room_user_result.favorite_type
        room_dto.push_notice = room_user_result.push_notice
        room_dto.last_read_idx = room_user_result.last_read_idx

        uuids = RoomUser.get_users_uuid(room_result.ruuid) 
        
        if user_id in uuids:
            uuids.remove(user_id)
        if len(uuids) >= 4 :
            uuids = uuids[0:4]
        profiles = WSUser.get_users_profile(uuids)
        room_dto.thumbnails = profiles
        room_dto.register_at = room_dto.register_at.strftime('%Y-%m-%d %H:%M:%S')
        room_dto.update_at = room_dto.update_at.strftime('%Y-%m-%d %H:%M:%S')
        room_dto_list.append(room_dto.__dict__)
    
    ret = {"rooms": room_dto_list,"last_messages":landing_data}
    
    data = { "rooms": room_dto_list, "last_messages" : landing_data }
    return jsonify(status=200, message="채팅방 목록 로드 성공", data=data)


@landing.route('/public/company/<subroot_cid>', methods=['GET'])
def show_public_rooms(subroot_cid):
    public_result = Room.get_public_rooms_with_cid(subroot_cid)

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
    
    data = { "open_rooms" : rooms }
    return jsonify(status=200, messages="전체 오픈채팅방 로드 성공", data=data)