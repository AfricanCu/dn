--
module("UI.Windows.ProxyGameRecWin",package.seeall)
local MainUserData = require("DynamicData.MainUserData").MainUserData
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local TimeFormat = require("TimeFormat").TimeFormat

ProxyGameRecWin = UIWindow:new()
ProxyGameRecWin.name = "ProxyGameRecWin" 

function ProxyGameRecWin:Init()
    local t = self.Container.transform
    self.CloseBtn=UIUtility.GetChildTransform("Close",t,true)
    self.ShowPanel=UIUtility.GetChildTransform("ShowPanel",t,true)
    local UIScrollView = require("UI.Com.UIScrollView").UIScrollView
    self.ShowScrollView = UIScrollView:new()
    self.ShowScrollView:Init(self.ShowPanel)
    self:BindEvents() 
end

function ProxyGameRecWin:OnShow()
    self.Record = MainUserData.ProxyRecord
    --Debug.log(#self.Record.."sdadadadadwqdqdadasdadafsfaffff1111")
    self:ShowRecord()
end

function ProxyGameRecWin:BindEvents()
    function Close()
        self:Close()
    end
    UIHelper.BindUIEvnet("Click", Close, self.CloseBtn.gameObject)
end
--[[function ProxyGameRecWin:GameRecordSm(recordSm)
    Debug.log("xxxxxxxxxxxxxxxxxxxxxxxxsssss")
    self:Close()
    UIWinMgr:CloseMask()
    local MoreRecordWin = UIWinMgr:GetWindow("MoreRecordWin")
    MoreRecordWin.PlayerName = self.Record[self.reCordLen+1-self.ItemNum].nickname
    MoreRecordWin.Round = recordSm.round
    MoreRecordWin.Index = recordSm.index
    MoreRecordWin:Show()
end]]--

function ProxyGameRecWin:ShowRecord()
    self.ShowScrollView:DestroyItems()
    self.reCordLen = #self.Record
    local num = #self.Record
    if num>10 then
        num= 10
    end
    if self.Record and #self.Record>0 then
        for i=1, num do
            function OnAdd(obj)
                local ChangShuLabel=UIUtility.FindContorl('UILabel',"ChangShu",obj.transform)
                local RoomNumLabel=UIUtility.FindContorl('UILabel',"RoomNum",obj.transform)
                local TimeLabel=UIUtility.FindContorl('UILabel',"TimeLabel",obj.transform)
                local PlayerName1=UIUtility.FindContorl('UILabel',"PlayerName1",obj.transform)
                local PlayerName2=UIUtility.FindContorl('UILabel',"PlayerName2",obj.transform)
                local PlayerName3=UIUtility.FindContorl('UILabel',"PlayerName3",obj.transform)
                local PlayerName4=UIUtility.FindContorl('UILabel',"PlayerName4",obj.transform)
                local PlayerName5=UIUtility.FindContorl('UILabel',"PlayerName5",obj.transform)
                local PlayerScore1=UIUtility.FindContorl('UILabel',"PlayerScore1",obj.transform)
                local PlayerScore2=UIUtility.FindContorl('UILabel',"PlayerScore2",obj.transform)
                local PlayerScore3=UIUtility.FindContorl('UILabel',"PlayerScore3",obj.transform)
                local PlayerScore4=UIUtility.FindContorl('UILabel',"PlayerScore4",obj.transform)
                local PlayerScore5=UIUtility.FindContorl('UILabel',"PlayerScore5",obj.transform)
                ChangShuLabel.text="第"..i.."场"
                RoomNumLabel.text=self.Record[self.reCordLen+1-i].roomId
                local tiemTab = os.date("*t", self.Record[self.reCordLen+1-i].time/1000)
                TimeLabel.text = tiemTab["year"].."-"..tiemTab["month"].."-"..tiemTab["day"].."   "..tiemTab["hour"]..":"..tiemTab["min"]
                PlayerName1.text=self.Record[self.reCordLen+1-i].nickname[1]
                PlayerName2.text=self.Record[self.reCordLen+1-i].nickname[2]
                PlayerName3.text=self.Record[self.reCordLen+1-i].nickname[3]
                PlayerName4.text=self.Record[self.reCordLen+1-i].nickname[4]
                PlayerName5.text=self.Record[self.reCordLen+1-i].nickname[5]
                PlayerScore1.text=self.Record[self.reCordLen+1-i].coin[1]
                PlayerScore2.text=self.Record[self.reCordLen+1-i].coin[2]
                PlayerScore3.text=self.Record[self.reCordLen+1-i].coin[3]
                PlayerScore4.text=self.Record[self.reCordLen+1-i].coin[4]
                PlayerScore5.text=self.Record[self.reCordLen+1-i].coin[5]
                --[[function ShowRound()
                    self.ItemNum = i
                    local NetMgr = require("NetWork").NetMgr
                    local GameRecordCm = require("Protol.loginMessage_pb").GameRecordCm()
                    GameRecordCm.index = self.Record[self.reCordLen+1-i].index
                    Debug.log(GameRecordCm.index.."cscscsscs")
                    NetMgr:SendMsg(9,GameRecordCm)
                    UIWinMgr:OpenMask("数据加载中...")
                end
                UIHelper.BindUIEvnet("Click",ShowRound,obj.gameObject)]]--
            end
            self.ShowScrollView:Add(OnAdd)
        end
    else
        self:Close()
        UIWinMgr:OpenNotice("您在过去10小时内无游戏记录，点击确定返回") ------ 
    end
end