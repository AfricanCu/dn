
module("NetWork.Action.JulebuRecordAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

JulebuRecordAction = NetCmdActionBase:new()

function JulebuRecordAction:Action(bytes)
    local JulebuRecordSm  = require("Protol.guildMessage_pb").JulebuRecordSm()
    JulebuRecordSm:ParseFromString(bytes)
    Debug.log(stringify(JulebuRecordSm))
    if JulebuRecordSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenNotice("访问数据失败...")
        return
    end
    Debug.log("777777777777777777777777777755")
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.ClubRecordSm(JulebuRecordSm)
end

require("NetWork.NetCmdSet").CmdSet[1540] = JulebuRecordAction --注册处理对象