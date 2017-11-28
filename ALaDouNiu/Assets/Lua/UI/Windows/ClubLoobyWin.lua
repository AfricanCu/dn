--设置窗口
module("UI.Windows.ClubLoobyWin", package.seeall)

local NetMgr = require("NetWork").NetMgr
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
ClubLoobyWin = UIWindow:new()
ClubLoobyWin.name = "ClubLoobyWin"
ClubLoobyWin.job = 3
ClubLoobyWin.ClubName = nil
ClubLoobyWin.id = nil
ClubLoobyWin.Notice = nil
ClubLoobyWin.playType = nil
ClubLoobyWin.prohibitIp = nil
ClubLoobyWin.ClubMemberList = nil
ClubLoobyWin.Clubtabels = nil
ClubLoobyWin.playTypeDesc = nil
function ClubLoobyWin:Init()
    local t = self.Container.transform
    self.UpTime = 0
    self.cManageBtn = UIUtility.GetChildTransform("cManageBtn", t, true)
    self.cGroupBtn = UIUtility.GetChildTransform("cGroupBtn", t, true)
    self.cWinnerBtn = UIUtility.GetChildTransform("cWinnerBtn", t, true)
    self.cRecordBtn = UIUtility.GetChildTransform("cRecordBtn", t, true)
    self.CloseBtn = UIUtility.GetChildTransform("CloseBtn", t, true)
    self.SettingBtn = UIUtility.GetChildTransform("SettingBtn", t, true)
    self.Tabels = UIUtility.GetChildTransform("Tabels", t, true)
    self.cNameLabel = UIUtility.FindContorl("UILabel", "cNameLabel", t)
    self.cIDLabel = UIUtility.FindContorl("UILabel", "cIDLabel", t)
    self.NoticeLabel = UIUtility.FindContorl("UILabel", "NoticeLabel", t)
    self.TiShiDian = UIUtility.GetChildTransform("TiShiDian", t, true)

    self:BindEvents()
end

function ClubLoobyWin:OnShow()
    self:FreshInfo()
end

function ClubLoobyWin:FreshInfo()
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.isInClubLobby = true
    self:ShowTable()
    self.cNameLabel.text = self.ClubName
    if self.Notice then
        self.NoticeLabel.text = self.playTypeDesc .. "  " .. self.Notice
    else
        self.NoticeLabel.text = self.playTypeDesc
    end
    if self.id ~= nil then
        self.cIDLabel.text = "ID:" .. self.id
        if ClubLoobyWin.job ~= 1 then
            -- body
            local MemberListWin = UIWinMgr:GetWindow("MemberListWin")
            MemberListWin.DianNum = 1
            self.MyDian = UnityEngine.PlayerPrefs.GetInt("MyDian", 0)
            local ClubModule = require("Module.ClubModule").ClubModule
            ClubModule.ApplyMemberListCm(self.id)
            local MemberListWin = UIWinMgr:GetWindow("MemberListWin")
            if self.MyDian ~= #MemberListWin.ClubMemberApplyList and #MemberListWin.ClubMemberApplyList ~= 0 then
                MemberListWin.TiShi.gameObject:SetActive(true)
                self.TiShiDian.gameObject:SetActive(true)
            end
            if self.MyDian == #MemberListWin.ClubMemberApplyList then
                self.TiShiDian.gameObject:SetActive(false)
            end
        end
    end
end

