--基于WWW 封装的Http访问接口
module("NetWork.NetHttp",package.seeall)

--发起Http请求，这个调用是异步的
function  SendRequest(url,fun,wwwfrom)
    function coroFun()
        local www = nil
        if nil == wwwfrom then
            www = UnityEngine.WWW(ur)
        else
            www = UnityEngine.WWW(url,wwwfrom)
        end
	    coroutine.www(www)
        if nil ~= www.error then
            Debug.log("error： "..www.error)
            fun(false,nil) --是否成功，返回的数据
            return
        end
        local strdata = tolua.tolstring(www.bytes)
        if nil ~= fun then
            fun(true,strdata)--是否成功，返回的数据
        end
    end
    coroutine.start(coroFun)
end


--发起一个WWW请求，回调函数 callBack 会传入一个 www 对象
function WWW(url,callBack,wwwfrom)
    function coroFun()
        local www = nil
        if nil == wwwfrom then
            www = UnityEngine.WWW(url)
        else
            www = UnityEngine.WWW(url,wwwfrom)
        end
	    coroutine.www(www)
        local isOk = true
        if nil ~= www.error then
            Debug.log("error： "..www.error)
            Debug.log("error url："..www.url)
            isOk = false
            www:Dispose()
            www = nil
        end
        if callBack then
            if type(callBack) == "function" then
                callBack(isOk,www) --是否成功，返回的数据
            end
        end
        if nil ~= www then
            www:Dispose()
        end
    end
    coroutine.start(coroFun)
end

--发起一个WWW请求图片下载，如果图片已经下载过，则从缓存里面读取，如果没有下载过，则从网上下载
function WWWTexture(url,callBack)
    local urlHash = PlatformTool.Instance:GetStringHashCode(url)
    local filePath = UnityEngine.Application.persistentDataPath.."/"..urlHash
    if System.IO.File.Exists(filePath) then
        Debug.log("加载本地图片")
        local localPath = "file:///"..filePath
        local WWW = require("NetWork.NetHttp").WWW
        WWW(localPath,callBack)
    else
        Debug.log("需要下载图片")  
        local WWW = require("NetWork.NetHttp").WWW
        function callBackWarp(isok,wwwrtn)
            if isok then
                image = wwwrtn.texture
                local pngData = image:EncodeToPNG()
                System.IO.File.WriteAllBytes (filePath, pngData)
            end

            if callBack then
                if type(callBack) == "function" then
                    callBack(isok, wwwrtn)
                end
            end

        end
        WWW(url,callBackWarp)
    end
end
