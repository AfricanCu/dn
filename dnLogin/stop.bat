@echo off
setlocal enabledelayedexpansion
set ip=localhost
set port=8116
set secret=secret
set password=password
java -Xms64m -Xmx128m -Xmn32m -DsystemRoot=./ -cp target/classes/;%str% gm.server.StopServer %port% %secret% %password% %ip%
pause
