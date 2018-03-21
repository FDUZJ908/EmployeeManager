tar -zcf source.tar.gz pom.xml src
scp source.tar.gz root@phxxz.shiftlin.top:~/EmployeeManager/
ssh root@phxxz.shiftlin.top "bash ~/EmployeeManager/install.sh"
