from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

from sqlalchemy import *
import pandas as pd
import json
import datetime
# from sqlalchemy.orm import relationship

db = SQLAlchemy()

class Position(db.Model):
    __tablename__ = 'positions'
    
    pid = db.Column(db.Integer,primary_key=True, autoincrement=True,default='')
    pname = db.Column(db.String(50))
    
    # relationship
    # users = relationship('WSUser', backref='position')
    
    def __init__(self):
        super().__init__()

    @classmethod
    def get_pname_with_pid(model, pid):
        pname = model.query.filter(model.pid == pid).with_entities(model.pname).first()[0]
        
        return pname
