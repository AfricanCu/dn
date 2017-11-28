--UIrenderTexture组件
module("UI.Com.UIRenderTexture",package.seeall)

local UIUtility = require("UI.Utility")
local GameObject = UnityEngine.GameObject

UIRenderTexture = {}
--构造函数
function UIRenderTexture:new(o)    
	o = o or {};
    setmetatable(o, { __index = self });     
    return o;
end 

UIRenderTexture.rootTran = nil          --根节点
UIRenderTexture.renderCam = nil         --渲染摄像机
UIRenderTexture.UITex = nil             --UITexture
UIRenderTexture.renderTex = nil         --渲染目标

--
function UIRenderTexture:Init(root,rendSeting)
    self.rootTran = root
    self.UITex = UIUtility.FindContorl('UITexture',"UITex",self.rootTran)
    self.renderCam = UIUtility.FindContorl('Camera',"renderCam",self.rootTran)

    if not self.UITex or not self.renderCam then
        return
    end

    UIRenderTexture.renderTex = UnityEngine.RenderTexture(rendSeting.x,rendSeting.y,rendSeting.z) --创建RenderTexture,这个对象包含非托管资源
    self.renderCam.targetTexture = UIRenderTexture.renderTex
    self.UITex.mainTexture = UIRenderTexture.renderTex
end

--控件是否准备就绪
function UIRenderTexture:IsReady()
    if nil == self.UITex then
        return false
    end
    if nil == self.renderCam then
        return false
    end
    if nil == self.renderTex then
        return false
    end
    return true
end

--销毁
function UIRenderTexture:Destroy()
    if nil ~= renderTex then
        renderTex:Release()
        GameObject.Destroy(renderTex)
    end
end