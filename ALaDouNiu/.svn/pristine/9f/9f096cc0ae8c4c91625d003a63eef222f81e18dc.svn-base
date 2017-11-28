--
module("UI.Windows.CreateClubWin", package.seeall)
local UICreateRoomColor = require("UI.Com.UICreateRoomColor").UICreateRoomColor
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local json = require("cjson")
CreateClubWin = UIWindow:new()

CreateClubWin.name = "CreateClubWin" --窗体名字
local type = {}

--初始化界面
function CreateClubWin:Init()
    --初始化界面，保存引用控件
    local t = self.Container.transform

    self.crButton = UIUtility.GetChildTransform("m_Determine", t, true)
    self.closeButton = UIUtility.GetChildTransform("m_Cancel", t, true)
    self.eJuUIToggle = UIUtility.FindContorl("UIToggle", "ShiJu", t)
    --10局
    self.stJuUIToggle = UIUtility.FindContorl("UIToggle", "ErShiJu", t)
    --20局
    self.PtypeJD = UIUtility.FindContorl("UIToggle", "JingDianNiuNiu", t)
    --经典
    self.PtypeFK = UIUtility.FindContorl("UIToggle", "FengKuangJiaBei", t)
    --疯狂
    self.Q_Zhuang = UIUtility.FindContorl("UIToggle", "QianZhuang", t)
    --抢庄
    self.L_Zhuang = UIUtility.FindContorl("UIToggle", "LunZhuang", t)
    --轮庄
    self.G_Zhuang = UIUtility.FindContorl("UIToggle", "GuDingZhuang", t)
    --固定庄
    local x = UnityEngine.PlayerPrefs.GetString("Club2Type", "")
    if x == "" then
        type.round = 2
        type.pType = 2
        type.bankerMode = 2
    else
        local jsonData = json.decode(x)
        type.round = jsonData.round
        type.pType = jsonData.pType
        type.bankerMode = jsonData.bankerMode
    end

    self:ShowWin()
    self:BindEvents()
end

function CreateClubWin:ColorToOn(transform)
    local wsxColorCom = UICreateRoomColor:new()
    wsxColorCom:Init(transform)
    wsxColorCom:On()
end

function CreateClubWin:ColorToOff(transform)
    local wsxColorCom = UICreateRoomColor:new()
    wsxColorCom:Init(transform)
    wsxColorCom:Off()
end

function CreateClubWin:ShowWin()
    if type.round == 1 then
        self.eJuUIToggle.value = true
    else
        self.stJuUIToggle.value = true
    end
    if type.bankerMode == 1 then
        self.Q_Zhuang.value = true
    elseif type.bankerMode == 2 then
        self.L_Zhuang.value = true
    elseif type.bankerMode == 3 then
        self.G_Zhuang.value = true
    end
    if type.pType == 1 then
        self.PtypeJD.value = true
    else
        self.PtypeFK.value = true
    end
end

function CreateClubWin:OnShow()
end

function CreateClubWin:BindEvents()
    function PlayTypeCreater(playType, type)
        playType.bankerMode = type.bankerMode
        playType.pType = type.pType
        playType.round = type.round
        playType = type
        local gameType = json.encode(type)
        UnityEngine.PlayerPrefs.SetString("Club2Type", gameType)
    end

    function CreateRoom()
        self:Close()
        UIWinMgr:CloseWindow("ClubManageWin")
        local ClubModule = require("Module.ClubModule").ClubModule
        ClubModule.CreateClub(type, PlayTypeCreater)
    end
    UIHelper.BindUIEvnet("Click", CreateRoom, self.crButton.gameObject)

    function eJuUIToggleChange()
        if self.eJuUIToggle.value then
            type.round = 1
            self:ColorToOn(self.eJuUIToggle.transform)
        else
            self:ColorToOff(self.eJuUIToggle.transform)
        end
    end
    self.eJuUIToggle.onChange:Add(UIHelper.CreateEventDelegate(eJuUIToggleChange, self.eJuUIToggle.gameObject))

    function stJuUIToggleChange()
        if self.stJuUIToggle.value then
            type.round = 2
            self:ColorToOn(self.stJuUIToggle.transform)
        else
            self:ColorToOff(self.stJuUIToggle.transform)
        end
    end
    self.stJuUIToggle.onChange:Add(UIHelper.CreateEventDelegate(stJuUIToggleChange, self.stJuUIToggle.gameObject))

    function Bs1TogChange()
        if self.Q_Zhuang.value then
            type.bankerMode = 1
            self:ColorToOn(self.Q_Zhuang.transform)
        else
            self:ColorToOff(self.Q_Zhuang.transform)
        end
    end
    self.Q_Zhuang.onChange:Add(UIHelper.CreateEventDelegate(Bs1TogChange, self.Q_Zhuang.gameObject))

    function Bs2TogChange()
        if self.L_Zhuang.value then
            type.bankerMode = 2
            self:ColorToOn(self.L_Zhuang.transform)
        else
            self:ColorToOff(self.L_Zhuang.transform)
        end
    end
    self.L_Zhuang.onChange:Add(UIHelper.CreateEventDelegate(Bs2TogChange, self.L_Zhuang.gameObject))

    function Bs3TogChange()
        if self.G_Zhuang.value then
            type.bankerMode = 3
            self:ColorToOn(self.G_Zhuang.transform)
        else
            self:ColorToOff(self.G_Zhuang.transform)
        end
    end
    self.G_Zhuang.onChange:Add(UIHelper.CreateEventDelegate(Bs3TogChange, self.G_Zhuang.gameObject))

     function JDTogChange()
        if self.PtypeJD.value then
            type.pType = 1
            self:ColorToOn(self.PtypeJD.transform)
        else
            self:ColorToOff(self.PtypeJD.transform)
        end
    end
    self.PtypeJD.onChange:Add(UIHelper.CreateEventDelegate(JDTogChange, self.PtypeJD.gameObject))

    function FKTogChange()
        if self.PtypeFK.value then
            type.pType = 2
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
end
