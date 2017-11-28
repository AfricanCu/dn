--设置窗口
module("UI.Windows.PaiWin", package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
PaiWin = UIWindow:new()
PaiWin.name = "PaiWin" --窗体名字

--初始化界面
function PaiWin:Init()
    --初始化界面，保存引用控件
    local t = self.Container.transform
    self.player1 = UIUtility.GetChildTransform("player1", t, true)
    self.player2 = UIUtility.GetChildTransform("player2", t, true)
    self.player3 = UIUtility.GetChildTransform("player3", t, true)
    self.player4 = UIUtility.GetChildTransform("player4", t, true)
    self.player5 = UIUtility.GetChildTransform("player5", t, true)
    self.Card1 = UIUtility.GetChildTransform("card1", t, true)
    self.Card2 = UIUtility.GetChildTransform("card2", t, true)
    self.Card3 = UIUtility.GetChildTransform("card3", t, true)
    self.Card4 = UIUtility.GetChildTransform("card4", t, true)
    self.Card5 = UIUtility.GetChildTransform("card5", t, true)
    self.Card6 = UIUtility.GetChildTransform("card6", t, true)
    self.b_1 = UIUtility.GetChildTransform("b_1", t, true)
    self.b_2 = UIUtility.GetChildTransform("b_2", t, true)
    self.b_3 = UIUtility.GetChildTransform("b_3", t, true)

    self.UICard1 = {}
    local count = self.Card1.childCount - 1
    for i = 0, count do
        local root = self.Card1:GetChild(i)
        local tmpTable = {}
        tmpTable.root = root
        tmpTable.TP_CardP = tmpTable.root.transform:GetComponent("TweenPosition")
        tmpTable.TR_CardP = tmpTable.root.transform:GetComponent("TweenRotation")
        tmpTable.P_CardTween = tmpTable.root.transform:GetComponent("UIPlayTween")
        tmpTable.S_Paiinfo = tmpTable.root.transform:GetComponent("UISprite")
        tmpTable.Idx = 1
        self.UICard1[i + 1] = tmpTable
    end

    self.UICard2 = {}
    local count = self.Card2.childCount - 1
    for i = 0, count do
        local root = self.Card2:GetChild(i)
        local tmpTable = {}
        tmpTable.root = root
        tmpTable.TP_CardP = tmpTable.root.transform:GetComponent("TweenPosition")
        tmpTable.P_CardTween = tmpTable.root.transform:GetComponent("UIPlayTween")
        self.UICard2[i + 1] = tmpTable
    end

    self.UICard3 = {}
    local count = self.Card3.childCount - 1
    for i = 0, count do
        local root = self.Card3:GetChild(i)
        local tmpTable = {}
        tmpTable.root = root
        tmpTable.TP_CardP = tmpTable.root.transform:GetComponent("TweenPosition")
        tmpTable.P_CardTween = tmpTable.root.transform:GetComponent("UIPlayTween")
        self.UICard3[i + 1] = tmpTable
    end

    self.UICard4 = {}
    local count = self.Card4.childCount - 1
    for i = 0, count do
        local root = self.Card4:GetChild(i)
        local tmpTable = {}
        tmpTable.root = root
        tmpTable.TP_CardP = tmpTable.root.transform:GetComponent("TweenPosition")
        tmpTable.P_CardTween = tmpTable.root.transform:GetComponent("UIPlayTween")
        self.UICard4[i + 1] = tmpTable
    end

    self.UICard5 = {}
    local count = self.Card5.childCount - 1
    for i = 0, count do
        local root = self.Card5:GetChild(i)
        local tmpTable = {}
        tmpTable.root = root
        tmpTable.TP_CardP = tmpTable.root.transform:GetComponent("TweenPosition")
        tmpTable.P_CardTween = tmpTable.root.transform:GetComponent("UIPlayTween")
        self.UICard5[i + 1] = tmpTable
    end

    self:BindEvents()
end
function PaiWin:BindEvents()
end
function PaiWin:OnShow()
end
function PaiWin:FaiPai(P_num, C_num, puke1)
    if P_num == 1 then
        self.Card1.gameObject:SetActive(true)
    elseif P_num == 2 then
        self.Card2.gameObject:SetActive(true)
    elseif P_num == 3 then
        self.Card3.gameObject:SetActive(true)
    elseif P_num == 4 then
        self.Card4.gameObject:SetActive(true)
    elseif P_num == 5 then
        self.Card5.gameObject:SetActive(true)
    end
    function donghua(C_temp, p_temp)
        for i = 0, 3 do
            C_temp[i + 1].TP_CardP.from = Vector3(0, 0, 0)
            C_temp[i + 1].TP_CardP.to = p_temp.transform.localPosition
            C_temp[i + 1].TP_CardP.duration = 0.2 * i
            C_temp[i + 1].P_CardTween.resetOnPlay = true
            C_temp[i + 1].P_CardTween:Play(true)
            function zhangshi()
                C_temp[i + 1].TP_CardP.from = p_temp.transform.localPosition
                local x = p_temp.transform.localPosition.x
                local y = p_temp.transform.localPosition.y
                local z = p_temp.transform.localPosition.z
                if C_temp[i + 1].Idx == 1 then
                    function xuangzhuang()
                        C_temp[i + 1].TP_CardP.from = Vector3(x + (i * 108), y, z)
                        C_temp[i + 1].TP_CardP.to = Vector3(x + (i * 108), y, z)
                        C_temp[i + 1].TR_CardP.from = Vector3(0, 180, 0)
                        C_temp[i + 1].TR_CardP.to = Vector3(0, 0, 0)
                        C_temp[i + 1].TR_CardP.duration = 0.15
                        C_temp[i + 1].P_CardTween.resetOnPlay = true
                        C_temp[i + 1].P_CardTween:Play(true)
                        function FuZhi()
                            if puke1 ~= nil then
                                C_temp[i + 1].S_Paiinfo.spriteName =
                                    "pkc_" .. puke1[i + 1].num .. "0" .. puke1[i + 1].type
                            end
                        end
                        CountDownMgr.Instance:CreateCountDown(0.12, 0.12, FuZhi)
                    end
                    C_temp[i + 1].TP_CardP.to = Vector3(x + (i * 108), y, z)
                    CountDownMgr.Instance:CreateCountDown(0.4, 0.4, xuangzhuang)
                else
                    C_temp[i + 1].TP_CardP.to = Vector3(x + (i * 38), y, z)
                end

                C_temp[i + 1].TP_CardP.duration = 0.1 * i
                C_temp[i + 1].P_CardTween.resetOnPlay = true
                C_temp[i + 1].P_CardTween:Play(true)
            end

            CountDownMgr.Instance:CreateCountDown(0.8, 0.8, zhangshi)
        end
    end

    function FaiPai_1(C_temp, p_temp, puke1)
        C_temp[5].TP_CardP.from = Vector3(0, 0, 0)
        local x = p_temp.transform.localPosition.x
        local y = p_temp.transform.localPosition.y
        local z = p_temp.transform.localPosition.z
        if C_temp[5].Idx == 1 then
            function xuangzhuang()
                C_temp[5].TP_CardP.from = Vector3(x + (4 * 108), y, z)
                C_temp[5].TP_CardP.to = Vector3(x + (4 * 108), y, z)
                C_temp[5].TR_CardP.from = Vector3(0, 180, 0)
                C_temp[5].TR_CardP.to = Vector3(0, 0, 0)
                C_temp[5].TR_CardP.duration = 0.15
                C_temp[5].P_CardTween.resetOnPlay = true
                C_temp[5].P_CardTween:Play(true)
                function FuZhi()
                    if puke1 ~= nil then
                        C_temp[5].S_Paiinfo.spriteName = "pkc_" .. puke1[1].num .. "0" .. puke1[1].type
                    end
                end
                CountDownMgr.Instance:CreateCountDown(0.12, 0.12, FuZhi)
            end
            C_temp[5].TP_CardP.to = Vector3(x + (4 * 108), y, z)
            CountDownMgr.Instance:CreateCountDown(0.4, 0.4, xuangzhuang)
        else
            C_temp[5].TP_CardP.to = Vector3(x + (4 * 38), y, z)
        end

        C_temp[5].TP_CardP.duration = 0.2
        C_temp[5].P_CardTween.resetOnPlay = true
        C_temp[5].P_CardTween:Play(true)
    end

    if P_num == 1 then
        if C_num == 4 then
            self.Card1:GetChild(4).gameObject:SetActive(false)
            donghua(self.UICard1, self.player1)
        elseif C_num == 1 then
            self.Card1:GetChild(4).gameObject:SetActive(true)
            FaiPai_1(self.UICard1, self.player1, puke1)
        end
    elseif P_num == 2 then
        if C_num == 4 then
            self.Card2:GetChild(4).gameObject:SetActive(false)
            donghua(self.UICard2, self.player2)
        elseif C_num == 1 then
            self.Card2:GetChild(4).gameObject:SetActive(true)
            FaiPai_1(self.UICard2, self.player2)
        end
    elseif P_num == 3 then
        if C_num == 4 then
            self.Card3:GetChild(4).gameObject:SetActive(false)
            donghua(self.UICard3, self.player3)
        elseif C_num == 1 then
            self.Card3:GetChild(4).gameObject:SetActive(true)
            FaiPai_1(self.UICard3, self.player3)
        end
    elseif P_num == 4 then
        if C_num == 4 then
            self.Card4:GetChild(4).gameObject:SetActive(false)
            donghua(self.UICard4, self.player4)
        elseif C_num == 1 then
            self.Card4:GetChild(4).gameObject:SetActive(true)
            FaiPai_1(self.UICard4, self.player4)
        end
    elseif P_num == 5 then
        if C_num == 4 then
            self.Card5:GetChild(4).gameObject:SetActive(false)
            donghua(self.UICard5, self.player5)
        elseif C_num == 1 then
            self.Card5:GetChild(4).gameObject:SetActive(true)
            FaiPai_1(self.UICard5, self.player5)
        end
    end
end
function PaiWin:YouNiu(Pkc)
    for i = 1, 3 do
        if Pkc ~= nil then
            local x = self.Card1:GetChild(Pkc[i] - 1).transform.localPosition.x
            local y = self.Card1:GetChild(Pkc[i] - 1).transform.localPosition.y
            local z = self.Card1:GetChild(Pkc[i] - 1).transform.localPosition.z
            self.Card1:GetChild(Pkc[i] - 1).transform.localPosition = Vector3(x, y + 30, z)
        end
    end
end
function PaiWin:Finish(idx)
    if idx == 1 then
        self.Card6.gameObject:SetActive(true)
        self.player1:GetChild(0).gameObject:SetActive(true)
        self.Card1.gameObject:SetActive(false)
    end
    if idx == 2 then
        self.player2:GetChild(0).gameObject:SetActive(true)
    end
    if idx == 3 then
        self.player3:GetChild(0).gameObject:SetActive(true)
    end
    if idx == 4 then
        self.player4:GetChild(0).gameObject:SetActive(true)
    end
    if idx == 5 then
        self.player5:GetChild(0).gameObject:SetActive(true)
    end
end
function PaiWin:ShowPai(P_idx, pkc, pai)
    local temp = {}
    local paixing = nil
    local temp_Finish = nil
    local name = nil
    if P_idx == 1 then
        self.Card6.gameObject:SetActive(true)
        temp = self.Card6
        paixing = self.player1:GetChild(1)
        temp_Finish = self.player1:GetChild(0)
        name = self.player1:GetChild(1).gameObject:GetComponent("UISprite")
    elseif P_idx == 2 then
        self.Card2.gameObject:SetActive(true)
        local x = self.player2.transform.localPosition.x
        local y = self.player2.transform.localPosition.y
        local z = self.player2.transform.localPosition.z
        for i = 1, 5 do
            self.UICard2[i].root.transform.localPosition = Vector3(x + ((i - 1) * 38), y, z)
        end

        temp = self.Card2
        paixing = self.player2:GetChild(1)
        temp_Finish = self.player2:GetChild(0)
        name = self.player2:GetChild(1).gameObject:GetComponent("UISprite")
    elseif P_idx == 3 then
        self.Card3.gameObject:SetActive(true)
        local x = self.player3.transform.localPosition.x
        local y = self.player3.transform.localPosition.y
        local z = self.player3.transform.localPosition.z
        for i = 1, 5 do
            self.UICard3[i].root.transform.localPosition = Vector3(x + ((i - 1) * 38), y, z)
        end
        temp = self.Card3
        paixing = self.player3:GetChild(1)
        temp_Finish = self.player3:GetChild(0)
        name = self.player3:GetChild(1).gameObject:GetComponent("UISprite")
    elseif P_idx == 4 then
        self.Card4.gameObject:SetActive(true)
        local x = self.player4.transform.localPosition.x
        local y = self.player4.transform.localPosition.y
        local z = self.player4.transform.localPosition.z
        for i = 1, 5 do
            self.UICard4[i].root.transform.localPosition = Vector3(x + ((i - 1) * 38), y, z)
        end
        temp = self.Card4
        paixing = self.player4:GetChild(1)
        temp_Finish = self.player4:GetChild(0)
        name = self.player4:GetChild(1).gameObject:GetComponent("UISprite")
    elseif P_idx == 5 then
        self.Card5.gameObject:SetActive(true)
        local x = self.player5.transform.localPosition.x
        local y = self.player5.transform.localPosition.y
        local z = self.player5.transform.localPosition.z
        for i = 1, 5 do
            self.UICard5[i].root.transform.localPosition = Vector3(x + ((i - 1) * 38), y, z)
        end
        temp = self.Card5
        paixing = self.player5:GetChild(1)
        temp_Finish = self.player5:GetChild(0)
        name = self.player5:GetChild(1).gameObject:GetComponent("UISprite")
    end
    if paixing ~= nil and name ~= nil then
        temp_Finish.gameObject:SetActive(false)
        paixing.gameObject:SetActive(true)
        name.spriteName = "V2_Title_DNF_" .. pai
        local playTween = paixing.gameObject:GetComponent("UIPlayTween")
        playTween.resetOnPlay = true
        playTween:Play(true)
    end
    if temp ~= nil then
        for i = 0, 4 do
            local tempCard = temp.transform:GetChild(i).gameObject:GetComponent("UISprite")
            tempCard.spriteName = "pkc_" .. pkc[i + 1].num .. "0" .. pkc[i + 1].type
        end
    end
end
function PaiWin:ClearPai()
    self.Card1.gameObject:SetActive(false)
    self.Card2.gameObject:SetActive(false)
    self.Card3.gameObject:SetActive(false)
    self.Card4.gameObject:SetActive(false)
    self.Card5.gameObject:SetActive(false)
    self.Card6.gameObject:SetActive(false)
    for i = 0, 4 do
        self.Card1:GetChild(i).gameObject:GetComponent("UISprite").spriteName = "pkc_paimian"
        self.Card6:GetChild(i).gameObject:GetComponent("UISprite").spriteName = "pkc_paimian"
        self.Card2:GetChild(i).gameObject:GetComponent("UISprite").spriteName = "pkc_paimian"
        self.Card3:GetChild(i).gameObject:GetComponent("UISprite").spriteName = "pkc_paimian"
        self.Card4:GetChild(i).gameObject:GetComponent("UISprite").spriteName = "pkc_paimian"
        self.Card5:GetChild(i).gameObject:GetComponent("UISprite").spriteName = "pkc_paimian"
    end
    self.player1:GetChild(1).gameObject:SetActive(false)
    self.player2:GetChild(1).gameObject:SetActive(false)
    self.player3:GetChild(1).gameObject:SetActive(false)
    self.player4:GetChild(1).gameObject:SetActive(false)
    self.player5:GetChild(1).gameObject:SetActive(false)
end
function PaiWin:chonglian(idx, num, pkc)
    Debug.log("idx:" .. idx .. " num:" .. num)
    if idx == 1 then
        self.Card1.gameObject:SetActive(true)
        for i = 1, num do
            local x = self.player1.transform.localPosition.x
            local y = self.player1.transform.localPosition.y
            local z = self.player1.transform.localPosition.z
            self.UICard1[i].root.transform.gameObject:SetActive(true)
            self.UICard1[i].root.transform.localPosition = Vector3(x + ((i - 1) * 108), y, z)
            Debug.log("坐标：" .. self.Card1:GetChild(i - 1).transform.localPosition.y)
            if pkc ~= nil then
                self.UICard1[i].S_Paiinfo.spriteName = "pkc_" .. pkc[i].num .. "0" .. pkc[i].type
            end
        end

        for i = num + 1, 5 do
            self.UICard1[i].root.transform.gameObject:SetActive(false)
        end
    else
        local tempUICard = {}
        local tempplayer = {}
        if idx == 2 then
            self.Card2.gameObject:SetActive(true)
            tempUICard = self.UICard2
            tempplayer = self.player2
        elseif idx == 3 then
            self.Card3.gameObject:SetActive(true)
            tempUICard = self.UICard3
            tempplayer = self.player3
        elseif idx == 4 then
            self.Card4.gameObject:SetActive(true)
            tempUICard = self.UICard4
            tempplayer = self.player4
        elseif idx == 5 then
            self.Card5.gameObject:SetActive(true)
            tempUICard = self.UICard5
            tempplayer = self.player5
        end

        for i = 1, num do
            local x = tempplayer.transform.localPosition.x
            local y = tempplayer.transform.localPosition.y
            local z = tempplayer.transform.localPosition.z
            tempUICard[i].root.transform.gameObject:SetActive(true)
            tempUICard[i].root.transform.localPosition = Vector3(x + ((i - 1) * 38), y, z)

            if pkc ~= nil then
                tempUICard[i].S_Paiinfo.spriteName = "pkc_" .. pkc[i].num .. "0" .. pkc[i].type
            end
        end
        for i = num + 1, 5 do
            tempUICard[i].root.transform.gameObject:SetActive(false)
        end
    end
end
