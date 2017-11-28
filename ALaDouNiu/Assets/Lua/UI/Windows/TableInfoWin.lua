--
module("UI.Windows.TableInfoWin",package.seeall)
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local TimeFormat = require("TimeFormat").TimeFormat
TableInfoWin = UIWindow:new()
TableInfoWin.name = "TableInfoWin" 

TableInfoWin.Players = nil

function TableInfoWin:Init()
    local t = self.Container.transform
    self.CloseBtn=UIUtility.GetChildTransform("Close",t,true)
    self.Grids=UIUtility.GetChildTransform("Grids",t,true)
    self:BindEvents() 
end

function TableInfoWin:OnShow()
    self:ShowPlayers()   
end

function TableInfoWin:BindEvents()
    function Close()
        self:Close()
    end
    UIHelper.BindUIEvnet("Click", Close, self.CloseBtn.gameObject)

end

function TableInfoWin:ShowPlayers()
    for i=1, self.Grids.childCount do
        self.Grids:GetChild(i-1).gameObject:SetActive(false)
    end

    if self.Players and #self.Players>0 then
        for i=1, #self.Players do
            local Touxiang=UIUtility.FindContorl('UITextrue',"ChangShu",self.Grids:GetChild(i-1))
            local NameLabel=UIUtility.FindContorl('UILabel',"NameLabel",self.Grids:GetChild(i-1))
            local IDLabel =UIUtility.FindContorl('UILabel',"IDLabel",self.Grids:GetChild(i-1))
            local IPLabel=UIUtility.FindContorl('UILabel',"IPLabel",self.Grids:GetChild(i-1))
            local TouXiangSp=UIUtility.FindContorl('UITexture',"HeadPortrait",self.Grids:GetChild(i-1))
            NameLabel.text=self.Players[i].nickname
            IDLabel.text=self.Players[i].uid
            IPLabel.text=self.Players[i].ip
            function callBack(isOk,www)
                if isOk then
                    if TouXiangSp ~= nil then
                        TouXiangSp.mainTexture = www.texture
                    end
                end
            end
            require("NetWork.NetHttp").WWWTexture(self.Players[i].headimg,callBack)
            self.Grids:GetChild(i-1).gameObject:SetActive(true)
          
        end
    end
end

