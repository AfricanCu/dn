--
module("UI.Windows.ClubRecordWin",package.seeall)
local MainUserData = require("DynamicData.MainUserData").MainUserData
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local TimeFormat = require("TimeFormat").TimeFormat
ClubRecordWin = UIWindow:new()
ClubRecordWin.name = "ClubRecordWin" 

function ClubRecordWin:Init()
    self.Record = {}
    self.clubRecord = {}
    self.myRecord = {}

    local t = self.Container.transform
    self.CloseBtn=UIUtility.GetChildTransform("Close",t,true)
    self.Tog1 = UIUtility.FindContorl('UIToggle',"Tog1",t)
    self.Tog2 = UIUtility.FindContorl('UIToggle',"Tog2",t)
    self.ShowPanel = UIUtility.GetChildTransform("ShowPanel",t,true)
    local UIScrollView = require("UI.Com.UIScrollView").UIScrollView
    self.ShowScrollView = UIScrollView:new()
    self.ShowScrollView:Init(self.ShowPanel)
    self:BindEvents() 
end

function ClubRecordWin:OnShow()
    --self.Record = MainUserData.BattleRecord
    if (#self.Record <1 or self.Record == nil) and (#self.clubRecord <1 or self.clubRecord == nil) then
        self:Close()
        UIWinMgr:OpenNotice("您在过去10小时内无游戏记录，点击确定返回")
        return
    end
    --self.myRecord = self.Record
    --Debug.log(#self.Record.."sdadadadadwqdqdadasdadafsfaffff1111")
    --ClubRecordWin:ShowRecord()
    --if 
end

function ClubRecordWin:BindEvents()
    function Close()
        self:Close()
    end
    UIHelper.BindUIEvnet("Click", Close, self.CloseBtn.gameObject)

    function Tog1Change()
        if self.Tog1.value == true then
        
            self.myRecord = self.Record
            self.index = 1
            --Debug.log("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm")
            ClubRecordWin:ShowRecord()
        end
    end
    self.Tog1.onChange:Add(UIHelper.CreateEventDelegate(Tog1Change,self.Tog1.gameObject))

    function Tog2Change()
        if self.Tog2.value==true then
            self.myRecord = self.clubRecord
            self.index = 2
            --Debug.log("ccccccccccccccccccccccccccccccccccccccccccc")
            ClubRecordWin:ShowRecord()   
        end
    end
    self.Tog2.onChange:Add(UIHelper.CreateEventDelegate(Tog2Change,self.Tog2.gameObject))
end

function ClubRecordWin:GameRecordSm(recordSm)
    self:Close()
    UIWinMgr:CloseMask()
    local MoreRecordWin = UIWinMgr:GetWindow("MoreRecordWin")
    MoreRecordWin.PlayerName = self.myRecord[self.reCordLen+1-self.ItemNum].nickname
    MoreRecordWin.Round = recordSm.round
    MoreRecordWin.Index = recordSm.index
    MoreRecordWin:Show()
end

function ClubRecordWin:ShowRecord()
    self.ShowScrollView:DestroyItems()
    self.reCordLen = #self.myRecord
    local num = #self.myRecord
    if num>10 then
        num= 10
    end
    if self.myRecord and #self.myRecord>0 then
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
                local PlayerScore5=UIUtility.FindContorl('UILabel',"PlayerScore4",obj.transform)
                ChangShuLabel.text="第"..i.."场"
                RoomNumLabel.text=self.myRecord[self.reCordLen+1-i].roomId
                local tiemTab = os.date("*t", self.myRecord[self.reCordLen+1-i].time/1000)
                TimeLabel.text = tiemTab["year"].."-"..tiemTab["month"].."-"..tiemTab["day"].."   "..tiemTab["hour"]..":"..tiemTab["min"]
                PlayerName1.text=self.myRecord[self.reCordLen+1-i].nickname[1]
                PlayerName2.text=self.myRecord[self.reCordLen+1-i].nickname[2]
                PlayerName3.text=self.myRecord[self.reCordLen+1-i].nickname[3]
                PlayerName4.text=self.myRecord[self.reCordLen+1-i].nickname[4]
                PlayerName5.text=self.myRecord[self.reCordLen+1-i].nickname[5]
                PlayerScore1.text=self.myRecord[self.reCordLen+1-i].coin[1]
                PlayerScore2.text=self.myRecord[self.reCordLen+1-i].coin[2]
                PlayerScore3.text=self.myRecord[self.reCordLen+1-i].coin[3]
                PlayerScore4.text=self.myRecord[self.reCordLen+1-i].coin[4]
                PlayerScore5.text=self.myRecord[self.reCordLen+1-i].coin[5]
                function ShowRound()
                    self.ItemNum = i
                    local NetMgr = require("NetWork").NetMgr
                    local GameRecordCm = require("Protol.loginMessage_pb").GameRecordCm()
                    GameRecordCm.index = self.myRecord[self.reCordLen+1-i].index
                    NetMgr:SendMsg(9,GameRecordCm)
                    UIWinMgr:OpenMask("数据加载中...")
                end
                UIHelper.BindUIEvnet("Click",ShowRound,obj.gameObject)
            end
            self.ShowScrollView:Add(OnAdd)
        end
    end
end

