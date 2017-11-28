--场景基类
module("Scene.SceneBase",package.seeall)

SceneBase = {}

--构造函数
function SceneBase:new(o)    
	o = o or {};
    setmetatable(o, { __index = self });     
    return o;
end  

--场景名称
function SceneBase.GetSceneName()
    --返回场景名字，Unity将根据你这个函数返回的名字加载场景
    --return "场景名字"
end

--当场景加载完毕
function SceneBase:OnSceneLoadOver()
    --干点加载资源，初始化啥子的事
end

--当场景卸载
function SceneBase:OnSceneUnLoad(sceneName)
    -- 干点卸载回收之类的事
end

