--UI滚动视窗组件
module("UI.Com.UIScrollView",package.seeall)

local GameObject = UnityEngine.GameObject

UIScrollView = {}
--构造函数
function UIScrollView:new(o)    
	o = o or {};
    setmetatable(o, { __index = self });     
    return o;
end 

UIScrollView.rootTran = nil --滚动视窗根节点
UIScrollView.scrollview = nil --UIScrollView 组件
UIScrollView.grid = nil --UIGrid 组件 或者 UITable 组件
UIScrollView.template = nil --项目模板
UIScrollView.items = nil --项目列表
UIScrollView.PreCreate = 0 -- 预创建项目个数

function UIScrollView:Init(root)

    local UIUtility = require("UI.Utility")

    self.rootTran = root

    self.scrollview = self.rootTran:GetComponent('UIScrollView')
    self.grid = UIUtility.FindContorl('UIGrid',"Grid",self.rootTran) or UIUtility.FindContorl('UITable',"Grid",self.rootTran)
    self.template = UIUtility.GetChildTransform("template",self.grid.transform)

    self.items = {}
    
    for i=1,self.PreCreate do
        self:CreateNewItem()
    end

    self:Clear()
end

--添加项目
function UIScrollView:Add(callBack)
    if not self:IsReady() then
        return
    end

    local Ing = nil
    for i=1,#self.items do
        if true ~= self.items[i].activeSelf then
            Ing = self.items[i]
            break
        end
    end

    if nil == Ing then
       Ing = self:CreateNewItem()
    end

    Ing:SetActive(true)
    self.grid:Reposition()
    if not callBack or type(callBack) ~= "function" then
        return
    end

    callBack(Ing)
end

function UIScrollView:CreateNewItem()
    local Ing = GameObject.Instantiate(self.template.gameObject)
    Ing.transform.parent = self.template.parent
    Ing.transform.localScale = self.template.localScale
    table.insert(self.items,Ing)
    return Ing
end

--清除项目
function UIScrollView:Clear(isRest)
    if not self:IsReady() then
        return
    end

    if nil == isRest then
        isRest  = true
    end

    if nil ~= self.items then
        for i=1,#self.items do
            self.items[i]:SetActive(false)
        end
    end
    if isRest then
        self.grid:Reposition()
        self:GoToStart()
    end
end

function UIScrollView:OnDestroy()
    if not self:IsReady() then
        return
    end
    if nil ~= self.items then
        for i=1,#self.items do
            GameObject.Destroy(self.items[i])
        end
    end
end

function UIScrollView:DestroyItems()
    if not self:IsReady() then
        return
    end
    if nil ~= self.items then
        for i=1,#self.items do
            GameObject.DestroyImmediate(self.items[i])
        end
    end
    self.items = {}
    self.grid:Reposition()
end

--控件是否准备就绪
function UIScrollView:IsReady()
    if nil == self.scrollview then
        return false
    end
    if nil == self.grid then
        return false
    end
    if nil == self.template then
        return false
    end
    return true
end

--跳转到最前
function  UIScrollView:GoToStart()
    self.scrollview:SetDragAmount(0,0,false)
end

--跳转到最后
function  UIScrollView:GotoEnd()
    self.scrollview:SetDragAmount(1,1,false)
end


function UIScrollView:RestrictWithinBounds()
    self.scrollview:RestrictWithinBounds(true,true,true)
end
