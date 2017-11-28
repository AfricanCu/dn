--UI窗体基类
module("UI.UIWindow",package.seeall)

UIWindow = {}

--构造函数
function UIWindow:new(o)    
	o = o or {};
    setmetatable(o, { __index = self });     
    return o;
end 

UIWindow.name = nil --窗体名字
UIWindow.Container = nil --窗体的UIPanel
UIWindow.Root = nil --窗体的根物体

UIWindow.OnShow = nil --激活事件
UIWindow.OnClose = nil --关闭事件
UIWindow.myContainerIsOpend = false

--初始化界面
function UIWindow:Init()
    -- body
end

--打开界面
function UIWindow:Show()
    if nil ~= self.Container then
        self.Container.gameObject:SetActive(true)
        self.myContainerIsOpend = true
    end
    if not self.OnShow or type(self.OnShow) ~= "function" then
        return
    end
    self:OnShow()
end

--关闭界面
function UIWindow:Close()
    if self.myContainerIsOpend then
        self.myContainerIsOpend = false
        if nil ~= self.Container then
            self.Container.gameObject:SetActive(false)
        end
        --self:ResetDepthToZero()
        if not self.OnClose or type(self.OnClose) ~= "function" then
            return
        end
        self:OnClose()
    end
end

--界面置顶
function UIWindow:BringToTop()
    if not self.Root or not self.Container then
        return
    end

    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local maxDepth = nil
    for k,v in pairs(UIWinMgr.WinSet) do
        local win = v
        if win.Root then
            if win.Container then
                local panels = win.Root:GetComponentsInChildren(typeof(UIPanel),true)--返回UIPanel的数组
                for i=0,panels.Length -1 do  --遍历数组
                    if not maxDepth or panels[i].depth > maxDepth then
                        maxDepth = panels[i].depth
                    end
                end
            end
        end
    end
    local panels = self.Root:GetComponentsInChildren(typeof(UIPanel),true)--返回UIPanel的数组
    local localMax = 999999999
    for i=0,panels.Length -1 do  --遍历数组
        if panels[i].depth < localMax then
            localMax = panels[i].depth
        end
    end

    local change = localMax - maxDepth 
    if change <= 0 then
        change = maxDepth - localMax + 1
    else
        change = 0
    end

    for i=0,panels.Length-1 do 
        panels[i].depth = panels[i].depth + change
    end
end

--将界面深度置为0
function UIWindow:ResetDepthToZero()
    if not self.Container then
        return
    end
    if 0 >= self.Container.depth then
        return
    end
    local panels = self.Container.transform:GetComponentsInChildren(typeof(UIPanel),true)--返回子panel
    local down = self.Container.depth
    self.Container.depth = 0
    for i=0,panels.Length -1 do  --遍历数组
       panels[i].depth = panels[i].depth - down
    end
end

--销毁界面
function UIWindow:Destroy()
    if nil ~= self.Root then
        GameObject = UnityEngine.GameObject
        GameObject.Destroy(self.Root)
    end
end

--创建窗体
function UIWindow:CreateWin()
    --加载资源，创建Unity对象
    Debug.log("开始创建界面 "..self.name)

    local GameObject = UnityEngine.GameObject
    local ResMgr = require("Res.ResMgr").ResMgr
    local BehaviourEvent = require("BehaviourEvent").BehaviourEvent

    local root = GameObject(self.name)
    root.transform.parent = UIHelper.Get2DRoot().transform
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
