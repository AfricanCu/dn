module("UI.Windows.MemberListWin", package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
MemberListWin = UIWindow:new()
MemberListWin.name = "MemberListWin" --窗体名字

function MemberListWin:Init()
    local t = self.Container.transform
    self.CY_LookOver = UIUtility.GetChildTransform("CY_LookOver", t, true)
    self.FBZ_LookOver = UIUtility.GetChildTransform("FBZ_LookOver", t, true)
    self.BZ_LookOver = UIUtility.GetChildTransform("BZ_LookOver", t, true)
    self.ApplyFor = UIUtility.GetChildTransform("ApplyFor", t, true)
    self.di = UIUtility.GetChildTransform("di", t, true)
    self.OnClose = UIUtility.GetChildTransform("OnClose", t, true)

    self.CY_1 = UIUtility.GetChildTransform("1", t, true)
    self.CYGrid = UIUtility.GetChildTransform("Grid_1", t, true)
    self.FBZ_2 = UIUtility.GetChildTransform("2", t, true)
    self.FBZGrid = UIUtility.GetChildTransform("Grid_2", t, true)
    self.BZ_3 = UIUtility.GetChildTransform("3", t, true)
    self.BZGrid = UIUtility.GetChildTransform("Grid_3", t, true)
    self.ApplyPro = UIUtility.GetChildTransform("4", t, true)
    self.ApplyGrid = UIUtility.GetChildTransform("Grid_4", t, true)

    self.ArrayMember = UIUtility.GetChildTransform("ArrayMember", t, true)
    self.ApplyForBtn = UIUtility.GetChildTransform("ApplyForBtn", t, true)
    self.m_ArrayMember = UIUtility.GetChildTransform("m_ArrayMember", t, true)
    self.m_ApplyFor = UIUtility.GetChildTransform("m_ApplyFor", t, true)
    self.xBtn = UIUtility.GetChildTransform("xiayiye", t, true)
    self.sBtn = UIUtility.GetChildTransform("shangyiye", t, true)
    self.YsLabel = UIUtility.FindContorl("UILabel", "YsLabel", t)

    self.Input1 = UIUtility.FindContorl("UIInput", "Input1", t)
    self.SoSou1 = UIUtility.GetChildTransform("SoSou1", t, true)
    self.Input = UIUtility.GetChildTransform("Input1", t, true)
    self.fanye = UIUtility.GetChildTransform("fanye", t, true)

    self.TiShi = UIUtility.GetChildTransform("m_TiShi", t, true)

    self.AllApply = {} --所有的申请
    self.AllList = {} --所有的成员
    self.YeShu = nil
    self.page = 1
    local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
    self.Job = ClubLoobyWin.job
    self.ID = ClubLoobyWin.id

    self.ClubMemberApplyList = {}
    self.ListPro = {}
    self.ListGrid = {}
    self.DianNum = 0
    self:BindEvents()
end

function MemberListWin:OnShow()
    if self.YeShu == 0 then
        self.YsLabel.text = 0 .. "/" .. self.YeShu
    else
        self.YsLabel.text = self.page .. "/" .. self.YeShu
    end
    local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
    self.Job = ClubLoobyWin.job
    Debug.log(ClubLoobyWin.job .. "111111111111111222222222222")
    self.ID = ClubLoobyWin.id
    Debug.log(self.Job .. "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    self.m_ArrayMember.gameObject:SetActive(true)
    self.ArrayMember.gameObject:SetActive(false)
    self.m_ApplyFor.gameObject:SetActive(false)
    self.ApplyForBtn.gameObject:SetActive(true)
    self.ApplyFor.gameObject:SetActive(false)
    if self.Job == 3 then
        self.ListPro = self.BZ_3
        self.ListGrid = self.BZGrid

        self.BZ_LookOver.gameObject:SetActive(true)
        self.FBZ_LookOver.gameObject:SetActive(false)
        self.CY_LookOver.gameObject:SetActive(false)
    elseif self.Job == 2 then
        self.ListPro = self.FBZ_2
        self.ListGrid = self.FBZGrid

        self.BZ_LookOver.gameObject:SetActive(false)
        self.FBZ_LookOver.gameObject:SetActive(true)
        self.CY_LookOver.gameObject:SetActive(false)
    elseif self.Job == 1 then
        self.ListPro = self.CY_1
        self.ListGrid = self.CYGrid

        self.BZ_LookOver.gameObject:SetActive(false)
        self.FBZ_LookOver.gameObject:SetActive(false)
        self.CY_LookOver.gameObject:SetActive(true)
    end
end

function MemberListWin:BindEvents()
    function OnxPage()
        self.page = self.page + 1
        if self.page > self.YeShu then
            self.page = self.YeShu
            return
        end

        self.YsLabel.text = self.page .. "/" .. self.YeShu

        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.JulebuMemberListCm(self.ID, self.page)
    end
    UIHelper.BindUIEvnet("Click", OnxPage, self.xBtn.gameObject)

    function OnsPage()
        self.page = self.page - 1
        if self.page < 1 then
            self.page = 1
            return
        end

        self.YsLabel.text = self.page .. "/" .. self.YeShu

        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.JulebuMemberListCm(self.ID, self.page)
    end
    UIHelper.BindUIEvnet("Click", OnsPage, self.sBtn.gameObject)

    function onClose()
        self:Close()
    end

    function ArrayMemberFun()
        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.JulebuMemberListCm(self.ID, self.page)
    end

    function ApplyForFun()
        self.ApplyFor.gameObject:SetActive(true)
        self.BZ_LookOver.gameObject:SetActive(false)
        self.FBZ_LookOver.gameObject:SetActive(false)
        self.CY_LookOver.gameObject:SetActive(false)
        self.TiShi.gameObject:SetActive(false)

        self.m_ArrayMember.gameObject:SetActive(false)
        self.ArrayMember.gameObject:SetActive(true)
        self.m_ApplyFor.gameObject:SetActive(true)
        self.ApplyForBtn.gameObject:SetActive(false)

        self.DianNum = 2
        local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
        ClubLoobyWin.MyDian = #self.ClubMemberApplyList
        UnityEngine.PlayerPrefs.SetInt("MyDian", ClubLoobyWin.MyDian)
        ClubLoobyWin.TiShiDian.gameObject:SetActive(false)
        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.ApplyMemberListCm(self.ID)
        --self:ShowClubApplyList()
    end

    -- UIHelper.BindUIEvnet("Click", onClose, self.di.gameObject)
    UIHelper.BindUIEvnet("Click", onClose, self.OnClose.gameObject)

    UIHelper.BindUIEvnet("Click", ArrayMemberFun, self.ArrayMember.gameObject)
    UIHelper.BindUIEvnet("Click", ApplyForFun, self.ApplyForBtn.gameObject)

    function SoSouBtn()
        Debug.log(self.Input1.value)
        self.page = 1
        self.YsLabel.text = self.page .. "/" .. self.page
        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.SearchMemberCm(self.ID, self.Input1.value)
    end
    UIHelper.BindUIEvnet("Click", SoSouBtn, self.SoSou1.gameObject)
end

function MemberListWin:ShowClubList(ClubMemberList)
    self.m_ArrayMember.gameObject:SetActive(true)
    self.ApplyForBtn.gameObject:SetActive(true)
    self.ArrayMember.gameObject:SetActive(false)
    self.m_ApplyFor.gameObject:SetActive(false)
    self.ApplyFor.gameObject:SetActive(false)

    self.SoSou1.gameObject:SetActive(true)
    self.Input.gameObject:SetActive(true)
    self.fanye.gameObject:SetActive(true)
    self.ClubMemberList = ClubMemberList
    for i = #self.AllList, 1, -1 do
        UnityEngine.GameObject.Destroy(self.AllList[i])
    end
    self.AllList = {}
    if self.ClubMemberList and #self.ClubMemberList > 0 then
        local num = #self.ClubMemberList
        for i = 1, num do
            self.AllList[i] = UnityEngine.GameObject.Instantiate(self.ListPro.gameObject)
            self.AllList[i].gameObject:SetActive(true)
            self.AllList[i].transform.parent = self.ListGrid.transform
            self.AllList[i].transform.localScale = Vector3.one
            self.AllList[i].transform.localPosition = Vector3.zero
            self.AllList[i].transform.localEulerAngles = Vector3(0, 0, 0)
            local n = self.AllList[i].transform
            local m_Id = UIUtility.FindContorl("UILabel", "m_Id", n, true)
            local m_Name = UIUtility.FindContorl("UILabel", "m_Name", n, true)
            local m_ZhiZe1 = UIUtility.FindContorl("UILabel", "zhize1", n, true)
            local m_ZhiZe2 = UIUtility.FindContorl("UILabel", "zhize2", n, true)
            local m_Number = UIUtility.FindContorl("UILabel", "number", n, true)
            local m_ChangeZhiZe = UIUtility.GetChildTransform("PopupList", n, true)
            local mPopupList = UIUtility.FindContorl("UIPopupList", "PopupList", n, true)
            local mPopupListLable = UIUtility.FindContorl("UILabel", "PopupListLable", n, true)
            local m_QuitClub = UIUtility.GetChildTransform("Quite", n, true) --移出俱乐部
            local m_HuiZhang = UIUtility.GetChildTransform("HuiZhang", n, true)
            local ClubHeadPortrait = UIUtility.FindContorl("UITexture", "HeadPortrait", n)
            local m_ZhouHuoYue = UIUtility.FindContorl("UILabel", "ZhouHuoYue", n)

            local num = (self.page - 1) * 20
            m_Number.text = num + i
            m_Id.text = "ID:" .. self.ClubMemberList[i].uid
            m_Name.text = self.ClubMemberList[i].name
            m_ZhouHuoYue.text = self.ClubMemberList[i].active
            --头像

            function callBack(isOk, www)
                if isOk and ClubHeadPortrait ~= nil then
                    ClubHeadPortrait.mainTexture = www.texture
                end
            end
            require("NetWork.NetHttp").WWWTexture(self.ClubMemberList[i].headimg, callBack)

            if m_ZhiZe1 then
                if self.ClubMemberList[i].job == 1 then
                    m_ZhiZe1.text = "成员"
                elseif self.ClubMemberList[i].job == 2 then
                    m_ZhiZe1.text = "副会长"
                elseif self.ClubMemberList[i].job == 3 then
                    m_ZhiZe1.text = "会长"
                end
            end
            if m_ZhiZe2 then
                if self.ClubMemberList[i].job == 1 then
                    m_ZhiZe2.text = "成员"
                elseif self.ClubMemberList[i].job == 2 then
                    m_ZhiZe2.text = "副会长"
                elseif self.ClubMemberList[i].job == 3 then
                    m_ZhiZe2.text = "会长"
                end
            end
            if mPopupListLable then
                if self.ClubMemberList[i].job == 1 then
                    mPopupListLable.text = "成员"
                elseif self.ClubMemberList[i].job == 2 then
                    mPopupListLable.text = "副会长"
                end
            end
            if self.Job == 3 then
                if self.ClubMemberList[i].job == 3 then
                    m_QuitClub.gameObject:SetActive(false)
                end
            end
            if self.Job == 2 then
                if self.ClubMemberList[i].job == 3 or self.ClubMemberList[i].job == 2 then
                    m_QuitClub.gameObject:SetActive(false)
                end
            end
            if m_ChangeZhiZe then
                if self.ClubMemberList[i].job == 3 then
                    m_ChangeZhiZe.gameObject:SetActive(false)
                    m_HuiZhang.gameObject:SetActive(true)
                end
                function OnSoundChanged()
                    local value = mPopupList.value
                    local ClubModule = require("Module.ClubModule").ClubModule
                    if value == "副会长" then
                        ClubModule.LevelupMemberCm(self.ID, self.ClubMemberList[i].uid, 2)
                    elseif value == "成员" then
                        ClubModule.LevelupMemberCm(self.ID, self.ClubMemberList[i].uid, 1)
                    end
                end
                local SoundEventDelegate = UIHelper.CreateEventDelegate(OnSoundChanged, mPopupList.gameObject)
                mPopupList.onChange:Add(SoundEventDelegate)
            end
            function QuiteClubBtn()
                local ClubModule = require("Module.ClubModule").ClubModule
                --移除
                ClubModule.KickJulebuMemberCm(self.ID, self.ClubMemberList[i].uid, self.page)
            end
            if m_QuitClub then
                UIHelper.BindUIEvnet("Click", QuiteClubBtn, m_QuitClub.gameObject)
            end
        end
    end
    self.ListGrid.transform:GetComponent("UIGrid").enabled = true
end

function MemberListWin:RemoveClubApplyMember(uid)
    if self.ClubMemberApplyList ~= nil then
        local idx = 0
        for i = 1, #self.ClubMemberApplyList do
            local member = self.ClubMemberApplyList[i]
            if member ~= nil and member.uid == uid then
                idx = i
                break
            end
        end

        if idx > 0 then
            table.remove(self.ClubMemberApplyList, idx)
        end
    end
end

function MemberListWin:ShowClubApplyList()
    self.SoSou1.gameObject:SetActive(false)
    self.Input.gameObject:SetActive(false)
    self.fanye.gameObject:SetActive(false)
    for i = #self.AllApply, 1, -1 do
        UnityEngine.GameObject.Destroy(self.AllApply[i])
    end
    self.AllApply = {}

    if self.ClubMemberApplyList and #self.ClubMemberApplyList > 0 then
        local num = #self.ClubMemberApplyList
        for i = 1, num do
            self.AllApply[i] = UnityEngine.GameObject.Instantiate(self.ApplyPro.gameObject)
            self.AllApply[i].gameObject:SetActive(true)
            self.AllApply[i].transform.parent = self.ApplyGrid.transform
            self.AllApply[i].transform.localScale = Vector3.one
            self.AllApply[i].transform.localPosition = Vector3.zero
            self.AllApply[i].transform.localEulerAngles = Vector3(0, 0, 0)
            local n = self.AllApply[i].transform
            local m_Id = UIUtility.FindContorl("UILabel", "m_Id", n, true)
            local m_Name = UIUtility.FindContorl("UILabel", "m_Name", n, true)
            local m_ZhiZe = UIUtility.FindContorl("UILabel", "Zhize", n, true)
            local m_Number = UIUtility.FindContorl("UILabel", "number", n, true)
            local m_Consent = UIUtility.GetChildTransform("Consent", n, true) --同意
            local m_Refuse = UIUtility.GetChildTransform("Refuse", n, true) --拒绝

            local ClubHeadPortrait = UIUtility.FindContorl("UITexture", "HeadPortrait", n)
            local num = (self.page - 1) * 20
            m_Number.text = num + i
            m_Id.text = "ID:" .. self.ClubMemberApplyList[i].uid
            m_Name.text = self.ClubMemberApplyList[i].name
            --头像
            function callBack(isOk, www)
                if isOk and ClubHeadPortrait ~= nil then
                    ClubHeadPortrait.mainTexture = www.texture
                end
            end
            require("NetWork.NetHttp").WWWTexture(self.ClubMemberApplyList[i].headimg, callBack)

            function ConsentBtn()
                local ClubModule = require("Module.ClubModule").ClubModule
                --同意
                ClubModule.AgreeApplyCm(self.ID, self.ClubMemberApplyList[i].uid)
                local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
                ClubLoobyWin.MyDian = #self.ClubMemberApplyList - 1
                UnityEngine.PlayerPrefs.SetInt("MyDian", ClubLoobyWin.MyDian)
            end
            function RefuseBtn()
                local ClubModule = require("Module.ClubModule").ClubModule
                --拒绝
                ClubModule.DisagreeApplyCm(self.ID, self.ClubMemberApplyList[i].uid)
                local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
                ClubLoobyWin.MyDian = #self.ClubMemberApplyList - 1
                UnityEngine.PlayerPrefs.SetInt("MyDian", ClubLoobyWin.MyDian)
            end
            UIHelper.BindUIEvnet("Click", ConsentBtn, m_Consent.gameObject)
            UIHelper.BindUIEvnet("Click", RefuseBtn, m_Refuse.gameObject)
        end
    end
    self.ApplyGrid.transform:GetComponent("UIGrid").enabled = true
end
