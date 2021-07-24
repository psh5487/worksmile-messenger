from flask_sqlalchemy import SQLAlchemy

from sqlalchemy import *
import pandas as pd
import json
from datetime import datetime as dtime
import uuid
from flask import Flask, render_template,request, redirect, jsonify, Response
db = SQLAlchemy()
from model.wsuser import *
from model.room_user import *
class Room(db.Model):
    __tablename__ = 'rooms'
   
    ruuid = db.Column(db.String(50, 'utf8mb4_unicode_ci'), primary_key=True, nullable=False)
    memcnt = db.Column(db.Integer, nullable=False, default=0)
    register_at = db.Column(db.TIMESTAMP, default=dtime.today().strftime("%Y/%m/%d %H:%M:%S"))
    update_at = db.Column(db.TIMESTAMP,default=dtime.today().strftime("%Y/%m/%d %H:%M:%S"))
    room_type = db.Column(db.String(30, 'utf8mb4_unicode_ci'), nullable=False, default='')
    init_name = db.Column(db.String(50, 'utf8mb4_unicode_ci'), nullable=False, default='')
    
    leader_id = db.Column(db.String(45, 'utf8mb4_unicode_ci'), db.ForeignKey('worksmile_users.uid'), nullable=False, default='')
    root_cid = db.Column(db.Integer, db.ForeignKey('companys.cid'), db.ForeignKey('companys.cid'), nullable=False, default=0)

    def __init__(self, ruuid, leader_id, room_type, init_name, memcnt, root_cid):
        self.ruuid = ruuid
        self.leader_id =leader_id
        self.room_type = room_type
        self.memcnt = memcnt
        self.init_name = init_name
        self.root_cid = root_cid

    @classmethod 
    def get_room_with_ruuid(model, ruuid):
        room = model.query.filter(model.ruuid == ruuid).first()
        return room

    @classmethod
    def get(model, ruuid): 
        room = model.query.filter(model.ruuid == ruuid)
        df = pd.read_sql(room.statement, room.session.bind)
        result = json.loads(df.to_json(orient='records'))[0]
        return result

    @classmethod
    def get_rname_with_ruuid(model, ruuid):
        rname = model.query.filter(model.ruuid == ruuid).with_entities(model.init_name).first()[0]
        return rname
    
    @classmethod
    def set_roomType_with_ruuid(model, ruuid, room_type):
        room = model.query.filter(model.ruuid == ruuid).first()
        room.room_type = room_type
        db.session.commit()
   
    @classmethod
    def create(model, leader_id, userlist, room_type, room_name, memcnt, cid):
        ruuid = str(uuid.uuid4())
        room = model(ruuid=ruuid, leader_id=leader_id, room_type=room_type, init_name=room_name, memcnt=memcnt, root_cid=cid)
        db.session.add(room)
        db.session.commit()
        return room
        
    @classmethod
    def get_public_rooms_with_cid_rname(model, cid, rname):
        public_rooms = model.query.filter(and_(model.init_name.like(rname), model.root_cid == cid, model.room_type == 'public')).all()
        return public_rooms

    @classmethod
    def get_public_rooms_with_cid(model, cid):
        public_rooms = model.query.filter(and_(model.root_cid == cid, model.room_type == 'public')).all()
        return public_rooms

    @classmethod
    def set_memcnt_with_ruuid(model, ruuid, count):
        room = model.query.filter(model.ruuid == ruuid).first()
        room.memcnt = room.memcnt + count
        db.session.commit()
        return room.memcnt
    
    @classmethod
    def delete_with_ruuid(model, ruuid):
        room = model.query.filter(model.ruuid == ruuid).delete()
        db.session.commit()

    @classmethod
    def get_room_leader_uid_with_ruuid(model, ruuid):
        uid = model.query.filter(model.ruuid == ruuid).with_entities(model.leader_id).first()[0]
        return uid
