--


local pairs = pairs
local arr1 = {}
local arr2 = { "a", "b", "c", "d", "e" }

local function fn1()
    local n = 1
    local len = 999
    local v1, v2
    for i = 1, 9999 do
        if i % 2 == 0 then
            n = n + 2
            n = n * 2
        else
            n = n - 2
            n = n / 2
        end

        for j = 1, len do
            v1 = arr1[j]
        end

        for k, v in pairs(arr2) do
            v2 = v
        end
    end
end


--
local t = 0

function fn2()
    local time
    for i = 1, 5 do
        time = getTime()
        fn1()
        t = getTime() - time + t
    end
    print(string.format("5 times, ave: %sms", t / 5))
end

fn2()


