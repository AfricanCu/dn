--
module("UI.Windows.NoticeWin",package.seeall)

local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow

NoticeWin = UIWindow:new()

NoticeWin.name = "NoticeWin" 


function NoticeWin:Init()
    local t = self.Container.transform
    self.OkButton = UIUtility.GetChildTransform("OkBtn",t,true)
    self.NoButton = UIUtility.GetChildTransform("NoBtn",t,true)
    local labelt = UIUtility.GetChildTransform("MsgLabel",t,true)
    self.MsgLabel = labelt:GetComponent('UILabel')
    self.Grid = UIUtility.FindContorl('UIGrid',"Grid",t)
    self.CloseBtn = UIUtility.GetChildTransform("Close",t,true) 
    self.di = UIUtility.GetChildTransform("di",t,true) 
    UIHelper.BindUIEvnet("Click",self.OnOkClick,self.OkButton.gameObject)--绑定按钮事件
    UIHelper.BindUIEvnet("Click",self.OnNoClick,self.NoButton.gameObject)--绑定按钮事件
    UIHelper.BindUIEvnet("Click",self.CloseBtnClick,self.CloseBtn.gameObject)
    UIHelper.BindUIEvnet("Click",self.CloseBtnClick,self.di.gameObject)
end

function NoticeWin:CloseBtnClick()
    NoticeWin:Close()
end

function NoticeWin:SetMsg(msg,ok,no)
    self.onOk = ok
    self.onNo = no
    self.MsgLabel.text = msg
    if ok == nil and no == nil then
        self.NoButton.gameObject:SetActive(false)
    end
    self.Grid:Reposition()
end

function  NoticeWin.OnOkClick()
    NoticeWin:Close()
 	if nil ~= NoticeWin.onOk then
        if type(NoticeWin.onOk) == "function"  then
            NoticeWin.onOk()
        end
    end
    NoticeWin:Reset()
end

function  NoticeWin:OnNoClick()
    NoticeWin:Close()
 	if nil ~= NoticeWin.onNo then
        if type(NoticeWin.onNo) == "function"  then
            NoticeWin.onNo()
        end
    end
    NoticeWin:Reset()
end

function NoticeWin:Reset()
    self.onOk = nil
    self.onNo = nil
    self.MsgLabel.text = ""
    self.OkButton.gameObject:SetActive(true)
    self.NoButton.gameObject:SetActive(true)
end