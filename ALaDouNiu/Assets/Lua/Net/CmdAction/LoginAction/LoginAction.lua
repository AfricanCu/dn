--登陆消息处理
module("NetWork.Action.LoginAction",package.seeall)

NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

LoginAction = NetCmdActionBase:new()

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local RoomModule = require("Module.RoomModule").RoomModule

function LoginAction:RemoveJoinRoomListener()
    Event.RemoveListener(EventDefine.OnJoinRoom,self.OnJoinRoom)
end

function LoginAction.OnJoinRoom(rlt)
    Debug.log("OnJoinRoom:"..rlt)
    LoginAction:RemoveJoinRoomListener()

    if rlt == 1 then
        local SceneMgr = require("Scene").SceneMgr
        SceneMgr:LordScene(require("Scene.LobbyScene").LobbyScene)
    end
end

function LoginAction:Action(bytes)
    Debug.log("LoginAction~~~~~~~~~~~~~~~~~~~~~~~")
    PlatformTool.Instance:ReqProducts()
    local SceneMgr = require("Scene").SceneMgr
    local GameScene = require("Scene.GameScene").GameScene
    --游戏场景之外重连
    if not GameScene.resLoaded then   
        Debug.log("LoginAction1~~~~~~~~~~~~~~~~~~~~~~~")
        self:NormalLogin(bytes)
    else
        local GameHost = require("Module.GameModule.GameHost").GameHost
        local roomID = GameHost.RoomId
        if roomID ~= nil then
        --再游戏中
            --VoiceTool:Pause()
            self:FightLogin(bytes)
        else
        --再大厅中
            self:LobbyLogin(bytes)
        end
    end
end

function LoginAction:LobbyLogin(bytes)
    local LoginSm = require("Protol.loginMessage_pb").LoginSm()
    LoginSm:ParseFromString(bytes)
    Debug.log(stringify(LoginSm))

    local state = LoginSm.code --0失败1成功2用户不存在3用户已经在线4服务器爆满

    Debug.log("登陆状态返回1："..state)
    if 0 == state then
        local FightOfflineModule = require("Module.FightOfflineModule").FightOfflineModule
        FightOfflineModule.FightOffLineNotice("重连失败")
    elseif 1 == state then
        local MainUserData = require("DynamicData.MainUserData").MainUserData
        MainUserData:LoginUserData(LoginSm)
        UIWinMgr:CloseMask()
    else
        local FightOfflineModule = require("Module.FightOfflineModule").FightOfflineModule
        FightOfflineModule.FightOffLineNotice("重连失败")
    end
end


function LoginAction:FightLogin(bytes)
    local LoginSm = require("Protol.loginMessage_pb").LoginSm()
    LoginSm:ParseFromString(bytes)

    Debug.log(stringify(LoginSm))

    local state = LoginSm.code --0失败1成功2用户不存在3用户已经在线4服务器爆满

    Debug.log("登陆状态返回2："..state)
    if 0 == state then
        local FightOfflineModule = require("Module.FightOfflineModule").FightOfflineModule
        FightOfflineModule.FightOffLineNotice("重连失败")
    elseif 1 == state then
        local GameHost = require("Module.GameModule.GameHost").GameHost
        GameHost:ClearPai()
        local MainUserData = require("DynamicData.MainUserData").MainUserData
        MainUserData:LoginUserData(LoginSm)

        local offlineRoomID = LoginSm.roomId
        if offlineRoomID ~= nil and string.len(offlineRoomID) > 0 then
            local NetMgr = require("NetWork").NetMgr
            Debug.log("断线重连~~~~~~~~~~~~~~~~~~~~~~~~")
            local Msg = require("Protol.mjMessage_pb").ReconnectCm()
            NetMgr:SendMsg(517,Msg)
        else
            --local FightOfflineModule = require("Module.FightOfflineModule").FightOfflineModule
            --FightOfflineModule.FightOffLineNotice("您已经不在房间中", true)
            UIWinMgr:CloseWindow("GameWin")
            RoomModule.BackToLobby()
        end
    else
        local FightOfflineModule = require("Module.FightOfflineModule").FightOfflineModule
        FightOfflineModule.FightOffLineNotice("重连失败")
    end
