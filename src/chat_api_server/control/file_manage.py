import datetime
from flask import Flask, render_template,request, redirect, jsonify, Response
from flask import Blueprint
from werkzeug.utils import secure_filename
import os
from connection import s3_connection
from config import BUCKET_NAME

s3_file = Blueprint('upload',__name__)

@s3_file.route('/upload')
def render_file():
   return render_template('upload.html')

@s3_file.route('/file-upload', methods = ['POST'])
def upload_file():
   if request.method == 'POST':
        file = request.files['file']
        file_type = request.form["type"]
        user_id = request.form["uid"]
        
        if file:
            filename = secure_filename(file.filename) #.txt
            original_name = file.filename
            s3 = s3_connection()
            
            basename = file_type
            suffix = datetime.datetime.now().strftime("%y%m%d_%H%M%S")
            filename = "_".join([basename,user_id ,suffix,file.filename])
            # image_160827_135633_from_user

            if file_type == "IMAGE":
                ret = s3.put_object(Bucket = BUCKET_NAME, Body = file, Key = filename, ContentType = "image/png")
            elif file_type == "FILE":
                ret = s3.put_object(Bucket = BUCKET_NAME, Body = file, Key = filename)
            
            location = "ap-northeast-1"
            bucket_name = BUCKET_NAME
            key = filename
            url = "https://s3-%s.amazonaws.com/%s/%s" % (location, bucket_name, key)

            file_dto = {"type" : file_type, "s3_url" : url, "file_name" : original_name}
            data = {"file" : file_dto}
            return jsonify(status=200, message="s3에 저장되었습니다", data=data)
