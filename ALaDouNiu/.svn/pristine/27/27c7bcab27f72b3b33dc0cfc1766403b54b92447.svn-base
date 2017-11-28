module("Module.SoundModule",package.seeall)

SoundModule = {}
local PlayerPrefs = UnityEngine.PlayerPrefs

function SoundModule:Init()
    self.soundVolume = PlayerPrefs.GetFloat("Sound", 1)
    self.bgVolume = PlayerPrefs.GetFloat("BgSound", 0.7)

    Debug.log("self.soundVolume init:"..self.soundVolume)
end

--获取全局音效声音
function SoundModule:GetAllEffectVolume()
    return self.soundVolume
end

--获取所有音效声音
function SoundModule:GetAllBgVolume()
    return self.bgVolume
end

--调整全局音效声音
function SoundModule:SetAllEffectVolume(volume)
    self.soundVolume = volume
    --此处会自动保存到sound PlayerPrefs 中
    NGUITools.soundVolume = volume
end
 

--调整所有音效声音
function SoundModule:SetAllBgVolume(volume)
    self.bgVolume = volume
    PlayerPrefs.SetFloat("BgSound", volume)
    local SoundMgr = require("Sound").SoundMgr
    if SoundMgr.BGM ~= nil then
        if SoundMgr.BGM.AudioSource ~= nil then
            SoundMgr.BGM.AudioSource.volume = volume
        end
    end
end

function SoundModule:SetBGM(soundResName)
    local SoundMgr = require("Sound").SoundMgr
    local Bgm = SoundMgr:SetBGM(soundResName)
    Bgm.AudioSource.volume = self.bgVolume
end

function SoundModule:PlaySound(soundResName)
    local SoundMgr = require("Sound").SoundMgr
    local agant = SoundMgr:PlaySound(soundResName)
    Debug.log("self.soundVolume:"..self.soundVolume)
    agant.AudioSource.volume = self.soundVolume
end

function SoundModule:GetIdleSoundAgent()
    local SoundMgr = require("Sound").SoundMgr
    local agant = SoundMgr:GetIdleSoundAgent()
    if agant ~= nil then
        agant.AudioSource.volume = self.soundVolume
    end

    return agant
end

function SoundModule:MuteAllSound()
    local SoundMgr = require("Sound").SoundMgr
    SoundMgr:MuteAllSound()
end

function SoundModule:StopBGM()
    local SoundMgr = require("Sound").SoundMgr
    SoundMgr:StopBGM()
end

function SoundModule:BeginBGM()
    local SoundMgr = require("Sound").SoundMgr
    if nil ~= SoundMgr.BGM.AudioSource then
        SoundMgr.BGM.AudioSource.gameObject:SetActive(true)
    end
end