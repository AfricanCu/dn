module("Module.LocationModule",package.seeall)

local NetMgr = require("NetWork").NetMgr
LocationModule = {}

function LocationModule:Init()
    self.locTable = {}
    --Test
    --[[local NsCm1 = {}
    NsCm1.j = 114
    NsCm1.w = 28
    NsCm1.ad = "湖南长沙xxxyyy"
    self.locTable[1] = NsCm1

    local NsCm2 = {}
    NsCm2.j = 114
    NsCm2.w = 29
    NsCm2.ad = "湖南长沙aaabbb"
    self.locTable[2] = NsCm2

    local NsCm3 = {}
    NsCm3.j = 115
    NsCm3.w = 30
    NsCm3.ad = "湖南长沙cccddd"
    self.locTable[3] = NsCm3

    local NsCm4 = {}
    NsCm4.j = 113
    NsCm4.w = 29
    NsCm4.ad = "湖南长沙eeefff"
    self.locTable[4] = NsCm4

    --local MyMath = require("MyMath").MyMath
    --local dis = MyMath.LantitudeLongitudeDist(NsCm1,NsCm2)
    --Debug.log("dis~~~~~~~~~~~~~~~~~~~~~~~~~~:"..dis)]]
end

function LocationModule:Reset()
    self.locTable = {}
end

function LocationModule:SetMyNs(strData)
    local json = require("cjson")
    local jsondata = json.decode(strData)
    
    if jsondata.isTrue ~= 0 then
        Debug.log("SetMyNs rtn:"..jsondata.isTrue)
        return
    end
    Debug.log("jsondata.longitude:"..jsondata.longitude)
    self.MyNs = {}
    self.MyNs.j = jsondata.longitude
    Debug.log("self.MyNs.j:"..self.MyNs.j)
    self.MyNs.w = jsondata.latitude
    self.MyNs.ad = jsondata.address
    self.MyNs.country = jsondata.country
    self.MyNs.province = jsondata.province
    self.MyNs.city = jsondata.city
    self.MyNs.district = jsondata.district
    self.MyNs.street = jsondata.street
    self.MyNs.houseNumber = jsondata.houseNumber

    local GameHost = require("Module.GameModule.GameHost").GameHost
    if GameHost.myIndex ~= nil then
        self:NsCm()
    end
end

function LocationModule:NsCm()
    if self.MyNs ~= nil then
        local Msg = require("Protol.roomMessage_pb").NsCm()
        Debug.log("self.MyNs.j:"..self.MyNs.j)
        Msg.j = self.MyNs.j
        Msg.w = self.MyNs.w
        Msg.ad = self.MyNs.ad
        Msg.country = self.MyNs.country
        Msg.province = self.MyNs.province
        Msg.city = self.MyNs.city
        Msg.district = self.MyNs.district
        Msg.street = self.MyNs.street
        Msg.houseNumber = self.MyNs.houseNumber

        NetMgr:SendMsg(237,Msg)

        local GameHost = require("Module.GameModule.GameHost").GameHost
        self.locTable[GameHost.myIndex] = self.MyNs
    end
end

function LocationModule:NsCast(seetIndex, ns)
    self.locTable[seetIndex] = ns
end

function LocationModule:RemoveNs(seetIndex)
    self.locTable[seetIndex] = nil
end