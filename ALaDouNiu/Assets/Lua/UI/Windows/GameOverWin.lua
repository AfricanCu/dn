--
module("UI.Windows.GameOverWin", package.seeall)

local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local GameObject = UnityEngine.GameObject
GameOverWin = UIWindow:new()

GameOverWin.name = "GameOverWin" --窗体名字

--初始化界面
function GameOverWin:Init()
    --初始化界面，保存引用控件
    local t = self.Container.transform
    self.di = UIUtility.GetChildTransform("di", t, true)
    self.XuanYaoBtn = UIUtility.GetChildTransform("XuanYaoBtn", t, true)
    self.BackBtn = UIUtility.GetChildTransform("BackBtn", t, true)
    self.PlayerNode = UIUtility.GetChildTransform("PlayerNode", t, true)
    self.playGrid = UIUtility.FindContorl('UIGrid',"PlayerNode",t)
    self.L_RoomID = UIUtility.FindContorl("UILabel", "L_RoomID", t)
    self.L_Info = UIUtility.FindContorl("UILabel", "L_Info", t)
    self.showMask = UIUtility.GetChildTransform("showMask", t, true)
    self.UIPLayers = {}
    local count = self.PlayerNode.childCount - 1
    for i = 0, count do
        local root = self.PlayerNode:GetChild(i)
        local tmpTable = {}
        tmpTable.root = root
        tmpTable.S_FangZhu = UIUtility.FindContorl("UISprite", "fangzhu", root.transform)
        tmpTable.L_NickName = UIUtility.FindContorl("UILabel", "Name", root.transform)
        tmpTable.L_ID = UIUtility.FindContorl("UILabel", "ID", root.transform)

        tmpTable.tongsha = UIUtility.FindContorl("UILabel", "tongsha", root.transform)
        tmpTable.tongpei = UIUtility.FindContorl("UILabel", "tongpei", root.transform)
        tmpTable.niuniu = UIUtility.FindContorl("UILabel", "niuniu", root.transform)
        tmpTable.wuniu = UIUtility.FindContorl("UILabel", "wuniu", root.transform)
        tmpTable.shengli = UIUtility.FindContorl("UILabel", "shengli", root.transform)

        tmpTable.L_score = UIUtility.FindContorl("UILabel", "zongchengji", root.transform)
        tmpTable.S_winner = UIUtility.FindContorl("UISprite", "BigWin", root.transform)
        tmpTable.HeadIcon = UIUtility.FindContorl("UITexture", "touxiang", root.transform)
        self.UIPLayers[i + 1] = tmpTable
    end

    self:BindEvents()
end

function GameOverWin:OnShow()
    self.showMask.gameObject:SetActive(true)
    local IosPendingTool = require("IosPendingTool").IosPendingTool
    local isIosPending = IosPendingTool.IsPending()
    if isIosPending then
        --ios审核游客登陆类
        self.XuanYaoBtn.gameObject:SetActive(false)
    else
        self.XuanYaoBtn.gameObject:SetActive(true)
    end

    function Capture()
        PlatformTool.Instance:GetCapture()
    end
    CountDownMgr.Instance:CreateCountDown(0.5, 0.5, Capture)

    function CloseMask()
        self.showMask.gameObject:SetActive(false)
    end
    CountDownMgr.Instance:CreateCountDown(1, 1, CloseMask)
end

function GameOverWin:OnClose()
end

