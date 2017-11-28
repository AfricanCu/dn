--
module("UI.Windows.MoreRecordWin",package.seeall)

local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local NetMgr = require("NetWork").NetMgr
MoreRecordWin = UIWindow:new()
MoreRecordWin.name = "MoreRecordWin"
MoreRecordWin.PlayerName={}
MoreRecordWin.Index=nil
MoreRecordWin.Round={}
function MoreRecordWin:Init()
    local t = self.Container.transform
    self.HuiFangBtn=UIUtility.GetChildTransform("HuiFangBtn",t,true)
    self.ShowPanel=UIUtility.GetChildTransform("ShowPanel",t,true)
    local UIScrollView = require("UI.Com.UIScrollView").UIScrollView
    self.ShowScrollView = UIScrollView:new()
    self.ShowScrollView:Init(self.ShowPanel)
    self.CloseBtn=UIUtility.GetChildTransform("Close",t,true)
    self.Player1Label=UIUtility.FindContorl('UILabel',"PlayerName1",t)
    self.Player2Label=UIUtility.FindContorl('UILabel',"PlayerName2",t)
    self.Player3Label=UIUtility.FindContorl('UILabel',"PlayerName3",t)
    self.Player4Label=UIUtility.FindContorl('UILabel',"PlayerName4",t)
    self.Player5Label=UIUtility.FindContorl('UILabel',"PlayerName5",t)
    self:BindEvents()
end

function MoreRecordWin:OnShow()
     MoreRecordWin:ShowRound()
end

function MoreRecordWin:BindEvents()
    function FindRecord()
        local HuiFangWin = UIWinMgr:OpenWindow("HuiFangWin")
    end
    function Close()
        self:Close()
    end
    UIHelper.BindUIEvnet("Click", FindRecord, self.HuiFangBtn.gameObject)
    UIHelper.BindUIEvnet("Click", Close, self.CloseBtn.gameObject)
end

function MoreRecordWin:ShowRound()
    self.ShowScrollView:DestroyItems()
    if self.Round then
        self.Player1Label.text=self.PlayerName[1]
        self.Player2Label.text=self.PlayerName[2]
        self.Player3Label.text=self.PlayerName[3]
        self.Player4Label.text=self.PlayerName[4]
        self.Player5Label.text=self.PlayerName[5]
        for i=1,#self.Round do
            function OnAdd(obj)
                local TimeLabel=UIUtility.FindContorl('UILabel',"Time",obj.transform)
                local PlayerScore1=UIUtility.FindContorl('UILabel',"PlayerScore1",obj.transform)
                local PlayerScore2=UIUtility.FindContorl('UILabel',"PlayerScore2",obj.transform)
                local PlayerScore3=UIUtility.FindContorl('UILabel',"PlayerScore3",obj.transform)
                local PlayerScore4=UIUtility.FindContorl('UILabel',"PlayerScore4",obj.transform)
                local PlayerScore5=UIUtility.FindContorl('UILabel',"PlayerScore5",obj.transform)
                local cLabel=UIUtility.FindContorl('UILabel',"cLabel",obj.transform,true)
                local FenXiangBtn=UIUtility.GetChildTransform("FenXiang",obj.transform,true)
                local HuiKanBtn=UIUtility.GetChildTransform("HuiKan",obj.transform,true)
                cLabel.text=i
                local tiemTab = os.date("*t", self.Round[i].time/1000)
                TimeLabel.text = tiemTab["year"].."-"..tiemTab["month"].."-"..tiemTab["day"].."   "..tiemTab["hour"]..":"..tiemTab["min"]
                PlayerScore1.text=self.Round[i].coin[1]
                PlayerScore2.text=self.Round[i].coin[2]
                PlayerScore3.text=self.Round[i].coin[3]
                PlayerScore4.text=self.Round[i].coin[4]
                PlayerScore5.text=self.Round[i].coin[5]
                function FenXClick()
                    self:Close()
                end
                function HuiKBtn()
                    local ReplayModule = require("Module.ReplayModule").ReplayModule
                    ReplayModule:BattleBackCm(self.Round[i].battlebackIndex)
                end
                --Debug.log(FenXiangBtn.name)
                UIHelper.BindUIEvnet("Click", FenXClick, FenXiangBtn.gameObject)
                UIHelper.BindUIEvnet("Click", HuiKBtn, HuiKanBtn.gameObject)
            end
            self.ShowScrollView:Add(OnAdd)
        end
    end
end