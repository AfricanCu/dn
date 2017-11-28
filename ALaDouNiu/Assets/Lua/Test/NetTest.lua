--网络测试

SendRequest = require("NetWork.NetHttp").SendRequest

function Test()
    wwwfrom = UnityEngine.WWWForm()
    wwwfrom:AddField("type","login")
    wwwfrom:AddField("userName","222")
    --local data = NetHelper.SendHttpRequestBySyn("http://www.baidu.com",5000)
    --Debug.log(data)
    --NetHelper.SendHttpRequestByASyn("http://192.168.0.42:8115/wolfKillLogin/functionServlet",wwwfrom,CallBack);
    --NetHelper.SendHttpRequestByASyn("http://www.baidu.com",wwwfrom,CallBack);
    local url = "http://192.168.0.42:8115/wolfKillLogin/functionServlet"
    --local url = "http://www.baidu.com"
    SendRequest(url,CallBack,wwwfrom)
end


function CallBack(dataStr)
    Debug.log(dataStr)
end