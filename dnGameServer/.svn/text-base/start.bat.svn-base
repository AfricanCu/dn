@echo off
setlocal enabledelayedexpansion 
set str=
for /f "delims=" %%i in ('dir /b lib') do ( 
set str=!str!lib/%%i;
)
set memostart=768m
set memomax=1536m
rem 年轻代大小为。整个JVM内存大小=年轻代大小 + 年老代大小 + 持久代大小
set yongmemo=512m
java -server -d64 -XX:+UseCompressedOops -Xms%memostart% -Xmx%memomax% -Xmn%yongmemo%  -XX:+AggressiveOpts -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:ParallelGCThreads=8 -DsystemRoot=./ -cp target/classes/;%str% com.wk.server.Server
pause
