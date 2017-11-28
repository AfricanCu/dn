--资源管理器
module("Res.ResMgr",package.seeall)

local Resources = UnityEngine.Resources

ResMgr = {}

--同步加载资源,先从AB找资源，如果找不到，则从Resources找
function ResMgr:LordBySyn(pathOrName,owner)
    owner = owner or nil;
    Obj = nil
    if nil ~= owner then
        Obj = ResHelper.LoadResBySyn(pathOrName,owner)
    else
        Obj = ResHelper.LoadResBySyn(pathOrName)
    end
    return Obj
end

--异步加载资源,先从AB找资源，如果找不到，则从Resources找
function ResMgr:LordByAsync(callBack,pathOrName,owner)
    if not callBack or type(callBack) ~= "function" then
        return
    end
    owner = owner or nil;
    if nil ~= owner then
        ResHelper.LoadResByAsync(pathOrName,callBack,owner)
    else
        ResHelper.LoadResByAsync(pathOrName,callBack)
    end
end

--为资源指定引用对象
function ResMgr:Retain(abName,owner)
    ResHelper.Retain(abName,owner)
end

--卸载未使用的资源
function ResMgr:UnLoadUnUse()
    ResHelper.UnLoadUnUse()
end