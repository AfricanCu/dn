
module("NetWork.Action.CreateJulebuAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

CreateJulebuAction = NetCmdActionBase:new()

function CreateJulebuAction:Action(bytes)
    local CreateJulebuSm  = require("Protol.guildMessage_pb").CreateJulebuSm()
    CreateJulebuSm:ParseFromString(bytes)
    Debug.log(stringify(CreateJulebuSm))
    if CreateJulebuSm.code == 1 then
        --local MainUserData = require("DynamicData.MainUserData").MainUserData
        --[[if #MainUserData.MyClub>9 then 
            local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
            UIWinMgr:OpenNotice("无法创建更多俱乐部...")
            return
        end]]
        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.CreateClubSm(CreateJulebuSm)
    else
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenNotice("无法创建更多俱乐部...")
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(CreateJulebuSm.code)
    end
end

require("NetWork.NetCmdSet").CmdSet[1507] = CreateJulebuAction --注册处理对象