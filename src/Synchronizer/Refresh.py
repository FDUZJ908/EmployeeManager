import os
import sys
import urllib
import urllib2

url="http://localhost:4908/Refresh"
req=urllib2.Request(url)
res=urllib2.urlopen(req)
content=res.read()
print content