function GameOverWin:SetInfo(GameOverCast)
    local GameHost = require("Module.GameModule.GameHost").GameHost
    local myContent = ""
    if GameHost.RoomId ~= nil then
        self.L_RoomID.text = "房号:" .. GameHost.RoomId
    else
        self.L_RoomID.text = GameHost.clubName .. "-" .. GameHost.clubDeskNum .. "桌"
    end
    if GameHost.PlayType.pType == 1 then
        myContent = "经典玩法"
    else
        myContent = "疯狂加倍"
    end
    if GameHost.PlayType.round == 1 then
        myContent = myContent.." 10局"
    else
        myContent = myContent.." 20局"
    end
    self.L_Info.text = myContent

    local winnerTable = {}
    winnerTable[1] = false
    winnerTable[2] = false
    winnerTable[3] = false
    winnerTable[4] = false
    winnerTable[5] = false
    for k, v in ipairs(GameOverCast.seetIndex) do
        winnerTable[v] = true
    end

    local len = #GameOverCast.rs
    for i=len+1,5 do
        self.UIPLayers[i].root.gameObject:SetActive(false)
    end

    local GameHost = require("Module.GameModule.GameHost").GameHost
    for i = 1, #GameOverCast.rs do
        local rs = GameOverCast.rs[i]
        if rs ~= nil then
            self.UIPLayers[i].root.gameObject:SetActive(true)
            local uiPlayer = self.UIPLayers[i]
            local player = GameHost:GetPlayer(rs.seetIndex)
            uiPlayer.L_NickName.text = player.userInfo.nickname
            uiPlayer.L_ID.text = "ID:" .. player.userInfo.uid
            uiPlayer.S_winner.gameObject:SetActive(false)
            if player.isMaster then
                uiPlayer.S_FangZhu.gameObject:SetActive(true)
            else
                uiPlayer.S_FangZhu.gameObject:SetActive(false)
            end
            if winnerTable[rs.seetIndex] then
                uiPlayer.S_winner.gameObject:SetActive(true)
            end

            if rs.coin >= 0 then
                uiPlayer.L_score.text = "+" .. rs.coin
                uiPlayer.L_score.color = Color(255/255,250/255,166/255)
            else
                uiPlayer.L_score.text = tostring(rs.coin)
                uiPlayer.L_score.color = Color(173/255,22/255,3/255)
            end

            if rs.allWinTimes >= 0 then
                uiPlayer.tongsha.text = "+" .. rs.allWinTimes
            else
                uiPlayer.tongsha.text = tostring(rs.allWinTimes)
            end

            if rs.allFailTimes >= 0 then
                uiPlayer.tongpei.text = "+" .. rs.allFailTimes
            else
                uiPlayer.tongpei.text = tostring(rs.allFailTimes)
            end

            if rs.niuNiuTimes >= 0 then
                uiPlayer.niuniu.text = "+" .. rs.niuNiuTimes
            else
                uiPlayer.niuniu.text = tostring(rs.niuNiuTimes)
            end

            if rs.noNiuTImes >= 0 then
                uiPlayer.wuniu.text = "+" .. rs.noNiuTImes
            else
                uiPlayer.wuniu.text = tostring(rs.noNiuTImes)
            end

            if rs.winTimes >= 0 then
                uiPlayer.shengli.text = "+" .. rs.winTimes
            else
                uiPlayer.shengli.text = tostring(rs.winTimes)
            end

            uiPlayer.HeadIcon.gameObject:SetActive(false)
            function callBack(isOk, www)
                if isOk then
                    uiPlayer.HeadIcon.gameObject:SetActive(true)
                    uiPlayer.HeadIcon.mainTexture = www.texture
                end
            end
            require("NetWork.NetHttp").WWWTexture(player.userInfo.headimg, callBack)
        end
    end

    self.playGrid.repositionNow = true
    self.playGrid:Reposition()
end

function GameOverWin:BindEvents()
    function BackToLobby()
        self:Close()
        local RoomModule = require("Module.RoomModule").RoomModule
        RoomModule.BackToLobby()
    end

    UIHelper.BindUIEvnet("Click", BackToLobby, self.BackBtn.gameObject)
    UIHelper.BindUIEvnet("Click", BackToLobby, self.di.gameObject)

    function WXShareCallBack()
    end

    function XuanYao()
        local json = require("cjson")
        local share = {}
        share.type = 2
        share.mediatype = 12
        share.img = UnityEngine.Application.persistentDataPath .. "/screenCaputer.jpg"
        local s_Share = json.encode(share)
        PlatformTool.Instance:MWShare("share_Unity", s_Share, "wx1463fc544854bfd0", WXShareCallBack)

        
    end

    UIHelper.BindUIEvnet("Click", XuanYao, self.XuanYaoBtn.gameObject)

    function Pause(pause)
        if self.UIClosed == false then
            if pause then
                local NetMgr = require("NetWork").NetMgr
                NetMgr.HeartbeatPause()
            else
                local NetMgr = require("NetWork").NetMgr
                NetMgr.HeartbeatResume()
            end
        end
    end

    local BehaviourEvent = require("BehaviourEvent").BehaviourEvent
    BehaviourEvent:Bind("OnApplicationPause", self.Container.gameObject, Pause)
end
