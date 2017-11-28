--
module("UI.Windows.FenXiangWin",package.seeall)

local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow

FenXiangWin = UIWindow:new()

FenXiangWin.name = "FenXiangWin" 


function FenXiangWin:Init()
    local t = self.Container.transform
    self.PyqBtn = UIUtility.GetChildTransform("PyqBtn",t,true) 
    self.HyqBtn = UIUtility.GetChildTransform("HyqBtn",t,true)
    self.CloseBtn = UIUtility.GetChildTransform("Close",t,true) 
    self.di = UIUtility.GetChildTransform("di",t,true)

    function Close()
        self:Close()
    end

    function mWShareCallBack()
        -- body
    end

    function InviteFriend()
        local json = require("cjson")
        local share = {}
        share.type = 1
        share.mediatype = 13
        share.title = "疯狂阿拉"
        share.content = "疯狂阿拉邀请您点击链接加入游戏,☞下载"
        share.link = "http://fir.im/aladn"
        local s_Share = json.encode(share)
        PlatformTool.Instance:MWShare("share_Unity", s_Share, "wx1463fc544854bfd0", mWShareCallBack)
    end

    function WXShareCallBack()
        -- body
    end

    function InviteArea()
        Debug.log("朋友圈")
        local json = require("cjson")
        local share = {}
        share.type = 3
        share.mediatype = 13
        share.content = "疯狂阿拉邀请您点击链接加入游戏,☞下载"
        self.platform = PlatformTool.Instance:GetPlatformID()
        share.link = "http://fir.im/aladn"
        local s_Share = json.encode(share)
        PlatformTool.Instance:MWShare("share_Unity", s_Share, "wx1463fc544854bfd0", WXShareCallBack)
    end

    UIHelper.BindUIEvnet("Click",Close,self.CloseBtn.gameObject)
    UIHelper.BindUIEvnet("Click",InviteArea,self.PyqBtn.gameObject)
    UIHelper.BindUIEvnet("Click",InviteFriend,self.HyqBtn.gameObject)
    UIHelper.BindUIEvnet("Click",Close,self.di.gameObject)
end


