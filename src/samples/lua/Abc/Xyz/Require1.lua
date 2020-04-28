--


print("Require1.lua")

require("Abc.Require2") -- require 同一个文件，可以重复调用，只会加载一次
require("Abc.Require2")
require("Abc.Require2")
local fn = require("Abc.Require2")

local tab = {}

function tab:Test1(arg1, arg2)
    print("MyClass.Test1()", self, arg1, arg2)
end

function tab.Test2(arg1, arg2)
    print("MyClass.Test2()", self, arg1, arg2, fn())
end

return tab