function ClubLoobyWin:ShowTable()
    for i = 1, self.Tabels.childCount do
        local InfoBtn1 = UIUtility.GetChildTransform("InfoBtn1", self.Tabels:GetChild(i - 1), true)
        local man1Sp = UIUtility.GetChildTransform("man1", self.Tabels:GetChild(i - 1), true)
        local man2Sp = UIUtility.GetChildTransform("man2", self.Tabels:GetChild(i - 1), true)
        local man3Sp = UIUtility.GetChildTransform("man3", self.Tabels:GetChild(i - 1), true)
        local man4Sp = UIUtility.GetChildTransform("man4", self.Tabels:GetChild(i - 1), true)
        local man5Sp = UIUtility.GetChildTransform("man5", self.Tabels:GetChild(i - 1), true)
        local seat3 = UIUtility.GetChildTransform("t3", self.Tabels:GetChild(i - 1), true)
        local seat4 = UIUtility.GetChildTransform("t4", self.Tabels:GetChild(i - 1), true)
        local seat5 = UIUtility.GetChildTransform("t5", self.Tabels:GetChild(i - 1), true)
        local isBegin = UIUtility.GetChildTransform("isBegin", self.Tabels:GetChild(i - 1), true)
        InfoBtn1.gameObject:SetActive(false)
        man1Sp.gameObject:SetActive(false)
        man2Sp.gameObject:SetActive(false)
        man3Sp.gameObject:SetActive(false)
        man4Sp.gameObject:SetActive(false)
        man5Sp.gameObject:SetActive(false)
        seat3.gameObject:SetActive(true)
        seat4.gameObject:SetActive(true)
        seat5.gameObject:SetActive(true)
        isBegin.gameObject:SetActive(false)
    end

    if self.Clubtabels and #self.Clubtabels > 0 then
        Debug.log(#self.Clubtabels .. ">>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        for i = 1, #self.Clubtabels do
            Debug.log(self.Clubtabels[i].num .. "+++jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj")
            if self.Clubtabels[i].num > 0 then
                local InfoBtn1 =
                    UIUtility.GetChildTransform("InfoBtn1", self.Tabels:GetChild((self.Clubtabels[i].index) - 1), true)
                local man1Sp =
                    UIUtility.GetChildTransform("man1", self.Tabels:GetChild((self.Clubtabels[i].index) - 1), true)
                local man2Sp =
                    UIUtility.GetChildTransform("man2", self.Tabels:GetChild((self.Clubtabels[i].index) - 1), true)
                local man3Sp =
                    UIUtility.GetChildTransform("man3", self.Tabels:GetChild((self.Clubtabels[i].index) - 1), true)
                local man4Sp =
                    UIUtility.GetChildTransform("man4", self.Tabels:GetChild((self.Clubtabels[i].index) - 1), true)
                local man5Sp =
                    UIUtility.GetChildTransform("man5", self.Tabels:GetChild((self.Clubtabels[i].index) - 1), true)
                local seat3 =
                    UIUtility.GetChildTransform("t3", self.Tabels:GetChild((self.Clubtabels[i].index) - 1), true)
                local seat4 =
                    UIUtility.GetChildTransform("t4", self.Tabels:GetChild((self.Clubtabels[i].index) - 1), true)
                local seat5 =
                    UIUtility.GetChildTransform("t5", self.Tabels:GetChild((self.Clubtabels[i].index) - 1), true)
                local isBegin =
                    UIUtility.GetChildTransform("isBegin", self.Tabels:GetChild((self.Clubtabels[i].index) - 1), true)
                if self.Clubtabels[i].num == 1 then
                    InfoBtn1.gameObject:SetActive(true)
                    man1Sp.gameObject:SetActive(true)
                    man2Sp.gameObject:SetActive(false)
                    man3Sp.gameObject:SetActive(false)
                    man4Sp.gameObject:SetActive(false)
                    man5Sp.gameObject:SetActive(false)
                elseif self.Clubtabels[i].num == 2 then
                    InfoBtn1.gameObject:SetActive(true)
                    man1Sp.gameObject:SetActive(true)
                    man2Sp.gameObject:SetActive(true)
                    man3Sp.gameObject:SetActive(false)
                    man4Sp.gameObject:SetActive(false)
                    man5Sp.gameObject:SetActive(false)
                    if self.Clubtabels[i].run == true then
                        seat3.gameObject:SetActive(false)
                        seat4.gameObject:SetActive(false)
                        seat5.gameObject:SetActive(false)
                        isBegin.gameObject:SetActive(true)
                    end
                elseif self.Clubtabels[i].num == 3 then
                    InfoBtn1.gameObject:SetActive(true)
                    man1Sp.gameObject:SetActive(true)
                    man2Sp.gameObject:SetActive(true)
                    man3Sp.gameObject:SetActive(true)
                    man4Sp.gameObject:SetActive(false)
                    man5Sp.gameObject:SetActive(false)
                    if self.Clubtabels[i].run == true then
                        seat4.gameObject:SetActive(false)
                        seat5.gameObject:SetActive(false)
                        isBegin.gameObject:SetActive(true)
                    end
                elseif self.Clubtabels[i].num == 4 then
                    InfoBtn1.gameObject:SetActive(true)
                    man1Sp.gameObject:SetActive(true)
                    man2Sp.gameObject:SetActive(true)
                    man3Sp.gameObject:SetActive(true)
                    man4Sp.gameObject:SetActive(true)
                    man5Sp.gameObject:SetActive(false)
                    if self.Clubtabels[i].run == true then
                        seat5.gameObject:SetActive(false)
                        isBegin.gameObject:SetActive(true)
                    end
                elseif self.Clubtabels[i].num == 5 then
                    InfoBtn1.gameObject:SetActive(true)
                    man1Sp.gameObject:SetActive(true)
                    man2Sp.gameObject:SetActive(true)
                    man3Sp.gameObject:SetActive(true)
                    man4Sp.gameObject:SetActive(true)
                    man5Sp.gameObject:SetActive(true)
                    if self.Clubtabels[i].run == true then
                        isBegin.gameObject:SetActive(true)
                    end
                end
            end
        end
    end
end

function ClubLoobyWin:OnClose()
    if self.countTimeID ~= nil then
        CountDownMgr.Instance:RemoveCountDown(self.countTimeID, false)
    end
end

function ClubLoobyWin:FuncCountDown(countTime, id)
    function FuncRate(countDown)
        self.UpTime = self.UpTime + 1
        if self.UpTime == 5 then
            local ClubModule = require("Module.ClubModule").ClubModule
            ClubModule.TableInfoCm(id)
            self.UpTime = 0
        end
    end
    function FuncEnd()
        self.UpTim = 0
    end
    self.countTimeID = CountDownMgr.Instance:CreateCountDown(countTime, 1, FuncRate, FuncEnd)
end

function ClubLoobyWin:BindEvents()
    for i = 1, self.Tabels.childCount do
        function OnClick()
            --self:Close()
            local RoomModule = require("Module.RoomModule").RoomModule
            RoomModule.JoinRoom(nil, self.id, i)
        end
        UIHelper.BindUIEvnet("Click", OnClick, self.Tabels:GetChild(i - 1).gameObject)
    end

    for i = 1, self.Tabels.childCount do
        local InfoBtn1 = UIUtility.GetChildTransform("InfoBtn1", self.Tabels:GetChild(i - 1), true)
        function OnInfoClick()
            local ClubModule = require("Module.ClubModule").ClubModule
            ClubModule.TableDetailCm(self.id, i)
            Debug.log("?????????????????????????????")
        end
        UIHelper.BindUIEvnet("Click", OnInfoClick, InfoBtn1.gameObject)
    end

    function OncMBtnClick()
        UIWinMgr:OpenWindow("ClubManageWin")
    end
    UIHelper.BindUIEvnet("Click", OncMBtnClick, self.cManageBtn.gameObject)

    function OncGBtnClick()
        local ClubModule = require("Module.ClubModule").ClubModule
        local MemberListWin = require("UI.Windows.MemberListWin").MemberListWin
        MemberListWin.page = 1
        ClubModule.JulebuMemberListCm(self.id, MemberListWin.page)
    end
    UIHelper.BindUIEvnet("Click", OncGBtnClick, self.cGroupBtn.gameObject)

    function OncWBtnClick()
        --大赢家
        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.WinnerCm(self.id, 1)
    end
    UIHelper.BindUIEvnet("Click", OncWBtnClick, self.cWinnerBtn.gameObject)

    function OncRBtnClick()
        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.ClubRecordCm(self.id)
    end
    UIHelper.BindUIEvnet("Click", OncRBtnClick, self.cRecordBtn.gameObject)

    function OnCloseBtnClick()
        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.isInClubLobby = false
        if UnityEngine.PlayerPrefs.HasKey("lastClubID") then
            UnityEngine.PlayerPrefs.DeleteKey("lastClubID")
        end
        self:Close()
        UIWinMgr:OpenWindow("MainWin")
    end
    UIHelper.BindUIEvnet("Click", OnCloseBtnClick, self.CloseBtn.gameObject)

    function OnSBtnClick()
        UIWinMgr:OpenWindow("ClubSettingWin")
    end
    UIHelper.BindUIEvnet("Click", OnSBtnClick, self.SettingBtn.gameObject)
end
