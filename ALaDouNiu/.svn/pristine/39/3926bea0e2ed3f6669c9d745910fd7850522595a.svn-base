module("IosPendingTool", package.seeall)

IosPendingTool = {}

--ios是否在审核期间(如果在审核true, 审核过了false)
local iosIsPending = false

--是否为ios平台，且是否再审核期间
function IosPendingTool.IsPending()
    local platform = PlatformTool.Instance:GetPlatformID()
    if platform == 2 and iosIsPending then
        return true
    else
        return false
    end
end


function IosPendingTool.IsIos()
    
    local platform = PlatformTool.Instance:GetPlatformID()
    if platform == 2 then
        return true
    else
        return false
    end
end