module("Module.RoomModule",package.seeall)

local NetMgr = require("NetWork").NetMgr
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local VoiceTool = require("VoiceTool").VoiceTool

RoomModule = {}
RoomModule.roomNumber = nil

--创建房间预处理
function RoomModule.CreateRoomBefore(type, playTypeCreater)
    local Msg = require("Protol.roomMessage_pb").CreateRoomBeforeCm()
    
    RoomModule.playTypeCreater = playTypeCreater
    playTypeCreater(Msg.playType, type)

    NetMgr:SendMsg(233,Msg)
    UIWinMgr:OpenMask("创建房间中...")
end

--预加入房间返回
function RoomModule.CreateRoomBeforeSm(CreateRoomBeforeSm)
    UIWinMgr:CloseMask()
    if 1 == CreateRoomBeforeSm.code then
        --不需要切服，直接进入房间
        Debug.log("直接加入房间")

        local RoomModule = require("Module.RoomModule").RoomModule
        RoomModule.CreateRoom(CreateRoomBeforeSm.playType)
    elseif 2 == CreateRoomBeforeSm.code then
        --走切服流程
        Debug.log("开始切换服务器")
        local MainUserData = require("DynamicData.MainUserData").MainUserData
        agrs = 
        {
            MainUserData.name,
            CreateRoomBeforeSm.sw.myCode,
            CreateRoomBeforeSm.sw.sId,
            CreateRoomBeforeSm.sw.type,
            CreateRoomBeforeSm.sw.host,
            CreateRoomBeforeSm.sw.port,
            "",
            CreateRoomBeforeSm.playType
        }
        local LoginModule = require("Module.LoginModule").LoginModule
        LoginModule:SwLogin(unpack(agrs))
    else
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(CreateRoomBeforeSm.code)
    end
end


--创建房间
function RoomModule.CreateRoom(type)
    local Msg = require("Protol.roomMessage_pb").CreateRoomCm()
    RoomModule.playTypeCreater(Msg.playType, type)

    NetMgr:SendMsg(200,Msg)
    UIWinMgr:OpenMask("创建房间中...")
end

--创建房间返回
function RoomModule.CreateRoomSm(createRoomSm)
    UIWinMgr:CloseMask()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    if 1 ~= createRoomSm.code then
        UIWinMgr:OpenErrorNotice(createRoomSm.code)
        return
    end

    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:Destroy() --进入游戏前本地清理残余游戏数据

    GameHost.PlayType = createRoomSm.playType
    GameHost.RoomId = createRoomSm.roomId
    Debug.log("房间号："..GameHost.RoomId)
    GameHost.myIndex = createRoomSm.info.seetIndex
    Debug.log("GameHost.myIndex:"..GameHost.myIndex)

    UIWinMgr:CloseWindow("CreateRoomWin")
    UIWinMgr:CloseWindow("MainWin")
    
    local GameWin = UIWinMgr:GetWindow("GameWin")
    GameWin:ClearUIPlayers()
    
    local MyPlayer = require("MyPlayer").MyPlayer
    local ply = MyPlayer:new()
    ply:Init(createRoomSm.info)
    GameHost:AddPlayer(ply) --添加自己的玩家信息
    
    GameWin:Show()
    GameWin:InitInfo()
    Event.Brocast(EventDefine.OnGameStart,false)
    Event.Brocast(EventDefine.OnGameRoundChange,0)
end



--代开房预处理
function RoomModule.CreateProxyRoomBefore(type, playTypeCreater)
    local Msg = require("Protol.roomMessage_pb").ProxyCreateRoomBeforeCm()
    RoomModule.playTypeCreater = playTypeCreater
    playTypeCreater(Msg.playType, type)
    NetMgr:SendMsg(240,Msg)
    Debug.log("发送：240")
    -- UIWinMgr:OpenMask("创建房间中...")
end

