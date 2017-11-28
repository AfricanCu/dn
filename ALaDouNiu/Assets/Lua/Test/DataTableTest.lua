local DTMgr = require("DataTable").DTMgr

function test()
    DTMgr = DTMgr
    t = DTMgr:GetTabel("test")
    value = DTMgr:GetValueByKey(t,"ID",4,"test2")
    print(value)
end