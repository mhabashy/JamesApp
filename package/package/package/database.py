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

# Users table

class Users(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(255), unique=True)
    password = db.Column(db.String(255))
    firstname = db.Column(db.String(100))
    lastname = db.Column(db.String(100))
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
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<Users %r:% r>' % (self.id, self.lname)

# Rider table

class Riders(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(255), unique=True)
    rating = db.Column(db.Integer)
    rider_x = db.Column(db.String(255))
    rider_y = db.Column(db.String(255))
    rider_address = db.Column(db.String(500))
    ride_id = db.relationship('Ride', backref='Riders', lazy='dynamic', primaryjoin="Riders.id==Ride.rider_id")
    #ride_rider_x = db.relationship('Ride', backref='Riders', lazy='dynamic', primaryjoin="Riders.rider_x==Ride.rider_x")
    #ride_rider_y = db.relationship('Ride', backref='Riders', lazy='dynamic', primaryjoin="Riders.rider_y==Ride.rider_y")
    #ride_rider_rating = db.relationship('Ride', backref='Riders', lazy='dynamic', primaryjoin="Riders.rating==Ride.rider_rating")
    credit_rider_id = db.relationship('Credit', backref='Riders', lazy='dynamic', primaryjoin="Credit.rider_id==Riders.id")
    PayPal_rider_id = db.relationship('PayPal', backref='Riders', lazy='dynamic', primaryjoin="PayPal.rider_id==Riders.id")

    def __init__(self, email, rating, rider_x, rider_y, rider_address):
        self.email = email
        self.rating = rating
        self.rider_x = rider_x
        self.rider_y = rider_y
        self.rider_address  = rider_address

    def Get_Rider_Info(self):
        return {
                    'id': self.id,
                    'email': self.email,
                    'rating': self.rating,
                    'rider_x': self.rider_x,
                    'rider_y': self.rider_y,
                    'rider_address': self.rider_address
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<Riders %r:% r>' % (self.id, self.email)

# Driver table
class Drivers(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(255), unique=True)
    rating = db.Column(db.Integer)
    driver_x = db.Column(db.String(255))
    driver_y = db.Column(db.String(255))
    driver_address = db.Column(db.String(500))
    car_type = db.Column(db.String(255))
    active = db.Column(db.Boolean, default=False, nullable=False)
    ride_driver_rating = db.relationship('Ride', backref='Drivers', lazy='dynamic', primaryjoin="Drivers.rating==Ride.driver_rating")
    ride_driver_id = db.relationship('Ride', backref='Drivers', lazy='dynamic', primaryjoin="Drivers.id==Ride.driver_id")

    def __init__(self, email, rating, driver_x, driver_y, driver_address, car_type, active):
        self.email = email
        self.rating = rating
        self.driver_x = driver_x
        self.driver_y = driver_y
        self.driver_address = driver_address
        self.car_type = car_type
        self.active = active

    def Get_Driver_Info(self):
        return {
                    'id': self.id,
                    'email': self.email,
                    'rating': self.rating,
                    'driver_x': self.driver_x,
                    'driver_y': self.driver_y,
                    'driver_address': self.driver_address,
                    'car_type': self.car_type,
                    'active': self.active
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<Drivers %r:% r>' % (self.id, self.email)


# Admin table

class Admin(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(255), unique=True)

    def __init__(self, email):
        self.email = email

    def Get_Admin_Info(self):
        return {
                    'id': self.id,
                    'email': self.email
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<Admin %r:% r>' % (self.id, self.email)


# Package table

class Package(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    size = db.Column(db.String(100))
    height = db.Column(db.Float)
    width = db.Column(db.Float)
    price = db.Column(db.Float)

    def __init__(self, size, height, width, price):
        self.size = size
        self.height = height
        self.width = width
        self.price = price

    def Get_Package_Info(self):
        return {
                    'id': self.id,
                    'size': self.size,
                    'height': self.height,
                    'width': self.width,
                    'price': self.price
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<Package %r:% r>' % (self.id)

# Ride table

class Ride(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    rider_id = db.Column(db.Integer, db.ForeignKey('riders.id'))
    driver_id = db.Column(db.Integer, db.ForeignKey('drivers.id'))
    pickup_time = db.Column(db.DateTime, default=db.func.now())
    drop_off_time = db.Column(db.DateTime, default=db.func.now())
    #rider_x = db.Column(db.String(255), db.ForeignKey('riders.rider_x'))
    #rider_y = db.Column(db.String(255), db.ForeignKey('riders.rider_y'))
    drop_off_x = db.Column(db.String(255))
    drop_off_y = db.Column(db.String(255))
    drop_off_address = db.Column(db.String(500))
    ride_type = db.Column(db.String(200))
    #rider_rating = db.Column(db.Integer, db.ForeignKey('riders.rating'))
    #driver_rating = db.Column(db.Integer, db.ForeignKey('drivers.rating'))

    def __init__(self, rider_id, driver_id, pickup_time, drop_off_time, drop_off_x ,drop_off_y, drop_off_address , ride_type, rider_rating, driver_rating):
        self.rider_id = rider_id
        self.driver_id = driver_id
        self.pickup_time = pickup_time
        self.drop_off_time = drop_off_time
        self.drop_off_x = drop_off_x
        self.drop_off_y = drop_off_y
        self.drop_off_address = drop_off_address
        self.ride_type = ride_type
        self.rider_rating = rider_rating
        self.driver_rating = driver_rating

    def Get_Ride_Info(self):
        return {
                    'id': self.id,
                    'rider_id': self.rider_id,
                    'driver_id': self.driver_id,
                    'pickup_time': self.pickup_time,
                    'drop_off_time': self.drop_off_time,
                    'rider_x': self.rider_x,
                    'rider_y': self.rider_y,
                    'drop_off_x': self.drop_off_x,
                    'drop_off_y':self.drop_off_y,
                    'drop_off_address': self.drop_off_address,
                    'ride_type': self.ride_type,
                    'rider_rating': self.rider_rating,
                    'driver_rating': self.driver_rating
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<Ride %r:% r>' % (self.id)

# Car table

class Car(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    model = db.Column(db.String(200))
    plate_number = db.Column(db.String(250))
    driver_id = db.Column(db.String(250))
    car_type = db.Column(db.String(250))
    car_year = db.Column(db.String(20))

    def __init__(self, model, plate_number, driver_id, car_type, car_year):
        self.id = id
        self.model = model
        self.plate_number = plate_number
        self.driver_id = driver_id
        self.car_type = car_type
        self.car_year = car_year

    def Get_Car_Info(self):
        return {
                    'id': self.id,
                    'model': self.model,
                    'plate_number': self.plate_number,
                    'driver_id': self.driver_id,
                    'car_type': self.car_type,
                    'car_year': self.car_year
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<Car %r:% r>' % (self.id)

# Motorcycle table

class Motorcycle(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    model = db.Column(db.String(200))
    plate_number = db.Column(db.String(250))
    driver_id = db.Column(db.String(250))
    motorcycle_type = db.Column(db.String(250))
    motorcycle_year = db.Column(db.String(20))

    def __init__(self, model, plate_number, driver_id, motorcycle_type, motorcycle_year):
        self.model = model
        self.plate_number = plate_number
        self.driver_id = driver_id
        self.car_type = motorcycle_type
        self.car_year = motorcycle_year

    def Get_Motorcycle_Info(self):
        return {
                    'id': self.id,
                    'model': self.model,
                    'plate_number': self.plate_number,
                    'driver_id': self.driver_id,
                    'motorcycle_type': self.motorcycle_type,
                    'motorcycle_year': self.motorcycle_year
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<Motorcycle %r:% r>' % (self.id)

# Bike table

class Bike(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    model = db.Column(db.String(200))
    driver_id = db.Column(db.String(250))

    def __init__(self, model, driver_id):
        self.model = model
        self.driver_id = driver_id

    def Get_Bike_Info(self):
        return {
                    'id': self.id,
                    'model': self.model,
                    'driver_id': self.driver_id,
                    'motorcycle_year': self.motorcycle_year
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<Bike %r:% r>' % (self.id)

# Credit table

class Credit(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    card_number = db.Column(db.String(100))
    civ = db.Column(db.Integer)
    card_name = db.Column(db.String(100))
    card_zipcode = db.Column(db.String(100))
    card_valid_date = db.Column(db.String(100))
    rider_id = db.Column(db.Integer, db.ForeignKey('riders.id'))


    def __init__(self, card_number, civ, card_name, card_zipcode, card_valid_date, rider_id):
        self.card_number = card_number
        self.civ = civ
        self.card_name = card_name
        self.card_zipcode = card_zipcode
        self.card_valid_date = card_valid_date
        self.rider_id = rider_id

    def Get_Bike_Info(self):
        return {
                    'id': self.id,
                    'card_number':self.card_number,
                    'civ': self.civ,
                    'card_name':self.card_name,
                    'card_zipcode':self.card_zipcode,
                    'card_valid_date':self.card_valid_date,
                    'rider_id':self.rider_id
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<Credit %r:% r>' % (self.id)


# PayPal table

class PayPal(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(100))
    password = db.Column(db.String(100))
    rider_id = db.Column(db.Integer, db.ForeignKey('riders.id'))


    def __init__(self, username, password, rider_id):
        self.username = username
        self.password = password
        self.rider_id = rider_id

    def Get_PayPal_info(self):
        return {
                    'id': self.id,
                    'password':self.password,
                    'username':self.username,
                    'rider_id':self.rider_id
                }
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.Columns}

    def __repr__(self):
        return '<PayPal %r:% r>' % (self.id)

if __name__ == "__main__":
    db.drop_all()
    db.create_all()
    print("CHECKED")
