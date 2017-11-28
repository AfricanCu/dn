--数据表管理器
module("DataTable",package.seeall)

DTMgr = {}

DTMgr["TableSet"] = {}
--将列表中每一行的key值跟index关联
local TableKeyValue = {}

--获取数据表
-- tabelName 数据表名称
function DTMgr:GetTabel(tabelName)
    local table = DTMgr["TableSet"]["DataTable."..tabelName];

    if TableKeyValue[tabelName] == nil then
        local kvTable = {};
        local data = table["data"];
        local i = 1
        for k,v in pairs(data) do
            kvTable[v[1]] = i
            i = i + 1
        end
        TableKeyValue[tabelName] = kvTable
    end

    return table;
end

--查询数据表
--t 要查询的数据表
--KeyName key字段名
--Key Kye值
--valueTitel 要获取的字段名
function DTMgr:GetValueByKey(t,KeyName,Key,valueTitel)

    local titels = t["titels"];
    --local length = table.getn(titels);

    local index = titels[KeyName]
    local valueIndex = titels[valueTitel]
    local row = nil;

    if nil == index then
        return nil
    end
    if nil == valueIndex then
        return nil
    end

    local data = t["data"];
    for k,v in ipairs(data) do
        if(v[index] == Key) then
            row = v;
            break
        end
    end


    if nil == row then
        return nil;
    end

    return row[valueIndex]
end

--返回表中的一行
--t 要查询的数据表
--KeyName key字段名
--Key Kye值
function DTMgr:GetRowByKey(t,tabelName,key)
    local row = nil;
    local keyIndex = TableKeyValue[tabelName][key]

    if nil == keyIndex then
        return nil
    end

    row = t["data"][keyIndex]

    return row
end

--返回表中关键字的值
--t 要查询的数据表
--KeyName key字段名
function DTMgr:GetKeyIndex(t,KeyName)

    local titels = t["titels"];

    local index = titels[KeyName]
    
    return index
end

