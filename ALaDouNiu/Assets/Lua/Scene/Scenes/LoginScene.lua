--大厅场景
module("Scene.LoginScene",package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local SceneBase = require("Scene.SceneBase").SceneBase

LoginScene = SceneBase:new()

--场景名称
function LoginScene.GetSceneName()
    return "Login_Scene"
end

--当场景加载完毕
function LoginScene:OnSceneLoadOver()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local IosPendingTool = require("IosPendingTool").IosPendingTool
    local isIosPending = IosPendingTool.IsPending()
    if isIosPending then
        UIWinMgr:OpenWindow("IosLoginWin")
    else
        UIWinMgr:OpenWindow("LoginWin")
    end
    --[[local SoundModule = require("Module.SoundModule").SoundModule
    SoundModule:Init()]]--
end

--当场景卸载
function LoginScene:OnSceneUnLoad()
    -- 干点卸载回收之类的事
    
end

 

