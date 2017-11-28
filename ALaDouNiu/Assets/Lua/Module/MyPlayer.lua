--出牌的基础类
module("MyPlayer", package.seeall)

local Player = require("Player").Player
MyPlayer = Player:new()

--构造函数
function MyPlayer:new(o)
    o = o or {}
    setmetatable(o, {__index = self})
    return o
end

function MyPlayer:FaPaiAni(num,puke)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local PaiWin = UIWinMgr:GetWindow("PaiWin")
    PaiWin:FaiPai(self.dir, num, puke)
end

--抢庄
function MyPlayer:QiangZhuang(state)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local GameWin = UIWinMgr:GetWindow("GameWin")

    if state then
        GameWin:QzBeginCast()
    else
        GameWin:QZEnd()
    end
end

function MyPlayer:YaZhuBegin()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local GameWin = UIWinMgr:GetWindow("GameWin")
    GameWin:BetOnBeginCast()
end


function MyPlayer:YaZhuEnd()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local GameWin = UIWinMgr:GetWindow("GameWin")
    GameWin:BetEnd()
end

function MyPlayer:PinNiuBegin(FaLastPukeCast)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local GameWin = UIWinMgr:GetWindow("GameWin")
    GameWin:BeginPinNiu(FaLastPukeCast)
end

function MyPlayer:PinNiuEnd()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local GameWin = UIWinMgr:GetWindow("GameWin")
    GameWin:EndPinNiu()
end

function MyPlayer:OffLineInit(usersRound, reconnectSm)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local PaiWin = UIWinMgr:GetWindow("PaiWin")
    local GameWin = UIWinMgr:GetWindow("GameWin")
    Debug.log("self.userInfo.gstate:"..self.userInfo.gstate)
    --未准备
    if self.userInfo.gstate == 0 then
        GameWin.b_ZhunBei.gameObject:SetActive(true)
        GameWin.b_Break.gameObject:SetActive(true)
        self:ShowReady(false)
    --已准备
    elseif self.userInfo.gstate == 1 then
        Debug.log("已准备已准备已准备已准备已准备已准备已准备已准备")
        GameWin.b_ZhunBei.gameObject:SetActive(false)
        GameWin.b_Break.gameObject:SetActive(false)
        self:ShowReady(true)
    --未抢庄
    elseif self.userInfo.gstate == 2 then
        PaiWin:chonglian(self.dir, 4, usersRound.faLastPukeCast.puke)
        
        self:QiangZhuangState(false)
        self:QiangZhuang(true)
    --已抢庄
    elseif self.userInfo.gstate == 3 then
        PaiWin:chonglian(self.dir, 4, usersRound.faLastPukeCast.puke)
        self:QiangZhuangState(true, usersRound.qiang)
        self:QiangZhuang(false)
    --未押注
    elseif self.userInfo.gstate == 4 then
        PaiWin:chonglian(self.dir, 4, usersRound.faLastPukeCast.puke)
        self:CloseBetLv()
        local GameHost = require("Module.GameModule.GameHost").GameHost
        if self.userInfo.seetIndex ~= GameHost.bankerIndex then
            self:YaZhuBegin()
        end
    --已押注
    elseif self.userInfo.gstate == 5 then
        PaiWin:chonglian(self.dir, 4, usersRound.faLastPukeCast.puke)
        self:BetLv(usersRound.betTimes)
        self:YaZhuEnd()
    --未完成拼牛
    elseif self.userInfo.gstate == 6 then
        PaiWin:chonglian(self.dir, 5, usersRound.faLastPukeCast.puke)
        if usersRound.faLastPukeCast.niuIndex ~= nil and table.getn(usersRound.faLastPukeCast.niuIndex) > 0 then
            PaiWin:YouNiu(usersRound.faLastPukeCast.niuIndex)
        else
            PaiWin:YouNiu()
        end

        self:PinNiuBegin(usersRound.faLastPukeCast)
    --已完成拼牛
    elseif self.userInfo.gstate == 7 then
        PaiWin:chonglian(self.dir, 5, usersRound.faLastPukeCast.puke)
        PaiWin:Finish(self.dir)
        self:PinNiuEnd()
    --未选继续
    elseif self.userInfo.gstate == 8 then
        PaiWin:ShowPai(self.dir, usersRound.faLastPukeCast.puke, usersRound.faLastPukeCast.niu)
        local XiaoJieSuanWin = UIWinMgr:OpenWindow("XiaoJieSuanWin")
        XiaoJieSuanWin:SetInfo(reconnectSm.bullResultCast)
    --已选继续
    elseif self.userInfo.gstate == 9 then
        local GameHost = require("Module.GameModule.GameHost").GameHost
        GameHost:ClearPai()
        self:ShowReady(true)
    end
end