#!/bin/bash --posix
#根目录
basePath=/root/bus
checkPath=`pwd`
if [ $basePath = $checkPath ]
then
 echo $checkPath
else
 echo "根目录错误"
 exit
fi
#class文件目录
classPath=target/classes
#执行主类
mainclass=gm.server.DnJettyServer
#log文件路径
logPath=BUS.log
#bak log文件目录
baklogPath=logs
#初始内存
memostart=128m
#最大内存
memomax=256m
cd $basePath
libname=`find ./lib -name *.jar |xargs |sed -e 's/ /:/g'`
cmd="java -server -d64 -XX:+UseCompressedOops -Xms$memostart -Xmx$memomax  -XX:+AggressiveOpts -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:ParallelGCThreads=8 -DsystemRoot=./ -cp $classPath:$libname $mainclass"
gsId=`ps -ef |grep java |grep $mainclass |grep -v grep |awk '{print $2}'`
if [ "$gsId" != "" ]
then
 gsPath=`pwdx $gsId |grep $basePath |sed 's/.*: //g'`
 #echo  $gsPath
if [ "$gsPath" != "$basePath" ]
 then
  echo "Some servers is running, but no matches. Now execute start cmd"
  echo $cmd
  if [ -f $logPath ]
  then
  mv $logPath "$baklogPath/$logPath`date '+%Y-%m-%d %H:%M:%S'`"
  fi
  nohup $cmd > $logPath 2>&1 &
   tail -400f $logPath
 else 
  echo "Server is running."
 fi
else 
 echo "no server is running"
 echo $cmd
  if [ -f $logPath ]
 then
 mv $logPath "$baklogPath/$logPath`date '+%Y-%m-%d %H:%M:%S'`"
 fi
 nohup $cmd > $logPath 2>&1 &
 sleep 1s
 tail -400f $logPath
fi
