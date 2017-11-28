--字符串工具
module("StringTool", package.seeall)

StringTool = {}

function StringTool:Split(szFullString,szSeparator)  
    local nFindStartIndex = 1  
    local nSplitIndex = 1  
    local nSplitArray = {}  
    while true do  
        local nFindLastIndex = string.find(szFullString, szSeparator, nFindStartIndex)  
        if not nFindLastIndex then  
            nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, string.len(szFullString))  
            break  
        end  
        nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, nFindLastIndex - 1)  
        nFindStartIndex = nFindLastIndex + string.len(szSeparator)  
        nSplitIndex = nSplitIndex + 1  
    end  
    return nSplitArray  
end  

--获取字符宽度
function StringTool:chsize(char)
     if not char then
         print("not char")
         return 0
     elseif char > 240 then
         return 4
     elseif char > 225 then
         return 3
     elseif char > 192 then
         return 2
     else
         return 1
     end
end

--获取UTF8字符串字符个数
function StringTool:utf8len(str)
    local len = 0
    local currentIndex = 1
    while currentIndex <= #str do
        local char = string.byte(str, currentIndex)
        currentIndex = currentIndex + self:chsize(char)
        len = len +1
    end
    return len
end

function StringTool:utf8sub(str, startChar, numChars)
     local startIndex = 1
     while startChar > 1 do
        local char = string.byte(str, startIndex)
         startIndex = startIndex + self:chsize(char)
         startChar = startChar - 1
     end
 
     local currentIndex = startIndex
 
     while numChars > 0 and currentIndex <= #str do
         local char = string.byte(str, currentIndex)
         currentIndex = currentIndex + self:chsize(char)
         numChars = numChars -1
     end
     return str:sub(startIndex, currentIndex - 1)
end