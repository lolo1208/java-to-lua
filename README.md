# java-to-lua
使用 [JNA](https://github.com/java-native-access/jna) 在 Java 中运行 [ToLua](https://github.com/topameng/tolua_runtime)



# Lua JIT
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
