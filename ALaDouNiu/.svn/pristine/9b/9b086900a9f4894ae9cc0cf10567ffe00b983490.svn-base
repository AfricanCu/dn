--UI窗体管理器
module("UI.UIWinMgr",package.seeall)

UIWinMgr = {}

UIWinMgr.WinSet = {}

--获得界面
function UIWinMgr:GetWindow(WindowName)
    window = UIWinMgr.WinSet[WindowName]
    if nil ~= window then
        if nil == window.Root or window.Root:Equals(nil) or nil == window.Container or window.Container:Equals(nil) then
            window:CreateWin()
            window:Init()
            if nil ~= window.Container then
                window.Container.gameObject:SetActive(false) --黄老板要求界面第一次搞出来的时候要是关闭状态
            end
        end
    else
        Debug.log("获取界面 "..WindowName.." 失败")
    end
    return window
end

--打开界面
function UIWinMgr:OpenWindow(WindowName)
    window = UIWinMgr:GetWindow(WindowName)
    if nil ~= window then
        window:Show()
    end
    return window
end

--关闭界面
function UIWinMgr:CloseWindow(WindowName)
    window = UIWinMgr:GetWindow(WindowName)
    if nil ~= window then
        window:Close()
    end
    return window
end

--打开提示界面
function UIWinMgr:OpenNotice(msg,okfun,nofun)
    local noticeWin = UIWinMgr:OpenWindow("NoticeWin")
    --noticeWin.Container.depth = 99999
    noticeWin:SetMsg(msg,okfun,nofun)
    noticeWin:BringToTop()
    return noticeWin
end

function UIWinMgr:OpenErrorNotice(code)
    local DTMgr = require("DataTable").DTMgr
    local NoticeText = DTMgr:GetTabel("NoticeText")
    local str = DTMgr:GetValueByKey(NoticeText,"value",code,"text")
    if not str then
        Debug.log("服务器Notice："..code.." 没有定义")
        return
    end
    self:OpenNotice(str)
end

function UIWinMgr:OpenMask(msg)
    local MaskWin = UIWinMgr:OpenWindow("MaskWin")
    MaskWin:SetTxt(msg)
    MaskWin:BringToTop()
    return MaskWin
end

function UIWinMgr:CloseMask()
    UIWinMgr:CloseWindow("MaskWin")
end


-- 打开小提示框
function UIWinMgr:OpenPromptMaskWin(msg)
    local PromptMaskWin = UIWinMgr:OpenWindow("PromptMaskWin")
    PromptMaskWin:SetTxt(msg)
    PromptMaskWin:BringToTop()
    PromptMaskWin:PlayTween()

    return PromptMaskWin
end

-- 关闭小提示框
function UIWinMgr:ClosePromptMaskWin()
    UIWinMgr:CloseWindow("PromptMaskWin")
end

-- PromptMaskWin