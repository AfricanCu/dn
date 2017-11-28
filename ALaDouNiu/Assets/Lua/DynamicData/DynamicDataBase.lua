module("DynamicData",package.seeall)

DynamicDataBase = {};

--构造函数
function DynamicDataBase:new(o)    
	o = o or {};
    setmetatable(o, { __index = self });     
    return o;
end  

--初始化
function DynamicDataBase:Init()
    
end

--回收
function DynamicDataBase:Dispose()
    
end