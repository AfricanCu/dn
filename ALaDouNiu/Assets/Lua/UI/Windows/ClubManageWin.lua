module("UI.Windows.ClubManageWin", package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local MainUserData = require("DynamicData.MainUserData").MainUserData
ClubManageWin = UIWindow:new()

ClubManageWin.name = "ClubManageWin" --窗体名字
function ClubManageWin:Init()
    local t = self.Container.transform
    self.ClubClose = UIUtility.GetChildTransform("ClubClose", t, true)
    self.EreateClub = UIUtility.GetChildTransform("EreateClub", t, true)
    self.JoinClub = UIUtility.GetChildTransform("JoinClub", t, true)
    self.MyJoin = UIUtility.GetChildTransform("MyJoin", t, true)
    self.MyCreate = UIUtility.GetChildTransform("MyCreate", t, true)
    self.m_MyJoin = UIUtility.GetChildTransform("m_MyJoin", t, true)
    self.m_MyCreate = UIUtility.GetChildTransform("m_MyCreate", t, true)
    self.MyCreateClub = UIUtility.GetChildTransform("MyCreateClub", t, true)
    self.MyJoinClub = UIUtility.GetChildTransform("MyJoinClub", t, true)
    self.di = UIUtility.GetChildTransform("di", t, true)
    self.MyJoinClubPre = UIUtility.GetChildTransform("1", t, true)
    self.MyJoinClubGrid = UIUtility.GetChildTransform("Grid_1", t, true)
    self.MyCreateClubPre = UIUtility.GetChildTransform("2", t, true)
    self.MyCreateClubGrid = UIUtility.GetChildTransform("Grid_2", t, true)

    self.SoSou = UIUtility.GetChildTransform("SoSou", t, true)

    --所有的加入的俱乐部
    self.AllMyCreateCulb = MainUserData.MyClub
    --所有的创建的俱乐部
    self.AllClubNumber_Create = {}
    self.AllClubNumber_Join = {}
    self.change = 1
    self:BindEvents()
end
function ClubManageWin:OnShow()
    if self.change == 1 then
        self:ShowJoinClub()
    else
        self:ShowCreateClub()
    end
end

function ClubManageWin:BindEvents()
    function onClose()
        self:Close()
    end
    function CreateCulb()
        --创建俱乐部
        --self:Close()
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenWindow("CreateClubWin")
        --self:Close()
    end
    function MyJoinCulbBtn()
        self:ShowJoinClub()
    end
    function MyCreateCulbBtn()
        self:ShowCreateClub()
    end
    function JoiningCulb()
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenWindow("JoinClubWin")
    end
    UIHelper.BindUIEvnet("Click", onClose, self.ClubClose.gameObject)
    UIHelper.BindUIEvnet("Click", CreateCulb, self.EreateClub.gameObject)
    UIHelper.BindUIEvnet("Click", JoiningCulb, self.JoinClub.gameObject)
    UIHelper.BindUIEvnet("Click", MyJoinCulbBtn, self.MyJoin.gameObject)
    UIHelper.BindUIEvnet("Click", MyCreateCulbBtn, self.MyCreate.gameObject)
    --ClubManageWin:ShowJoinClub()
    self.MyJoinClubGrid.transform:GetComponent("UIGrid").enabled = true
end
function ClubManageWin:ShowJoinClub()
    --加入的俱乐部
    self.MyJoin.gameObject:SetActive(false)
    self.MyCreate.gameObject:SetActive(true)
    self.m_MyJoin.gameObject:SetActive(true)
    self.m_MyCreate.gameObject:SetActive(false)
    self.MyJoinClub.gameObject:SetActive(true)
    self.MyCreateClub.gameObject:SetActive(false)
    Debug.log(#MainUserData.JoinClub .. "------------")
    self.AllMyJoinCulb = MainUserData.JoinClub
    for i = #self.AllMyJoinCulb, 1, -1 do
        if self.AllMyJoinCulb[i].state == 1 then
            table.remove(self.AllMyJoinCulb, i)
        end
    end

    for i = #self.AllClubNumber_Join, 1, -1 do
        UnityEngine.GameObject.Destroy(self.AllClubNumber_Join[i])
    end
    if self.AllMyJoinCulb and #self.AllMyJoinCulb > 0 then
        local num = #self.AllMyJoinCulb
        if num > 10 then
            num = 10
        end
        for i = 1, num do
            self.AllClubNumber_Join[i] = UnityEngine.GameObject.Instantiate(self.MyJoinClubPre.gameObject)
            self.AllClubNumber_Join[i].gameObject:SetActive(true)
            self.AllClubNumber_Join[i].transform.parent = self.MyJoinClubGrid.transform
            self.AllClubNumber_Join[i].transform.localScale = Vector3.one
            self.AllClubNumber_Join[i].transform.localPosition = Vector3.zero
            self.AllClubNumber_Join[i].transform.localEulerAngles = Vector3(0, 0, 0)
            local n = self.AllClubNumber_Join[i].transform

            local ClubName = UIUtility.FindContorl("UILabel", "ClubName", n)
            local ClubHZName = UIUtility.FindContorl("UILabel", "name", n)
            --头像
            local ClubMessage = UIUtility.FindContorl("UILabel", "ClubMessage", n)
            local ClubHeadPortrait = UIUtility.FindContorl("UITexture", "HeadPortrait", n)
            local QuitlveClub = UIUtility.GetChildTransform("Quite", n, true)
            local JoinClub = UIUtility.GetChildTransform("Entrance", n, true)
            --local CreateCulb
            ClubName.text = MainUserData.JoinClub[i].name .. "(" .. MainUserData.JoinClub[i].id .. ")"
            ClubMessage.text = MainUserData.JoinClub[i].playType
            --头像
            function callBack(isOk, www)
                if isOk and ClubHeadPortrait ~= nil then
                    ClubHeadPortrait.mainTexture = www.texture
                end
            end
            require("NetWork.NetHttp").WWWTexture(MainUserData.JoinClub[i].headimg, callBack)
            --
            ClubHZName.text = MainUserData.JoinClub[i].masterName
            function QuitClubBtn()
                local ClubModule = require("Module.ClubModule").ClubModule

                Debug.log(MainUserData.JoinClub[i].id .. "oooooooooooooooooooo")
                ClubModule.QuitJulebuCm(MainUserData.JoinClub[i].id)
                --退出申请
                local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
                if MainUserData.JoinClub[i].id == ClubLoobyWin.id then
                    self:Close()
                end
            end
            function JoinClubBeforeBtn()
                local ClubModule = require("Module.ClubModule").ClubModule
                ClubModule.InClub(MainUserData.JoinClub[i].id)
                self:Close()
            end
            UIHelper.BindUIEvnet("Click", QuitClubBtn, QuitlveClub.gameObject)
            UIHelper.BindUIEvnet("Click", JoinClubBeforeBtn, JoinClub.gameObject)
        end
    else
        --没有加入俱乐部
        --Debug.log(#self.AllMyJoinCulb .. "无入驻俱乐部。。。")
    end

    self.MyJoinClubGrid.transform:GetComponent("UIGrid").enabled = true
end

function ClubManageWin:ShowCreateClub()
    --创建的俱乐部
    self.MyJoin.gameObject:SetActive(true)
    self.MyCreate.gameObject:SetActive(false)
    self.m_MyJoin.gameObject:SetActive(false)
    self.m_MyCreate.gameObject:SetActive(true)
    self.MyJoinClub.gameObject:SetActive(false)
    self.MyCreateClub.gameObject:SetActive(true)
    self.AllMyCreateCulb = MainUserData.MyClub
    for i = #self.AllClubNumber_Create, 1, -1 do
        UnityEngine.GameObject.Destroy(self.AllClubNumber_Create[i])
    end
    if self.AllMyCreateCulb and #self.AllMyCreateCulb > 0 then
        local num = #self.AllMyCreateCulb
        if num > 10 then
            num = 10
        end
        for i = 1, num do
            self.AllClubNumber_Create[i] = UnityEngine.GameObject.Instantiate(self.MyCreateClubPre.gameObject)
            self.AllClubNumber_Create[i].gameObject:SetActive(true)
            self.AllClubNumber_Create[i].transform.parent = self.MyCreateClubGrid.transform
            self.AllClubNumber_Create[i].transform.localScale = Vector3.one
            self.AllClubNumber_Create[i].transform.localPosition = Vector3.zero
            self.AllClubNumber_Create[i].transform.localEulerAngles = Vector3(0, 0, 0)
            local n = self.AllClubNumber_Create[i].transform
            local ClubName = UIUtility.FindContorl("UILabel", "ClubName", n, true)
            local ClubHZName = UIUtility.FindContorl("UILabel", "name", n, true)
            local ClubMessage = UIUtility.FindContorl("UILabel", "ClubMessage", n, true)

            local DissolveClub = UIUtility.GetChildTransform("Dissolve", n, true)
            local JoinClub = UIUtility.GetChildTransform("Entrance", n, true)
            --（头像）
            local ClubHeadPortrait = UIUtility.FindContorl("UITexture", "HeadPortrait", n)
            ClubName.text = MainUserData.MyClub[i].name .. "(" .. MainUserData.MyClub[i].id .. ")"
            ClubMessage.text = MainUserData.MyClub[i].playType
            ClubHZName.text = MainUserData.MyClub[i].masterName

            function callBack(isOk, www)
                if isOk and ClubHeadPortrait ~= nil then
                    ClubHeadPortrait.mainTexture = www.texture
                end
            end
            require("NetWork.NetHttp").WWWTexture(MainUserData.MyClub[i].headimg, callBack)

            function DissolveClubBtn()
                local ClubModule = require("Module.ClubModule").ClubModule
                ClubModule.DissolveClubCm(MainUserData.MyClub[i].id)
                local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
                if MainUserData.MyClub[i].id == ClubLoobyWin.id then
                    Debug.log("hahahhahahha")
                    self:Close()
                end
            end
            function JoinClubBeforeBtn()
                local ClubModule = require("Module.ClubModule").ClubModule
                ClubModule.InClub(MainUserData.MyClub[i].id)
                self:Close()
            end

            UIHelper.BindUIEvnet("Click", DissolveClubBtn, DissolveClub.gameObject)
            UIHelper.BindUIEvnet("Click", JoinClubBeforeBtn, JoinClub.gameObject)
        end
    else
        --没有创建俱乐部
    end

    self.MyCreateClubGrid.transform:GetComponent("UIGrid").enabled = true
end
