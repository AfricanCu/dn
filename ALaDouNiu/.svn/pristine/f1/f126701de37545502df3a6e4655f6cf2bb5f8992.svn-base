--提示弹框界面
module("UI.Windows.PromptMaskWin", package.seeall)

local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow

PromptMaskWin = UIWindow:new()

PromptMaskWin.name = "PromptMaskWin" --窗体名字

--初始化界面
function PromptMaskWin:Init()
    local t = self.Container.transform
    self.PromptMaskLabel = UIUtility.FindContorl("UILabel", "PromptMaskLabel", t)
    self.PromptMask = UIUtility.FindContorl("UISprite", "PromptMask", t)

    -- PromptMask
end

function PromptMaskWin:SetTxt(txt)
    self.PromptMaskLabel.text = txt
end

function PromptMaskWin:PlayTween()

    Debug.log("--------调用---")
    local tmpTable = {}
    -- tmpTable.alpha = self.PromptMask.transform:GetComponent("TweenAlpha")
    tmpTable.playTween  = self.PromptMask.gameObject.transform:GetComponent("UIPlayTween")
    local playTween = tmpTable.playTween
    playTween.resetOnPlay = true
    playTween:Play(true)

end