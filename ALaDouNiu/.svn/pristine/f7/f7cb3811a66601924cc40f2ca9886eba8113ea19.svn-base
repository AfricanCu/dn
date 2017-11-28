
module("NetWork.Action.TableInfoCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

TableInfoCastAction = NetCmdActionBase:new()

function TableInfoCastAction:Action(bytes)
    local TableInfoCast  = require("Protol.guildMessage_pb").TableInfoCast()
    TableInfoCast:ParseFromString(bytes)
    Debug.log(stringify(TableInfoCast))
    --local RoomModule = require("Module.RoomModule").RoomModule
    --RoomModule.TableInfoCast(TableInfoCast)
end

require("NetWork.NetCmdSet").CmdSet[1520] = TableInfoCastAction --注册处理对象