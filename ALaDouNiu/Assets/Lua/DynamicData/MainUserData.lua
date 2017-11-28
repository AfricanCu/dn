--主用户数据
module("DynamicData.MainUserData",package.seeall)
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local DynamicDataBase = require("DynamicData").DynamicDataBase
MainUserData = DynamicDataBase:new()

MainUserData.uid = 0
MainUserData.diamond = 0                --钻石
MainUserData.nickname = "unknow"        --昵称
MainUserData.headimg = nil
MainUserData.BattleRecord = {}          --战斗记录
MainUserData.BattleRecordDic = {}       --战斗记录查询字典，传入index的字符串，得到对应的GameRecord
MainUserData.ProxyRecord = {}           --战斗记录
MainUserData.ProxyRoom = {} 
MainUserData.JoinClub = {}   
MainUserData.MyClub = {}
MainUserData.ProxyRoomInfoArr = {}

function MainUserData:LoginUserData(loginSm)

    self.nickname = loginSm.nickname
    self.headimg = loginSm.headimg
    self.JoinClub = loginSm.join
    self.MyClub = loginSm.my

end


--更新代理
function MainUserData:ProxyCreateRoomData(ProxyCreateRoomSm)

    local RoomInfo = {}
    RoomInfo.roomId = ProxyCreateRoomSm.roomId
    RoomInfo.playType = ProxyCreateRoomSm.playType
    table.insert( self.ProxyRoomInfoArr,RoomInfo )
    local ProxyRoomWin = UIWinMgr:GetWindow("ProxyRoomWin")
    ProxyRoomWin:ViewDidLoad(ProxyCreateRoomSm)
    -- Event.Brocast(EventDefine.OnProxyRoomInfoChange,self.ProxyRoomInfoArr)

end



--更新用户数据
function MainUserData:UpdateUser(PlayerCast)
    self.diamond = PlayerCast.diamond
    -- self.diamond = 50
    Event.Brocast(EventDefine.OnMainUserDataChange,self)
    Debug.log("玩家数据更新")
end

--更新战斗记录数据
function MainUserData:UpdateBattleRecord(record)
    self.BattleRecord = record
    self.BattleRecordDic = {}
    for k,v in ipairs(record) do
        local index = tostring(v.index)
        self.BattleRecordDic[index] = v
        Debug.log("index:"..index)
    end
    Event.Brocast(EventDefine.OnBattleRecordChange,self.BattleRecord)
    Debug.log("战斗记录数据更新")
end

function MainUserData:UpdateProxyRecord(record)
    self.ProxyRecord = record
    Event.Brocast(EventDefine.OnProxyRecordChange,self.ProxyRecord)
    Debug.log("proxy战斗记录数据更新")
end

function MainUserData:UpdateProxyRoom(rooms)
    self.ProxyRoom = rooms
    Debug.log("77777777777777777777777777")
    Debug.log(stringify(self.ProxyRoom))
    Event.Brocast(EventDefine.OnProxyRoomChange,self.ProxyRoom)
    Debug.log("proxy房间数据更新")
end

function MainUserData:AddProxyRoom(rooms)

    if rooms ~= nil and table.getn(rooms) > 0 then
        for i=1, #rooms do 
            table.insert(self.ProxyRoom, rooms[i])
        end 
        Event.Brocast(EventDefine.OnProxyRoomChange,self.ProxyRoom)
        Debug.log("proxy增加房间数据更新")
    end
end

function MainUserData:DelProxyRoom(roomIds)
    if roomIds ~= nil and table.getn(roomIds) > 0 then
        for i=1, #roomIds do 
            for j=1, #self.ProxyRoom do 
                if roomIds[i] == self.ProxyRoom[j].roomId then
                    table.remove(self.ProxyRoom,j) 
                    Debug.log("remove:~~~~~~~~~~~~~~~~~~~~"..j)
                    break
                end
            end
        end 
        Event.Brocast(EventDefine.OnProxyRoomChange,self.ProxyRoom)
        Debug.log("proxy删除房间数据更新")
    end
end

function MainUserData:ChgProxyRoom(rooms)
    if rooms ~= nil and table.getn(rooms) > 0 then
        for i=1, #rooms do 
            for j=1, #self.ProxyRoom do 
                if rooms[i].roomId == self.ProxyRoom[j].roomId then
                    self.ProxyRoom[j] = rooms[i]
                    break
                end
            end
        end 
        Event.Brocast(EventDefine.OnProxyRoomChange,self.ProxyRoom)
        Debug.log("proxy更新房间数据更新")
    end
end

--加俱乐部广播
function MainUserData:AddClubCast(AddJulebuCast)
Debug.log("22222222222222222222222222333")
    if AddJulebuCast.type == 2 then
        table.insert(self.MyClub, AddJulebuCast.my)
        Debug.log("1111111111111111111111111")
    elseif AddJulebuCast.type == 1 then
        table.insert(self.JoinClub,AddJulebuCast.my)
        Debug.log("22222222222222222222222222")
        --Debug.log(#self.MyClub)
        --local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        --UIWinMgr:OpenWindow("MainWin")
    end
    Event.Brocast(EventDefine.OnAddClub)
end

function MainUserData:UpdateClubCast(UpdateJulebuCast)

    Debug.log(UpdateJulebuCast.my.playType.."sdada222222222222222222")
    if UpdateJulebuCast.type == 1 then
        --后台已经做了处理（状态从审批变为加入state=2）
        table.insert(self.JoinClub,UpdateJulebuCast.my)
    elseif UpdateJulebuCast.type == 2 then
        if UpdateJulebuCast.my.state == 3 then
            for i=1 ,#self.MyClub do
                if self.MyClub[i].id == UpdateJulebuCast.my.id then
                    self.MyClub[i] = UpdateJulebuCast.my
                    break
                end
            end
        elseif UpdateJulebuCast.my.state == 2 then
            for i=1,#self.JoinClub do
                if self.JoinClub[i].id == UpdateJulebuCast.my.id then
                    self.JoinClub[i] = UpdateJulebuCast.my
                    break
                end 
            end
        elseif UpdateJulebuCast.type == 3 then
            for i=1,#self.JoinClub do
                if self.JoinClub[i].id == UpdateJulebuCast.my.id then
                    self.JoinClub[i] = UpdateJulebuCast.my
                    break
                end 
            end
            for i=1 ,#self.MyClub do
                if self.MyClub[i].id == UpdateJulebuCast.my.id then
                    self.MyClub[i] = UpdateJulebuCast.my
                    break
                end
            end
        end
    end 
    Debug.log("更新俱乐部")
    Event.Brocast(EventDefine.OnUpClub)
end

--删除俱乐部数据
function MainUserData:DelMyClub(DelJulebuCast)
    if DelJulebuCast.type == 3 then
        local a = #MainUserData.MyClub
        local b = #MainUserData.JoinClub
        for i = a, 1, -1 do
            if MainUserData.MyClub[i].id == DelJulebuCast.id then
                table.remove(self.MyClub, i)
                break
            end
        end
        for i = b, 1, -1 do
            if MainUserData.JoinClub[i].id == DelJulebuCast.id then
                table.remove(self.JoinClub, i)
                break
            end
        end
    else
        local a = #MainUserData.JoinClub
        for i = a, 1, -1 do
            if MainUserData.JoinClub[i].id == DelJulebuCast.id then
                table.remove(self.JoinClub, i)
                break
            end
        end
    end
    Event.Brocast(EventDefine.OnDelClub)
end