--代开房预处理返回
function RoomModule.CreateProxyRoomBeforeSm(ProxyCreateRoomBeforeSm)
    UIWinMgr:CloseMask()
    if 1 == ProxyCreateRoomBeforeSm.code then
        --不需要切服，直接进入房间
        Debug.log("直接加入房间")

        local RoomModule = require("Module.RoomModule").RoomModule
        RoomModule.CreateRoom(ProxyCreateRoomBeforeSm.playType)
    elseif 2 == ProxyCreateRoomBeforeSm.code then
        --走切服流程
        Debug.log("开始切换服务器")
        local MainUserData = require("DynamicData.MainUserData").MainUserData
        agrs = 
        {
            MainUserData.name,
            ProxyCreateRoomBeforeSm.sw.myCode,
            ProxyCreateRoomBeforeSm.sw.sId,
            ProxyCreateRoomBeforeSm.sw.type,
            ProxyCreateRoomBeforeSm.sw.host,
            ProxyCreateRoomBeforeSm.sw.port,
            "",
            ProxyCreateRoomBeforeSm.playType
        }
        local LoginModule = require("Module.LoginModule").LoginModule
        LoginModule:SwLogin(unpack(agrs))
    else

        if 235 == ProxyCreateRoomBeforeSm.code or 161 == ProxyCreateRoomBeforeSm.code then
            UIWinMgr:OpenPromptMaskWin("钻石不足")
        end

        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(ProxyCreateRoomBeforeSm.code)


    end
end


-- 代开房间
function RoomModule.ProxyCreateRoom(type)
    local Msg = require("Protol.roomMessage_pb").ProxyCreateRoomCm()
    RoomModule.playTypeCreater(Msg.playType, type)
    NetMgr:SendMsg(225,Msg)
    UIWinMgr:OpenMask("创建房间中...")
end

--预加入房间
function RoomModule.JoinRoomBeforeCm(RoomId)
    local Msg = require("Protol.roomMessage_pb").JoinRoomBeforeCm()
    Msg.roomId = RoomId
    NetMgr:SendMsg(202,Msg)
    -- UIWinMgr:OpenMask("加入房间中...")
end

--预加入房间返回
function RoomModule.JoinRoomBeforeSm(JoinRoomBeforeSm)
    UIWinMgr:CloseMask()

    Debug.log("JoinRoomBeforeSm"..JoinRoomBeforeSm.code)
    if 1 == JoinRoomBeforeSm.code then
        --不需要切服，直接进入房间
        Debug.log("直接加入房间")

        local RoomModule = require("Module.RoomModule").RoomModule
        RoomModule.JoinRoom("%%%%%%%%%%%%%%%%"..JoinRoomBeforeSm.roomId)
    elseif 2 == JoinRoomBeforeSm.code then
        --走切服流程
        Debug.log("开始切换服务器")
        local MainUserData = require("DynamicData.MainUserData").MainUserData
        agrs = 
        {
            MainUserData.name,
            JoinRoomBeforeSm.sw.myCode,
            JoinRoomBeforeSm.sw.sId,
            JoinRoomBeforeSm.sw.type,
            JoinRoomBeforeSm.sw.host,
            JoinRoomBeforeSm.sw.port,
            JoinRoomBeforeSm.roomId,
        }
        local LoginModule = require("Module.LoginModule").LoginModule
        LoginModule:SwLogin(unpack(agrs))
    else

        Debug.log("JoinRoomBeforeSm---------------------------------------")
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        -- UIWinMgr:OpenErrorNotice(JoinRoomBeforeSm.code)
        if 102 == JoinRoomBeforeSm.code or 146 == JoinRoomBeforeSm.code then
            UIWinMgr:OpenPromptMaskWin("房间号不存在")
        elseif 167 == JoinRoomBeforeSm.code  then
            UIWinMgr:OpenPromptMaskWin("钻石不足")

        elseif 213 == JoinRoomBeforeSm.code then
            UIWinMgr:OpenPromptMaskWin("已经在房间中")

        end
        local JoinRoomWin = require("UI.Windows.JoinRoomWin").JoinRoomWin
        JoinRoomWin:OnShow()
    end
end

--加入房间
function RoomModule.JoinRoom(RoomId, ClubID, DeskID)
    Debug.log("joinRoom")
    local Msg = require("Protol.roomMessage_pb").JoinRoomCm()
    if RoomId ~= nil then
        Msg.roomId = RoomId
    else
        Msg.julebuRoom.id = ClubID
        Msg.julebuRoom.num = DeskID
    end
    --FightRoomData.SetRoomID(Msg.roomId)
    Debug.log("roomId:"..Msg.roomId)
    NetMgr:SendMsg(204,Msg)
    UIWinMgr:OpenMask("加入房间中...")
end

