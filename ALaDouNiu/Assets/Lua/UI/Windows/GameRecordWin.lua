--
module("UI.Windows.GameRecordWin",package.seeall)
local MainUserData = require("DynamicData.MainUserData").MainUserData
local UIUtility = require("UI.Utility")
local UIWindow = require("UI.UIWindow").UIWindow
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local TimeFormat = require("TimeFormat").TimeFormat

GameRecordWin = UIWindow:new()
GameRecordWin.name = "GameRecordWin" 

function GameRecordWin:Init()
    local t = self.Container.transform
    self.QueryShareVideo = UIUtility.GetChildTransform("QueryShareVideo",t,true)
    self.PromptLabel = UIUtility.GetChildTransform(" PromptLabel",t,true)
    self.ShowPanel = UIUtility.GetChildTransform("ShowPanel",t,true)
    local UIScrollView = require("UI.Com.UIScrollView").UIScrollView
    self.ShowScrollView = UIScrollView:new()
    self.ShowScrollView:Init(self.ShowPanel)
    self.di = UIUtility.GetChildTransform("di",t,true)

    self:BindEvents() 
end

function GameRecordWin:OnShow()
    self.Record = MainUserData.BattleRecord
    --Debug.log(#self.Record.."sdadadadadwqdqdadasdadafsfaffff1111")
    GameRecordWin:ShowRecord()
end

function GameRecordWin:BindEvents()
    function Close()
        self:Close()
    end
    
    UIHelper.BindUIEvnet("Click", Close, self.di.gameObject)

    function clickQueryShareVideoBut()

        
        -- UIWinMgr:OpenPromptMaskWin("敬请期待")
        self:Close()
        UIWinMgr:OpenWindow("GameRecordCodeWin")
        
    end
    UIHelper.BindUIEvnet("Click", clickQueryShareVideoBut, self.QueryShareVideo.gameObject)

end
function GameRecordWin:GameRecordSm(recordSm)
    Debug.log("xxxxxxxxxxxxxxxxxxxxxxxxsssss")
    self:Close()
    UIWinMgr:CloseMask()
    local MoreRecordWin = UIWinMgr:GetWindow("MoreRecordWin")
    MoreRecordWin.PlayerName = self.Record[self.reCordLen+1-self.ItemNum].nickname
    MoreRecordWin.Round = recordSm.round
    MoreRecordWin.Index = recordSm.index
    MoreRecordWin:Show()
end

function GameRecordWin:ShowRecord()
    self.ShowScrollView:DestroyItems()
    self.reCordLen = #self.Record
    local num = #self.Record
    if num>10 then
        num= 10
    end
    if self.Record and #self.Record>0 then
        self.PromptLabel.gameObject:SetActive(false)
        self.ShowPanel.gameObject:SetActive(true)
        for i=1, num do
            function OnAdd(obj)
                
                self.QueryVideo = UIUtility.GetChildTransform("QueryVideo",obj.transform,true)
                self.CopyRecord = UIUtility.GetChildTransform("CopyRecord",obj.transform,true)
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
                if self.Record[self.reCordLen+1-i].nickname[1] ~= nil then
                    PlayerName1.text=self.Record[self.reCordLen+1-i].nickname[1]
                    PlayerName1.color = Color(226/255,214/255,159/255)
                end

                if self.Record[self.reCordLen+1-i].nickname[2] ~= nil then
                    PlayerName2.text=self.Record[self.reCordLen+1-i].nickname[2]
                    PlayerName2.color = Color(226/255,214/255,159/255)
                end

                if self.Record[self.reCordLen+1-i].nickname[3] ~= nil then
                    PlayerName3.text=self.Record[self.reCordLen+1-i].nickname[3]
                    PlayerName3.color = Color(226/255,214/255,159/255)
                end

                if self.Record[self.reCordLen+1-i].nickname[4] ~= nil then
                    PlayerName4.text=self.Record[self.reCordLen+1-i].nickname[4]
                    PlayerName4.color = Color(226/255,214/255,159/255)
                end

                if self.Record[self.reCordLen+1-i].nickname[5] ~= nil then
                    PlayerName5.text=self.Record[self.reCordLen+1-i].nickname[5]
                    PlayerName5.color = Color(226/255,214/255,159/255)
                end

                if self.Record[self.reCordLen+1-i].coin[1] ~= nil then 
                    if self.Record[self.reCordLen+1-i].coin[1] >= 0 then
                        PlayerScore1.color = Color(120/255,222/255,72/255)
                        PlayerScore1.text = "+"..self.Record[self.reCordLen+1-i].coin[1]
                    else
                        PlayerScore1.color = Color(230/255,52/255,38/255)
                        PlayerScore1.text = self.Record[self.reCordLen+1-i].coin[1]
                    end
                end

                if self.Record[self.reCordLen+1-i].coin[2] ~= nil then                   
                    if self.Record[self.reCordLen+1-i].coin[2] >= 0 then
                        PlayerScore2.color = Color(120/255,222/255,72/255)
                        PlayerScore2.text = "+"..self.Record[self.reCordLen+1-i].coin[2]
                    else
                        PlayerScore2.color = Color(230/255,52/255,38/255)
                        PlayerScore2.text = self.Record[self.reCordLen+1-i].coin[2]
                    end                    
                end

                if self.Record[self.reCordLen+1-i].coin[3] ~= nil then
                    if self.Record[self.reCordLen+1-i].coin[3] >= 0 then
                         PlayerScore3.color = Color(120/255,222/255,72/255)
                         PlayerScore3.text="+"..self.Record[self.reCordLen+1-i].coin[3]
                    else
                         PlayerScore3.color = Color(230/255,52/255,38/255)
                         PlayerScore3.text = self.Record[self.reCordLen+1-i].coin[3]
                    end                   
                end

                if self.Record[self.reCordLen+1-i].coin[4] ~= nil then
                    if self.Record[self.reCordLen+1-i].coin[4] >= 0 then
                         PlayerScore4.color = Color(120/255,222/255,72/255)
                         PlayerScore4.text = "+"..self.Record[self.reCordLen+1-i].coin[4]
                    else
                         PlayerScore4.color = Color(230/255,52/255,38/255)
                         PlayerScore4.text = self.Record[self.reCordLen+1-i].coin[4]
                    end                    
                end

                if self.Record[self.reCordLen+1-i].coin[5] ~= nil then
                    if self.Record[self.reCordLen+1-i].coin[5] >= 0 then
                         PlayerScore5.color = Color(120/255,222/255,72/255)
                         PlayerScore5.text = "+"..self.Record[self.reCordLen+1-i].coin[5]
                    else
                         PlayerScore5.color = Color(230/255,52/255,38/255)
                         PlayerScore5.text = self.Record[self.reCordLen+1-i].coin[5]
                    end
                end

                function clickCopyCecordAction()
                   Debug.log("复制战绩")
                --    UIWinMgr:OpenPromptMaskWin("敬请期待")

                    local room_ID = self.Record[self.reCordLen+1-i].roomId
                    local coin = self.Record[self.reCordLen+1-i].coin
                -- Debug.log("PlayerScore2.text"..PlayerScore2.text)
                    local text = nil
                    if #coin == 2 then
                        text = room_ID.."\n"..PlayerName1.text.."："..PlayerScore1.text..","..PlayerName2.text.."："..PlayerScore2.text
                    end

                    if #coin == 3 then
                        text = room_ID.."\n"..PlayerName1.text.."："..PlayerScore1.text..","..PlayerName2.text.."："..PlayerScore2.text.."，"..PlayerName3.text.."："..PlayerScore3.text
                    end

                    if #coin == 4 then
                        text = room_ID.."\n"..PlayerName1.text.."："..PlayerScore1.text..","..PlayerName2.text.."："..PlayerScore2.text.."，"..PlayerName3.text.."："..PlayerScore3.text.."，"..PlayerName4.text.."："..PlayerScore4.text
                    end

                    if #coin == 5 then
                        text = room_ID.."\n"..PlayerName1.text.."："..PlayerScore1.text..","..PlayerName2.text.."："..PlayerScore2.text.."，"..PlayerName3.text.."："..PlayerScore3.text.."，"..PlayerName4.text.."："..PlayerScore4.textt.."，"..PlayerName5.text.."："..PlayerScore5.text
                    end

                   local messger = "疯狂阿拉，房号"..text
                   local RoomModule = require("Module.RoomModule").RoomModule
                   RoomModule.copyOtherString_Action(messger,"战绩信息")

                end
                UIHelper.BindUIEvnet("Click", clickCopyCecordAction, self.CopyRecord.gameObject)

                --战绩回访
                function clickQueryVideoAction()

                    --UIWinMgr:OpenPromptMaskWin("敬请期待")
                    self.ItemNum = i
                    local NetMgr = require("NetWork").NetMgr
                    local GameRecordCm = require("Protol.loginMessage_pb").GameRecordCm()
                    GameRecordCm.index = self.Record[self.reCordLen+1-i].index
                    Debug.log(GameRecordCm.index.."cscscsscs")
                    NetMgr:SendMsg(9,GameRecordCm)
                    UIWinMgr:OpenMask("数据加载中...")

                end
                UIHelper.BindUIEvnet("Click", clickQueryVideoAction, self.QueryVideo.gameObject)
                --
                --function ShowRound()
                --    self.ItemNum = i
                --    local NetMgr = require("NetWork").NetMgr
                --    local GameRecordCm = require("Protol.loginMessage_pb").GameRecordCm()
                --    GameRecordCm.index = self.Record[self.reCordLen+1-i].index
                --    Debug.log(GameRecordCm.index.."cscscsscs")
                --    NetMgr:SendMsg(9,GameRecordCm)
                --    UIWinMgr:OpenMask("数据加载中...")
                --end
                --UIHelper.BindUIEvnet("Click",ShowRound,obj.gameObject)
            end
            self.ShowScrollView:Add(OnAdd)
        end
    else
        -- self:Close()
        -- UIWinMgr:OpenNotice("您在过去10小时内无游戏记录，点击确定返回") ------ 
        self.PromptLabel.gameObject:SetActive(true)
        self.ShowPanel.gameObject:SetActive(false)
    end
end