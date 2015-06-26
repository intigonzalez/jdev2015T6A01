from flask import Flask, request, redirect, url_for, \
    render_template
from flask import session
from flask import abort
import json

from settings import SECRET_KEY


app = Flask(__name__)
app.config.from_object(__name__)
app.debug = True


@app.route('/')
def index():

    return redirect(url_for('static', filename='index.html'), code=302)


if __name__ == '__main__':
    app.secret_key = SECRET_KEY
    app.run()