--加入房间返回
function RoomModule.JoinRoomSm(joinRoomSm)
    UIWinMgr:CloseMask()
    Debug.log("----------joinRoomSm"..joinRoomSm.code)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    if 1 ~= joinRoomSm.code then
        UIWinMgr:OpenErrorNotice(joinRoomSm.code)
        Debug.log("sdadadadsa222222222222222")
        --登陆加魔窗的房间失败，直接打开主界面
        UIWinMgr:OpenWindow("MainWin")
        return
    end

    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:Destroy() --进入游戏前本地清理残余游戏数据

    GameHost.PlayType = joinRoomSm.playType
    
    Debug.log("进游戏界面")
    Debug.log("+++++++++进游戏："..joinRoomSm.julebuRoom.id.."桌号"..joinRoomSm.julebuRoom.num)
    if joinRoomSm.julebuRoom ~= nil and joinRoomSm.julebuRoom.id > 0 then
        GameHost.clubName = joinRoomSm.julebuRoom.id
        GameHost.clubDeskNum = joinRoomSm.julebuRoom.num
        --TODO
        Debug.log("+++++++++++从俱乐部进游戏")
        UIWinMgr:CloseWindow("ClubLoobyWin")
    else
        Debug.log("+++++++++++从主大厅进游戏")
        GameHost.clubName = nil
        GameHost.clubDeskNum = nil
        UIWinMgr:CloseWindow("JoinRoomWin")
    end
    GameHost.RoomId = joinRoomSm.roomId

    Debug.log("房间号："..GameHost.RoomId)
    
    UIWinMgr:CloseWindow("MainWin")

    local Player = require("Player").Player
    local MyPlayer = require("MyPlayer").MyPlayer

    local MainUserData = require("DynamicData.MainUserData").MainUserData

    Debug.log("自己的UID："..MainUserData.uid)

    local users = joinRoomSm.users

    local GameWin = UIWinMgr:GetWindow("GameWin") --在添加玩家之前初始化界面
    GameWin:ClearUIPlayers()
    
    --首先添加自己
    for i=1,#users do
        if tonumber(users[i].uid) == tonumber(MainUserData.uid) then
            GameHost.myIndex = users[i].seetIndex
            local ply = MyPlayer:new()
            ply:Init(users[i])
            GameHost:AddPlayer(ply)
        end
        Debug.log("当前玩家UID:"..users[i].uid)
    end

    --再添加别人
    for i=1,#users do
        if tonumber(users[i].uid) ~= tonumber(MainUserData.uid) then
            local ply = Player:new()
            ply:Init(users[i])
            GameHost:AddPlayer(ply)
        end
    end

    GameWin:Show()
    GameWin:InitInfo()
    Event.Brocast(EventDefine.OnGameStart,false)
    Event.Brocast(EventDefine.OnGameRoundChange,0)
    --GameWin:BringToTop()
end

--玩家加入房间或者离开房间广播
function RoomModule.CastRoom(joinRoomCast)
    local GameHost = require("Module.GameModule.GameHost").GameHost
    if 0 ~= joinRoomCast.delSeatIndex then
        local delPlayer = GameHost:GetPlayer(joinRoomCast.delSeatIndex)
        if delPlayer ~= nil then
            delPlayer:UnInit()
        end
        GameHost:RemovePlayer(joinRoomCast.delSeatIndex)
        return
    end
    
    local Player = require("Player").Player
    local ply = Player:new()
    ply:Init(joinRoomCast.addUser)
    GameHost:AddPlayer(ply)
end

--玩家准备操作
function RoomModule.PrepareRoomCm()
    Debug.log("PrepareRoomCm")
    local Msg = require("Protol.roomMessage_pb").PrepareRoomCm()
    NetMgr:SendMsg(211,Msg)
end

--玩家准备操作返回
function RoomModule.PrepareRoomSm(prepareRoomSm)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    if 1 == prepareRoomSm.code then
        local GameHost = require("Module.GameModule.GameHost").GameHost
        local player = GameHost:GetSelfPlayer()
        if player ~= nil then
            player:ShowReady(true)
        else
            Debug.log("准备玩家为空")
        end
    end
end

--已准备好玩家的广播信息
function RoomModule.PrepareRoomCast(prepareRoomCast)
    local seetIndex = prepareRoomCast.seatIndex
    local GameHost = require("Module.GameModule.GameHost").GameHost
    local player = GameHost:GetPlayer(seetIndex)
    Debug.log("准备好的座位号："..seetIndex)
    if player ~= nil then
        player:ShowReady(true)
    else
        Debug.log("准备玩家为空")
    end
