from flask import Flask, render_template

import sys


global box_uri
app = Flask(__name__)

def bootstrap(abox_uri):
    global box_uri 
    box_uri = abox_uri
    return app


@app.route('/')
def index():    
    return render_template("index.html", box_uri=box_uri);

if __name__ == "__main__":
    print "Box address", sys.argv[1] # box_uri 
    bootstrap(sys.argv[1])
    app.run(host="0.0.0.0", port=8080)







