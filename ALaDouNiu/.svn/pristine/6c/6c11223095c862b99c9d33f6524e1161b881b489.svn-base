--出牌的基础类
module("Player",package.seeall)

Player = {}

--构造函数
function Player:new(o)    
	o = o or {};
    setmetatable(o, { __index = self });     
    return o;
end 

function Player:Init(userInfo)
    self.userInfo = userInfo
    self.lastAutoPlayCountID = nil
    self:CalcDir()
    
    function ZhuangChg(bankerSeetIndex)
        if self.userInfo.seetIndex == bankerSeetIndex then
            self.uiPlayer:ShowZhuangState(true)
        else
            self.uiPlayer:ShowZhuangState(false)
        end
        self.uiPlayer:CloseQiangZhuang()
    end
    self.ZhuangChg = ZhuangChg
    Event.AddListener(EventDefine.OnBankerChange,self.ZhuangChg)
end

function Player:UnInit()
    self.uiPlayer:Clear()
    Event.RemoveListener(EventDefine.OnBankerChange,self.ZhuangChg)
end

--必须在自己玩家初始化好之后，才能排出方位
function Player:CalcDir()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local GameWin = UIWinMgr:GetWindow("GameWin")
    local UIPLayers = GameWin:GetUIPlayerTable()
    local GameHost = require("Module.GameModule.GameHost").GameHost
    local playersNum = GameHost.PlayType.num
    self.dir = GameHost:CalcDir(self.userInfo.seetIndex)
    Debug.log("self.dir:"..tostring(self.dir))

    self.uiPlayer = UIPLayers[self.dir]
    self.uiPlayer:SetPlayerInfo(self)
end

function Player:FaPaiAni(num)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local PaiWin = UIWinMgr:GetWindow("PaiWin")
    PaiWin:FaiPai(self.dir, num)
end

--抢庄状态
function Player:QiangZhuangState(state, qiang)
    if state then
        self.uiPlayer:ShowQiangZhuang(qiang)
        local soundName = nil
        --男性
        if self.userInfo.sex == 1 then
            if qiang then
                soundName = "Voice_qiang_1"
            else
                soundName = "Voice_buqiang_1"
            end
        else
            if qiang then
                soundName = "Voice_qiang_2"
            else
                soundName = "Voice_buqiang_2"
            end
        end
        
        Debug.log("soundName:"..soundName)
        local SoundModule = require("Module.SoundModule").SoundModule
        SoundModule:PlaySound(soundName)
    else
        self.uiPlayer:CloseQiangZhuang()
    end
end

function Player:RandZhuangAni()
    self.uiPlayer:RandZhuangAni()
end

function Player:ShowZhuangState(state)
    self.uiPlayer:ShowZhuangState(state)
end

function Player:BetLv(beilv)
    self.uiPlayer:ShowBet(true, beilv)

    local soundName = nil
    --男性
    if self.userInfo.sex == 1 then
        soundName = "CallLandlord_Score"..beilv.."_1"
    else
        soundName = "CallLandlord_Score"..beilv.."_2"
    end
    
    local SoundModule = require("Module.SoundModule").SoundModule
    SoundModule:PlaySound(soundName)
end

function Player:CloseBetLv()
    self.uiPlayer:ShowBet(false)
end

function Player:ShowReady(state)
    self.uiPlayer:ShowReady(state)
end

function Player:ScoreUpdate()
    self.uiPlayer:ScoreUpdate()
end

function Player:OffLineInit(usersRound)
    Debug.log("self.dir:"..self.dir.." self.userInfo.gstate:"..self.userInfo.gstate)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local PaiWin = UIWinMgr:GetWindow("PaiWin")
    --未准备
    if self.userInfo.gstate == 0 then
        self:ShowReady(false)
    --已准备
    elseif self.userInfo.gstate == 1 then
        self:ShowReady(true)
    --未抢庄
    elseif self.userInfo.gstate == 2 then
        PaiWin:chonglian(self.dir, 4, nil)
        self:QiangZhuangState(false)
    --已抢庄
    elseif self.userInfo.gstate == 3 then
        PaiWin:chonglian(self.dir, 4, nil)
        Debug.log("usersRound.seetIndex:"..usersRound.seetIndex)
        Debug.log("usersRound.qiang:"..tostring(usersRound.qiang))
        self:QiangZhuangState(true, usersRound.qiang)
    --未押注
    elseif self.userInfo.gstate == 4 then
        PaiWin:chonglian(self.dir, 4, nil)
        self:CloseBetLv()
    --已押注
    elseif self.userInfo.gstate == 5 then
        PaiWin:chonglian(self.dir, 4, nil)
        self:BetLv(usersRound.betTimes)
    --未完成拼牛
    elseif self.userInfo.gstate == 6 then
        PaiWin:chonglian(self.dir, 5, nil)
    --已完成拼牛
    elseif self.userInfo.gstate == 7 then
        PaiWin:chonglian(self.dir, 5, nil)
        PaiWin:Finish(self.dir)
    --未选继续
    elseif self.userInfo.gstate == 8 then
        PaiWin:ShowPai(self.dir, usersRound.faLastPukeCast.puke, usersRound.faLastPukeCast.niu)
    --已选继续
    elseif self.userInfo.gstate == 9 then
        PaiWin:ShowPai(self.dir, usersRound.faLastPukeCast.puke, usersRound.faLastPukeCast.niu)
        self:ShowReady(true)
    end
end

function Player:OfflineHander(offline)
    if offline then
        self.userInfo.userState = 2
    else
        self.userInfo.userState = 1
    end
    self.uiPlayer:OffLine(offline)
end
function Player:WordChat(ChatCast)
    self.uiPlayer:WordChat(ChatCast, self.userInfo.sex)
end

function Player:VoiceChat(len)
    self.uiPlayer:VoiceChat(len)
end

function Player:StopVoiceChat()
    self.uiPlayer:StopVoiceChat()
end