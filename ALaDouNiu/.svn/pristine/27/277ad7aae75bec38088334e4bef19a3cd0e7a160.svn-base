module("Module.ClubModule", package.seeall)

local NetMgr = require("NetWork").NetMgr
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local VoiceTool = require("VoiceTool").VoiceTool

ClubModule = {}
ClubModule.isInClubLobby = false

--创建俱乐部请求
function ClubModule.CreateClub(type, playTypeCreater)
    local Msg = require("Protol.guildMessage_pb").CreateJulebuCm()
    ClubModule.pTCreater = playTypeCreater
    playTypeCreater(Msg.playType, type)
    NetMgr:SendMsg(1506, Msg)
    --UIWinMgr:OpenMask("创建俱乐部中...")
end

--创建俱乐部返回
function ClubModule.CreateClubSm(CreateJulebuSm)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    --UIWinMgr:CloseMask()
    ClubModule.isFirst = true
    ClubModule.InClub(CreateJulebuSm.id)
end

--预进入俱乐部大厅请求
function ClubModule.JoinClubBeforeCm(ClubId)
    local Msg = require("Protol.guildMessage_pb").InJulebuBeforeCm()
    Msg.id = ClubId
    NetMgr:SendMsg(1516, Msg)
    --UIWinMgr:OpenMask("进入俱乐部中...")
end

--预进入俱乐部大厅预处理返回
function ClubModule.InClubBeforeSm(InJulebuBeforeSm)
    --UIWinMgr:CloseMask()
    if 1 == InJulebuBeforeSm.code then
        --不需要切服，直接进入房间
        Debug.log("直接加入房间")
        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.InClub(InJulebuBeforeSm.id)
    elseif 2 == InJulebuBeforeSm.code then
        --走切服流程
        Debug.log("开始切换服务器")
        local MainUserData = require("DynamicData.MainUserData").MainUserData
        agrs = {
            MainUserData.name,
            InJulebuBeforeSm.sw.myCode,
            InJulebuBeforeSm.sw.sId,
            InJulebuBeforeSm.sw.type,
            InJulebuBeforeSm.sw.host,
            InJulebuBeforeSm.sw.port,
            InJulebuBeforeSm.id
        }
        local LoginModule = require("Module.LoginModule").LoginModule
        LoginModule:SwLogin(unpack(agrs))
    else
        --local JoinRoomWin = require("UI.Windows.JoinRoomWin").JoinRoomWin
        --JoinRoomWin:OnShow()
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(InJulebuBeforeSm.code)
    end
end

--进入俱乐部大厅请求
function ClubModule.InClub(ClubId)
    Debug.log("InClub")
    local Msg = require("Protol.guildMessage_pb").InJulebuCm()
    Msg.id = ClubId
    Debug.log("ClubId:" .. Msg.id)
    NetMgr:SendMsg(1518, Msg)
    --UIWinMgr:OpenMask("进入俱乐部中...")
end

--进入俱乐部大厅返回
function ClubModule.InClubSm(InJulebuSm)
    --UIWinMgr:CloseMask()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    if 1 ~= InJulebuSm.code then
        if UnityEngine.PlayerPrefs.HasKey("lastClubID") then
            UnityEngine.PlayerPrefs.DeleteKey("lastClubID")
        end
        UIWinMgr:CloseWindow("ClubLoobyWin")
        UIWinMgr:OpenWindow("MainWin")
        UIWinMgr:OpenErrorNotice(InJulebuSm.code)
        return
    end
    local ClubLoobyWin = UIWinMgr:GetWindow("ClubLoobyWin")
    ClubLoobyWin.ClubName = InJulebuSm.name
    ClubLoobyWin.job = InJulebuSm.job
    ClubLoobyWin.id = InJulebuSm.id
    ClubLoobyWin.Notice = InJulebuSm.notice
    ClubLoobyWin.playTypeDesc = InJulebuSm.playTypeDesc
    ClubLoobyWin.playType = InJulebuSm.playType
    ClubLoobyWin.prohibitIp = InJulebuSm.prohibitIp
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.TableInfoCm(InJulebuSm.id)
    ClubLoobyWin.UpTime = 0
    ClubLoobyWin:FuncCountDown(99999999, InJulebuSm.id)

    local PlayerPrefs = UnityEngine.PlayerPrefs
    PlayerPrefs.SetInt("lastClubID", InJulebuSm.id)
    ClubLoobyWin:Show()
    UIWinMgr:CloseWindow("MainWin")
    if ClubModule.isFirst == true then
        UIWinMgr:OpenWindow("ClubSettingWin")
        ClubModule.isFirst = false
    end
    Debug.log("ssssssssssssssxxxxx")
