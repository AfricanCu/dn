module("PayTool", package.seeall)

PayTool = {}


function PayTool.Pay(payid,allNum, _goodPrice,callBack)
    local payUrl = ""
    local payNoticeUrl = ""
    local platform = PlatformTool.Instance:GetPlatformID()
    local IosPendingTool = require("IosPendingTool").IosPendingTool
    local isIosPending = IosPendingTool.IsPending()
    if isIosPending then
        -- payUrl = "http://119.23.104.30:8116/Login/genOrderIdServlet"
        -- payNoticeUrl = "http://119.23.104.30:8116/Login/iosSandboxChargeServletServlet"

        payUrl = "http://120.78.174.39:8118/Login/genOrderIdServlet"
        payNoticeUrl = "http://120.78.174.39:8118/Login/iosSandboxChargeServletServlet"
       
    else
        -- payUrl = "http://119.23.104.30:8116/Login/genOrderIdServlet"
        -- payNoticeUrl = "http://119.23.104.30:8116/Login/iosChargeServlet"
    end
    local appID = "wx1463fc544854bfd0"
    local uid = UnityEngine.PlayerPrefs.GetString("LastAccount")
    local sPayid = tostring(payid)
    local goodPrice = tostring(_goodPrice)
    local goodName = tostring(allNum).."颗钻石"
    
    --Debug.log("uid"..uid.." sPayid:"..sPayid.." goodName:"..goodName.." goodPrice:"..goodPrice)
    if isIosPending then
        PlatformTool.Instance:PayAppStore(uid, sPayid, goodName, goodPrice, appID, payUrl, payNoticeUrl,callBack)
    else
       --安卓直接H5支付
        callBack("2")
        --UnityEngine.Application.OpenURL(payUrl.."?uid="..uid.."&pay_id="..payid)
        --return
    end
end