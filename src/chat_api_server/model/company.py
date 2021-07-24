from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

from sqlalchemy import *
import pandas as pd
import json
import datetime

db = SQLAlchemy()

class Company(db.Model):
    __tablename__ = 'companys'
    
    cid = db.Column(db.Integer, primary_key=True, nullable=False) # autoincrement??
    cname = db.Column(db.String(50),nullable=False)
    is_subroot = db.Column(db.String(1),nullable=False, default='N')

    # relationship
    #wsuser = relationship("WSUser", backref='workers', lazy=True)
    
    def __init__(self):
        super().__init__()
   
    @classmethod
    def get_cid(model, cname):
        cid = model.query.filter(model.cname == cname).with_entities(model.cid).first()
        return cid[0]

    @classmethod
    def get_cname_with_cid(model, cid):
        cname = model.query.filter(model.cid == cid).with_entities(model.cname).first()[0]
        return cname
