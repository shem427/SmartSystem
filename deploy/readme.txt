前提：
MySQL与JDK8正确安装

MySQL下载地址
https://dev.mysql.com/downloads/file/?id=482336

JDK下载地址
https://download.oracle.com/otn-pub/java/jdk/8u201-b09/42970487e3af4f5aa5bca3f542482c60/jdk-8u201-windows-i586.exe


部署：
1. 新建数据库，名称中输入monitor，字符集设置为UTF8mb4
2. 在MySQL中运行db/monitor_ddl.sql，设置DB的结构
3. 在MySQL中运行db/monitor_data.sql，导入初始数据
4. 用记事本打开deploy.bat，修改以下两项
	--spring.datasource.username=shen
	--spring.datasource.password=0944
	修改为正确的数据库用户名与密码，保存。
5. 双击deploy.bat，保持命令行窗口不关闭。
6. 浏览器中打开： http://localhost:8080/monitor/index
7. 用户名/密码中输入000001/123456