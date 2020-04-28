--


print("TestRequire.lua")


--
local function Main()
    print("TestRequire.lua # Main()")
    local tab = require("Abc.Xyz.Require1")

    tab:Test1("abc", 123)
    tab.Test2("xyz", 456)

    print(aaa.sss)
    print("上面那句会报错，不会执行到这句！")
end


--
local function errorHandler(msg)
    print("!!!!!!!! catch !!!!!!!!")
    print(msg)
    print("!!!!!!!!!!!!!!!!!!!!!!!")
end


--
xpcall(Main, errorHandler)