end

--申请加入俱乐部请求
function ClubModule.ApplyClubCm(ClubId)
    local Msg = require("Protol.guildMessage_pb").ApplyJulebuCm()
    Msg.id = tonumber(ClubId)
    NetMgr:SendMsg(1524, Msg)
end

--解散俱乐部请求
function ClubModule.DissolveClubCm(ClubId)
    Debug.log("DissolveClub" .. "---------------------------")
    local Msg = require("Protol.guildMessage_pb").DissolveJulebuCm()
    Msg.id = ClubId
    Debug.log("ClubId:" .. Msg.id)
    NetMgr:SendMsg(1514, Msg)
    --UIWinMgr:OpenMask("解散俱乐部俱乐部中...")
end
--解散俱乐部返回
function ClubModule.DissolveClubSm(DissolveClubSm)
    --UIWinMgr:CloseMask()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    if 1 ~= DissolveClubSm.code then
        UIWinMgr:OpenErrorNotice(DissolveClubSm.code)
        return
    end
    local ClubManageWin = UIWinMgr:GetWindow("ClubManageWin")
    local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
    if DissolveClubSm.id == ClubLoobyWin.id then
        UIWinMgr:CloseWindow("ClubLoobyWin")
        UIWinMgr:OpenWindow("MainWin")
        return
    end
    ClubManageWin.change = 2
    ClubManageWin:Show()
end

--退出俱乐部请求
function ClubModule.QuitJulebuCm(ClubId)
    local Msg = require("Protol.guildMessage_pb").QuitJulebuCm()
    Msg.id = ClubId
    Debug.log("ClubId:" .. Msg.id)
    NetMgr:SendMsg(1520, Msg)
    --UIWinMgr:OpenMask("退出俱乐部中...")
end

--退出俱乐部返回
function ClubModule.QuitJulebuSm(QuitJulebuSm)
    --UIWinMgr:CloseMask()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    Debug.log(QuitJulebuSm.code)
    if 1 ~= QuitJulebuSm.code then
        UIWinMgr:OpenErrorNotice(QuitJulebuSm.code)
        return
    end
    local ClubManageWin = UIWinMgr:GetWindow("ClubManageWin")
    local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
    if QuitJulebuSm.id == ClubLoobyWin.id then
        UIWinMgr:CloseWindow("ClubLoobyWin")
        UIWinMgr:OpenWindow("MainWin")
        return
    end
    ClubManageWin.change = 1
    ClubManageWin:Show()
end

--俱乐部成员列表请求
function ClubModule.JulebuMemberListCm(ID, num)
    local Msg = require("Protol.guildMessage_pb").JulebuMemberListCm()
    Msg.id = ID
    Msg.index = num
    NetMgr:SendMsg(1532, Msg)
    --UIWinMgr:OpenMask("成员列表请求...")
end

--俱乐部成员列表返回
function ClubModule.JulebuMemberListSm(JulebuMemberListSm)
    --UIWinMgr:CloseMask()
    if 1 ~= JulebuMemberListSm.code then
        UIWinMgr:OpenErrorNotice(JulebuMemberListSm.code)
        return
    end
    local MemberListWin = UIWinMgr:GetWindow("MemberListWin")
    local num, mod = math.modf(JulebuMemberListSm.totalNum / 20)
    if mod ~= 0 then
        num = num + 1
    end
    MemberListWin.YeShu = num
    MemberListWin:OnShow()
    MemberListWin:Show()
    MemberListWin:ShowClubList(JulebuMemberListSm.member)
