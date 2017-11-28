module("Module.ConfigModule",package.seeall)

ConfigModule = {}

function ConfigModule:Init()
    local UpdateConfig = Asset.Script.Update.UpdateConfig
    local strData = UpdateConfig.Instance.LoginServerUrl
    local json = require("cjson")
    local jsondata = json.decode(strData)
    self.SessionUrl = jsondata.login[1]
    self.ImRul = jsondata.im
    if PlatformTool.Instance:GetPlatformID() == 2 then
        self.smallVersion = jsondata.iosversion
    else
        self.smallVersion = jsondata.androidversion
    end
    local VoiceTool = require("VoiceTool").VoiceTool
    VoiceTool:Init(self.ImRul)
end