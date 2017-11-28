--UI常用工具
module("UI.Utility",package.seeall)

--查找控件
--type 控件类型
--name 控件名称
--tranformt 从指定Tranform往下找 
function FindContorl(type,name,tranform)
    local t = GetChildTransform(name,tranform,true)
    if nil ~= t then
        return t:GetComponent(type)
    end
end

--寻找子节点
--name 子节点名字
--t 从指定Tranform t往下找
--isFindAll 是否查找所有子节点，如果为false则只找t的下一级，默认false
function GetChildTransform(name,t,isFindAll)
    if nil == isFindAll then
        isFindAll = false
    end
    child = UIHelper.GetChildTran(name,t,isFindAll)
    return child
end

--获取UI文本
function GetUIText(key)
    DTMgr = require("DataTable").DTMgr
    local t = DTMgr:GetTabel("UIText")
    local value = DTMgr:GetValueByKey(t,"Key",key,"Text")
    return value
end