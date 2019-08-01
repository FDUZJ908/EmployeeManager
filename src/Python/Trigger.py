import os
import sys
import time
import urllib
import urllib2
import ssl

context = ssl._create_unverified_context()

flog = open("/root/Log/trigger_py.log", "a")
time_str = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
flog.write(time_str+"\n")
try:
    url = "http://localhost/wechat/%s?password=%s" % (
        sys.argv[1], os.getenv("AdminPassword"))
    req = urllib2.Request(url)
    res = urllib2.urlopen(req, context=context)
    content = res.read()
    flog.write(content+"\n")
except Exception as e:
    flog.write(str(e)+"\n")
