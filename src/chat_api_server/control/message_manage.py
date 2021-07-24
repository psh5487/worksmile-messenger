from datetime import time, datetime
from flask import Flask, render_template,request, redirect, jsonify, Response
from flask import Blueprint
from model.room import *
from model.company import *
from model.room_user import *
from model.wsuser import *
from datetime import datetime as dtime
import uuid
import time
import json
import app as mongo
from dto.room_user_dto import *
from bson.objectid import ObjectId
search_messages = Blueprint('msg_keyword', __name__)

# mock data
@search_messages.route('/remove-messages')
def remove_messages():
    dbResponse = mongo.mongo_db.messages.remove({ "ruuid": "f9d47a1f-e4d3-49ea-bd31-55490190752a" })
    dbResponse = mongo.mongo_db.messages.remove({ "ruuid": "b84af7b8-c767-42cf-b87c-71389be170b0" })

# mock data insert
@search_messages.route('/sample-message', methods=['GET'])
def insert_messages():
    message1 ={
        "ruuid":"177eea3f-ff2e-40bf-b985-dd4a194ea7be", 
        "msg":[
                {
                "type" : "TALK", 
                "midx" : 0,
                "parent_id" : 0,
                "content" : "헬로, 좋은 아침이에요",
                "sender" : "uid01",
                "uname" : "uname01",
                "cname" : "AAA",
                "pname" : "본부장",
                "device" : "app",
                "created_at" : "2021-01-31 15:08:31",
                "deleted_at" : ""
                }
            ]
        }

    dbResponse = mongo.mongo_db.messages.insert_one(message1)
    # midx :1
    dbResponse = mongo.mongo_db.messages.update_one(
        {
            "ruuid" : "177eea3f-ff2e-40bf-b985-dd4a194ea7be"
        },
        {
            "$push" : 
            {
                "msg" : 
                {
                    "type" : "TALK", 
                    "midx" : 1,
                    "parent_id" : 0,
                    "content" : "헬로, 헬로 월드",
                    "sender" : "uid05",
                    "uname" : "uname05",
                    "cname" : "AAB",
                    "pname" : "과장",
                    "device" : "app",
                    "created_at" : "2021-01-31 15:08:33",
                    "deleted_at" : ""
                }
            }
        }
    )
    # midx 2
    dbResponse = mongo.mongo_db.messages.update_one(
        {
            "ruuid" : "177eea3f-ff2e-40bf-b985-dd4a194ea7be"
        },
        {
            "$push" : 
            {
                "msg" : 
                {
                    "type" : "TALK", 
                    "midx" : 2,
                    "parent_id" : 0,
                    "content" : "hello world",
                    "sender" : "uid05",
                    "uname" : "uname05",
                    "cname" : "AAB",
                    "pname" : "과장",
                    "device" : "app",
                    "created_at" : "2021-01-31 15:08:35",
                    "deleted_at" : ""
                }
            }
        }
    )
    # midx 3
    dbResponse = mongo.mongo_db.messages.update_one(
        {
            "ruuid" : "177eea3f-ff2e-40bf-b985-dd4a194ea7be"
        },
        {
            "$push" : 
            {
                "msg" : 
                {
                    "type" : "TALK", 
                    "midx" : 3,
                    "parent_id" : 0,
                    "content" : "오늘 점심 나가서 먹을까요?",
                    "sender" : "uid01",
                    "uname" : "uname01",
                    "cname" : "AAA",
                    "pname" : "본부장",
                    "device" : "app",
                    "created_at" : "2021-01-31 15:08:37",
                    "deleted_at" : ""
                }
            }
        }
    )
    # midx 4
    dbResponse = mongo.mongo_db.messages.update_one(
        {
            "ruuid" : "177eea3f-ff2e-40bf-b985-dd4a194ea7be"
        },
        {
            "$push" : 
            {
                "msg" : 
                {
                    "type" : "TALK", 
                    "midx" : 4,
                    "parent_id" : 0,
                    "content" : "네넵!",
                    "sender" : "uid05",
                    "uname" : "uname05",
                    "cname" : "AAB",
                    "pname" : "과장",
                    "device" : "app",
                    "created_at" : "2021-01-31 15:08:55",
                    "deleted_at" : ""
                }
            }
        }
    )
    # midx 5
    dbResponse = mongo.mongo_db.messages.update_one(
        {
            "ruuid" : "177eea3f-ff2e-40bf-b985-dd4a194ea7be"
        },
        {
            "$push" : 
            {
                "msg" : 
                {
                    "type" : "TALK", 
                    "midx" : 5,
                    "parent_id" : 0,
                    "content" : "네, 그럼 이따가 지하 1층에서 봅시다",
                    "sender" : "uid01",
                    "uname" : "uname01",
                    "cname" : "AAA",
                    "pname" : "본부장",
                    "device" : "app",
                    "created_at" : "2021-01-31 15:08:57",
                    "deleted_at" : ""
                }
            }
        }
    )
    
    # 다른 방 하나에 첫번 째 INSERT (6,13,14방)
    message2 ={
        "ruuid":"45edb8ee-b1fc-4f07-8ee6-879d3c1fa321", 
        "msg":[
                {
                "type" : "TALK", 
                "midx" : 0,
                "parent_id" : 0,
                "content" : "오늘 회의 몇시인지 아시죠?",
                "sender" : "uid06",
                "uname" : "uname01",
                "cname" : "AAC",
                "pname" : "팀장",
                "device" : "app",
                "created_at" : "2021-01-31 15:09:31",
                "deleted_at" : ""
                }
            ]
        }
    dbResponse = mongo.mongo_db.messages.insert_one(message2)
    # midx 1
    dbResponse = mongo.mongo_db.messages.update_one(
        {
            "ruuid" : "45edb8ee-b1fc-4f07-8ee6-879d3c1fa321"
        },
        {
            "$push" : 
            {
                "msg" : 
                {
                    "type" : "TALK", 
                    "midx" : 1,
                    "parent_id" : 0,
                    "content" : "wiki에 회이 공지 확인하세요 다들~",
                    "sender" : "uid06",
                    "uname" : "uname06",
                    "cname" : "AAC",
                    "pname" : "팀장",
                    "device" : "app",
                    "created_at" : "2021-01-31 15:09:37",
                    "deleted_at" : ""
                }
            }
        }
    )
    # midx 2
    dbResponse = mongo.mongo_db.messages.update_one(
        {
            "ruuid" : "45edb8ee-b1fc-4f07-8ee6-879d3c1fa321"
        },
        {
            "$push" : 
            {
                "msg" : 
                {
                    "type" : "TALK", 
                    "midx" : 2,
                    "parent_id" : 0,
                    "content" : "넵, 확인하겠습니다.",
                    "sender" : "uid013",
                    "uname" : "uname13",
                    "cname" : "AAC",
                    "pname" : "인턴",
                    "device" : "app",
                    "created_at" : "2021-01-31 15:09:40",
                    "deleted_at" : ""
                }
            }
        }
    )
    # midx 3
    dbResponse = mongo.mongo_db.messages.update_one(
        {
            "ruuid" : "45edb8ee-b1fc-4f07-8ee6-879d3c1fa321"
        },
        {
            "$push" : 
            {
                "msg" : 
                {
                    "type" : "TALK", 
                    "midx" : 3,
                    "parent_id" : 1,
                    "content" : "공지에 장소가 없는 것 같습니다",
                    "sender" : "uid14",
                    "uname" : "uname14",
                    "cname" : "AAA",
                    "pname" : "인턴",
                    "device" : "web",
                    "created_at" : "2021-01-31 15:09:43",
                    "deleted_at" : ""
                }
            }
        }
    )

    # 또 다른 방 하나에 첫번 째 INSERT (12,13,14방)
    message3 ={
        "ruuid":"88f17812-b1fb-474c-983e-c90291f27328", 
        "msg":[
                {
                "type" : "TALK", 
                "midx" : 0,
                "parent_id" : 0,
                "content" : "안녕하십니까",
                "sender" : "uid012",
                "uname" : "uname12",
                "cname" : "AAB",
                "pname" : "인턴",
                "device" : "app",
                "created_at" : "2021-01-31 15:30:31",
                "deleted_at" : ""
                }
            ]
        }
    dbResponse = mongo.mongo_db.messages.insert_one(message3)
    return "messages_saved"

