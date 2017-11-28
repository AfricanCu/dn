--商城界面
module("UI.Windows.RoomInfoWin", package.seeall)

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local UIWindow = require("UI.UIWindow").UIWindow
local UIUtility = require("UI.Utility")
local GameObject = UnityEngine.GameObject
local NetMgr = require("NetWork").NetMgr
local MainUserData = require("DynamicData.MainUserData").MainUserData

RoomInfoWin = UIWindow:new()
RoomInfoWin.name = "RoomInfoWin"



function RoomInfoWin:Init()
     --初始化界面，保存引用控件
    local t = self.Container.transform
    self.Close_Button = UIUtility.GetChildTransform("Close_Button", t, true)
    self.di = UIUtility.GetChildTransform("di", t, true)
    self.roomList = UIUtility.GetChildTransform("roomList", t, true)
    self.Room_Num = UIUtility.FindContorl("UILabel", "Room_Num", t)
    self.ScrollView_Panel = UIUtility.GetChildTransform("ScrollView_Panel", t, true) -- scrollView
    self.Grid = UIUtility.GetChildTransform("Grid", t, true) -- Grid
    self.GR_1 = UIUtility.GetChildTransform("1", t, true)
    self.itmeList = {}

    self:BindEvents()
    self:BindHander()
 end

function RoomInfoWin:BindEvents()
     function UPDataProxyRoom()

        Debug.log("---------------数据数据数据")
            self:UPDataActionNow()
     end
    self.UPDataAction = UPDataProxyRoom
    -- Event.AddListener(EventDefine.OnProxyRoomChange, self.UPDataAction)
    Event.AddListener(EventDefine.OnProxyRoomChange, self.UPDataAction)
    function clickCloseButtonAction()
        UIWinMgr:CloseWindow("CreateRoomWin")
        UIWinMgr:CloseWindow("ProxyRoomWin")
        self:Close()
    end
        UIHelper.BindUIEvnet("Click", clickCloseButtonAction, self.Close_Button.gameObject) 
        UIHelper.BindUIEvnet("Click", clickCloseButtonAction, self.di.gameObject) 

    function OpenRecWin()
        UIWinMgr:OpenWindow("ProxyGameRecWin")
    end
    UIHelper.BindUIEvnet("Click", OpenRecWin, self.roomList.gameObject) 
end     

function RoomInfoWin:BindHander()
        -- body
 end