end

function LoginAction:NormalLogin(bytes)
    local LoginSm = require("Protol.loginMessage_pb").LoginSm()
    LoginSm:ParseFromString(bytes)
    Debug.log(stringify(LoginSm))

    local state = LoginSm.code --0失败1成功2用户不存在3用户已经在线4服务器爆满

    Debug.log("登陆状态返回3："..state)
    if 0 == state then
        Debug.log("登陆大厅失败")
    elseif 1 == state then
        Debug.log("登陆大厅成功")
        local IosPendingTool = require("IosPendingTool").IosPendingTool
        local isIosPending = IosPendingTool.IsPending()
        local loginWin = nil
        local loginWinName = ""
        if isIosPending then
            loginWinName = "IosLoginWin"
            loginWin = UIWinMgr:GetWindow(loginWinName)
        else
            loginWinName = "LoginWin"
            loginWin = UIWinMgr:GetWindow(loginWinName)
        end
        
        local MainUserData = require("DynamicData.MainUserData").MainUserData
        MainUserData:LoginUserData(LoginSm)

        local GameScene = require("Scene.GameScene").GameScene

        function GetMWRoomCallBack(roomID)
            --[[if roomID ~= nil and string.len(roomID) > 0 then
                Event.AddListener(EventDefine.OnJoinRoom,self.OnJoinRoom)
                local RoomModule = require("Module.RoomModule").RoomModule
                RoomModule.JoinRoom(roomID)
                PlatformTool.Instance:ClearMWRoom()
            else
                local SceneMgr = require("Scene").SceneMgr
                SceneMgr:LordScene(require("Scene.GameScene").GameScene)
            end]]--
            GameScene.mwRoomID = roomID
            Debug.log("GameScene.mwRoomID:"..GameScene.mwRoomID)
            --[[local SceneMgr = require("Scene").SceneMgr
            SceneMgr:LordScene(GameScene)]]--
            if not GameScene.resLoaded then
                GameScene.resLoaded = true
            end
            
            local lastClubID = nil
            if UnityEngine.PlayerPrefs.HasKey("lastClubID") then
                lastClubID = UnityEngine.PlayerPrefs.GetInt("lastClubID")
            end

            if lastClubID == nil then
                UIWinMgr:GetWindow("MainWin")
            else
                UIWinMgr:GetWindow("ClubLoobyWin")
            end
            -- loginWin.LoadingMask.gameObject:SetActive(false)
            UIWinMgr:CloseWindow(loginWinName)
            GameScene.isNeedNormalLogin = false

            if lastClubID == nil then
                UIWinMgr:OpenWindow("MainWin")
            else
                --UIWinMgr:OpenWindow("ClubLoobyWin")
                local ClubModule = require("Module.ClubModule").ClubModule
                ClubModule.InClub(lastClubID)
            end
        end

        local offlineRoomID = LoginSm.roomId
        if offlineRoomID ~= nil and string.len(offlineRoomID) > 0 then
            --[[local SceneMgr = require("Scene").SceneMgr
            SceneMgr:LordScene(GameScene)]]--
            if not GameScene.resLoaded then
                GameScene.resLoaded = true
            end
            loginWin.LoadingMask.gameObject:SetActive(false)
            UIWinMgr:CloseWindow(loginWinName)
            --UIWinMgr:CloseWindow("LoginDoorWin")

            local NetMgr = require("NetWork").NetMgr
            Debug.log("断线重连~~~~~~~~~~~~~~~~~~~~~~~~")
            local Msg = require("Protol.mjMessage_pb").ReconnectCm()
            NetMgr:SendMsg(517,Msg)

            return
        end

        --是否是魔窗进房间
        --PlatformTool.Instance:GetMWRoom("GetRoomNumber", GetMWRoomCallBack)
        GetMWRoomCallBack("")
    end

    local LoginModule = require("Module.LoginModule").LoginModule
    LoginModule.Logining = false
end

require("NetWork.NetCmdSet").CmdSet[2] = LoginAction --注册处理对象