# java-to-lua
本项目使用 [JNA](https://github.com/java-native-access/jna) 在 Java 中运行 [ToLua](https://github.com/topameng/tolua_runtime)

实现了 Java 与 Lua 相互调用和传值

Linux 和 Windows 环境可以 JIT 模式运行 Lua


# 代码效率测试
```
ver20100 jit:     true    SSE2    SSE3    SSE4.1    BMI2    fold    cse    dce    fwd    dse    narrow    loop    abc    sink    fuse
os: Linux, arch: x64

-----------[ JIT ]-----------
load lua file: TestLuaJit_JitBytes.lua
5 times, ave: 5.8ms

-----------[ LUA ]-----------
load lua file: TestLuaJit.lua
5 times, ave: 271.2ms

-----------[ JAVA ]----------
5 times, ave: 4ms
```
