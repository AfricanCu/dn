--小结算窗口
module("UI.Windows.XiaoJieSuanWin",package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
XiaoJieSuanWin = UIWindow:new()
XiaoJieSuanWin.name = "XiaoJieSuanWin" --窗体名字

--初始化界面
function XiaoJieSuanWin:Init()
    --初始化界面，保存引用控件
    local t = self.Container.transform
    self.shengli = UIUtility.FindContorl('SpineCtrl',"shengli",t)
    self.shibai = UIUtility.FindContorl('SpineCtrl',"shibai",t)
    self.queding = UIUtility.GetChildTransform("queding",t,true)
    self.Grid = UIUtility.GetChildTransform("Grid",t,true)
    self.di = UIUtility.GetChildTransform("di",t,true)
    
    self.UIPLayers = {}
    local count = self.Grid.childCount - 1
    for i = 0, count do
        local root = self.Grid:GetChild(i)
        local tmpTable = {}
        tmpTable.root = root
        tmpTable.Name1 = UIUtility.FindContorl("UILabel", "Name1", root.transform)
        tmpTable.paixing = UIUtility.FindContorl("UILabel", "paixing", root.transform)
        tmpTable.jifen = UIUtility.FindContorl("UILabel", "jifen", root.transform)
        tmpTable.zhuang = UIUtility.GetChildTransform("zhuang",root.transform,true)
        tmpTable.card = {}
        tmpTable.card[1] = UIUtility.FindContorl("UISprite", "1", root.transform)
        tmpTable.card[2] = UIUtility.FindContorl("UISprite", "2", root.transform)
        tmpTable.card[3] = UIUtility.FindContorl("UISprite", "3", root.transform)
        tmpTable.card[4] = UIUtility.FindContorl("UISprite", "4", root.transform)
        tmpTable.card[5] = UIUtility.FindContorl("UISprite", "5", root.transform)
        self.UIPLayers[i + 1] = tmpTable
    end


    self:BindEvents()
end

function XiaoJieSuanWin:OnShow()
    
end

function XiaoJieSuanWin:SetInfo(BullResultCast)
    local GameHost = require("Module.GameModule.GameHost").GameHost
    local tmp = 0
    local isWin = true
    for k,v in ipairs(BullResultCast.rs) do
        local player = GameHost:GetPlayer(v.seetIndex)
        tmp = tmp + 1
        local uiplayer = self.UIPLayers[tmp]
        uiplayer.root.gameObject:SetActive(true)
        if v.changeCoin >= 0 then
            uiplayer.jifen.text = "+"..v.changeCoin
            uiplayer.jifen.color = Color(255/255,250/255,166/255)
        else
            uiplayer.jifen.text = tostring(v.changeCoin)
            uiplayer.jifen.color = Color(173/255,22/255,3/255)
        end

        if v.seetIndex == GameHost.myIndex then
            if v.changeCoin >= 0 then
                isWin = true
            else
                isWin = false
            end
        end

        if v.seetIndex == GameHost.bankerIndex then
            uiplayer.zhuang.gameObject:SetActive(true)
        else
            uiplayer.zhuang.gameObject:SetActive(false)
        end
        uiplayer.Name1.text = player.userInfo.nickname

        local rs = BullResultCast.rs[tmp]
        local niuType = GameHost.NiuTypeEnum[tostring(rs.niu)]
        uiplayer.paixing.text = niuType

        local tmpPai = {}
        if rs.niu >= 1 and rs.niu <= 10 then
            tmpPai = {1,2,3,4,5}
        else
            local tmp = {1,2,3,4,5}
            tmpPai = rs.niuIndex
            for k,v in ipairs(tmp) do
                if v ~= tmpPai[1] and v ~= tmpPai[2] and v ~= tmpPai[3] then
                    table.insert(tmpPai, v)
                end
            end
        end

        for i=1,#tmpPai do
            local puke = rs.puke[tmpPai[i]]
            uiplayer.card[i].spriteName = "pkc_s_"..puke.num.."0"..puke.type
        end
    end

    local allUsers = table.getn(BullResultCast.rs)
    for i= allUsers+1,5 do
        self.UIPLayers[i].root.gameObject:SetActive(false)
    end

    if isWin then
        self.shengli.gameObject:SetActive(true)
        self.shibai.gameObject:SetActive(false)
        self.shengli:DoAni("shengli", false, false)

        local SoundModule = require("Module.SoundModule").SoundModule
        SoundModule:PlaySound("GameSound_win")
    else
        self.shengli.gameObject:SetActive(false)
        self.shibai.gameObject:SetActive(true)
        self.shibai:DoAni("shibai", false, false)

        local SoundModule = require("Module.SoundModule").SoundModule
        SoundModule:PlaySound("GameSound_lose")
    end
end

function XiaoJieSuanWin:BindEvents()
    
    function OnCloseBtnClick()
        local NetMgr = require("NetWork").NetMgr
        local Msg = require("Protol.bullMessage_pb").NextRoundCm()
        NetMgr:SendMsg(521,Msg)
        
        local GameHost = require("Module.GameModule.GameHost").GameHost
        GameHost:ClearPai()
        self:Close()
    end
    UIHelper.BindUIEvnet("Click",OnCloseBtnClick,self.queding.gameObject)
    UIHelper.BindUIEvnet("Click",OnCloseBtnClick,self.di.gameObject)
end
