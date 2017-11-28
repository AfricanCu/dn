--启动引导，游戏所有功能模块的初始化在这里进行
module("Boot",package.seeall)

Debug.log("开始引导初始化")

local rate = 30
UnityEngine.Application.targetFrameRate = rate
Debug.log("渲染帧率 "..rate)
UnityEngine.Screen.sleepTimeout = UnityEngine.SleepTimeout.NeverSleep

--*********************************基础模块初始化**********************************
--初始化核心代码
LuaHelper.Instance:LoadAllLuaScriptByPath("/Core/");

--初始化工具模块
LuaHelper.Instance:LoadAllLuaScriptByPath("/Tools/");

--数据表模块
require("DataTable.DataTableMgr")

--加载事件模块
require("Event.Eventlib")
require("Event.Events")
require("Event.EventDefine")
require("Event.BehaviourEvent")

--网络模块
require("Net.NetHttp")
require("Net.NetCmdActionBase")
require("Net.NetCmdSet")
require("Net.NetMgr")

--资源管理模块
require("Res.ResMgr")

--音效管理
require("Sound.SoundAgant")
require("Sound.SoundMgr")

--UI模块
require("UI.Utility")
require("UI.UIWindow")
require("UI.UIWinMgr")

--场景管理模块
require("Scene.SceneBase")
require("Scene.SceneMgr")

--加载完成
Debug.log("基础模块初始化完成！！！！")
--********************************************************************************

--*********************************加载逻辑脚本************************************
Debug.log("开始加载逻辑脚本")

--加载通信协议
LuaHelper.Instance:LoadAllLuaScriptByPath("/Protol/");

--加载数据表
LuaHelper.Instance:LoadAllLuaScriptByPath("/DataTable/Tables/");

--加载动态数据代码
LuaHelper.Instance:LoadAllLuaScriptByPath("/DynamicData/");

--加载UI代码
LuaHelper.Instance:LoadAllLuaScriptByPath("/UI/Windows/");
require("UI.UIRegisterWindow")

--加载Moudle代码
LuaHelper.Instance:LoadAllLuaScriptByPath("/Module/");

--加载游戏场景代码
LuaHelper.Instance:LoadAllLuaScriptByPath("/Scene/Scenes/");

--加载消息处理代码
LuaHelper.Instance:LoadAllLuaScriptByPath("/Net/CmdAction/");

Debug.log("逻辑脚本加载完成！！！！！")

--加载离线语音模块
--[[local gMsg = GMsgMgr.Instance
local GmsgModule = require("Module.GmsgModule").GmsgModule
GmsgModule:Init()
GmsgModule:SetLuaInterface()]]--

local ConfigModule = require("Module.ConfigModule").ConfigModule
ConfigModule:Init()

--local LocationModule = require("Module.LocationModule").LocationModule
--LocationModule:Init()

--[[local IosPendingTool = require("IosPendingTool").IosPendingTool
local isIosPending = IosPendingTool.IsPending()
if isIosPending then
    --审核期间不能打开地图
else
    function LocCallBack(strData)
        local LocationModule = require("Module.LocationModule").LocationModule
        LocationModule:SetMyNs(strData)
    end
    --调用物理位置接口
    LocationTool.Instance:ReqLoc(LocCallBack)
end]]--

local SceneMgr = require("Scene").SceneMgr
SceneMgr:LordScene(require("Scene.GameScene").GameScene)