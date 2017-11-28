--ios审核登录界面
module("UI.Windows.IosLoginWin",package.seeall)

local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr

IosLoginWin = UIWindow:new()

IosLoginWin.name = "IosLoginWin" 



function IosLoginWin:Init()
    local t = self.Container.transform

    self.LoginButton = UIUtility.GetChildTransform("Login_button",t,true)
    -- self.S_State = UIUtility.FindContorl('UISprite',"S_State",t)
    -- self.S_ChgArea = UIUtility.GetChildTransform("S_ChgArea",t,true)
    -- self.L_City = UIUtility.FindContorl('UILabel',"L_City",t)

    self.platform = PlatformTool.Instance:GetPlatformID()

    -- self.LoadingMask = UIUtility.GetChildTransform("LoadingMask",t,true)
    
    self:BindEvents()
end



function IosLoginWin:OnShow()
    -- self.LoadingMask.gameObject:SetActive(false)
    -- local MapModule = require("Module.MapModule").MapModule
    -- local areaName = MapModule:GetLastAreaName()
    -- if areaName ~= nil then
    --     self.L_City.text = areaName
    -- end
    self:TryOldSessionLogin()
end

function IosLoginWin:OnClose()
    
end

function IosLoginWin:BindEvents()
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

        Debug.log("登录-------")
        UIWinMgr:OpenMask("游戏登陆中...")
        -- self.LoadingMask.gameObject:SetActive(true)
        LoginModule:GetLoginData(UnityEngine.SystemInfo.deviceUniqueIdentifier,GetLoginDataCallBack)
    end

    -- function ChangeArea()
    --     self:Close()
    --     --UIWinMgr:CloseWindow("LoginDoorWin")
    --     local MapModule = require("Module.MapModule").MapModule
    --     MapModule:CloseDoor()
    --     MapModule:ShowMap(true)
    -- end

    UIHelper.BindUIEvnet("Click", OnLoginBtnClick, self.LoginButton.gameObject)
    -- UIHelper.BindUIEvnet("Click", ChangeArea, self.S_ChgArea.gameObject)
end


function IosLoginWin:TryOldSessionLogin()
    local LoginModule = require("Module.LoginModule").LoginModule
    local hasLastSession = UnityEngine.PlayerPrefs.HasKey("lastSession")
    if hasLastSession then
        UIWinMgr:OpenMask("游戏登陆中...")
        -- self.LoadingMask.gameObject:SetActive(true)
        local lastSession = UnityEngine.PlayerPrefs.GetString("lastSession")
        LoginModule:GetLoginData(lastSession,self.GetLoginDataCallBack,true)
    else
        
    end
end


