from flask import Flask, render_template


global box_uri
app = Flask(__name__)

def bootstrap(abox_uri):
    global box_uri 
    box_uri = abox_uri
    return app


@app.route('/')
def index():
    
    return render_template("index.html", box_uri=box_uri);







