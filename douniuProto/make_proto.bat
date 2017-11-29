echo on
set java_pro=..\dnUtils\src
if exist %java_pro% javaProtoc.exe  --java_out=%java_pro% *.proto 
rem set java_pro=..\..\workspace2\utils\src
rem if exist %java_pro% javaProtoc.exe  --java_out=%java_pro% *.proto 
rem set java_pro=..\..\workspace\utils\src
rem if exist %java_pro% javaProtoc.exe  --java_out=%java_pro% *.proto
pause
del /s/q .\Lua\Protol\*.lua
for %%i in (*.proto) do (  
    protoc.exe --plugin=protoc-gen-lua="plugin\build.bat" --lua_out=.\Lua\Protol %%i      
)
pause