end

--玩家离开房间操作
function RoomModule.LeaveRoomCm()
    Debug.log("LeaveRoomCm")
    local Msg = require("Protol.roomMessage_pb").LeaveRoomCm()
    NetMgr:SendMsg(207,Msg)
end

--玩家离开房间操作返回
function RoomModule.LeaveRoomSm(leaveRoomSm)
    if leaveRoomSm.code == 1 then
        RoomModule.BackToLobby()
    else

    end
end

function RoomModule.BackToLobby()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    UIWinMgr:CloseWindow("GameWin")
    UIWinMgr:CloseWindow("GameOverWin")
    UIWinMgr:CloseWindow("JieSanWin")
    UIWinMgr:CloseWindow("PaiWin")
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:Destroy()

    local lastClubID = nil
    if UnityEngine.PlayerPrefs.HasKey("lastClubID") then
        lastClubID = UnityEngine.PlayerPrefs.GetInt("lastClubID")
    end
    local ClubModule = require("Module.ClubModule").ClubModule
    if ClubModule.isInClubLobby and lastClubID ~= nil then
        ClubModule.InClub(lastClubID)
        Debug.log("++++++++++++++++++++++退出俱乐部："..lastClubID)
    else
        UIWinMgr:OpenWindow("MainWin")
    end
end

--玩家上传语音房间的座位号
function RoomModule.ImInfoSaveCm(index)
    local Msg = require("Protol.roomMessage_pb").ImInfoSaveCm()
    Msg.index = index
    NetMgr:SendMsg(117,Msg)
end

--玩家上传语音房间的座位号返回
function RoomModule.ImInfoSaveSm(imInfoSaveSm)

end

--玩家语音房间座位号码更新
function RoomModule.ImInfoSaveCast(imInfoSaveCast)
    
end

--上传离线语音消息
function RoomModule.ImCm(fileid)
    local Msg = require("Protol.roomMessage_pb").ImCm()
    Msg.fileid = fileid
    NetMgr:SendMsg(222,Msg)
end

--游戏开始前解散房间操作
function RoomModule.DissolveRoomCm()
    local Msg = require("Protol.roomMessage_pb").DissolveRoomCm()
    NetMgr:SendMsg(209,Msg)
end

--游戏开始前解散房间操作返回
function RoomModule.DissolveRoomSm(DissolveRoomSm)
    
end

function RoomModule.ProxyRoomsDissovleCm(roomId)
    local Msg = require("Protol.roomMessage_pb").ProxyRoomsDissovleCm()
    Msg.roomId = roomId
    NetMgr:SendMsg(231,Msg)
end

--游戏开始前解散房间广播
function RoomModule.DissolveRoomCast(DissolveRoomCast)
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:Destroy()
    Debug.log("jjjjjjjjjjjjjjjjjjsssssssssssssssssssss")
    RoomModule.BackToLobby()
end

--断线重连处理
function RoomModule.ReconnectSm(ReconnectSm)
    
end

--玩家在线状态广播
function RoomModule.OfflineCast(OfflineCast)
    local seetIndex = OfflineCast.seetIndex
    local offline = false
    if OfflineCast.isOk == 1 then
        offline = false
    else
        offline = true
    end
    local GameHost = require("Module.GameModule.GameHost").GameHost
    local player = GameHost:GetPlayer(seetIndex)
    if player ~= nil then
        player:OfflineHander(offline)
    end
end

