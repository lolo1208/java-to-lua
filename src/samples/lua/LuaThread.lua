--



-- test require
local n = 0
n = n + require("Abc.Xyz.N1")
n = n + require("Abc.Xyz.N2")
n = n + require("Abc.Xyz.N3")

local name
local step = 0


--
function SetName(value)
    name = value
    print("LuaThread.lua", name, "n ".. n)
end


--
function Next()
    step = step + 1
    print(name, "step " .. step)
end