end

--俱乐部申请列表请求
function ClubModule.ApplyMemberListCm(ID)
    local Msg = require("Protol.guildMessage_pb").ApplyMemberListCm()
    Msg.id = ID
    NetMgr:SendMsg(1526, Msg)
    --UIWinMgr:OpenMask("申请成员列表请求...")
end
--俱乐部申请成列表返回
function ClubModule.ApplyMemberListSm(ApplyMemberListSm)
    --UIWinMgr:CloseMask()
    if 1 ~= ApplyMemberListSm.code then
        UIWinMgr:OpenErrorNotice(ApplyMemberListSm.code)
        return
    end
    local MemberListWin = UIWinMgr:GetWindow("ClubLoobyWin")
    local MemberListWin = UIWinMgr:GetWindow("MemberListWin")
    Debug.log(#ApplyMemberListSm.apply .. "-------------------------------------------")
    MemberListWin.ClubMemberApplyList = ApplyMemberListSm.apply
    if MemberListWin.DianNum == 2 then
        MemberListWin:ShowClubApplyList()
    end
end

--拒绝申请请求
function ClubModule.DisagreeApplyCm(C_Id, U_Id)
    local Msg = require("Protol.guildMessage_pb").DisagreeApplyCm()
    Msg.id = C_Id
    Msg.uid = U_Id
    NetMgr:SendMsg(1530, Msg)
    --UIWinMgr:OpenMask("拒绝申请请求...")
end

--拒绝申请返回
function ClubModule.DisagreeApplySm(DisagreeApplySm)
    --UIWinMgr:CloseMask()
    if 1 ~= DisagreeApplySm.code then
        UIWinMgr:OpenErrorNotice(DisagreeApplySm.code)
        return
    end
    Debug.log("----------拒绝")
    local MemberListWin = UIWinMgr:GetWindow("MemberListWin")

    MemberListWin:RemoveClubApplyMember(DisagreeApplySm.uid)
    MemberListWin:ShowClubApplyList()
end

--同意申请请求
function ClubModule.AgreeApplyCm(C_Id, U_Id)
    local Msg = require("Protol.guildMessage_pb").AgreeApplyCm()
    Msg.id = C_Id
    Msg.uid = U_Id
    NetMgr:SendMsg(1528, Msg)
    --UIWinMgr:OpenMask("同意申请请求...")
end

--同意申请返回
function ClubModule.AgreeApplySm(AgreeApplySm)
    --UIWinMgr:CloseMask()
    if 1 ~= AgreeApplySm.code then
        UIWinMgr:OpenErrorNotice(AgreeApplySm.code)
        return
    end
    Debug.log("----------同意")
    local MemberListWin = UIWinMgr:GetWindow("MemberListWin")

    MemberListWin:RemoveClubApplyMember(AgreeApplySm.uid)
    MemberListWin:ShowClubApplyList()
end

--踢出俱乐部成员请求
function ClubModule.KickJulebuMemberCm(C_Id, U_Id, page)
    local Msg = require("Protol.guildMessage_pb").KickJulebuMemberCm()
    Msg.id = C_Id
    Msg.uid = U_Id
    Msg.index = page
    NetMgr:SendMsg(1534, Msg)
    --UIWinMgr:OpenMask("踢出申请中...")
end

--踢出俱乐部成员返回
function ClubModule.KickJulebuMemberSm(KickJulebuMemberSm)
    --UIWinMgr:CloseMask()
    if 1 ~= KickJulebuMemberSm.code then
        UIWinMgr:OpenErrorNotice(KickJulebuMemberSm.code)
        return
    end

    local MemberListWin = UIWinMgr:GetWindow("MemberListWin")
    local num, mod = math.modf(KickJulebuMemberSm.totalNum / 20)
    if mod ~= 0 then
        num = num + 1
    end
    MemberListWin.YeShu = num
    MemberListWin.page = KickJulebuMemberSm.index
    MemberListWin:Show()
    MemberListWin:ShowClubList(KickJulebuMemberSm.member)
end

--俱乐部游戏记录请求
function ClubModule.ClubRecordCm(ClubId)
    local Msg = require("Protol.guildMessage_pb").JulebuRecordCm()
    Msg.id = ClubId
    NetMgr:SendMsg(1539, Msg)
end

--俱乐部游戏记录返回
function ClubModule.ClubRecordSm(JulebuRecordSm)
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local ClubRecordWin = UIWinMgr:GetWindow("ClubRecordWin")
    local MainUserData = require("DynamicData.MainUserData").MainUserData
    ClubRecordWin.clubRecord = JulebuRecordSm.record
    ClubRecordWin.Record = {}
    ClubRecordWin.index = 1
    local a = #ClubRecordWin.clubRecord
    for i = a, 1, -1 do
        for j = 1, #ClubRecordWin.clubRecord[i].nickname do
            if ClubRecordWin.clubRecord[i].nickname[j] == MainUserData.nickname then
                table.insert(ClubRecordWin.Record, ClubRecordWin.clubRecord[i])
                break
            end
        end
    end
    ClubRecordWin:Show()
end

--提升权限请求
function ClubModule.LevelupMemberCm(C_Id, U_Id, Job)
    local Msg = require("Protol.guildMessage_pb").LevelupMemberCm()
    Msg.id = C_Id
    Msg.uid = U_Id
    Msg.job = Job
    NetMgr:SendMsg(1536, Msg)
    --UIWinMgr:OpenMask("升职...")
end

--提升权限请求返回
function ClubModule.LevelupMemberSm(LevelupMemberSm)
    --UIWinMgr:CloseMask()
    if 1 ~= LevelupMemberSm.code then
        UIWinMgr:OpenErrorNotice(LevelupMemberSm.code)
        return
    end
    Debug.log("成功------------------------------------------------")
end

--大赢家请求
function ClubModule.WinnerCm(j_id, index)
    local Msg = require("Protol.guildMessage_pb").WinnerCm()
    Msg.id = j_id
    Msg.index = index
    NetMgr:SendMsg(1541, Msg)
    --UIWinMgr:OpenMask("大赢家...")
end
function ClubModule.WinnerSm(WinnerSm)
    --UIWinMgr:CloseMask()
    if 1 ~= WinnerSm.code then
        UIWinMgr:OpenErrorNotice(WinnerSm.code)
        return
    end
    local BigWinnerWin = UIWinMgr:GetWindow("BigWinnerWin")
    local num, mod = math.modf(WinnerSm.totalNum / WinnerSm.pageNum)
    --[[if WinnerSm.pageNum ~= 0 then 
        local num, mod = math.modf(WinnerSm.totalNum / WinnerSm.pageNum)
    else
        num = 0
        mod = 0
    end
    if mod ~= 0 then
        num = num + 1
    end]]
    BigWinnerWin.YeShu = num
    BigWinnerWin.AllBigWinnerNum = WinnerSm.winner
    BigWinnerWin:Show()
    BigWinnerWin:BigWinShow(WinnerSm.winner)
end

--搜索请求
function ClubModule.SearchMemberCm(j_id, U_Name)
    local Msg = require("Protol.guildMessage_pb").SearchMemberCm()
    Msg.id = j_id
    Msg.nickname = U_Name
    NetMgr:SendMsg(1545, Msg)
    --UIWinMgr:OpenMask("搜索成员...")
end
function ClubModule.SearchMemberSm(SearchMemberSm)
    --UIWinMgr:CloseMask()
    if 1 ~= SearchMemberSm.code then
        UIWinMgr:OpenErrorNotice(SearchMemberSm.code)
        return
    end
    local MemberListWin = UIWinMgr:GetWindow("MemberListWin")
    MemberListWin:Show()
    MemberListWin:ShowClubList(SearchMemberSm.member)
    Debug.log(#SearchMemberSm.member)
end

--清除请求
function ClubModule.ClearWinnerCm(C_Id, t_info, page)
    local Msg = require("Protol.guildMessage_pb").ClearWinnerCm()
    Debug.log(C_Id .. "///" .. t_info .. "//" .. page)
    Msg.id = C_Id
    Msg.info = t_info
    Msg.index = page
    NetMgr:SendMsg(1543, Msg)
    --UIWinMgr:OpenMask("清除请求...")
end

function ClubModule.ClearWinnerSm(ClearWinnerSm)
    --UIWinMgr:CloseMask()
    if 1 ~= ClearWinnerSm.code then
        UIWinMgr:OpenErrorNotice(ClearWinnerSm.code)
        return
    end
    local BigWinnerWin = UIWinMgr:GetWindow("BigWinnerWin")
    local num, mod = math.modf(ClearWinnerSm.totalNum / ClearWinnerSm.pageNum)
    if mod ~= 0 then
        num = num + 1
    end
    BigWinnerWin.YeShu = num
    BigWinnerWin.AllBigWinnerNum = ClearWinnerSm.winner
    BigWinnerWin:Show()
    BigWinnerWin:BigWinShow(ClearWinnerSm.winner)
end

--俱乐部玩法设置请求
function ClubModule.ClubPlaySetCm(clubId, type, playTypeCreater)
    local Msg = require("Protol.guildMessage_pb").PlaySetCm()
    Msg.id = clubId
    playTypeCreater(Msg.playType, type)
    NetMgr:SendMsg(1510, Msg)
end

--俱乐部玩法设置返回
function ClubModule.ClubPlaySetSm(PlaySetSm)
    local ClubLoobyWin = UIWinMgr:GetWindow("ClubLoobyWin")
    ClubLoobyWin.playType = PlaySetSm.playType
    ClubModule.InClub(PlaySetSm.id)
end

--俱乐部其他设置请求
function ClubModule.ClubOtherSetCm(clubId, isIp)
    local Msg = require("Protol.guildMessage_pb").OtherSetCm()
    Msg.id = clubId
    Msg.prohibitIp = isIp
    NetMgr:SendMsg(1512, Msg)
end

--俱乐部其他设置返回
function ClubModule.ClubOtherSetSm(OtherSetSm)
    local ClubLoobyWin = UIWinMgr:GetWindow("ClubLoobyWin")
    ClubLoobyWin.prohibitIp = OtherSetSm.prohibitIp
    ClubLoobyWin:Show()
end

--俱乐部信息设置请求
function ClubModule.ClubInfoSetCm(clubId, clubName, clubGongGao)
    local Msg = require("Protol.guildMessage_pb").InfoSetCm()
    Msg.id = clubId
    Msg.name = clubName
    Msg.notice = clubGongGao
    NetMgr:SendMsg(1508, Msg)
end

--俱乐部信息返回
function ClubModule.ClubInfoSetSm(InfoSetSm)
    local ClubLoobyWin = UIWinMgr:GetWindow("ClubLoobyWin")
    ClubLoobyWin.ClubName = InfoSetSm.name
    ClubLoobyWin.Notice = InfoSetSm.notice
    ClubLoobyWin:Show()
end

--桌子信息请求
function ClubModule.TableInfoCm(ClubId)
    local Msg = require("Protol.guildMessage_pb").TableInfoCm()
    Msg.id = ClubId
    NetMgr:SendMsg(1522, Msg)
end

--桌子信息返回
function ClubModule.TableInfoSm(TableInfoSm)
    local ClubLoobyWin = UIWinMgr:GetWindow("ClubLoobyWin")
    ClubLoobyWin.Clubtabels = TableInfoSm.table
    --Debug.log("poooooopopoppoopop")
    ClubLoobyWin:FreshInfo()
end

--桌子详情请求
function ClubModule.TableDetailCm(ClubId, Index)
    local Msg = require("Protol.guildMessage_pb").TableDetailCm()
    Msg.id = ClubId
    Msg.index = Index
    NetMgr:SendMsg(1547, Msg)
    UIWinMgr:OpenWindow("TableInfoWin")
end

--桌子信息返回
function ClubModule.TableDetailSm(TableDetailSm)
    local TableInfoWin = UIWinMgr:GetWindow("TableInfoWin")
    TableInfoWin.Players = TableDetailSm.detail
    TableInfoWin:Show()
end
