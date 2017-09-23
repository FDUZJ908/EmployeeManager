tar -zcf source.tar.gz EmployeeManager.iml pom.xml src
scp source.tar.gz root@139.196.123.150:~/EmployeeManager/
ssh root@139.196.123.150 "bash ~/EmployeeManager/install.sh"

