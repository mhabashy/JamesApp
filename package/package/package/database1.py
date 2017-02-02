from functools import wraps
from flask import request, json, Response
from package import app, config
import pymysql.cursors
from flask_sqlalchemy import SQLAlchemy
import datetime
import hashlib

app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False


def GetPassword(value):
    m = hashlib.md5()
    m.update((value+config.psolt).encode("utf-8"))
    return m.hexdigest()

db = SQLAlchemy(app)


class Users(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(255), unique=True)
    password = db.Column(db.String(255))
    fname = db.Column(db.String(100))
    lname = db.Column(db.String(100))
    created = db.Column(db.DateTime, default=datetime.datetime.utcnow)
    temp_password = db.Column(db.String(255))

    def __init__(self, email, password, fname, lname):
        self.email = email
        self.password = GetPassword(password)
        self.fname = fname
        self.lname = lname

    def Set_Password(self, newPassword):
        self.password = GetPassword(newPassword)

    def Check_Password(self, checkPassword):
        return GetPassword(checkPassword) == self.password

    def Get_User_Info(self):
        return {
                    'id': self.id,
                    'fname': self.fname,
                    'lname': self.lname,
                    'email': self.email,
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.columns}

    def __repr__(self):
        return '<Users %r:%r>' % (self.id, self.lname)

if __name__ == "__main__":
    db.drop_all()
    db.create_all()

    db.update(Users)
    print("CHECKED")