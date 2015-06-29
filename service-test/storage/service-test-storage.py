#!/usr/bin/env python
# __author__ = 'nherbaut'
import requests
import json
import time
import subprocess

import unittest

montebank_uri="http://localhost:2525/imposters"
storage_uri="http://localhost:8082/api/storage"

class SimpleTestCase(unittest.TestCase):


    def setUp(self):
        """Call before every test case."""
        self.mountebank=subprocess.Popen(["mb"])
        print "waiting 5s for mb to boot"
        time.sleep(5)

    def tearDown(self):
        self.mountebank.terminate()



    def testAll(self):




        stub_third_party={}
        stub_third_party["port"]=8081
        stub_third_party["protocol"]="http"

        print  "register stub on mb"
        r=requests.post(montebank_uri,data=json.dumps(stub_third_party))
        mountebank_imposter=r.headers['Location']
        assert r.status_code==201

        print "put data on the storage"
        r=requests.post(storage_uri+"/123/456",data="dummy")
        assert r.status_code==200
        time.sleep(1)

        print "check that we can retreive it"
        r=requests.get(storage_uri+"/123/456")
        assert r.status_code==200
        assert r.text=="dummy"

        time.sleep(1)
        print "check that box has been made aware of this new resource"
        r=requests.get(mountebank_imposter)
        assert r.status_code==200

        mb_response = json.loads(r.text)
        r_to_box=mb_response["requests"][0]
        assert r_to_box["method"]=="PUT"
        assert r_to_box["path"]=="/api/content/123/456"
        json_body=json.loads(r_to_box["body"].replace(u"\\",u"").replace("'","\""))
        assert json_body["url"].endswith("8082/api/storage/123/456")





if __name__ == "__main__":
    unittest.main() # run all tests
