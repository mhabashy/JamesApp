"""
The flask application package.
"""

from flask import Flask
app = Flask(__name__)
from flask_sqlalchemy import SQLAlchemy

app.secret_key = 'B0 Mike Zr98j/3yX Habashy R~XHH!jmN]LW/?,?RT'

import package.views
import package.api
import package.config

app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://{user}:{password}@{host}/{database}'.format(**config.localdb)