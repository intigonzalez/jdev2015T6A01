
import subprocess
from celery import Celery

app = Celery('tasks', backend='amqp', broker='amqp://')

@app.task(ignore_result=True)
def print_hello():
    print 'hello there'

#Generate prime numbers (just a test)
@app.task
def gen_prime(x):
    multiples = []
    results = []
    for i in xrange(2, x+1):
        if i not in multiples:
            results.append(i)
            for j in xrange(i*i, x+1, i):
                multiples.append(j)
    return results

@app.task
def print_shell(in_file, out_file):
	print in_file
	subprocess.call("/home/user/workspace/Media-home/coreVideo/video.sh  " + in_file + " " + out_file, shell=True)
	print out_file

@app.task
def add(x, y):
    return x + y


@app.task
def test():
    #subprocess.call("/Users/chippekclement/Documents/workspace/Test_celery/copy.sh", shell=True)
    print 'test'
