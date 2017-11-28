--设置窗口
module("UI.Windows.HelpWin", package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
HelpWin = UIWindow:new()
HelpWin.name = "HelpWin" --窗体名字
function HelpWin:Init()
    local t = self.Container.transform
    self.tuichu = UIUtility.GetChildTransform("tuichu", t, true)
    self.di = UIUtility.GetChildTransform("di", t, true)
    self:BindEvents()
end
function HelpWin:BindEvents()
    function Close()
        self:Close()
    end
    UIHelper.BindUIEvnet("Click", Close, self.tuichu.gameObject)
    UIHelper.BindUIEvnet("Click", Close, self.di.gameObject)
end
