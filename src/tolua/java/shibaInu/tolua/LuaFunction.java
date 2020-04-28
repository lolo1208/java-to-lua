package shibaInu.tolua;

import com.sun.jna.Pointer;


/**
 * Lua 函数
 * 对 lua 函数的调用至少包含 3 个步骤：
 * begin() -> 2.call() -> 3.end()
 * Created by LOLO on 2020/04/25.
 */
public class LuaFunction {

    //

    private LuaState state;
    private Pointer L;
    private int fnRef;
    private String name;

    private int argCount = 0;
    private int oldTop = -1;


    public LuaFunction(LuaState state, String name) {
        this.state = state;
        this.L = state.L;
        this.name = name;

        ToLua.getglobal(L, name);
        fnRef = ToLua.ref(L);
    }


    /**
     * 开始调用该函数
     */
    public void begin() {
        argCount = 0;
        oldTop = ToLua.beginpcall(L, fnRef);
    }

    /**
     * 调用函数
     */
    public void call() {
        if (!ToLua.pcall(L, argCount, ToLua.LUA_MULTRET, oldTop)) {
            state.errorHandler();
        }
    }

    /**
     * 结束对该函数的调用
     */
    public void end() {
        if (oldTop != -1) {
            ToLua.settop(L, oldTop - 1);
            oldTop = -1;
        }
    }


    /**
     * 在调用时，添加函数的参数值
     * 支持类型 String, boolean, int, float, double
     * 请在调用 call() 函数之前调用 push()
     */
    public void push(String value) {
        argCount++;
        state.push(value);
    }

    public void push(boolean value) {
        argCount++;
        state.push(value);
    }

    public void push(int value) {
        argCount++;
        state.push(value);
    }

    public void push(float value) {
        argCount++;
        state.push(value);
    }

    public void push(double value) {
        argCount++;
        state.push(value);
    }


    /**
     * 获取函数的返回值
     */
    public String getString() {
        return ToLua.tostring(L, oldTop + 1);
    }

    public boolean getBoolean() {
        return ToLua.toboolean(L, oldTop + 1);
    }

    public double getNumber() {
        return ToLua.tonumber(L, oldTop + 1);
    }


    //

    public String getName() {
        return name;
    }


    //
}