function RoomInfoWin:OnShow()
    
    self:UPDataAction()

 end
 function RoomInfoWin:OnClose()
    
    self:Destroy()
    -- self.itmeList = {}

 end


 --房间数据变化调用
 function RoomInfoWin:UPDataActionNow()

        local Room_Num = 0
        Debug.log(#self.itmeList)
    if #MainUserData.ProxyRoom ~= #self.itmeList then
        for i=1,#MainUserData.ProxyRoom do

             Debug.log("-------------------")
            self.itmeList[i] = UnityEngine.GameObject.Instantiate(self.GR_1.gameObject)
            self.itmeList[i].gameObject:SetActive(false)
            self.itmeList[i].transform.parent = self.Grid.transform
            self.itmeList[i].transform.localScale = Vector3.one
            self.itmeList[i].transform.localPosition = Vector3.zero
            self.itmeList[i].transform.localEulerAngles = Vector3(0, 0, 0)

        end
    end

        for i=1,#MainUserData.ProxyRoom do
            
            local n = self.itmeList[i].transform
            self.Label_roomNum = UIUtility.FindContorl("UILabel", "Label_roomNum", n)
            self.Label_roomNum.text = MainUserData.ProxyRoom[i].roomId
    
            if MainUserData.ProxyRoom[i].playType.round == 1 then
                self.round = "10局"   
            elseif MainUserData.ProxyRoom[i].playType.round == 2 then
                self.round = "20局"        
            end
    
            if MainUserData.ProxyRoom[i].playType.pType == 1 then
                self.pType = "经典牛牛"   
            elseif MainUserData.ProxyRoom[i].playType.pType == 2 then
                self.pType = "疯狂加倍"        
            end
        
            if MainUserData.ProxyRoom[i].playType.bankerMode == 1 then
                self.bankerMode = "抢庄"   
            elseif MainUserData.ProxyRoom[i].playType.bankerMode == 2 then
                self.bankerMode = "轮庄"        
            elseif  MainUserData.ProxyRoom[i].playType.bankerMode == 3 then
                self.bankerMode = "固定庄"        
            end
    
            Debug.log(self.round.."、"..self.pType.."、"..self.bankerMode)
            self.Label_pType = UIUtility.FindContorl("UILabel","Label_pType",n)
            self.Label_pType.text = self.round.."、"..self.pType.."、"..self.bankerMode
    
            self.Play_no = UIUtility.FindContorl("UISprite","Play_no", n)
            self.Play_star = UIUtility.FindContorl("UISprite","Play_star", n)
    
            if MainUserData.ProxyRoom[i].state == 1 then
                -- 未开始
                self.Play_no.gameObject:SetActive(true)
                self.Play_star.gameObject:SetActive(false)
            elseif MainUserData.ProxyRoom[i].state == 2 then
                    Room_Num = Room_Num+1
                    self.Room_Num.text = Room_Num..""
                --进行中
                self.Play_no.gameObject:SetActive(false)
                self.Play_star.gameObject:SetActive(true)  
            elseif MainUserData.ProxyRoom[i].state == 3 then
                -- 已结束
                
            end
            self.Label_playerNum = UIUtility.FindContorl("UILabel","Label_playerNum",n)
            self.Label_playerNum.text = #MainUserData.ProxyRoom[i].user.."/5"
            Debug.log("22222222222")

            local timeData = MainUserData.ProxyRoom[i].time
            local timeStr = os.date("%Y%m%d%H%M",timeData/1000)..""
            local year = string.sub(timeStr, 1, 4)
            local moth = string.sub(timeStr, 5, 6)
            local day = string.sub(timeStr, 7, 8)
            local hour = string.sub(timeStr, 9, 10)
            local second = string.sub(timeStr, 11, #timeStr)
            Debug.log("year:"..year)
            Debug.log("moth:"..moth)
            Debug.log("day:"..moth)
            Debug.log("hour:"..hour)
            Debug.log("second:"..second)
            self.Label_time_year =  UIUtility.FindContorl("UILabel","Label_time_year",n) -- 时间
            self.Label_time_year.text = year.."."..moth.."."..day.."  "..hour..":"..second -- 时间显示

----------------------------------------------------玩家显示-----------------------------------------------------------
            self.ID_1 = UIUtility.FindContorl("UILabel","ID_1",n)
            self.ID_2 = UIUtility.FindContorl("UILabel","ID_2",n)
            self.ID_3 = UIUtility.FindContorl("UILabel","ID_3",n)
            self.ID_4 = UIUtility.FindContorl("UILabel","ID_4",n)
            self.ID_5 = UIUtility.FindContorl("UILabel","ID_5",n)
            self.ID_1.gameObject:SetActive(false)
            self.ID_2.gameObject:SetActive(false)
            self.ID_3.gameObject:SetActive(false)
            self.ID_4.gameObject:SetActive(false)
            self.ID_5.gameObject:SetActive(false)
            if #MainUserData.ProxyRoom[i].user > 0 then
                for j=1, #MainUserData.ProxyRoom[i].user do
                    if j == 1 then
                        self.ID_1.gameObject:SetActive(true)
                        Debug.log("$$$$$$$$$$$$$$$$$$$$$$$")
                        self.ID_1.text = MainUserData.ProxyRoom[i].user[j].nickname.."[ID:"..MainUserData.ProxyRoom[i].user[j].uid.."]" 
                    elseif j == 2 then
                        self.ID_2.gameObject:SetActive(true)
                        Debug.log("3333333333333333333")
                        self.ID_2.text = MainUserData.ProxyRoom[i].user[j].nickname.."[ID:"..MainUserData.ProxyRoom[i].user[j].uid.."]"  
                    elseif j == 3 then
                        self.ID_3.gameObject:SetActive(true)
                        self.ID_3.text = MainUserData.ProxyRoom[i].user[j].nickname.."[ID:"..MainUserData.ProxyRoom[i].user[j].uid.."]"  
                    elseif j == 4 then
                        self.ID_4.gameObject:SetActive(true)
                        self.ID_4.text = MainUserData.ProxyRoom[i].user[j].nickname.."[ID:"..MainUserData.ProxyRoom[i].user[j].uid.."]"  
                    elseif j == 5 then
                        self.ID_5.gameObject:SetActive(true)
                        self.ID_5.text = MainUserData.ProxyRoom[i].user[j].nickname.."[ID:"..MainUserData.ProxyRoom[i].user[j].uid.."]" 
                    end
                end
            end
------------------------------------------------------------------------------------------------------------
            self.Copy_But = UIUtility.GetChildTransform("Copy_But", n, true) -- 复制房间号信息
            function clickCopybuttAction() -- 复制房间信息事件
                local roomInfo = {}
                roomInfo.PlayType = MainUserData.ProxyRoom[i].playType
                roomInfo.RoomId = MainUserData.ProxyRoom[i].roomId
                local RoomModule = require("Module.RoomModule").RoomModule
                RoomModule.CopyTextToClipBoard(roomInfo)
            end
            UIHelper.BindUIEvnet("Click", clickCopybuttAction, self.Copy_But.gameObject)


            self.Invitation_But = UIUtility.GetChildTransform("Invitation_But", n, true) -- 邀请好友
            function clickInvitationButtAction() -- 邀请好友事件
                Debug.log("----------邀请好友---------")
                local titleRoom = MainUserData.ProxyRoom[i].roomId
                if MainUserData.ProxyRoom[i].playType.pType == 1 then
                    myContent = "经典玩法"
                else
                    myContent = "疯狂加倍"
                end
                if MainUserData.ProxyRoom[i].playType.round == 1 then
                    myContent = myContent.." 10局"
                else
                    myContent = myContent.." 20局"
                end
                local json = require("cjson")
                local share = {}
                share.type = 1
                share.mediatype = 13
                share.title = "疯狂阿拉"..titleRoom
                share.content = myContent
                share.link = "http://fir.im/aladn"
                local s_Share = json.encode(share)
                PlatformTool.Instance:MWShare("share_Unity", s_Share, "wx1463fc544854bfd0", WXShareCallBack)
            end
            UIHelper.BindUIEvnet("Click", clickInvitationButtAction, self.Invitation_But.gameObject)

            self.Join_But = UIUtility.GetChildTransform("Join_But", n, true) -- 加入按钮
            function clickJoin_ButAction()  --加入按钮事件
                Debug.log("&&&&&&&&&&&")
                self:Close()
                UIWinMgr:CloseWindow("CreateRoomWin")
                
                -- UIWinMgr:CloseWindow("MainWin")
                local RoomModule = require("Module.RoomModule").RoomModule
                RoomModule.JoinRoomBeforeCm(MainUserData.ProxyRoom[i].roomId)
            end
            UIHelper.BindUIEvnet("Click", clickJoin_ButAction, self.Join_But.gameObject) 
            self.itmeList[i].gameObject:SetActive(true)   
        end
        self.Grid.transform:GetComponent("UIGrid").enabled = true
 end