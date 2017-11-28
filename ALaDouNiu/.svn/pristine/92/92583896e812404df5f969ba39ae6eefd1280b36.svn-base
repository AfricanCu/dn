--消息处理对象合集
module("NetWork.NetCmdSet",package.seeall)

CmdSet = {}

--相应消息
function CmdSet:ExecuteAction(cmd,bytes)
    Action = CmdSet[cmd]
    if nil == Action then
        Debug.log("未找到消息 "..cmd.."的处理对象")
        return
    end
    return Action:Action(bytes)
end
