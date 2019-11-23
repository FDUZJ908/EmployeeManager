all:
	tar -zcf source.tar.gz pom.xml src
	scp source.tar.gz root@www.phxxz.top:~/EmployeeManager
	ssh root@www.phxxz.top "bash ~/EmployeeManager/install.sh"
