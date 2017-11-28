module("NetWork.Action.QuitJulebuAction", package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

QuitJulebuAction = NetCmdActionBase:new()

function QuitJulebuAction:Action(bytes)
    local QuitJulebuSm = require("Protol.guildMessage_pb").QuitJulebuSm()
    QuitJulebuSm:ParseFromString(bytes)
    Debug.log(stringify(QuitJulebuSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.QuitJulebuSm(QuitJulebuSm)
end

require("NetWork.NetCmdSet").CmdSet[1521] = QuitJulebuAction --注册处理对象