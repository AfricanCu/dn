module("MWTool", package.seeall)

MWTool = {}

function MWTool.RemoveJoinRoomListener()
    Event.RemoveListener(EventDefine.OnJoinRoom,MWTool.OnJoinRoom)
end

function MWTool.OnJoinRoom(rlt)
    Debug.log("OnJoinRoom:"..rlt)
    MWTool.RemoveJoinRoomListener()

    if rlt == 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenNotice("加入房间失败")
    end
end

function MWTool.AppResume()
    --[[local FightRoomData = require("DynamicData.FightRoomData").FightRoomData
    local room = FightRoomData.GetRoomID()
    if room == nil or string.len(room) <= 0 then
        function GetMWRoomCallBack(roomID)
            if roomID ~= nil and string.len(roomID) > 0 then
                Event.AddListener(EventDefine.OnJoinRoom,MWTool.OnJoinRoom)
                local RoomModule = require("Module.RoomModule").RoomModule
                RoomModule.JoinRoom(roomID)
                PlatformTool.Instance:ClearMWRoom()
            end
        end

        PlatformTool.Instance:GetMWRoom("GetRoomNumber", GetMWRoomCallBack)
    else
        --如果已经在游戏中，切出去读取到了另外的房间也要先清除，防止其缓存，影响下一次操作
        PlatformTool.Instance:ClearMWRoom()
    end]]--

    local SceneMgr = require("Scene").SceneMgr
    local nowScene = SceneMgr.CurScene
    --游戏场景之外重连
    if nowScene ~= require("Scene.GameScene").GameScene then
        --好像不需要处理
    else
        local GameHost = require("Module.GameModule.GameHost").GameHost
        local nowRoomID = GameHost.RoomId
        if nowRoomID ~= nil then
        --再游戏中
            PlatformTool.Instance:ClearMWRoom()
        else
        --再大厅中
            function GetMWRoomCallBack(roomID)
                if roomID ~= nil and string.len(roomID) > 0 then
                    local RoomModule = require("Module.RoomModule").RoomModule
                    RoomModule.JoinRoom(roomID)
                    PlatformTool.Instance:ClearMWRoom()
                end
            end

            PlatformTool.Instance:GetMWRoom("GetRoomNumber", GetMWRoomCallBack)
        end
        
    end
end