function RoomModule.InviteFriend()
    local GameHost = require("Module.GameModule.GameHost").GameHost
    local myContent = ""
    local myRoom = ""
    local titleRoom = ""
    if GameHost.clubName ~= nil then
        titleRoom = "-"..GameHost.clubName.."_"..GameHost.clubDeskNum.."桌"
        myRoom = " "
    else
        titleRoom = "-"..GameHost.RoomId
        myRoom = GameHost.RoomId
    end

    if GameHost.PlayType.pType == 1 then
        myContent = "经典玩法"
    else
        myContent = "疯狂加倍"
    end
    if GameHost.PlayType.round == 1 then
        myContent = myContent.." 10局"
    else
        myContent = myContent.." 20局"
    end

    if GameHost.PlayType.bankerMode == 1 then
        myContent = myContent.."抢庄"
    elseif GameHost.PlayType.bankerMode == 2 then
        myContent = myContent.."轮庄"  
    elseif GameHost.PlayType.bankerMode == 3 then
        myContent = myContent.."固定庄" 
    end

    --[[local json = require("cjson")
    local share = {}
    share.type = 1
    share.title = "土豪金"..PlayTypeName.."3D"..titleRoom
    share.content = myContent
    share.room = myRoom
    share.mo_chuang_link = "https://a6dolt.mlinks.cc/A0FF?room="
    local s_Share = json.encode(share)
    PlatformTool.Instance:MWShare("share_Unity", s_Share, "wx4043e25ad5e000bd", mWShareCallBack)]]--

    local json = require("cjson")
    local share = {}
    share.type = 1
    share.mediatype = 13
    share.title = "疯狂阿拉"..titleRoom.."已有"..# GameHost.players.."人"
    share.content = myContent
    share.link = "http://fir.im/aladn"
    local s_Share = json.encode(share)
    PlatformTool.Instance:MWShare("share_Unity", s_Share, "wx1463fc544854bfd0", mWShareCallBack)


    -- local json = require("cjson")
    -- local share = {}
    -- share.type = 1
    -- share.mediatype = 13
    -- share.title = "疯狂阿拉"
    -- share.content = "疯狂阿拉邀请您点击链接加入游戏,☞下载"
    -- share.link = "http://fir.im/aladn"
    -- local s_Share = json.encode(share)
    -- PlatformTool.Instance:MWShare("share_Unity", s_Share, "wx1463fc544854bfd0", mWShareCallBack)
end


--复制房间信息
function RoomModule.CopyTextToClipBoard(playType)
    Debug.log("-----------------调了------------------------------")
    Debug.log(stringify(playType))

    if playType == nil or playType == {} then

        UIWinMgr:OpenPromptMaskWin("房间信息复制失败")
        
        return
    end
    local text = nil
    if playType.PlayType.bankerMode == 1 then
        text = "抢庄"
    elseif playType.PlayType.bankerMode == 2 then
        text = "轮庄"
    elseif playType.PlayType.bankerMode == 3 then
        text = "固定庄"
    end
    local round = nil
    if playType.PlayType.round == 1 then
        round = "10局"
    elseif playType.PlayType.round then
        round = "20局"
    end
    local pType = nil
    if playType.PlayType.pType == 1 then
        pType = "金典牛牛"
    elseif playType.PlayType.pType == 2 then
        pType = "疯狂加倍"
    end

    local text = "疯狂阿拉房号["..playType.RoomId.."]"..round.."、"..pType.."、"..text.."(复制此消息打开游戏可直接进入该房间)"

    Debug.log("疯狂阿拉房号:"..text)
    if text ~= nil then
        UIWinMgr:OpenPromptMaskWin("房间信息复制成功")
        UnityEngine.PlayerPrefs.SetInt("copyState", 1)

    end
    PlatformTool.Instance:CopyTextToClipBoard(text)
    
end


-- 复制其他信息
function RoomModule.copyOtherString_Action(text,messge)


    Debug.log("text = "..text)
    if text ~= nil then

        UIWinMgr:OpenPromptMaskWin(messge.."复制成功")

        PlatformTool.Instance:CopyTextToClipBoard(text)
    else
        UIWinMgr:OpenPromptMaskWin(messge.."复制失败")
    end

end

-- 获取粘贴板信息
function RoomModule.getCopyClipBoardString()

    Debug.log("###调用")

    function getCopyClipBoardCallback(boardString)
                local copyState = UnityEngine.PlayerPrefs.GetInt("copyState")
                if boardString == "1" or boardString == nil or boardString == "" or copyState == 1 then
                    UnityEngine.PlayerPrefs.DeleteKey("copyState")
                    return
                end
                if string.match(boardString ,"疯狂阿拉房号") == "疯狂阿拉房号" then
                    local p1 = string.find(boardString,"%[")
                    local p2 = string.find(boardString,"%]")
                    local code = string.sub(boardString, p1+1, p2-1)                
                    Debug.log("code"..#code) 
                    function joinRoomAction()
                        self.JoinRoomBeforeCm(code)
                    end
                    UIWinMgr:OpenNotice("是否加入房间："..code, joinRoomAction)
                end 
            end
            
            -- self.callback = getCopyClipBoardCallback
            PlatformTool.Instance:CopyClipboardString(getCopyClipBoardCallback)

end





