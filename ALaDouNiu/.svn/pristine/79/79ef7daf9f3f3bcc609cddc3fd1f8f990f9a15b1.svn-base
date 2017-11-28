
module("NetWork.Action.InfoSetAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

InfoSetAction = NetCmdActionBase:new()

function InfoSetAction:Action(bytes)
    local InfoSetSm  = require("Protol.guildMessage_pb").InfoSetSm()
    InfoSetSm:ParseFromString(bytes)
    Debug.log(stringify(InfoSetSm))
    if InfoSetSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenNotice("其他设置失败...")
        return
    end
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.ClubInfoSetSm(InfoSetSm)
end
require("NetWork.NetCmdSet").CmdSet[1509] = InfoSetAction --注册处理对象