
module("NetWork.Action.OtherSetAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

OtherSetAction = NetCmdActionBase:new()

function OtherSetAction:Action(bytes)
    local OtherSetSm  = require("Protol.guildMessage_pb").OtherSetSm()
    OtherSetSm:ParseFromString(bytes)
    Debug.log(stringify(OtherSetSm))
    if OtherSetSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenNotice("其他设置失败...")
        return
    end
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.ClubOtherSetSm(OtherSetSm)
end

require("NetWork.NetCmdSet").CmdSet[1513] = OtherSetAction --注册处理对象