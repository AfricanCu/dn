#!/bin/bash --posix
#根目录
basePath=/root/gs
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
mainclass=com.wk.server.StopServer
#地址
ip=127.0.0.1
#log文件路径
logPath=stop.log
#bak log文件目录
baklogPath=logs
cd $basePath
libname=`find ./lib -name *.jar |xargs |sed -e 's/ /:/g'`
cmd="java -d64 -DsystemRoot=./ -cp $classPath:$libname $mainclass $ip"
gsId=`ps -ef |grep java |grep $mainclass |grep -v grep |awk '{print $2}'`
if [ "$gsId" != "" ]
then
 gsPath=`pwdx $gsId |grep $basePath |sed 's/.*: //g'`
 #echo  $gsPath
if [ "$gsPath" != "$basePath" ]
 then
  echo "Some $mainclass is running, but no matches. Now execute start cmd"
  echo $cmd
  if [ -f $logPath ]
  then
  mv $logPath "$baklogPath/$logPath`date '+%Y-%m-%d %H:%M:%S'`"
  fi
  nohup $cmd > $logPath 2>&1 &
   tail -400f $logPath
 else 
  echo "$mainclass is running."
 fi
else 
 echo "no $mainclass is running"
 echo $cmd
  if [ -f $logPath ]
 then
 mv $logPath "$baklogPath/$logPath`date '+%Y-%m-%d %H:%M:%S'`"
 fi
 nohup $cmd > $logPath 2>&1 &
 sleep 1s
 tail -400f $logPath
fi
