--



-- sum() 和 max() 都是在 java 定义的全局函数
local retval = sum(100, "200", 0.000321, max(1, 2, 5, 4), 0xff, -255)
print("sum() return:", retval)
print("             ", retval - 100)


-- 按 say() 的参数类型来传参
retval = say("Hi!", math.pi * 10, 999, true)
print("say() return:", retval)