# 특정 방 메세지 검색
@search_messages.route('/search/room/<ruuid>', methods=['GET'])
def search_message(ruuid):
    keyword = request.args["keyword"]
    
    key2 = mongo.mongo_db.messages.aggregate( [
        {"$match": {"ruuid":ruuid}},
        {"$unwind" : "$msg"},
        {"$match": {"msg.content":{"$regex": keyword}}}
    ])

    temp = list(key2)
    mlist = []
    for x in temp:
        mlist.append(x["msg"])
    data = {"messages":mlist }
    return jsonify(status=200, message="검색 성공", data=data)

# 특정 방 입장 시 메세지 로드
@search_messages.route('/room/<ruuid>', methods=['GET'])
def past_messages(ruuid):
    start = int(request.args["start"])
    num = int(request.args["num"])
    room_last_idx = list(mongo.mongo_db.messages.find( {"ruuid":ruuid }, {"msg": { "$slice": -1 }} ))
    room_last_idx = ((room_last_idx[0]).get("msg"))[0].get("midx")
    
    if start+num-1 > room_last_idx:
        s = start
        n = room_last_idx - start + 1
    else:
        s = start
        n = num
    some_data = list(mongo.mongo_db.messages.find( {"ruuid":ruuid}, {"msg": { "$slice": [s,n]}}))[0]
    msgs = some_data["msg"]
    
    mlist = []
    for msg in msgs:
        mlist.append(msg)
    data = { "messages": mlist}
    
    return jsonify(status=200, message="채팅방 입장시 메세지 일부 로드 성공", data=data)

