from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import *
# from sqlalchemy.orm import relationship
from datetime import datetime
import pandas as pd
import json

db = SQLAlchemy()

class WSUser(db.Model):
    __tablename__ = 'worksmile_users'
    
    uuid = db.Column(db.String(36, 'utf8mb4_unicode_ci'), primary_key=True, nullable=False)
    uid = db.Column(db.String(45, 'utf8mb4_unicode_ci'), nullable=False, default='')
    pwd = db.Column(db.String(60, 'utf8mb4_unicode_ci'), nullable=False, default='')
    salt = db.Column(db.String(45, 'utf8mb4_unicode_ci'), nullable=True, default='')
    uname = db.Column(db.String(20, 'utf8mb4_unicode_ci'), nullable=False, default='')
    profile = db.Column(db.String(255, 'utf8mb4_unicode_ci'), nullable=True)
    role = db.Column(db.String(50, 'utf8mb4_unicode_ci'), nullable=False, default='ROLE_NOT_PERMITTED')
    email = db.Column(db.String(100, 'utf8mb4_unicode_ci'), nullable=False, default='')
    phone = db.Column(db.String(11, 'utf8mb4_unicode_ci'), nullable=False, default='01000000000')
    register_at = db.Column(db.TIMESTAMP, nullable=False)
    login_at = db.Column(db.TIMESTAMP, nullable=True)
    all_push_notice = db.Column(db.String(3, 'utf8mb4_unicode_ci'), nullable=False, default='on')

    cid = db.Column(db.Integer,  db.ForeignKey('companys.cid'), nullable=False, default=0)
    subroot_cid = db.Column(db.Integer, db.ForeignKey('companys.cid'), nullable=False, default=0)
    root_cid = db.Column(db.Integer,  db.ForeignKey('companys.cid'), nullable=False, default=0)
    pid = db.Column(db.Integer, db.ForeignKey('positions.pid'), nullable=False, default=0)

    # relationship
    # rooms = relationship('Room', secondary='room_user', backref='users')
    # relationship() 설정 및 test 완료
    # to do : query refactoring

    def __init__(self):
        super().__init__()
    
    @classmethod
    def get_uname_with_uuid(model, uuid):
        uname = model.query.filter(model.uuid == uuid).with_entities(model.uname).first()[0]
        print(uname)
        return uname
 
    @classmethod
    def get_uname_with_uid(model, uid):
        uname = model.query.filter(model.uid == uid).with_entities(model.uname).first()[0]
        return uname

    @classmethod
    def get_user_with_uid(model, uid):
        user = model.query.filter(model.uid == uid).first()
        return user

    @classmethod
    def get_user_with_uuid(model, uuid):
        user = model.query.filter(model.uuid == uuid).first()
        return user

    @classmethod
    def get_users_with_uuids(model, uuidlist):
        users =  model.query.filter(model.uuid.in_(uuidlist))
        df = pd.read_sql(users.statement, users.session.bind)
        result = json.loads(df.to_json(orient='records'))

        return result
    @classmethod
    def get_current_user_subroot_id(model, user_id):
        current_user_company = model.query.filter(model.uid == user_id).with_entities(model.subroot_cid).all()
        result = current_user_company_result = (current_user_company[0])[0]
        return result
       
    @classmethod
    def get_users_with_uname_subid_except_uid(model, uname, subroot_cid, uid):
        users = model.query.filter(and_(model.uname.like(uname), (model.subroot_cid == subroot_cid),(model.uid != uid))).all()
        return users

    @classmethod
    def get_users_with_uname_subid_except_uid_role(model, uname, subroot_cid, uid):
        users = model.query.filter(and_(model.uname.like(uname), (model.subroot_cid == subroot_cid),(model.uid != uid),not_(model.role.like('ROLE_NOT%')))).all()
        return users

    @classmethod
    def get_current_user_uuid(model, user_id):
        ret = model.query.filter(model.uid == user_id).with_entities(model.uuid).first()
        return ret[0]

    @classmethod
    def get_uid_with_uuid(model, uuid):
        ret = model.query.filter(model.uuid == uuid).with_entities(model.uid).first()
        return ret[0]
    
    @classmethod
    def get_users_profile(model, uuids):
        ret = model.query.filter(model.uid.in_(uuids)).with_entities(model.profile).all()
        profiles = []
        for x in ret:
            profiles.append(x[0])
        return profiles
