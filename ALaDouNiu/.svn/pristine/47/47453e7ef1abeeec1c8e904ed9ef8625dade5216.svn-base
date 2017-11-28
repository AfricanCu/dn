
module("NetWork.Action.TableInfoAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

TableInfoAction = NetCmdActionBase:new()

function TableInfoAction:Action(bytes)
    local TableInfoSm  = require("Protol.guildMessage_pb").TableInfoSm()
    TableInfoSm:ParseFromString(bytes)
    Debug.log(stringify(TableInfoSm))
    if TableInfoSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(TableInfoSm.code)
        return
    end
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.TableInfoSm(TableInfoSm)
end

require("NetWork.NetCmdSet").CmdSet[1523] = TableInfoAction