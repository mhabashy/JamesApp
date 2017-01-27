from functools import wraps
from flask import request, json, Response
from package import app, config, database
import pymysql.cursors

from flask_cors import CORS, cross_origin

CORS(app, resources=r'/api/*')


def check_auth(username, password):
    return username == 'package' and password == 'josephpetermichael'
def authenticate():
    """Sends a 401 response that enables basic auth"""
    return Response(
    'No Access for you.\n'
    'You have to login with proper credentials', 401,
    {'WWW-Authenticate': 'Basic realm="Login Required"'})
def requires_auth(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        auth = request.authorization
        if not auth or not check_auth(auth.username, auth.password):
            return authenticate()
        return f(*args, **kwargs)
    return decorated


@app.route("/api/")
@requires_auth
def api():
    database.create_all()
    database.db.update(database.Users)
    return "OK"

@app.route("/api/users/<action>/", methods=['GET'])
@requires_auth
def users(action):
    data = {'status': 'fail'}
    try:
        if action == "insert":
            s = dict(request.args)
            s1 = {}
            for k, v in enumerate(s):
                s1[v] = s[v][0]
            u = database.Users(**s1)
            database.db.session.add(u)
            database.db.session.commit()
            data['lastid'] = u.id
            data['status'] = 'success'
        elif action == "select" and 'id' in request.args:
            s = database.Users.query.filter_by(id=int(request.args.get('id'))).first()
            data['data'] = s.Get_User_Info()
            #data['user'] = s.as_dict()
            data['status'] = 'success'
        elif action == "name" and 'id' in request.args:
            u = database.Users.query.filter_by(id=int(request.args.get('id'))).first()
            data['name'] = (str(u.fname) + " " + str(u.lname)).title()
            data['status'] = 'success'
        elif action == "passwordcheck" and 'email' in request.args:
            u = database.Users.query.filter_by(email=request.args.get('email')).first()
            data['bool'] = u.Check_Password(request.args.get('password'))
            data['status'] = 'success'
        else:
            data['data'] = {}
            data['status'] = 'success'
    except Exception as e:
        data['message'] = str(e)
    finally:
        return json.dumps(data)