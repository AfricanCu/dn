--
module("UI.Windows.ClubSettingWin",package.seeall)
local UICreateRoomColor = require("UI.Com.UICreateRoomColor").UICreateRoomColor
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
ClubSettingWin = UIWindow:new()
ClubSettingWin.name = "ClubSettingWin" --窗体名字
local playTypeTmp = {}
local isIp = true

--初始化界面
function ClubSettingWin:Init()
    --初始化界面，保存引用控件
    --local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
    --playTypeTmp = ClubLoobyWin.playType
    local t = self.Container.transform
    --三个设置面板容器
    self.InfoSetCtn = UIUtility.GetChildTransform("InfoSetCtn",t,true)
    self.WanfaSetCtn = UIUtility.GetChildTransform("WanfaSetCtn",t,true)
    --俱乐部信息设置
    self.InfoTog = UIUtility.FindContorl('UIToggle',"InfoTog",t)
    self.NameInput = UIUtility.FindContorl('UIInput',"NameInput",t)
    self.GongGaoInput = UIUtility.FindContorl('UIInput',"GongGaoInput",t)
    self.AiOnBtn = UIUtility.FindContorl('UIToggle',"AiOnBtn",t)
    self.AiOffBtn = UIUtility.FindContorl('UIToggle',"AiOffBtn",t)
    --玩法设置
    self.WanfaTog = UIUtility.FindContorl('UIToggle',"WanfaTog",t)
    self.closeButton = UIUtility.GetChildTransform("Close",t,true)
    self.eJuUIToggle = UIUtility.FindContorl("UIToggle", "JuBtn1", t)
    --10局
    self.stJuUIToggle = UIUtility.FindContorl("UIToggle", "JuBtn2", t)
    --20局
    self.PtypeJD = UIUtility.FindContorl("UIToggle", "JingDianBtn", t)
    --经典
    self.PtypeFK = UIUtility.FindContorl("UIToggle", "JiaBeiBtn", t)
    --疯狂
    self.Q_Zhuang = UIUtility.FindContorl("UIToggle", "QzhuangBtn", t)
    --抢庄
    self.L_Zhuang = UIUtility.FindContorl("UIToggle", "LzhuangBtn", t)
    --轮庄
    self.G_Zhuang = UIUtility.FindContorl("UIToggle", "GzhuangBtn", t)
    self.OKBtn = UIUtility.GetChildTransform("OKBtn",t,true)
    self:BindEvents()
end

function ClubSettingWin:ColorToOn(transform)
    local wsxColorCom = UICreateRoomColor:new()
    wsxColorCom:Init(transform)
    wsxColorCom:On()
end

function ClubSettingWin:ColorToOff(transform)
    local wsxColorCom = UICreateRoomColor:new()
    wsxColorCom:Init(transform)
    wsxColorCom:Off()
end

function ClubSettingWin:OnShow()
    local ClubLoobyWin = UIWinMgr:GetWindow("ClubLoobyWin")
    playTypeTmp = {}
    playTypeTmp.round = ClubLoobyWin.playType.round
    playTypeTmp.pType = ClubLoobyWin.playType.pType
    playTypeTmp.bankerMode = ClubLoobyWin.playType.bankerMode
    self.NameInput.value = ClubLoobyWin.ClubName
    self.GongGaoInput.value = ClubLoobyWin.Notice
    self.isIp = ClubLoobyWin.prohibitIp
    self:ShowWin()
end

function ClubSettingWin:OnClose()
    self:Destroy()
end

function ClubSettingWin:ShowWin()
    local ClubLoobyWin = UIWinMgr:GetWindow("ClubLoobyWin")
    if ClubLoobyWin.prohibitIp == true then
        self.AiOnBtn.value = true
    elseif ClubLoobyWin.prohibitIp == false then
        self.AiOffBtn.value = true
    end
    if ClubLoobyWin.job == 1 then
        self.OKBtn.gameObject:SetActive(false)
    else
        self.OKBtn.gameObject:SetActive(true)
    end
    self.InfoTog.value=true
    if playTypeTmp.round == 1 then
        self.eJuUIToggle.value = true
    else
        self.stJuUIToggle.value = true
    end
    if playTypeTmp.bankerMode == 1 then
        self.Q_Zhuang.value = true
    elseif playTypeTmp.bankerMode == 2 then
        self.L_Zhuang.value = true
    elseif playTypeTmp.bankerMode == 3 then
        self.G_Zhuang.value = true
    end
    if playTypeTmp.pType == 1 then
        self.PtypeJD.value = true
    else
        self.PtypeFK.value = true
    end
end

