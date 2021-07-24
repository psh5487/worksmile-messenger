from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

from sqlalchemy import *
import pandas as pd
import json
import datetime

db = SQLAlchemy()

class ForbiddenWord(db.Model):

    __tablename__ = 'forbidden_words'
    
    wid = db.Column(db.Integer, primary_key=True, autoincrement=True)
    word = db.Column(db.String(100), default='')

    def __init__(self):
        super().__init__()

    @classmethod
    def get_all(model):
         words = model.query.all()
         return words