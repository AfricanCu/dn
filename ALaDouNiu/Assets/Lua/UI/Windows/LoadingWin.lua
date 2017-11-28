--加载窗口
module("UI.Windows.LoadingWin",package.seeall)


local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow

LoadingWin = UIWindow:new()

LoadingWin.name = "LoadingWin" --窗体名字

--初始化界面
function LoadingWin:Init()
    --初始化界面，保存引用控件
    local t = self.Container.transform
    self.LoadBar = UIUtility.FindContorl('UIProgressBar',"LoadBar",t)
end

function LoadingWin:OnShow()
   
end


function LoadingWin:SetProgress(progress)
    --self.LoadBar.value = progress + 0.1
end