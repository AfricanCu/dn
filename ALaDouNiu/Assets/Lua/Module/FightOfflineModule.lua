module("Module.FightOfflineModule",package.seeall)

FightOfflineModule = {}

local NetMgr = require("NetWork").NetMgr
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr


function FightOfflineModule.RegisterOffLine()
    Debug.log("添加断线重连处理")
    --连接上网络了，先关闭一次语音，防止之前没退出成功的情况出现
    --VoiceTool:QuitRoom()
    FightOfflineModule.registerOffline = true
    Event.AddListener(EventDefine.OnConnectClose,FightOfflineModule.OffLine)
end

function FightOfflineModule.UnRegisterOffLine()
    Debug.log("移除断线重连处理")
    if FightOfflineModule.registerOffline then
        Event.RemoveListener(EventDefine.OnConnectClose,FightOfflineModule.OffLine)
    end

    FightOfflineModule.registerOffline = false
end

function FightOfflineModule.FightOffLine()
    UIWinMgr:OpenMask("游戏重连中...")
    local LoginModule = require("Module.LoginModule").LoginModule
    function OldSeesionGetCallBack(jsondata) --使用缓存Seesion获取登陆信息回调
        if not jsondata then
            Debug.log("重连失败1")
            FightOfflineModule.FightOffLineNotice("重连失败")
            return
        end
        if 1 == jsondata.code then
            UIWinMgr:CloseMask()
            if nil ~= jsondata.target then
                LoginModule:Login(jsondata.uid,jsondata.target.sessionCode,jsondata.target.loginTime,jsondata.target.serverIp,jsondata.target.port)
            else
                
            end 
        else
            Debug.log("codexxxxxxx:"..jsondata.code)
            FightOfflineModule.FightOffLineNotice("重连失败")
        end
    end

    Debug.log("FightOffLine~~~~~~~~~~~~~~~~~~~~~")
    local lastSession = UnityEngine.PlayerPrefs.GetString("lastSession")
    LoginModule:GetLoginData(lastSession,OldSeesionGetCallBack,true)
end

function FightOfflineModule.OffLine()
    Debug.log("收到断线重连消息")
    function Reconnect()
        --重连 TODO  应该只要处理下登陆页面的遮罩即可
        UIWinMgr:CloseMask()
    end

    --VoiceTool:QuitRoom()

    local SceneMgr = require("Scene").SceneMgr
    local nowScene = SceneMgr.CurScene
    --游戏场景之外重连
    if nowScene ~= require("Scene.GameScene").GameScene then
        UIWinMgr:OpenNotice("您已经离线")
        Reconnect()
    else
        --TODO 断线重连提示置顶
        FightOfflineModule.FightOffLine()
    end
end

--str 提示信息
function FightOfflineModule.FightOffLineNotice(str)
    --游戏场景内重连失败
    function Reconnect()
        FightOfflineModule.OffLine()
    end
    CountDownMgr.Instance:CreateCountDown(2, 2, Reconnect)
end

function FightOfflineModule.ReconnectSm(reconnectSm)
    if reconnectSm.code ~= 1 then
        local RoomModule = require("Module.RoomModule").RoomModule
        RoomModule.BackToLobby()
        return
    end

    local Player = require("Player").Player
    local MyPlayer = require("MyPlayer").MyPlayer
    local MainUserData = require("DynamicData.MainUserData").MainUserData
    local GameHost = require("Module.GameModule.GameHost").GameHost
    --玩法数据
    GameHost.PlayType = reconnectSm.playType 
    --房间号
    GameHost.RoomId = reconnectSm.roomId
    GameHost.bankerIndex = reconnectSm.bankerIndex
    if reconnectSm.juelebuRoom ~= nil and reconnectSm.juelebuRoom.id ~= 0 then
        GameHost.clubName = reconnectSm.juelebuRoom.id
        GameHost.clubDeskNum = reconnectSm.juelebuRoom.num
        Debug.log("dddddddddddddddddOK"..GameHost.clubName)
    else
        Debug.log("dddddddddddddddddd空的？")
        GameHost.clubName = nil
        GameHost.clubDeskNum = nil
    end
    local isGameStart = reconnectSm.start

    local GameWin = UIWinMgr:GetWindow("GameWin") --在添加玩家之前初始化界面
    GameWin:ClearUIPlayers()
    GameWin:InitInfo()
    local users = reconnectSm.users
    
    --首先添加自己
    local myPlayer = nil
    local myUserRound = nil
    for i=1,#users do
        if tonumber(users[i].uid) == tonumber(MainUserData.uid) then
            myPlayer = GameHost:GetSelfPlayer()
            if myPlayer ~= nil then
                --重新初始化信息即可
                myPlayer:Init(users[i])
            else
                GameHost.myIndex = users[i].seetIndex
                myPlayer = MyPlayer:new()
                myPlayer:Init(users[i])
                GameHost:AddPlayer(myPlayer)
            end

            myUserRound = reconnectSm.usersRound[i]
            break
        end
        Debug.log("当前玩家UID:"..users[i].uid)
    end

    --再添加别人
    for i=1,#users do
        if tonumber(users[i].uid) ~= tonumber(MainUserData.uid) then
            Debug.log("i::::::::::::::::"..i)
            local ply = GameHost:GetPlayer(users[i].seetIndex)
            if ply ~= nil and tonumber(users[i].uid) ~= tonumber(ply.userInfo.uid) then
                GameHost:RemovePlayer(users[i].seetIndex)
                ply = nil
            end

            if ply ~= nil then
                --重新初始化信息即可
                ply:Init(users[i])
            else
                ply = Player:new()
                ply:Init(users[i])
                GameHost:AddPlayer(ply)
            end

            ply:OffLineInit(reconnectSm.usersRound[i])
        end
    end

    --最后初始化自己的断线信息
    myPlayer:OffLineInit(myUserRound, reconnectSm)
    if GameHost.bankerIndex > 0 then
        GameHost:ZhuangChg(GameHost.bankerIndex)
    end

    GameWin:Show()
    Event.Brocast(EventDefine.OnGameStart,isGameStart)
    Event.Brocast(EventDefine.OnGameRoundChange,reconnectSm.round)
end