--设置窗口
module("UI.Windows.ChatWin",package.seeall)

local NetMgr = require("NetWork").NetMgr
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
ChatWin = UIWindow:new()
ChatWin.name = "ChatWin" --窗体名字

--初始化界面
function ChatWin:Init()
    --初始化界面，保存引用控件
    local t = self.Container.transform
    self.CloseDi = UIUtility.GetChildTransform("di",t,true)
    self.BiaoQingPanel = UIUtility.GetChildTransform("BiaoQingPanel",t,true)
    self.ChangYongYuPanel = UIUtility.GetChildTransform("ChangYongYuPanel",t,true)
    self.BiaoQingTog = UIUtility.FindContorl('UIToggle',"BiaoQingTog",t)
    self.ChangYongYuTog = UIUtility.FindContorl('UIToggle',"ChangYongYuTog",t)
    self.SendBtn = UIUtility.GetChildTransform("SendBtn",t,true)
    self.Input = UIUtility.FindContorl('UIInput',"InputSp",t)
    self.BiaoQingGrid = UIUtility.GetChildTransform("BiaoQingGrid",t,true)
    self.WordGrid = UIUtility.GetChildTransform("WordGrid",t,true)
    self:BindEvents()
end

function ChatWin:OnShow()
    --self.Input.value=""
end

function ChatWin:BindEvents()

    function BiaoQingTogChange()
        if self.BiaoQingTog.value then
            self.BiaoQingPanel.gameObject:SetActive(true)
        else
            self.BiaoQingPanel.gameObject:SetActive(false)
        end
    end
    self.BiaoQingTog.onChange:Add(UIHelper.CreateEventDelegate(BiaoQingTogChange,self.BiaoQingTog.gameObject))

    function ChangYyTogChange()
        if self.ChangYongYuTog.value then
            self.ChangYongYuPanel.gameObject:SetActive(true)
        else
            self.ChangYongYuPanel.gameObject:SetActive(false)
        end
    end
    self.ChangYongYuTog.onChange:Add(UIHelper.CreateEventDelegate(ChangYyTogChange,self.ChangYongYuTog.gameObject))

    function OnCloseClick()
        self:Close()
        self.Input.value=""
    end
    UIHelper.BindUIEvnet("Click",OnCloseClick,self.CloseDi.gameObject)

    for i=1,self.BiaoQingGrid.childCount do 
        function OnClick()
            local Msg = require("Protol.roomMessage_pb").ChatCm()
            Msg.type = 1
            Msg.id=i
            Msg.content =""
            NetMgr:SendMsg(219,Msg)
            self:Close()
        end
        UIHelper.BindUIEvnet("Click", OnClick,self.BiaoQingGrid:GetChild(i-1).gameObject)
    end

    for i=1,self.WordGrid.childCount do 
        function OnClick()
            local Msg = require("Protol.roomMessage_pb").ChatCm()
            Msg.type = 2
            Msg.id=i
            Msg.content = ""
            NetMgr:SendMsg(219,Msg)
            self:Close()
        end
        UIHelper.BindUIEvnet("Click", OnClick,self.WordGrid:GetChild(i-1).gameObject)
    end

    function OnSendBtnClick()
        if string.len( self.Input.value )>0 then 
            local Msg = require("Protol.roomMessage_pb").ChatCm()
            Msg.type = 3
            Msg.content = self.Input.value
            NetMgr:SendMsg(219,Msg)
            self.Input.value=""
        end
    end
    UIHelper.BindUIEvnet("Click",OnSendBtnClick,self.SendBtn.gameObject)
end