# last_read_idx 갱신
@search_messages.route('/user-off', methods=['PUT'])
def update_last_read_idx(): 
    request_body = request.json
    uid =  request_body['uid']
    uuid = WSUser.get_current_user_uuid(uid)
    ruuid = request_body['ruuid']
    room_last_message_idx = request_body['room_last_message_idx']
    
    room_user_result = RoomUser.update_last_read_idx(uuid,ruuid,room_last_message_idx)

    user = RoomUserDto()
    user.uid = uid
    user.ruuid = room_user_result.ruuid
    user.rname = room_user_result.rname
    user.favorite_type = room_user_result.favorite_type
    user.push_notice = room_user_result.push_notice
    user.last_read_idx = room_user_result.last_read_idx
    
    data = {"user" : user.__dict__}
    return jsonify(status=200, message="채팅방 퇴장시 last_read_idx를 그 방의 last_msg_idx로 갱신", data=data)


@search_messages.route('/user-off-html', methods=['GET'])
def update_last_read_idx_html(): 
    return render_template('update.html')


@search_messages.route('/collection/room/<ruuid>', methods=['GET'])
def get_collections(ruuid):
    collection_type = request.args["type"]
    
    key2 = mongo.mongo_db.messages.aggregate( [
        {"$match": {"ruuid": ruuid}},
        {"$unwind" : "$msg"},
        {"$match": {"msg.type": collection_type.upper()}}
    ])

    temp = list(key2)
    mlist = []
    if collection_type == "image" or collection_type == "file":
        for x in temp:
            s3_url = x["msg"]["content"]
            file_name = s3_url.split("_")[4]
            created_at = x["msg"]["created_at"]
            file_dto = { "file_name" : file_name, "s3_url" : s3_url, "created_at" : created_at }
            mlist.append(file_dto)
        data = { "type" : collection_type, "collections" : mlist }  
        return jsonify(status=200, message="이미지, 파일 모아보기 성공", data=data)
          
    
    elif collection_type == "notify" or collection_type == "link":
        for x in temp:
            content = x["msg"]["content"]
            created_at = x["msg"]["created_at"]
            file_dto = { "content" : content, "created_at" : created_at }
            mlist.append(file_dto)
        data = { "type" : collection_type, "collections" : mlist}
        return jsonify(status=200, message="공지, 링크 모아보기 성공", data=data)

    else :
        return jsonify(status=400, message="유효하지 않은 type입니다.", data=data)