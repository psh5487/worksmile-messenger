'''
샘플 쿼리 및 테스트
'''
# 인서트
count = db.messages.find({"room":3},  {"$exists": True} ).count()
if count >=1:
    print("있음")
else:
    print("없음")

message ={"room":1, "msg":[{"mid":0,"content":"배아파요..", "created_at":str(dtime.today().strftime("%Y/%m/%d %H:%M:%S"))}]}
dbResponse = db.messages.insert_one(message)
print(dbResponse.inserted_id)

# 가져오기
data = list(db.messages.find())
for user in data:
    user["_id"] = str(user["_id"])
print(json.dumps(data))

# 수정하기
dbResponse = db.messages.update_one(
    {"room":0},
    {"$push":{"msg":{"mid":1,"content":"hello guys", "created_at":str(dtime.today().strftime("%Y/%m/%d %H:%M:%S"))}}}
)
dbResponse = db.messages.update_one(
    {"room":0},
    {"$push":{"msg":{"mid":2,"content":"what are you do", "created_at":str(dtime.today().strftime("%Y/%m/%d %H:%M:%S"))}}}
)
dbResponse = db.messages.update_one(
    {"room":0},
    {"$push":{"msg":{"mid":3,"content":"what?", "created_at":str(dtime.today().strftime("%Y/%m/%d %H:%M:%S"))}}}
)
dbResponse = db.messages.update_one(
    {"room":0},
    {"$push":{"msg":{"mid":4,"content":"hungry?", "created_at":str(dtime.today().strftime("%Y/%m/%d %H:%M:%S"))}}}
)
dbResponse = db.messages.update_one(
    {"room":0},
    {"$push":{"msg":{"mid":5,"content":"happy", "created_at":str(dtime.today().strftime("%Y/%m/%d %H:%M:%S"))}}}
)
dbResponse = db.messages.update_one(
    {"room":0},
    {"$push":{"msg":{"mid":6,"content":"sad", "created_at":str(dtime.today().strftime("%Y/%m/%d %H:%M:%S"))}}}
)
for attr in dir(dbResponse):
    print(f"*****{attr}****")


# 마지막 메세지 가져오기
last_data = list(db.messages.find( {"room":0}, { "msg": { "$slice": -1 } } ))

for user in last_data:
    user["_id"] = str(user["_id"])
    print(json.dumps(last_data))

# mid 3인 것부터 5개 가져오기 : 한채팅방에서 api 호출시!
room_list = [0, 1]
some_data = list(db.messages.find( {"room":0}, {"msg": { "$slice": [3,5]}})) #[a,b] : 인덱스a-1부터 b개

for user in some_data:
    user["_id"] = str(user["_id"])
    print(json.dumps(some_data))

# 랜딩페이지에서 각 방의 마지막 메세지 객체호출시
landing_data = db.messages.find( {"room":{ "$in": [0, 1]}}, { "msg": { "$slice": -1 } })

print("=======")
print(landing_data)
print("#####")
print(list(landing_data))
for user in landing_data:
    user["_id"] = str(user["_id"])
    print(json.dumps(landing_data))

# 메세지 검색 0번 방에서 'h'라는 단어가 포함된 메세지를 모두 가져와라
#db.messages.find({"room":0},{"$text": {"$search": "내일 피시방"}},{"score":{"$meta": "textScore"}}).sort({score:{$meta:"textScore"}})

#print(">>>>>>",db.messages.indexs())

# 인덱스 생성
#db.messages.create_index([('content', 'text')]) 
# keys = list(db.messages.find({"$text": {"$search": "\"hi\""} }).sort([("created_at",1)]))
#keys = list(db.messages.find({"$text": {"$search": "배아파요.."} }))

#keys=list(db.messages.find({"room":0},{"msg.content": {"$regex": "hi man"}}))
#keys = list(db.messages.find( { "$and" : [ {"room":0}, {"msg": {"content":"hello guys"} } ] }))

