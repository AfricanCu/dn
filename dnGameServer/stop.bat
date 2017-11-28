@echo off
setlocal enabledelayedexpansion 
set str=
for /f "delims=" %%i in ('dir /b lib') do ( 
set str=!str!lib/%%i;
)
java -Xms128m -Xmx256m -Xmn64m -DsystemRoot=./ -cp target/classes/;%str% com.wk.server.StopServer 192.168.0.42
pause
