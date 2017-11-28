--事件全局定义

EventDefine = {}

EventDefine.OnConnectClose = "OnConnectClose";--网络掉线事件
EventDefine.SWLogin = "SWLogin" 
--用户数据相关
EventDefine.OnMainUserDataChange = "OnMainUserDataChange"--当用户数据更新，消息参数:MainUserData(用户数据)
--战斗记录更新
EventDefine.OnBattleRecordChange = "OnBattleRecordChange"--当战斗记录数据更新
--proxy战斗记录更新
EventDefine.OnProxyRecordChange = "OnProxyRecordChange"--当战斗记录数据更新

EventDefine.OnPlayerJoinGame = "OnPlayerJoinGame" --当玩家加入游戏 参数player（加入游戏的玩家的数据）
EventDefine.OnPlayerLeaveGame = "OnPlayerLeaveGame" --当玩家离开游戏 参数player（离开游戏的玩家的数据）
EventDefine.OnPlayerOffLine = "OnPlayerOffLine" --当玩家离线 参数player（离线的玩家的数据）
EventDefine.OnPlayerOnLine = "OnPlayerOnLine" --当玩家上线 参数player（上线的玩家的数据）
EventDefine.OnPlayerReady = "OnPlayerReady" --当玩家准备 参数 player (进入准备状态的玩家)

EventDefine.OnGameStart = "OnGameStart"
EventDefine.OnDownCardChange = "OnDownCardChange"
EventDefine.OnGameRoundChange = "OnGameRoundChange"
EventDefine.OnBankerChange = "OnBankerChange"
EventDefine.OnAddClub = "OnAddClub" --加入俱乐部或者创建俱乐部广播
EventDefine.OnDelClub = "OnDelClub" --删除俱乐部或者退出俱乐部
EventDefine.OnUpClub = "OnUpClub" --更新俱乐部
EventDefine.OnBaoPaiUpdate = "OnBaoPaiUpdate" --宝牌
EventDefine.OnProxyRoomChange = "OnProxyRoomChange" --代开房数据跟新
-- EventDefine.OnProxyRoomInfoChange = "OnProxyRoomInfoChange" -- 更新界面信息