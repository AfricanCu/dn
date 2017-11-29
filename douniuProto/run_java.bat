echo on
protoc.exe  --java_out=../wolfKillGameServer/src *.proto 
protoc.exe  --java_out=../../workspace/wolfKillGameServer/src *.proto 
pause