function ClubSettingWin:BindEvents()

    function InfoTogChange()
        if self.InfoTog.value==true then
            self.InfoSetCtn.gameObject:SetActive(true)
            self.page = 1
        else
            self.InfoSetCtn.gameObject:SetActive(false)
        end
    end
    self.InfoTog.onChange:Add(UIHelper.CreateEventDelegate(InfoTogChange,self.InfoTog.gameObject))

    function WanfaTogChange()
        if self.WanfaTog.value==true then
            self.page = 2
            self.WanfaSetCtn.gameObject:SetActive(true)
        else
            self.WanfaSetCtn.gameObject:SetActive(false)
        end
    end
    self.WanfaTog.onChange:Add(UIHelper.CreateEventDelegate(WanfaTogChange,self.WanfaTog.gameObject))

    --绑定确认设置按钮
   
    function SetClub()
        local ClubLoobyWin = require("UI.Windows.ClubLoobyWin").ClubLoobyWin
        local ClubModule = require("Module.ClubModule").ClubModule 
        if self.page == 1 then 
            if self.NameInput.value and string.len(self.NameInput.value)>0 then
                ClubModule.ClubOtherSetCm(ClubLoobyWin.id,self.isIp)
                ClubModule.ClubInfoSetCm(ClubLoobyWin.id,self.NameInput.value,self.GongGaoInput.value)
                UIWinMgr:OpenNotice("信息设置成功!!!")
            else 
                UIWinMgr:OpenNotice("请输入正确的俱乐部名称!!!")
            end
        elseif self.page==2 then
            function PlayTypeCreater(playType, playTypeTmp)
                playType.round = playTypeTmp.round
                playType.pType = playTypeTmp.pType
                playType.bankerMode = playTypeTmp.bankerMode
                playType = playTypeTmp
            end
            ClubModule.ClubPlaySetCm(ClubLoobyWin.id,playTypeTmp,PlayTypeCreater)
        end
        self:Close()
    end
    UIHelper.BindUIEvnet("Click", SetClub, self.OKBtn.gameObject)

    --玩法设置
    function eJuUIToggleChange()
        if self.eJuUIToggle.value then
            playTypeTmp.round = 1
            self:ColorToOn(self.eJuUIToggle.transform)
        else
            self:ColorToOff(self.eJuUIToggle.transform)
        end
    end
    self.eJuUIToggle.onChange:Add(UIHelper.CreateEventDelegate(eJuUIToggleChange, self.eJuUIToggle.gameObject))

    function stJuUIToggleChange()
        if self.stJuUIToggle.value then
            playTypeTmp.round = 2
            self:ColorToOn(self.stJuUIToggle.transform)
        else
            self:ColorToOff(self.stJuUIToggle.transform)
        end
    end
    self.stJuUIToggle.onChange:Add(UIHelper.CreateEventDelegate(stJuUIToggleChange, self.stJuUIToggle.gameObject))

    function Bs1TogChange()
        if self.Q_Zhuang.value then
            playTypeTmp.bankerMode = 1
            self:ColorToOn(self.Q_Zhuang.transform)
        else
            self:ColorToOff(self.Q_Zhuang.transform)
        end
    end
    self.Q_Zhuang.onChange:Add(UIHelper.CreateEventDelegate(Bs1TogChange, self.Q_Zhuang.gameObject))

    function Bs2TogChange()
        if self.L_Zhuang.value then
            playTypeTmp.bankerMode = 2
            self:ColorToOn(self.L_Zhuang.transform)
        else
            self:ColorToOff(self.L_Zhuang.transform)
        end
    end
    self.L_Zhuang.onChange:Add(UIHelper.CreateEventDelegate(Bs2TogChange, self.L_Zhuang.gameObject))

    function Bs3TogChange()
        if self.G_Zhuang.value then
            playTypeTmp.bankerMode = 3
            self:ColorToOn(self.G_Zhuang.transform)
        else
            self:ColorToOff(self.G_Zhuang.transform)
        end
    end
    self.G_Zhuang.onChange:Add(UIHelper.CreateEventDelegate(Bs3TogChange, self.G_Zhuang.gameObject))

     function JDTogChange()
        if self.PtypeJD.value then
            playTypeTmp.pType = 1
            self:ColorToOn(self.PtypeJD.transform)
        else
            self:ColorToOff(self.PtypeJD.transform)
        end
    end
    self.PtypeJD.onChange:Add(UIHelper.CreateEventDelegate(JDTogChange, self.PtypeJD.gameObject))

    function FKTogChange()
        if self.PtypeFK.value then
            playTypeTmp.pType = 2
            self:ColorToOn(self.PtypeFK.transform)
        else
            self:ColorToOff(self.PtypeFK.transform)
        end
    end
    self.PtypeFK.onChange:Add(UIHelper.CreateEventDelegate(FKTogChange, self.PtypeFK.gameObject))

    function Close()
        self:Close()
    end
    UIHelper.BindUIEvnet("Click", Close, self.closeButton.gameObject)

    --俱乐部其他设置
    function AiOnBtnChange()
        if self.AiOnBtn.value == true then
            self.isIp = true
        end
    end
    self.AiOnBtn.onChange:Add(UIHelper.CreateEventDelegate(AiOnBtnChange,self.AiOnBtn.gameObject))

    function AiOffBtnChange()
        if self.AiOffBtn.value == true then
            self.isIp = false
        end
    end
    self.AiOffBtn.onChange:Add(UIHelper.CreateEventDelegate(AiOffBtnChange,self.AiOffBtn.gameObject))
end