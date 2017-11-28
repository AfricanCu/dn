--队列
module("Core.Queue",package.seeall)

Queue = {}
Queue.queue_table = {}

--创建一个队列容器
function Queue:new(o)
	o = o or {};
    setmetatable(o, { __index = self });     
    return o;
end

function Queue:EnQueue(element)
    table.insert(self.queue_table,element)  --插入队尾
end

function Queue:DeQueue()
    if self:IsEmpty() then
        Debug.log("Error: The Queue is empty.")
        return
    end
    local value = self.queue_table[1]
    table.remove(self.queue_table,1) --从队尾取元素
    return value 
end

function Queue:Peek()
    if self:IsEmpty() then
        Debug.log("Error: The Queue is empty.")
        return
    end
    return self.queue_table[self:Count()]
end

function Queue:Clear()
    self.queue_table = nil
    self.queue_table = {}
end

function Queue:IsEmpty()
    if self:Count() == 0 then
        return true
    end
    return false
end

function Queue:Count()
    return #self.queue_table
end

