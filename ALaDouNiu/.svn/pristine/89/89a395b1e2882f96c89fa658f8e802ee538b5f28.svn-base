module("QuitTool", package.seeall)

QuitTool = {}

function QuitTool.QuitGame()
    function QuitGame( ... )
        UnityEngine.Application.Quit()
    end

    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    UIWinMgr:OpenNotice("您确定要退出游戏吗", QuitGame)
end