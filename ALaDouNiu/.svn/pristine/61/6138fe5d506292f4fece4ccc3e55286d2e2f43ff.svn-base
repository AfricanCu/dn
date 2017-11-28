--设置窗口
module("UI.Windows.SettingWin", package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
SettingWin = UIWindow:new()
SettingWin.name = "SettingWin" --窗体名字

--初始化界面
function SettingWin:Init()
    --初始化界面，保存引用控件
    local t = self.Container.transform

    self.CloseBtn = UIUtility.GetChildTransform("TuiChu", t, true)

    self.BgsUIToggle = UIUtility.FindContorl("UIToggle", "YingYue", t)
    self.sUIToggle = UIUtility.FindContorl("UIToggle", "YingXiao", t)
    self.TuiChuBtn = UIUtility.GetChildTransform("Logout", t, true)
    self.di = UIUtility.GetChildTransform("m_di", t, true)

    local IosPendingTool = require("IosPendingTool").IosPendingTool
    local isIosPending = IosPendingTool.IsPending()
    if isIosPending then
        Debug.log("ssssssssss")
        self.TuiChuBtn.gameObject:SetActive(false)
    end
    self:BindEvents()
end

function SettingWin:OnShow()

    local GameHost = require("Module.GameModule.GameHost").GameHost
    if GameHost.PlayType ~= nil then
        self.TuiChuBtn.gameObject:SetActive(false)
    else
        if isIosPending then
            Debug.log("ssssssssss")
            self.TuiChuBtn.gameObject:SetActive(false)
        else
            self.TuiChuBtn.gameObject:SetActive(true)
        end
    end
end

function SettingWin:BindEvents()
    local SoundModule = require("Module.SoundModule").SoundModule
    function OnCloseBtnClick()

        self:Close()
    end
    UIHelper.BindUIEvnet("Click", OnCloseBtnClick, self.CloseBtn.gameObject)
    UIHelper.BindUIEvnet("Click", OnCloseBtnClick, self.di.gameObject)
    function BgsUIToggleChange()
        if self.BgsUIToggle.value then
            SoundModule:StopBGM()
        else
            SoundModule:BeginBGM()
        end
    end
    self.BgsUIToggle.onChange:Add(UIHelper.CreateEventDelegate(BgsUIToggleChange, self.BgsUIToggle.gameObject))

    function sUIToggleChange()
        if self.sUIToggle.value then
            SoundModule:SetAllEffectVolume(0)
        else
            SoundModule:SetAllEffectVolume(1)
        end
    end
    function LoginOut()
        if UnityEngine.PlayerPrefs.HasKey("lastSession") then
            UnityEngine.PlayerPrefs.DeleteKey("lastSession")
        end
        if UnityEngine.PlayerPrefs.HasKey("lastClubID") then
            UnityEngine.PlayerPrefs.DeleteKey("lastClubID")
        end
        local NetMgr = require("NetWork").NetMgr
        NetMgr:CloseConnection()
        
        self:Close()
        local GameScene = require("Scene.GameScene").GameScene
        GameScene.isNeedNormalLogin = true
        UIWinMgr:OpenWindow("LoginWin")
        
    end
    self.sUIToggle.onChange:Add(UIHelper.CreateEventDelegate(sUIToggleChange, self.sUIToggle.gameObject))
    function TuiChu()
        UIWinMgr:OpenNotice("您确定要退出登录吗", LoginOut)
    end
    UIHelper.BindUIEvnet("Click", TuiChu, self.TuiChuBtn.gameObject)
end
