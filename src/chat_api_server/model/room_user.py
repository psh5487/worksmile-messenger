from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

from sqlalchemy import *
import pandas as pd
import json
import datetime
from model.wsuser import *
db = SQLAlchemy()

class RoomUser(db.Model):
    __tablename__ = 'room_user'
    
    rname = db.Column(db.String(50, 'utf8mb4_unicode_ci'), nullable=False, default='')
    favorite_type = db.Column(db.String(3, 'utf8mb4_unicode_ci'), nullable=False, default="off")
    push_notice = db.Column(db.String(3, 'utf8mb4_unicode_ci'), nullable=False, default="on")
    last_read_idx = db.Column(db.Integer, nullable=False, default=0)
    
    uuid = db.Column(db.String(36,'utf8mb4_unicode_ci'), db.ForeignKey('worksmile_users.uuid'),  primary_key=True,nullable=False)
    ruuid = db.Column(db.String(50, 'utf8mb4_unicode_ci'), db.ForeignKey('rooms.ruuid'), primary_key=True, nullable=False)

    def __init__(self, uuid, ruuid, rname, favorite_type, push_notice, last_read_idx):
        self.uuid = uuid
        self.ruuid = ruuid
        self.rname = rname
        self.favorite_type = favorite_type
        self.push_notice = push_notice
        self.last_read_idx = last_read_idx

    @classmethod
    def create(model, uuid, ruuid, rname):
        room_user = model(uuid=uuid, ruuid=ruuid, rname=rname, favorite_type="off", push_notice="on", last_read_idx=0)
        db.session.add(room_user)
        db.session.commit()
        
        
    @classmethod
    def get_ranme_favorite_push_last_with_ruuid(model, ruuid):
        result = model.query.filter(model.ruuid == ruuid).with_entities(model.rname, model.favorite_type, model.push_notice, model.last_read_idx).first()
        return result

    @classmethod
    def update_last_read_idx(model, uuid, ruuid,room_last_message_idx):
        row = model.query.filter(and_(model.uuid == uuid, model.ruuid ==ruuid)).all()[0]
        row_dict = dict(uuid=row.uuid, ruuid=row.ruuid, rname=row.rname, favorite_type=row.favorite_type, push_notice=row.push_notice,last_read_idx=row.last_read_idx)
        row.last_read_idx = room_last_message_idx
        db.session.commit()
        result = model.query.filter(and_(model.uuid == uuid, model.ruuid ==ruuid)).first()
        return result
    
    @classmethod
    def get_user_rooms_with_uuid(model, uuid):
        rooms = model.query.filter(model.uuid == uuid).all()
        return rooms
    
    @classmethod
    def get_user_rooms(model, uuid):
        rooms = model.query.filter(model.uuid == uuid)
        df = pd.read_sql(rooms.statement, rooms.session.bind)
        result = json.loads(df.to_json(orient='records'))
        return result
    
    @classmethod
    def get_user_ruuids(model, uuid):
        ruuids = model.query.filter(model.uuid == uuid).with_entities(model.ruuid).all()
        rlist = []
        for x in ruuids:
            rlist.append(x[0])
        return rlist
    
    @classmethod
    def get_users(model, ruuid):
        uuids = model.query.filter(model.ruuid==ruuid).with_entities(model.uuid).all()
        uuidlist = []
        for uuid in uuids:
            uuidlist.append(uuid[0])
        users =  WSUser.get_users_with_uuids(uuidlist)
        return users

    @classmethod
    def get_users_uuid(model, ruuid):
        objects = model.query.filter(model.ruuid==ruuid).all()
        uids =[]
        for obj in objects:
            uids.append(WSUser.get_uid_with_uuid(obj.uuid))
        return uids

    @classmethod
    def get_uuids_with_ruuid(model, ruuid):
        uuids = model.query.filter(model.ruuid==ruuid).with_entities(model.uuid).all()
        uuidlist = []
        for uuid in uuids:
            uuidlist.append(uuid[0])
        return uuidlist

    @classmethod
    def get_room_with_ruuid_uuid(model, ruuid, uuid):
        room_user = model.query.filter(and_(model.ruuid == ruuid, model.uuid == uuid)).first()
        return room_user

    @classmethod
    def get_rooms_with_rname_uuid(model, rname, uuid):
        rooms = model.query.filter(and_(model.rname.like(rname), model.uuid == uuid)).all()
        return rooms

    @classmethod
    def delete_with_ruuid_uuid(model, ruuid, uuid):
        room_user = model.query.filter(and_(model.ruuid == ruuid, model.uuid == uuid)).delete()
        db.session.commit()
        return room_user
    
    @classmethod
    def delete_with_ruuid(model, ruuid):
        room_user = model.query.filter(model.ruuid == ruuid).delete()
        db.session.commit()

    @classmethod 
    def get_last_read_idx_with_uuid_ruuid(model, uuid, ruuid):
        last_read_idx = model.query.filter(and_(model.ruuid == ruuid, model.uuid == uuid)).with_entities(model.last_read_idx)[0][0]
        return last_read_idx
