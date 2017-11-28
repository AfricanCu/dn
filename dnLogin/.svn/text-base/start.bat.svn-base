@echo off
setlocal enabledelayedexpansion 
set str=
for /f "delims=" %%i in ('dir /b lib') do ( 
set str=!str!lib/%%i;
)
java -server -Xms512m -Xmx1024m -Xmn128m -DsystemRoot=./ -cp target/classes/;%str% gm.server.MjJettyServer
pause
