module("UI.Windows.MainBgWin",package.seeall)

local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr

MainBgWin = UIWindow:new()

MainBgWin.name = "MainBgWin" 

function MainBgWin:Init()

end

function MainBgWin:CreateWin()
    --加载资源，创建Unity对象
    Debug.log("开始创建界面 "..self.name)

    local GameObject = UnityEngine.GameObject
    local ResMgr = require("Res.ResMgr").ResMgr
    local BehaviourEvent = require("BehaviourEvent").BehaviourEvent

    local root = GameObject(self.name)
    root.transform.parent = GameObject.Find("2DBgUIRoot").transform
    root.transform.localScale = Vector3(1,1,1);
    root.transform.localPosition = Vector3(0,0,0);    

    local obj = ResMgr:LordBySyn("UI/"..self.name,root)
    local Ing = GameObject.Instantiate(obj)
    Ing.transform.parent = root.transform
    Ing.transform.localScale = Vector3(1,1,1);
    Ing.transform.localPosition = Vector3(0,0,0);
    self.Container = Ing:GetComponent('UIPanel')

    self.Root = root
        
    function OnDestroy()
        self.Container = nil
        self.Root = nil
        Debug.log("销毁界面"..self.name)
        if not self.OnDestroy or type(self.OnDestroy) ~= "function" then
            return
        end
        self:OnDestroy()
    end

    BehaviourEvent:Bind("OnDestroy",root,OnDestroy) --绑定销毁事件

    Debug.log("界面创建完毕 "..self.name)
end