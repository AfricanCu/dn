--游戏主持人
module("Module.GameModule.GameHost",package.seeall)

GameHost = {}

--玩家列表
GameHost.players = {}
--玩法数据
GameHost.PlayType = nil 
--房间号
GameHost.RoomId = nil
--我自己的座位号
GameHost.myIndex = nil
--本局庄家座位号
GameHost.bankerIndex = nil
--俱乐部名字
GameHost.clubName = nil
--俱乐部桌号
GameHost.clubDeskNum = nil
GameHost.isGameOver = false

GameHost.NiuTypeEnum =
{
    ["0"] = "没牛",
    ["1"] = "牛一",
    ["2"] = "牛二",
    ["3"] = "牛三",
    ["4"] = "牛四",
    ["5"] = "牛五",
    ["6"] = "牛六",
    ["7"] = "牛七",
    ["8"] = "牛八",
    ["9"] = "牛九",
    ["10"] = "牛牛",
    ["11"] = "顺子",
    ["12"] = "同花",
    ["13"] = "葫芦",
    ["14"] = "五小牛",
    ["15"] = "五花牛",
    ["16"] = "炸弹",
    ["17"] = "同花顺",
}

GameHost.WordChatEnum = 
{
    [1] = "伐好意思，刚接了个电话",
    [2] = "房间没满，房主再找几个嘛",
    [3] = "爆发吧我的小宇宙，这把我要通吃",
    [4] = "哥这桩一柱擎天，稳如泰山啊",
    [5] = "别磨蹭了，时间就是金钱",
    [6] = "哎哟我滴妈，手气有点旺哈哈",
    [7] = "把把都是赢，你咋不上天呐",
    [8] = "牛牛斗起来，气氛搞起来",
    [9] = "这…这牌，气得我手直抖",
    [10] = "又是没牛，今晚我要输得吐血",
    [11] = "四五六七八，嘿嘿，要的就是发",
    [12] = "放学别走，我要跟你再作战三百回合"
}

--销毁游戏
function GameHost:Destroy()
    self.players = {}
    self.PlayType = nil
    self.RoomId = nil
    self.myIndex = nil
    self.bankerIndex = nil
    self.clubName = nil
    self.clubDeskNum = nil
    self.isGameOver = false
end

--添加玩家
function GameHost:AddPlayer(player)
    self.players[player.userInfo.seetIndex] = player
    Event.Brocast(EventDefine.OnPlayerJoinGame,player)
    --Debug.log("玩家："..player.userInfo.nickname.." 位置索引："..player.userInfo.seetIndex.." 状态"..player.userInfo.gstate.." 加入游戏")
end

--删除玩家
function GameHost:RemovePlayer(seetIndex)
    Debug.log("wan")
    local player = self:GetPlayer(seetIndex)
    if not player then
        return
    end
    self.players[seetIndex] = nil
    Event.Brocast(EventDefine.OnPlayerLeaveGame,player)
    Debug.log("玩家："..player.userInfo.nickname.." 位置索引："..player.userInfo.seetIndex.." 离开游戏")
end

--获取玩家数据
function GameHost:GetPlayer(seetIndex)
    local player = self.players[seetIndex]
    return player
end

function GameHost:GetSelfPlayer()
    local player = self.players[self.myIndex]
    return player
end

function GameHost:GetPlayerByUid(uid)
    for k,v in pairs(self.players) do
        local tmpUid = tonumber(v.userInfo.uid)
        if uid == tostring(tmpUid) then
            return v
        end
    end
end

function GameHost:CalcDir(idx)
    if idx == self.myIndex then
        return 1
    elseif idx > self.myIndex then
        return (1 + idx - self.myIndex)
    else
        return (5 + 1 + idx - self.myIndex)
    end
end

--清除桌上所有的牌
function GameHost:ClearPai()
    --清除每个人的牌
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local PaiWin = UIWinMgr:GetWindow("PaiWin")
    PaiWin:ClearPai()
    --清除每个人的押注倍率
    for k,v in pairs(self.players) do
        if v ~= nil then
            v:CloseBetLv()
        end
    end
end

--发牌动画
function GameHost:FaPaiAni(fapaiPlayers, mySeetIndex, num, myPuke)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local GameWin = UIWinMgr:GetWindow("GameWin")
    GameWin:FaPaiAni()
    local SoundModule = require("Module.SoundModule").SoundModule
    SoundModule:PlaySound("GameSound_startgame")

    for k,v in pairs(fapaiPlayers) do
        if v ~= nil then
            v:ShowReady(false)
        end
    end

    function Delay()
        for k,v in pairs(fapaiPlayers) do
            if v ~= nil then
                v:ShowReady(false)
                if v.userInfo.seetIndex ~= mySeetIndex then
                    v:FaPaiAni(num)
                else
                    v:FaPaiAni(num,myPuke)
                end
            end
        end

        local soundName = "GameSound_fapai3"
        function Rate()
            SoundModule:PlaySound(soundName)
        end
        function End()
            -- body
        end
        CountDownMgr.Instance:CreateCountDown(0.8, 0.1, Rate, End)
    end
    CountDownMgr.Instance:CreateCountDown(1.5, 1.5, Delay)
end

--开始抢庄
function GameHost:QiangZhuang()
    local myPlayer = self:GetSelfPlayer()
    if myPlayer ~= nil then
        myPlayer:QiangZhuang(true)
    end


end

--抢庄状态更新
function GameHost:QiangZhuangCast(seetIndex, qiang)
    local player = self:GetPlayer(seetIndex)
    if player ~= nil then
        player:QiangZhuangState(true, qiang)
    end

    for k,v in pairs(self.players) do
        if v ~= nil then
            v:ShowZhuangState(false)
        end
    end
