--
module("UI.Windows.VoiceChatWin",package.seeall)

local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
VoiceChatWin = UIWindow:new()
VoiceChatWin.name = "VoiceChatWin" --窗体名字

function VoiceChatWin:Init()
    local t = self.Container.transform
    self.S_volume1 = UIUtility.GetChildTransform("S_volume1",t,true)
    self.S_volume2 = UIUtility.GetChildTransform("S_volume2",t,true)
    self.S_volume3 = UIUtility.GetChildTransform("S_volume3",t,true)
    self.S_volume4 = UIUtility.GetChildTransform("S_volume4",t,true)
    self.S_volume5 = UIUtility.GetChildTransform("S_volume5",t,true)
    self.L_process = UIUtility.FindContorl('UILabel',"L_process",t)
    self.T_process = UIUtility.FindContorl('UISlider',"T_process",t)
    self.isOpend = false
end

function VoiceChatWin:OnShow()
    local SoundModule = require("Module.SoundModule").SoundModule
    SoundModule:StopBGM()
    self.isOpend = true
    local VoiceTool = require("VoiceTool").VoiceTool
    local GameHost = require("Module.GameModule.GameHost").GameHost
    local myPlayer = GameHost:GetSelfPlayer()
    VoiceTool:StartRecord(myPlayer.userInfo.uid, GameHost.RoomId)

    function MicRate(sCountDown, countDown)
        local process = countDown/30
        self.T_process.value = process
        self.L_process.text = string.format("%.1f", countDown)

        local randInx = math.random(1,5)
        if randInx == 1 then
            self.S_volume1.gameObject:SetActive(true)
            self.S_volume2.gameObject:SetActive(false)
            self.S_volume3.gameObject:SetActive(false)
            self.S_volume4.gameObject:SetActive(false)
            self.S_volume5.gameObject:SetActive(false)
        elseif randInx == 2 then
            self.S_volume1.gameObject:SetActive(true)
            self.S_volume2.gameObject:SetActive(true)
            self.S_volume3.gameObject:SetActive(false)
            self.S_volume4.gameObject:SetActive(false)
            self.S_volume5.gameObject:SetActive(false)
        elseif randInx == 3 then
            self.S_volume1.gameObject:SetActive(true)
            self.S_volume2.gameObject:SetActive(true)
            self.S_volume3.gameObject:SetActive(true)
            self.S_volume4.gameObject:SetActive(false)
            self.S_volume5.gameObject:SetActive(false)
        elseif randInx == 4 then
            self.S_volume1.gameObject:SetActive(true)
            self.S_volume2.gameObject:SetActive(true)
            self.S_volume3.gameObject:SetActive(true)
            self.S_volume4.gameObject:SetActive(true)
            self.S_volume5.gameObject:SetActive(false)
        elseif randInx == 5 then
            self.S_volume1.gameObject:SetActive(true)
            self.S_volume2.gameObject:SetActive(true)
            self.S_volume3.gameObject:SetActive(true)
            self.S_volume4.gameObject:SetActive(true)
            self.S_volume5.gameObject:SetActive(true)
        end
    end

    --计时器结束执行事件
    function MicEnd()
        --关闭界面
        self.micTimeID = nil
        self:Close()
    end

    self.micTimeID = CountDownMgr.Instance:CreateCountDown(30, 0.2, MicRate,MicEnd)
end

function VoiceChatWin:OnClose()
    local SoundModule = require("Module.SoundModule").SoundModule
    SoundModule:BeginBGM()

    if self.isOpend then
        local VoiceTool = require("VoiceTool").VoiceTool
        VoiceTool:StopRecord()
    end

    if self.micTimeID ~= nil then
        CountDownMgr.Instance:RemoveCountDown(self.micTimeID, false)
        self.micTimeID = nil
    end

    self.isOpend = false
end

