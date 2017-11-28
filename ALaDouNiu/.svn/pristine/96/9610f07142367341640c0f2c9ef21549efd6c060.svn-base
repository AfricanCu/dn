--场景管理器
module("Scene",package.seeall)

SceneManager = UnityEngine.SceneManagement.SceneManager

SceneMgr = {}

SceneMgr.CurScene = nil --当前场景

SceneMgr.UnloadCDid = nil --场景资源清理计时器ID

--加载场景
--Scene 场景脚本对象
function SceneMgr:LordScene(Scene)
    if nil ~= self.CurScene then
        self.CurScene:OnSceneUnLoad()
        if self.UnloadCDid then
            CountDownMgr.Instance:RemoveCountDown(self.UnloadCDid,false)
            Debug.log("移除资源清理计时器")
        end
    end
    Debug.log("开始加载关卡："..Scene.GetSceneName())
    self.CurScene = Scene;

    UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local LoadingWin = UIWinMgr:OpenWindow("LoadingWin")

    LoadingWin:SetProgress(0)

    local ResMgr = require("Res.ResMgr").ResMgr

    --当关卡加载完毕
    function OnSceneLoadOver(sceneName)
        self.CurScene:OnSceneLoadOver()
        Debug.log("加载关卡完成：".. sceneName)
        --LoadingWin:Destroy()
        local go = UnityEngine.GameObject(SceneMgr.CurScene.GetSceneName().."_场景AB关联对象，谁删谁没JJ")
        ResMgr:Retain(sceneName,go)--将场景AB关联场景中的一个对象
        function TimingUnLoadUnUse()
            ResMgr:UnLoadUnUse()
            Debug.log("清理无效资源")
        end
        self.UnloadCDid = CountDownMgr.Instance:CreateCountDown(30,TimingUnLoadUnUse)
    end

    --更新进度
    function LoadSceneAsyncCallBack(async)
        function myProcess()
            --self.CurScene.LoadProcess(async.progress)
        end

        CountDownMgr.Instance:CreateCountDown(0.05,myProcess)
    end
    
    if not UnityEngine.Application.isEditor then
        function OnSceneAbLoadCallBack(res)
            SceneHelper.LoadSceneASys(SceneMgr.CurScene.GetSceneName(),OnSceneLoadOver,LoadSceneAsyncCallBack)
        end
        ResMgr:LordByAsync(OnSceneAbLoadCallBack,SceneMgr.CurScene.GetSceneName())
    else
        SceneHelper.LoadSceneASys(SceneMgr.CurScene.GetSceneName(),OnSceneLoadOver,LoadSceneAsyncCallBack)
    end

end

--获得场景环境管理器
function SceneMgr:GetEnvironment()
    local SceneEnvironment = require("Scene.SceneEnvironment").SceneEnvironment
    SceneEnvironment:Init(self)
    return SceneEnvironment
end
