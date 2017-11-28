module("UI.Windows.MaskWin", package.seeall)

local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow

MaskWin = UIWindow:new()

MaskWin.name = "MaskWin" --窗体名字

--初始化界面
function MaskWin:Init()
    local t = self.Container.transform
    self.MaskLabel = UIUtility.FindContorl("UILabel", "MaskLabel", t)
end

function MaskWin:SetTxt(txt)
    self.MaskLabel.text = txt
end
