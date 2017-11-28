module("TimeFormat", package.seeall)

TimeFormat = {}

--- 获得CD倒计时
function TimeFormat:CD2Str(time)
    local h = math.floor(time / 3600)
    local m = math.floor((time % 3600) / 60)
    local s = math.floor(time % 60)
    return string.format("%02d:%02d:%02d", h, m, s)
end

function TimeFormat:CDMinFormat(sec)
    local m = math.floor((sec % 3600) / 60)
    local s = math.floor(sec % 60)
    return string.format("%02d:%02d", m, s)
end

function TimeFormat:CountDown(sec, playSound)
    if playSound == true then
        if tonumber(sec) <= 5 then
            local FightModule = require("Module.FightModule").FightModule
            FightModule.PlaySound("Sound_Effect_clockSound")
        end
    end

    if tonumber(sec) <= 5 then
        return "[FF0000]("..sec..")[-]"
    else
        return "[FFE057]("..sec..")[-]"
    end
    
end

--清除服务器时间信息
function TimeFormat:ClearServerTime()
    self.serverTimeAnchor = nil
end

--获取服务器时间信息
function TimeFormat:GetServerTimeInfo()
    self.localTimeAnchor = UnityEngine.Time.unscaledTime
    Debug.log("self.localTimeAnchor:"..self.localTimeAnchor)
    local NetMgr = require("NetWork").NetMgr
    local Msg = require("Protol.loginMessage_pb").CompTimeCm()
    NetMgr:SendMsg(-3,Msg)
end

--设置服务器时间信息
function TimeFormat:SetServerTimeInfo(CompTimeSm)
    --计时器结束执行事件
    local localTime = UnityEngine.Time.unscaledTime
    Debug.log("localTime:"..localTime)
    local gap = localTime - self.localTimeAnchor
    Debug.log("gap:"..gap)
    --防止误差过大，做一个初步的来回10秒间隔限制
    if gap > 10 then
        gap = 10
    end
    self.localTimeAnchor = self.localTimeAnchor + gap*0.5
    self.serverTimeAnchor = CompTimeSm.sTime * 0.001
    Debug.log("self.serverTimeAnchor:"..self.serverTimeAnchor)
end

--求服务器时间结构所剩下的剩余时间
function TimeFormat:LeftSecond(TimeInSecond)
    if self.serverTimeAnchor ~= nil then
        Debug.log("TimeInSecond.sTime:"..TimeInSecond.sTime.." TimeInSecond.second:"..TimeInSecond.second)
        local endTime = TimeInSecond.sTime * 0.001  + TimeInSecond.second
        local nowTime = self.serverTimeAnchor + UnityEngine.Time.unscaledTime - self.localTimeAnchor
        local leftTime = endTime - nowTime
        return leftTime
    else
        return TimeInSecond.second
    end
end