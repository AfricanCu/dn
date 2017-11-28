--音效代理对象
module("Sound.SoundAgant",package.seeall)

SoundAgant = {}

SoundAgant.AudioSource = nil

--构造函数
function SoundAgant:new(o)    
	o = o or {};
    setmetatable(o, { __index = self });     
    return o;
end 

--播放声音
--声音文件名
--是否循环播放
function SoundAgant:Play(soundResName,loop)

    if not loop then
        loop = false
    end

    if nil == self.AudioSource then
        local GameObject = UnityEngine.GameObject
        local go = GameObject("AudioSource")
        self.AudioSource = go:AddComponent(typeof(UnityEngine.AudioSource))
        function OnDestroy()
            self.AudioSource = nil
        end
        local BehaviourEvent = require("BehaviourEvent").BehaviourEvent
        BehaviourEvent:Bind("OnDestroy",go,OnDestroy)
    end

    local ResMgr = require("Res.ResMgr").ResMgr
    local clip = ResMgr:LordBySyn(soundResName,self.AudioSource.gameObject)
    self.AudioSource.clip = clip
    self.AudioSource.loop = loop
    self.AudioSource:Play()
end

--是否正在播放
function SoundAgant:isPlaying()
    if nil == self.AudioSource then
        return false
    end
    return  self.AudioSource.isPlaying
end

