module("UI.Windows.BigWinnerWin", package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
BigWinnerWin = UIWindow:new()

BigWinnerWin.name = "BigWinnerWin" --窗体名字
function BigWinnerWin:Init()
    local t = self.Container.transform
    self.AllClear = UIUtility.GetChildTransform("AllClear", t, true)
    self.OnClose = UIUtility.GetChildTransform("OnClose", t, true)
    self.di = UIUtility.GetChildTransform("di", t, true)
    self.ListPro = UIUtility.GetChildTransform("1", t, true)
    self.ListGrid = UIUtility.GetChildTransform("info", t, true)
    self.xBtn = UIUtility.GetChildTransform("xiayiye", t, true)
    self.sBtn = UIUtility.GetChildTransform("shangyiye", t, true)
    self.YsLabel = UIUtility.FindContorl("UILabel", "YsLabel", t)

    self.AllWinner = {} --要显示的所有的大赢家
    self.YeShu = nil
    self.page = 1
    self.AllBigWinnerNum = {}
    self:BindEvents()
end
function BigWinnerWin:OnShow()
    if self.YeShu == 0 then
        self.YsLabel.text = 0 .. "/" .. self.YeShu
    else
        self.YsLabel.text = self.page .. "/" .. self.YeShu
    end
    local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
    self.ID = ClubLoobyWin.id
end
function BigWinnerWin:BindEvents()
    function OnxPage()
        self.page = self.page + 1
        if self.page > self.YeShu then
            self.page = self.YeShu
            return
        end

        self.YsLabel.text = self.page .. "/" .. self.YeShu

        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.WinnerCm(self.ID, self.page)
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
        ClubModule.WinnerCm(self.ID, self.page)
    end
    UIHelper.BindUIEvnet("Click", OnsPage, self.sBtn.gameObject)
    function CloseBtn()
        self:Close()
    end
    function AllClearBtn()
        --清除所有
        local temp = ""
        for i = 1, #self.AllBigWinnerNum do
            temp = temp .. self.AllWinnerList[i].uid .. "#" .. self.AllWinnerList[i].num .. "#"
        end
        if temp ~= "" then
            local ClubModule = require("Module.ClubModule").ClubModule
            Debug.log(temp)
            ClubModule.ClearWinnerCm(self.ID, temp, self.page)
        end
    end
    UIHelper.BindUIEvnet("Click", CloseBtn, self.OnClose.gameObject)
    UIHelper.BindUIEvnet("Click", AllClearBtn, self.AllClear.gameObject)
end
function BigWinnerWin:BigWinShow(AllWinnerList)
    self.AllWinnerList = AllWinnerList --收到的大赢家数据
    for i = #self.AllWinner, 1, -1 do
        UnityEngine.GameObject.Destroy(self.AllWinner[i])
    end
    self.AllWinner = {}
    if self.AllWinnerList and #self.AllWinnerList > 0 then
        local num = #self.AllWinnerList
        for i = 1, num do
            self.AllWinner[i] = UnityEngine.GameObject.Instantiate(self.ListPro.gameObject)
            self.AllWinner[i].gameObject:SetActive(true)
            self.AllWinner[i].transform.parent = self.ListGrid.transform
            self.AllWinner[i].transform.localScale = Vector3.one
            self.AllWinner[i].transform.localPosition = Vector3.zero
            self.AllWinner[i].transform.localEulerAngles = Vector3(0, 0, 0)
            local n = self.AllWinner[i].transform
            local m_Id = UIUtility.FindContorl("UILabel", "ID", n)
            local m_Name = UIUtility.FindContorl("UILabel", "name", n)
            local m_Nnm = UIUtility.FindContorl("UILabel", "Num", n)
            local m_HeadPortrait = UIUtility.FindContorl("UITexture", "HeadPortrait", n)
            local m_Clear = UIUtility.GetChildTransform("clear", n, true)

            m_Id.text = "ID:" .. self.AllWinnerList[i].uid
            m_Name.text = self.AllWinnerList[i].nickname
            if self.AllWinnerList[i].num % 12 == 0 then
                m_Nnm.text = self.AllWinnerList[i].num / 12
            else
                local fmt = "%.1f"
                m_Nnm.text = string.format(fmt, self.AllWinnerList[i].num / 12)
            end

            function callBack(isOk, www)
                if isOk and m_HeadPortrait ~= nil then
                    m_HeadPortrait.mainTexture = www.texture
                end
            end
            require("NetWork.NetHttp").WWWTexture(self.AllWinnerList[i].headimg, callBack)

            function QuiteClubBtn()
                Debug.log("清除")
                local ClubModule = require("Module.ClubModule").ClubModule
                --清除
                local aa = self.AllWinnerList[i].uid .. "#" .. self.AllWinnerList[i].num
                Debug.log(aa)
                Debug.log(self.ID)
                ClubModule.ClearWinnerCm(self.ID, aa, self.page)
            end
            UIHelper.BindUIEvnet("Click", QuiteClubBtn, m_Clear.gameObject)
        end
    end
    self.ListGrid.transform:GetComponent("UIGrid").enabled = true
end
