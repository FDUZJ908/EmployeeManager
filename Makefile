all:
	tar -zcf source.tar.gz pom.xml src
	scp source.tar.gz root@phxxz.read-a-book.wang:~/EmployeeManager
	ssh root@phxxz.read-a-book.wang "bash ~/EmployeeManager/install.sh"
