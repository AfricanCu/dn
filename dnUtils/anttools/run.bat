rem windows用于运行ant的脚本
echo on
set path=%path%;%cd%\apache-ant-1.9.4\bin;
rem ant -buildfile server_update.xml
echo "Start build..."
call ant.bat -buildfile "server_update.xml" -logger org.apache.tools.ant.listener.TimestampedLogger
echo "End"
pause