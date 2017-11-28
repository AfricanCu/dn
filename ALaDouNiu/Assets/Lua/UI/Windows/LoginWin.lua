--
module("UI.Windows.LoginWin",package.seeall)

local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr

LoginWin = UIWindow:new()

LoginWin.name = "LoginWin" 


function LoginWin:Init()
    local t = self.Container.transform
    self.PC = UIUtility.GetChildTransform("PC",t,true)
    self.LoginButton = UIUtility.GetChildTransform("LoginButton",t,true)
    self.AccountInput = UIUtility.FindContorl('UIInput',"AccountInput",t)

    self.Android = UIUtility.GetChildTransform("Android",t,true)
    self.WXButton = UIUtility.GetChildTransform("WXButton",t,true)

    self.Ios = UIUtility.GetChildTransform("Ios",t,true)
    self.IosWXButton = UIUtility.GetChildTransform("IosWXButton",t,true)

    self.platform = PlatformTool.Instance:GetPlatformID()

    self.WxLoginMask = UIUtility.GetChildTransform("WxLoginMask",t,true)
    self.LoadingMask = UIUtility.GetChildTransform("LoadingMask",t,true)

    Debug.log("deviceID:"..UnityEngine.SystemInfo.deviceUniqueIdentifier)
    
    if self.platform == 1 then
    --android
        self.PC.gameObject:SetActive(false)
        self.Ios.gameObject:SetActive(false)
        self.Android.gameObject:SetActive(true)
    elseif self.platform == 2 then
        self.PC.gameObject:SetActive(false)
        self.Ios.gameObject:SetActive(true)
        self.Android.gameObject:SetActive(false)
    else
    --pc
        self.PC.gameObject:SetActive(true)
        self.Ios.gameObject:SetActive(false)
        self.Android.gameObject:SetActive(false)

        local hasLast = UnityEngine.PlayerPrefs.HasKey("LastPCAccount")
        if hasLast then
            self.AccountInput.value = UnityEngine.PlayerPrefs.GetString("LastPCAccount");
        end
    end

    self:BindEvents()
end

function LoginWin:OnShow()
    self.LoadingMask.gameObject:SetActive(false)
    self:TryOldSessionLogin()
end

function LoginWin:OnClose()
    
end


function LoginWin:BindEvents()
    local LoginModule = require("Module.LoginModule").LoginModule
    
    function GetLoginDataCallBack(jsondata)
        if nil == jsondata then
            self.LoadingMask.gameObject:SetActive(false)
            return
        end
        if 1 ~= jsondata.code then
            self.LoadingMask.gameObject:SetActive(false)
            UIWinMgr:OpenErrorNotice(jsondata.code)
            return
        end

        if nil ~= jsondata.target then
            Debug.log("jsondata.target~~~~~~~~~~~~~~~~~~~~~")
            LoginModule:Login(jsondata.uid,jsondata.target.sessionCode,jsondata.target.loginTime,jsondata.target.serverIp,jsondata.target.port)
            
        else
           
        end 
    end
    self.GetLoginDataCallBack = GetLoginDataCallBack

    function OnLoginBtnClick()
        if not self.AccountInput.value or "" == self.AccountInput.value then
            return
        end
        
        UnityEngine.PlayerPrefs.SetString("LastPCAccount", self.AccountInput.value);
        --UIWinMgr:OpenMask("游戏登陆中...")
        self.LoadingMask.gameObject:SetActive(true)
        LoginModule:GetLoginData(self.AccountInput.value,GetLoginDataCallBack)
    end

    function OnAndroidLogin()
        function GetWXSessionCallBack(WXSession) 
            self.WxLoginMask.gameObject:SetActive(false)
            if nil ~= WXSession then
                --UIWinMgr:OpenMask("游戏登陆中...")
                self.LoadingMask.gameObject:SetActive(true)
                LoginModule:GetLoginData(WXSession,GetLoginDataCallBack)
                --self.SelectLoginType.gameObject:SetActive(false)
                --self.EnterBtn.gameObject:SetActive(false)
            end
        end

        --self.WxLoginMask.gameObject:SetActive(true)

        function CallWxLogin()
            LoginModule:GetWXSession(GetWXSessionCallBack)
        end
        CountDownMgr.Instance:CreateCountDown(0.2, 0.2, CallWxLogin)
    end

    UIHelper.BindUIEvnet("Click", OnLoginBtnClick, self.LoginButton.gameObject)
    UIHelper.BindUIEvnet("Click", OnAndroidLogin, self.WXButton.gameObject)
    UIHelper.BindUIEvnet("Click", OnAndroidLogin, self.IosWXButton.gameObject)
end

function LoginWin:TryOldSessionLogin()
    local LoginModule = require("Module.LoginModule").LoginModule
    --[[function OldSeesionGetCallBack(jsondata)
        if not jsondata then
            
            return
        end
        if 1 == jsondata.code then
            if nil ~= jsondata.target then
                LoginModule:Login(jsondata.uid,jsondata.target.sessionCode,jsondata.target.loginTime,jsondata.target.serverIp,jsondata.target.port)
                
            else
                
            end 
        else
            
           
        end
    end]]--

    local hasLastSession = UnityEngine.PlayerPrefs.HasKey("lastSession")
    if hasLastSession then
        --UIWinMgr:OpenMask("游戏登陆中...")
        self.LoadingMask.gameObject:SetActive(true)
        local lastSession = UnityEngine.PlayerPrefs.GetString("lastSession")
        LoginModule:GetLoginData(lastSession,self.GetLoginDataCallBack,true)
    else
        
    end
end

