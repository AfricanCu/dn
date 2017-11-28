--
module("UI.Com.UICreateRoomColor",package.seeall)

local Utility = require("UI.Utility")
local GameObject = UnityEngine.GameObject

UICreateRoomColor = {}

--构造函数
function UICreateRoomColor:new(o)    
	o = o or {};
    setmetatable(o, { __index = self });     
    return o;
end 

function UICreateRoomColor:Init(root)
    self.root = root
    self.labelTable = self.root.transform:GetComponentsInChildren(typeof(UILabel))
end

function UICreateRoomColor:On()
    if self.labelTable ~= nil then
        local len = self.labelTable.Length
        Debug.log("len:"..len)
        for i = 0, len - 1 do
            self.labelTable[i].color = Color(42/255,162/255,210/255)
        end
    end
end

function UICreateRoomColor:Off()
    if self.labelTable ~= nil then
        local len = self.labelTable.Length
        Debug.log("len:"..len)
        for i = 0, len - 1 do
            self.labelTable[i].color = Color(255/255,255/255,255/255)
        end
    end
end