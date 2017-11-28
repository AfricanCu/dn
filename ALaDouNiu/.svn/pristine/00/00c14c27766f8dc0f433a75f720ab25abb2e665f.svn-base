--代开房弹出窗口
module("UI.Windows.ProxyRoomWin", package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
ProxyRoomWin = UIWindow:new()
ProxyRoomWin.name = "ProxyRoomWin" --窗体名字

--初始化界面
function ProxyRoomWin:Init()

    --初始化界面，保存引用控件
    local t = self.Container.transform
        
    self.CloseBtn = UIUtility.GetChildTransform("closeBut", t, true) --关闭按钮
    self.roomInfoBut = UIUtility.GetChildTransform("roomInfoBut", t, true) --房间信息按钮   
    self.invitingFriendBut = UIUtility.GetChildTransform("invitingFriendBut", t, true) -- 邀请好友
    self.copyRoomBut = UIUtility.GetChildTransform("copyRoomBut", t, true) -- 复制房间信息
    self.roomDataLable = UIUtility.FindContorl("UILabel", "roomDataLable", t)
    self.di = UIUtility.GetChildTransform("di", t, true)
    self.proxyCreateRoomSm = {}

    self:BindEvents()
end

function ProxyRoomWin:OnShow()

    -- self.roomDataLable.text = "dsfdsfsd"

end

function ProxyRoomWin:BindEvents()

    -- 绑定事件

    function clickRoomInfoButAction()
        UIWinMgr:OpenWindow("RoomInfoWin") --打开房间信息界面
        self:Close()
    end
    UIHelper.BindUIEvnet("Click", clickRoomInfoButAction, self.roomInfoBut.gameObject)
-----------------------------------------------------------------------------------------------------
    function clickInvitingFriendBut()
        --邀请好友
        Debug.log("----------邀请好友---------")
        local titleRoom = self.proxyCreateRoomSm.RoomId
        if self.proxyCreateRoomSm.PlayType.pType == 1 then
            myContent = "经典玩法"
        else
            myContent = "疯狂加倍"
        end
        if self.proxyCreateRoomSm.PlayType.round == 1 then
            myContent = myContent.." 10局"
        else
            myContent = myContent.." 20局"
        end
        local json = require("cjson")
        local share = {}
        share.type = 1
        share.mediatype = 13
        share.title = "疯狂阿拉"..titleRoom
        share.content = myContent
        share.link = "http://fir.im/aladn"
        local s_Share = json.encode(share)
        PlatformTool.Instance:MWShare("share_Unity", s_Share, "wx1463fc544854bfd0", WXShareCallBack)

    end
    UIHelper.BindUIEvnet("Click", clickInvitingFriendBut, self.invitingFriendBut.gameObject)
-----------------------------------------------------------------------------------------------------
    function clickCopyRoomButAction()

        local RoomModule = require("Module.RoomModule").RoomModule
        RoomModule.CopyTextToClipBoard(self.proxyCreateRoomSm)

        Debug.log("复制房间信息")
    end
    UIHelper.BindUIEvnet("Click", clickCopyRoomButAction, self.copyRoomBut.gameObject)
-----------------------------------------------------------------------------------------------------

function ClickCloseButAction()
    -- body
    self:Close()
end
UIHelper.BindUIEvnet("Click", ClickCloseButAction, self.CloseBtn.gameObject)
UIHelper.BindUIEvnet("Click", ClickCloseButAction, self.di.gameObject)

end

--视图显示之前
function ProxyRoomWin:ViewDidLoad(ProxyCreateRoomSm)

    self.proxyCreateRoomSm.PlayType = ProxyCreateRoomSm.playType
    self.proxyCreateRoomSm.RoomId = ProxyCreateRoomSm.roomId
    local round = nil --局数
    local pType = nil --模式
    local bankerMode = nil --坐庄
    if ProxyCreateRoomSm.playType.round == 1 then
        round = "10局"   
    elseif ProxyCreateRoomSm.playType.round == 2 then
        round = "20局"        
    end

    if ProxyCreateRoomSm.playType.pType == 1 then
        pType = "经典牛牛"   
    elseif ProxyCreateRoomSm.playType.pType == 2 then
        pType = "疯狂加倍"        
    end

    if ProxyCreateRoomSm.playType.bankerMode == 1 then
        bankerMode = "抢庄"   
    elseif ProxyCreateRoomSm.playType.bankerMode == 2 then
        bankerMode = "轮庄"        
    elseif  ProxyCreateRoomSm.playType.bankerMode == 3 then
        bankerMode = "固定庄"        
    end

    self.roomDataLable.text = "房号【"..ProxyCreateRoomSm.roomId.."】,"..round.."、"..pType.."、"..bankerMode

end