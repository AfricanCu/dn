module("VoiceTool", package.seeall)

VoiceTool = {}

function VoiceTool:Init(ImRul)
    self.ImRul = ImRul
    function MsgCallBack(rtn)
        Debug.log("MsgCallBack rtn:"..rtn)
        local json = require("cjson") 
        local jsonData = json.decode(rtn)
        if jsonData.Event == "OnWillPlay" then
            local params = jsonData.Param
            local userID = params.userid
            local len = params.record_len
            local GameHost = require("Module.GameModule.GameHost").GameHost
            local player = GameHost:GetPlayerByUid(userID)
            if player ~= nil then
                self.lastPlayer = player
                player:VoiceChat(len)
            else
                Debug.log("userID's player is null:"..userID)
            end
        elseif jsonData.Event == "OnPlayStop" then
            --[[if self.lastPlayer ~= nil then
                self.lastPlayer:StopVoiceChat()
            end
            local SoundModule = require("Module.SoundModule").SoundModule
            SoundModule:BeginBGM()]]--
        elseif jsonData.Event == "OnRecordStart" then

        elseif jsonData.Event == "OnRecordTimeOut" then
            --上传超时
        elseif jsonData.Event == "OnRecordFailed" then
            local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
            UIWinMgr:CloseWindow("VoiceChatWin")
            UIWinMgr:OpenNotice("录音不成功。")
        elseif jsonData.Event == "OnRecordStop" then
            local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
            UIWinMgr:CloseWindow("VoiceChatWin")
            if jsonData.Param.Url ~= nil then
                local RoomModule = require("Module.RoomModule").RoomModule
                RoomModule.ImCm(jsonData.Param.Url)
            end
        end
    end
    TFVoiceTool.Instance:SetMsgCallBack(MsgCallBack)
end

function VoiceTool:StartRecord(userid, roomid)
    self:StopPlay()
    local json = require("cjson") 
    local config = {}
    config.url = self.ImRul
    config.userid = tostring(userid)
    config.appid = "wx1463fc544854bfd0"
    local lastSession = UnityEngine.PlayerPrefs.GetString("lastSession")
    config.token = lastSession
    config.roomid = roomid
    local configJson = json.encode(config)
    TFVoiceTool.Instance:StartRecord(configJson)
end

function VoiceTool:StopRecord()
    TFVoiceTool.Instance:StopRecord()
end

function VoiceTool:CancelRecord()
    TFVoiceTool.Instance:CancelRecord()
end

function VoiceTool:AddPlay(url)
    local json = require("cjson") 
    local config = {}
    config.url = url
    local configJson = json.encode(config)
    TFVoiceTool.Instance:AddPlay(configJson)
end

function VoiceTool:StopPlay()
    TFVoiceTool.Instance:StopPlay()
end