end

--庄家变化，需要把抢庄相关东西先隐藏
function GameHost:ZhuangChg(seetIndex,ran,st)
    self.bankerIndex = seetIndex
    for k,v in pairs(self.players) do
        if v ~= nil then
            v:QiangZhuangState(false)
            v:ShowZhuangState(false)
        end
    end

    --TODO  需要做一个随机动画，如果存在随机庄的情况下
    if ran then
        local tmp = 0
        for k,v in ipairs(st) do
            if v ~= seetIndex then
                function Ani()
                    local player = self.players[v]
                    if player ~= nil then
                        player:RandZhuangAni()
                    end
                end
                
                if tmp == 0 then
                    Ani()
                else
                    CountDownMgr.Instance:CreateCountDown(tmp*0.2, tmp*0.2, Ani)
                end
                tmp = tmp + 1
            end
        end

        function Delay()
            local bankerPlayer = self.players[seetIndex]
            if bankerPlayer ~= nil then
                bankerPlayer:ShowZhuangState(true)
            end
        end

        CountDownMgr.Instance:CreateCountDown(tmp*0.2, tmp*0.2, Delay)
    else
        local bankerPlayer = self.players[seetIndex]
        if bankerPlayer ~= nil then
            bankerPlayer:ShowZhuangState(true)
        end
    end
end

--开始压注
function GameHost:YaZhuBegin()
    local myPlayer = self:GetSelfPlayer()
    if myPlayer ~= nil then
        myPlayer:YaZhuBegin()
    end
end

--压注状态更新
function GameHost:BetOnCast(seetIndex, betTimes)
    local player = self:GetPlayer(seetIndex)
    if player ~= nil then
        player:BetLv(betTimes)
    end

    if seetIndex == self.myIndex then
        player:YaZhuEnd()
    end
end

--发第5张牌
function GameHost:FaLastPukeCast(FaLastPukeCast)
    local renshu = 0
    for k,v in pairs(self.players) do
        if v ~= nil then
            renshu = renshu + 1
            if v.userInfo.seetIndex ~= self.myIndex then
                v:FaPaiAni(1)
            else
                local tmpTable = {}
                table.insert(tmpTable, FaLastPukeCast.puke[5])
                Debug.log("FaLastPukeCast.puke[5]:"..FaLastPukeCast.puke[5].num)
                v:FaPaiAni(1,tmpTable)
            end
        end
    end

    if renshu > 3 then
        renshu = 3
    end
    local SoundModule = require("Module.SoundModule").SoundModule
    local soundName = "GameSound_fapai1"
    function Rate()
        SoundModule:PlaySound(soundName)
    end
    function End()
        -- body
    end
    CountDownMgr.Instance:CreateCountDown(renshu*0.1, 0.1, Rate, End)

    function DelayShowNiuPai()
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        local PaiWin = UIWinMgr:GetWindow("PaiWin")
        if FaLastPukeCast.niuIndex ~= nil and table.getn(FaLastPukeCast.niuIndex) > 0 then
            PaiWin:YouNiu(FaLastPukeCast.niuIndex)
        else
            PaiWin:YouNiu()
        end

        local myPlayer = self:GetSelfPlayer()
        if myPlayer ~= nil then
            myPlayer:PinNiuBegin(FaLastPukeCast)
        end
    end
    CountDownMgr.Instance:CreateCountDown(0.7, 0.7, DelayShowNiuPai)
end

--完成拼牛状态更新
function GameHost:FinishPukeCast(seetIndex)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local player = self.players[seetIndex]
    if player ~= nil then
        local PaiWin = UIWinMgr:GetWindow("PaiWin")
        PaiWin:Finish(player.dir)
    end

    if seetIndex == self.myIndex then
        player:PinNiuEnd()
    end

    local soundName = "GameSound_deal"
    local SoundModule = require("Module.SoundModule").SoundModule
    SoundModule:PlaySound(soundName)
end

--小结算
function GameHost:XiaoJieSuan(BullResultCast)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local PaiWin = UIWinMgr:GetWindow("PaiWin")
    local tmpCount = 0
    for k,v in ipairs(BullResultCast.rs) do
        local player = self:GetPlayer(v.seetIndex)
        if player ~= nil then
            function ShowPai()
                local soundName = nil
                --男性
                if player.userInfo.sex == 1 then
                    soundName = "bf_"..v.niu.."_bull_f"
                else
                    soundName = "bf_"..v.niu.."_bull_m"
                end
                
                local SoundModule = require("Module.SoundModule").SoundModule
                SoundModule:PlaySound(soundName)
                PaiWin:ShowPai(player.dir, v.puke, v.niu)
            end
            tmpCount = tmpCount + 1
            CountDownMgr.Instance:CreateCountDown(tmpCount*0.5, tmpCount*0.5, ShowPai)
        end
    end

    function ShowXiaoJieSuanWin()
        for k,v in ipairs(BullResultCast.rs) do
            local player = self:GetPlayer(v.seetIndex)
            if player ~= nil then
                player.userInfo.coin = v.coin
                player:ScoreUpdate()
            end
        end
        local XiaoJieSuanWin = UIWinMgr:OpenWindow("XiaoJieSuanWin")
        XiaoJieSuanWin:SetInfo(BullResultCast)
    end
    CountDownMgr.Instance:CreateCountDown(tmpCount*0.5+0.7, tmpCount*0.5+0.7, ShowXiaoJieSuanWin)
end