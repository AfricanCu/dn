--栈
module("Core.Stack",package.seeall)

Stack = {}
Stack.stack_table = {}


--创建一个栈容器
function Stack:new(o)
	o = o or {};
    setmetatable(o, { __index = self });     
    return o;
end

function Stack:Push(element)
    self.stack_table[self:Count() + 1] = element
end

function Stack:Pop()
    if self:IsEmpty() then
        Debug.log("Error: Stack is empty!")
        return
    end
    local value = self.stack_table[self:Count()]
    table.remove(self.stack_table,self:Count())
    return value
end

function Stack:Top()
    if self:IsEmpty() then
        Debug.log("Error: Stack is empty!")
        return
    end
    return self.stack_table[self:Count()]
end

function Stack:IsEmpty()
    local size = self:Count()
    if size == 0 then
        return true
    end
    return false
end

function Stack:Count()
    return #self.stack_table
end

function Stack:Clear()
    self.stack_table = nil
    self.stack_table = {}
end
