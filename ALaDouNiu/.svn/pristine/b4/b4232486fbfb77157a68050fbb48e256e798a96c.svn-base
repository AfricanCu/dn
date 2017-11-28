--回放战绩码界面
module("UI.Windows.GameRecordCodeWin",package.seeall)
local MainUserData = require("DynamicData.MainUserData").MainUserData
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local TimeFormat = require("TimeFormat").TimeFormat

GameRecordCodeWin = UIWindow:new()
GameRecordCodeWin.name = "GameRecordCodeWin" 


function GameRecordCodeWin:Init()
    --初始化界面，保存引用控件
    local t = self.Container.transform
    self.di = UIUtility.GetChildTransform("di",t,true)
    self.PlayVideoButt = UIUtility.GetChildTransform("PlayVideoButt",t,true)
    self.CodeInput = UIUtility.FindContorl('UIInput',"CodeInput",t)

    self:BindEvents()
end


function GameRecordCodeWin:OnShow()


end

function GameRecordCodeWin:OnClose()

end

function GameRecordCodeWin:BindEvents()

    function clickDiAction()
        self:Close()
       
    end
    UIHelper.BindUIEvnet("Click", clickDiAction, self.di.gameObject)


    --播放录像按钮
    function clickPlayVideoButtAction()
    
        UIWinMgr:OpenPromptMaskWin("敬请期待")
        Debug.log("播放按钮")
    end
    UIHelper.BindUIEvnet("Click", clickPlayVideoButtAction, self.PlayVideoButt.gameObject)

end