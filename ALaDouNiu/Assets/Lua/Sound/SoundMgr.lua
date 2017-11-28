--音效管理
module("Sound",package.seeall)

local soundAgant = require("Sound.SoundAgant").SoundAgant

SoundMgr = {}

SoundMgr.maxAgantNum = 50 --程序能够同时创建的最大音源个数
SoundMgr.BGM = nil

--声音对象池
SoundMgr.Sounds = {}

function SoundMgr:SetBGM(soundResName)
    if nil == self.BGM then
        self.BGM = soundAgant:new()
    end

    self.BGM:Play(soundResName,true)
    self.BGM.AudioSource.gameObject:SetActive(true)
    self.BGM.AudioSource.gameObject.name = "BGM_AudioSource"
    return self.BGM
end

function SoundMgr:StopBGM()
    if nil ~= self.BGM.AudioSource then
        self.BGM.AudioSource.gameObject:SetActive(false)
    end
end

function SoundMgr:PlaySound(soundResName)
    local agant = nil
    for i=1,#self.Sounds do
        if not self.Sounds[i]:isPlaying() then
            agant = self.Sounds[i]
            break
        end
    end
    if nil == agant then
        agant = self:CreateSoundAgant()
    end
    if nil == agant then
        Debug.log("你造吗?"..self.maxAgantNum.."个音源还不够你播声音？")
        return nil
    else
        agant:Play(soundResName)
        return agant
    end
end

function SoundMgr:CreateSoundAgant()
    if #self.Sounds >= self.maxAgantNum then
        return nil
    end
    local agant = soundAgant:new()
    table.insert(self.Sounds,agant)
    return agant
end


