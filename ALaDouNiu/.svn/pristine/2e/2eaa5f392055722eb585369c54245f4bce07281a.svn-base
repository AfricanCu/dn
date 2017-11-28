--场景环境控制模块
module("Scene.SceneEnvironment",package.seeall)

SceneEnvironment = {}

SceneEnvironment.NightNode = nil
SceneEnvironment.DayNode = nil
SceneEnvironment.Combine = nil
SceneEnvironment.DaylightMapData = nil
SceneEnvironment.NightlightMapData = nil


--初始化
function SceneEnvironment:Init(SceneMgr)

    if nil == SceneMgr.CurScene then
        return
    end

    local GameObject = UnityEngine.GameObject
    local BehaviourEvent = require("BehaviourEvent").BehaviourEvent

    if nil == self.NightNode then
        self.NightNode = GameObject.Find("NightNode")-- 找到夜晚节点的GameObject
        if nil ~= self.NightNode then
            function OnDestroy() --当节点被销毁时调用
                self.NightNode = nil
                Debug.log("销毁夜晚节点")
            end
            BehaviourEvent:Bind("OnDestroy",self.NightNode,OnDestroy)
        end
    end
    if nil == self.DayNode then
        self.DayNode = GameObject.Find("DayNode")--找到白天节点的GameObject
        if nil ~= self.DayNode then
            function OnDestroy() --当节点被销毁时调用
                self.DayNode = nil
                Debug.log("销毁白天节点")
            end
            BehaviourEvent:Bind("OnDestroy",self.DayNode,OnDestroy)
        end
    end

    if nil == self.Combine then
        self.Combine = GameObject.Find(SceneMgr.CurScene.GetSceneName().."_combine")
        --Debug.log("查找光照贴图数据对象："..SceneMgr.CurScene.GetSceneName().."_combine")
        if nil ~= self.Combine then
            local Datas = self.Combine:GetComponents(typeof(PrefabLightmapData))
            local DataTab = Datas:ToTable()
            self.NightlightMapData = DataTab[1]
            self.DaylightMapData = DataTab[2]

            function OnDestroy()
                self.NightlightMapData = nil
                self.DaylightMapData = nil
                self.Combine = nil
                Debug.log("销毁光照贴图数据引用")
            end
            local lightMapDataDependent  = GameObject("删我没JJ_光照贴图数据引用销毁绑定对象")
            BehaviourEvent:Bind("OnDestroy",lightMapDataDependent,OnDestroy)
        end
    end
end

--设置白天
function SceneEnvironment:SetDay()
    if nil ~= self.DayNode then
        local t = self.DayNode.transform
        for i=0,t.childCount - 1 do
            t:GetChild(i).gameObject:SetActive(true)
        end
    end
    if nil ~= self.NightNode then
        local t = self.NightNode.transform
        for i=0,t.childCount - 1 do
            t:GetChild(i).gameObject:SetActive(false)
        end
    end
    if nil ~= self.DaylightMapData then
        self.DaylightMapData:Apply()
    else
        Debug.log("ERROR!!! 白天光照贴图数据为空")
    end
    Debug.log("设置场景到 白天")
    local SoundMgr = require("Sound").SoundMgr
    local SoundAgant = SoundMgr:PlaySound("Sound_Effect_brid")
    SoundAgant.AudioSource.volume = 1
end

--设置晚上
function SceneEnvironment:SetNight()
    if nil ~= self.DayNode then
        local t = self.DayNode.transform
        for i=0,t.childCount - 1 do
            t:GetChild(i).gameObject:SetActive(false)
        end
    end
    if nil ~= self.NightNode then
        local t = self.NightNode.transform
        for i=0,t.childCount - 1 do
            t:GetChild(i).gameObject:SetActive(true)
        end
    end
    if nil ~= self.DaylightMapData then
        self.NightlightMapData:Apply()
    else
        Debug.log("ERROR!!! 夜晚光照贴图数据为空")
    end
    Debug.log("设置场景到 夜晚")
    local SoundMgr = require("Sound").SoundMgr
    local SoundAgant = SoundMgr:PlaySound("Sound_Effect_wolf")
    SoundAgant.AudioSource.volume = 0.3
end