#keys = list(db.messages.find( { "$and" : [ {"room":0}, {"msg.content":"hello guys"}] }, {"msg.mid": 1}))
#keys = list(db.messages.find( { "$and" : [ {"room":0}, {"msg.content":"hello guys"} ]}, {"msg":"$slice":}}))

dbResponse = db.messages.update_one(
    {"room":1},
    {"$push":{"msg":{"mid":1,"content":"hi man", "created_at":str(dtime.today().strftime("%Y/%m/%d %H:%M:%S"))}}}
)

keyword = "what"
keys = list(db.messages.find(
    {   
        "$and": [
            {
                "msg":
                            {"$elemMatch":{"content": {"$regex": keyword} } }               
            },
            {
                "room":0
            }
        ]
        
    },
    {
        "room" : True,
        "msg" : {"$elemMatch":{"content": {"$regex": keyword} } },
        "mid":True,
        "content":True,
        "created_at":True
    }
))

for user in keys:
    user["_id"] = str(user["_id"])
    print(json.dumps(keys))



db.zip.aggregate(
  {$match: {zipcode: 63109}},
  {$unwind: "$students"},
  {$match: {"students.school": 102}}
)

key2 = db.messages.aggregate(
    {"$match": {"room":0}},
    {"$unwind" : "$msg"},
    {"$match":{"msg.content":"what?"}}
)

#keys = list(db.messages.find( { "$and" : [ {"room":0}, {"msg.content":"hello guys"}] }, {"msg.mid": 1}))

key2 = list(db.messages.find(
     {
        "$and" : [
            {"room":0},
            {"msg": {
                "$elemMatch" : {
                                "content" : "hello guys"
                }
                }
            }
        ]
        },
        {"msg": 1}

))


keyword = "at"
key2 = db.messages.aggregate( [
    {"$match": {"room":0}},
    {"$unwind" : "$msg"},
    {"$match": {"msg.content":{"$regex": keyword}}}
])

#db.messages.find( { "room":0, "msg":{ "$elemMatch": { "content":"hello guys" } }, { "msg": 1 })



# 그방의 마지막 메세지 인덱스 가져오기
room_last_idx = list(db.messages.find( {"room":0 }, {"msg": { "$slice": -1 }} ))
#index = db.messages.aggregate( 
#    {"$match": {"room":0}},
#    {"$project": { "count": { "$size":"$msg" }}})
#print("room_last_idx>>>>>",room_last_idx)

print(type(room_last_idx[0]))
print(room_last_idx[0])
print(((room_last_idx[0]).get("msg"))[0].get("mid"))


'''
기본 데이터 넣어놓음. 주석처리 필요.
x = mycol.insert_one({"name":"bjy", "age":3})
ms1 = mycol.insert_one({"ruuid":"ruuid01", "msg_info":[{"midx":"0", "content":"안녕 애들아", "created_at":dtime.today().strftime("%Y/%m/%d %H:%M:%S")}]})
ms2 = mycol.insert_one({"ruuid":"ruuid02", "msg_info":[{"midx":"0", "content":"단톡방입니다..", "created_at":dtime.today().strftime("%Y/%m/%d %H:%M:%S")}]})
ms3 = mycol.insert_one({"ruuid":"ruuid03", "msg_info":[{"midx":"0", "content":"피곤해.", "created_at":dtime.today().strftime("%Y/%m/%d %H:%M:%S")}]})

만약 방이 있다면, 배열에 추가. 없다면, 새로운 row 생성
mycol.update_one(
   {"ruuid":"ruuid03"}, {"$push": {"midx":1, "content":"뭐해?","created_at":dtime.today().strftime("%Y/%m/%d %H:%M:%S")}})

resp = user.find_one_and_update(
   {"worker_name": result['worker_name']},
   { '$push': {'detection' : result} },
   return_document = ReturnDocument.AFTER)
print(x.inserted_id)
list = mycol.find()
for x in list:
   print(x)

사용자가 속한 방들인 ruuid 인 메세지 객체들 전체
for문 by 방 개수만큼

print(x['_id'],"-",x['name'],"-",x['age'])
'''