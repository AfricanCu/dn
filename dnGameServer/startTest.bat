@echo off
setlocal enabledelayedexpansion 
set str=
for /f "delims=" %%i in ('dir /b lib') do ( 
set str=!str!lib/%%i;
)
java -Xms128m -Xmx512m -Xmn64m -DsystemRoot=./ -cp client/target/classes/;%str% test.client.TestClient
pause
