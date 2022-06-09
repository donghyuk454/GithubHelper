#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from sre_constants import SUCCESS
from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String

USER = "postgres"
PW = ""
URL = "database-1.cfc4ih6iawnd.ap-northeast-2.rds.amazonaws.com"
PORT = "5432"
DB = "postgres"

engine = create_engine("postgresql://{}:{}@{}:{}/{}".format(USER, PW, URL,PORT, DB))

db_session = scoped_session(sessionmaker(autocommit=False, autoflush=False, bind=engine))

Base = declarative_base()
Base.query = db_session.query_property()

class User(Base):
    __tablename__ = 'user'
    id = Column(Integer, primary_key=True)
    user_id = Column(String(100), unique=True)
    github_id = Column(String(100), unique=False)
    friends = Column(String(500), unique=False)

    def __init__(self, user_id=None, github_id=None):
        self.user_id = user_id
        self.github_id = github_id
        self.friends = ""
    
    def __repr__(self):
        return f'<User {self.user_id!r}>'
# Base.metadata.drop_all(bind=engine)
Base.metadata.create_all(bind=engine)

from pydoc import resolve
from flask import Flask
from flask import request
from flask import jsonify
import requests as req

# This line is for solving flask's CORS error
# You need 'pip install flask_cors' to use flask_cors
#from flask_cors import CORS
from werkzeug.serving import WSGIRequestHandler
import json
WSGIRequestHandler.protocol_version = "HTTP/1.1"

app = Flask(__name__)

# This line is for solving flask's CORS error
#CORS(app)

# https://api.github.com/users/donghyuk454

def checkIsUser(id):
    response = req.get("https://api.github.com/users/"+id).json()
    
    if 'message' not in response.keys():
        return True
    return False

def findUser(user_id):
    result = db_session.query(User).all()
    
    for i in result:
        if i.user_id == user_id:
            return i

    return None

@app.route("/", methods=['GET'])
def test():
    return jsonify(success=True)

# 사용자 추가
@app.route("/user", methods=['POST'])
def addUser():
    content = request.get_json(silent=True)
    user_id = content["user_id"]
    github_id = content["github_id"]

    if db_session.query(User).filter_by(user_id=user_id).first() is None:
        if checkIsUser(github_id):
            u = User(user_id=user_id, github_id=github_id)
            db_session.add(u)
            db_session.commit()
            return jsonify(success=True)
        else:
            return jsonify(success=False, msg="Check your github id")
    else:
        return jsonify(success=False, msg="Check your id, there is a same user")

# 로그인
@app.route("/login", methods=['POST'])
def login():
    content = request.get_json(silent=True)
    user_id = content["user_id"]

    if findUser(user_id=user_id) == None:
        return jsonify(success=False, msg="Check your id")
    else:
        return jsonify(success=True)

# 유저 정보 조회
@app.route("/user", methods=['GET'])
def get_my_info():
    user_id = str(request.args.get('user_id'))

    ret = {}

    user = findUser(user_id)
    github_id = user.github_id
    friends = user.friends.split(" ")
    friends.pop(-1)
    ret["friends"] = friends
    
    response = req.get("https://api.github.com/users/"+github_id).json()

    if response != None:
        res = {}
        res['html_url'] = response['html_url']
        res['avatar_url'] = response['avatar_url']
        res['name'] = response['name']
        if response['name'] == None:
            res['name'] = user.github_id

        ret["github"] = res
        ret['success'] = True

        response2 = list(req.get(response['repos_url']).json())

        print(len(response2))

        dataList = []

        for res in response2:
            data = {}
            data['name'] = res['name']
            data['full_name'] = res['full_name']
            data['html_url'] = res['html_url']
            data['description'] = res['description']
            dataList.append(data)
        
        ret['projects'] = dataList

        return jsonify(ret)

    return jsonify(success=False, msg="Check your login")

# 친구들 정보 조회 name
@app.route("/friends", methods=['GET'])
def get_friends():
    user_id = str(request.args.get('user_id'))

    friends = findUser(user_id).friends.split(" ")
    result = []
    print(friends)
    for friend in friends:
        if friend != '':
            response = req.get("https://api.github.com/users/"+friend).json()
            ret = {}
            ret['success'] = True
            ret['friend_id'] = friend
            ret['html_url'] = response['html_url']
            ret['avatar_url'] = response['avatar_url']
            ret['name'] = response['name']

            if response['name'] == None:
                ret['name'] = friend

            response2 = list(req.get(response['repos_url']).json())

            dataList = []

            for res in response2:
                data = {}
                data['name'] = res['name']
                data['full_name'] = res['full_name']
                data['html_url'] = res['html_url']
                data['description'] = res['description']
                dataList.append(data)
                
            ret['projects'] = dataList
            result.append(ret)
    
    return jsonify(result)


# 친구 추가
@app.route("/friend", methods=['POST'])
def add_friend():
    content = request.get_json(silent=True)

    u_id = content["user_id"]
    f_id = content["friend_id"]

    if checkIsUser(f_id):
        user = db_session.query(User).filter_by(user_id=u_id).first()
        
        friends = user.friends.split(" ")
        if f_id in friends:
            return jsonify(success=False, msg="Friend id that already exists")

        if f_id == user.user_id:
            return jsonify(success=False, msg="you can't add you as friend")
        
        user.friends += f_id + " "
        db_session.commit()
        return jsonify(success=True)
    else:
        return jsonify(success=False, msg="Check friend's id")


if __name__ == "__main__":
    app.run(host='localhost', port=8888)