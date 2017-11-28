
module("NetWork.Action.TableDetaiAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

TableDetaiAction = NetCmdActionBase:new()

function TableDetaiAction:Action(bytes)
    local TableDetailSm  = require("Protol.guildMessage_pb").TableDetailSm()
    TableDetailSm:ParseFromString(bytes)
    Debug.log(stringify(TableDetailSm))
    if TableDetailSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(TableDetailSm.code)
        return
    end
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.TableDetailSm(TableDetailSm)
end

require("NetWork.NetCmdSet").CmdSet[1548] = TableDetaiAction