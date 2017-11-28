--商城界面
module("UI.Windows.ShopWin", package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIWindow = require("UI.UIWindow").UIWindow
local UIUtility = require("UI.Utility")
local GameObject = UnityEngine.GameObject
local NetMgr = require("NetWork").NetMgr

ShopWin = UIWindow:new()
ShopWin.name = "ShopWin"


--数量，赠送数量，价格
ShopWin.IosPengdingPrice = {
    [1] = "1,0,6",
    [2] = "5,0,30",
    [3] = "10,0,50",
    [4] = "17,0,88",
    [5] = "55,18,328",
    [6] = "98,33,588"
}

ShopWin.NormalPrice = {
    [1] = "1,0,6",
    [2] = "5,0,30",
    [3] = "10,0,50",
    [4] = "17,0,88",
    [5] = "55,18,328",
    [6] = "98,33,588"
}


function ShopWin:Init()

        --初始化界面，保存引用控件
    local t = self.Container.transform

    self.FKBut_1 = UIUtility.GetChildTransform("FKBut_1", t, true)
    self.FKBut_2 = UIUtility.GetChildTransform("FKBut_2", t, true)
    self.FKBut_3 = UIUtility.GetChildTransform("FKBut_3", t, true)
    self.FKBut_4 = UIUtility.GetChildTransform("FKBut_4", t, true)
    self.Close_But = UIUtility.GetChildTransform("Close_But", t, true)

    self:BindEvents()
    self:BindHander()
end


function ShopWin:BindEvents()
    

    --一张房卡
    function clickFkButton_1Action()

        self:pay_Action(1)
        Debug.log("一张房卡")
        
    end
    UIHelper.BindUIEvnet("Click", clickFkButton_1Action, self.FKBut_1.gameObject)

        --六张房卡
    function clickFkButton_2Action()
            
        self:pay_Action(2)

        Debug.log("六张房卡")
                    
    end           
    UIHelper.BindUIEvnet("Click", clickFkButton_2Action, self.FKBut_2.gameObject)

    
        --十张房卡
    function clickFkButton_3Action()
            
        Debug.log("十张房卡")

        self:pay_Action(3)
                    
    end           
    UIHelper.BindUIEvnet("Click", clickFkButton_3Action, self.FKBut_3.gameObject)


    --十七张房卡
    function clickFkButton_4Action()
            
        Debug.log("十七张房卡")
        self:pay_Action(4)
                    
    end           
    UIHelper.BindUIEvnet("Click", clickFkButton_4Action, self.FKBut_4.gameObject)      
    
    function clickCloseButtonAction()

        self:Close()

        -- UIWinMgr:CloseWindow("ShopWin")
    end
    UIHelper.BindUIEvnet("Click", clickCloseButtonAction, self.Close_But.gameObject)  

end


function ShopWin:pay_Action(priceID)

    local platform = PlatformTool.Instance:GetPlatformID()
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr

    if platform == 3 then
        return
    end

    local IosPendingTool = require("IosPendingTool").IosPendingTool
    local isIosPending = IosPendingTool.IsPending()
    if isIosPending then
        self.priceTable = self.IosPengdingPrice
    else
        self.priceTable = self.NormalPrice
    end

    Debug.log(self.priceTable[priceID])

    local StringTool = require("StringTool").StringTool
    local priceParams = StringTool:Split(self.priceTable[priceID], ",")
    local num = tonumber(priceParams[1]) + tonumber(priceParams[2])

    function callBack(code)
        Debug.log("-----------------------------------------------------")
        Debug.log(code)
        UIWinMgr:CloseMask()
        if code == "0" then
            UIWinMgr:OpenNotice("充值成功")
        elseif code == "1" then
            UIWinMgr:OpenNotice("充值取消或失败")
        end
    end

    UIWinMgr:OpenMask("充值中...")
    local PayTool = require("PayTool").PayTool
    
    Debug.log("购买ID："..priceID.."房卡数量"..num.."价格"..priceParams[3])
    PayTool.Pay(priceID,num, priceParams[3],callBack)

end


function ShopWin:BindHander()
    -- body
end



function ShopWin:OnShow()
 
end

function ShopWin:OnClose()

end