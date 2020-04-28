package shibaInu.tolua;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.util.HashMap;
import java.util.Hashtable;


/**
 * Lua State
 * Created by LOLO on 2020/04/20.
 */
public class LuaState {


    // 已创建的 lua state 列表，L 为 key
    private static Hashtable<Pointer, LuaState> stateList = new Hashtable<>();

    /**
     * 使用 L 来获取对应的 lua state
     */
    public static LuaState getState(Pointer L) {
        return stateList.get(L);
    }


    // jit opt
    private static final String JIT_OPT = "" +
            " if jit then " +
            "     if jit.opt then " +
            "         jit.opt.start(3) " +
            "     end " +
            "     print('ver' .. jit.version_num .. ' jit: ', jit.status()) " +
            "     print(string.format('os: %s, arch: %s', jit.os, jit.arch)) " +
            " end ";

    private static final String JIT_CLOSE = "" +
            " if jit then " +
            "     jit.off() " +
            "     jit.flush() " +
            " end ";


    //

    public Pointer L;
    public LuaFileLoader luaFileLoader;
    private StringBuilder sb = new StringBuilder();
    private HashMap<String, LuaFunction> fnList = new HashMap<>();


    public LuaState() {
        this(LuaFileLoader.instance);
    }

    public LuaState(LuaFileLoader luaFileLoader) {
        this.luaFileLoader = luaFileLoader;
        this.L = ToLua.newstate();
        stateList.put(L, this);

        ToLua.openlibs(L);
        // panic()
        ToLua.lua_atpanic(L, this::panic);
        // print()
        addCallback("print", this::print);

        // package.loaders[#+1]()
        ToLua.getglobal(L, "package");
        ToLua.getfield(L, -1, "loaders");
        ToLua.pushcfunction(L, this::loader);
        for (int i = ToLua.objlen(L, -2) + 1; i > 2; i--) {
            ToLua.rawgeti(L, -2, i - 1);
            ToLua.rawseti(L, -3, i);
        }
        ToLua.rawseti(L, -2, 2);
        ToLua.pop(L, 2);

        // top->0
        ToLua.settop(L, 0);

        // 默认开启 jit
        doString(JIT_OPT);
    }


    /**
     * Error Handler
     */
    private void errorHandler(String msg) {
        System.out.println(String.format("[ERROR] %s", msg));
        destroy();
    }

    public void errorHandler() {
        errorHandler(ToLua.tostring(L, -1));
    }

    private int panic(Pointer L) {
        errorHandler(String.format("PANIC: unprotected error in call to Lua API (%s)", ToLua.tostring(L, -1)));
        return 0;
    }


    /**
     * [lua] print()
     */
    private int print(Pointer L) {
        int num = ToLua.gettop(L);
        for (int i = 1; i <= num; i++) {
            if (i > 1) sb.append("    ");
            if (ToLua.isstring(L, i)) {
                sb.append(ToLua.tostring(L, i));
            } else if (ToLua.isnil(L, i)) {
                sb.append("nil");
            } else if (ToLua.isboolean(L, i)) {
                sb.append(ToLua.toboolean(L, i));
            } else {
                IntByReference p = ToLua.topointer(L, i);
                sb.append(ToLua.typename(L, i)).append(":0x").append(Integer.toHexString(p.getValue()));
            }
        }
        sb.append(System.lineSeparator());
        System.out.print(sb.toString());
        sb.setLength(0);
        return 0;
    }


    /**
     * [lua] package.loaders[#]() / require()
     */
    private int loader(Pointer L) {
        String fileName = ToLua.tostring(L, 1);
        loadFile(fileName);
        return 1; // 必须返回 1
    }


    //

    /**
     * 添加一个供 lua 调用的 java 函数
     * 这个函数在 lua 中是全局函数
     */
    public void addCallback(String name, ToLuaLib.LibCallback fn) {
        ToLua.pushcfunction(L, fn);
        ToLua.setglobal(L, name);
    }

    /**
     * 添加一个供 lua 调用的 java 函数
     * 这个函数在 lua 中是全局函数
     * 函数的参数类型将会全部转换成 String，并组合成 String 数组传给 fn
     */
    public void addStringCallback(String name, ToLua.StringCallback fn) {
        ToLuaLib.LibCallback cb = (L) ->
        {
            int num = ToLua.gettop(L);
            String val;
            String[] args = new String[num];
            for (int i = 1; i <= num; i++) {
                if (ToLua.isnil(L, i))
                    val = "nil";
                else if (ToLua.isboolean(L, i))
                    val = Boolean.toString(ToLua.toboolean(L, i));
                else
                    val = ToLua.tostring(L, i);
                args[i - 1] = val;
            }
            ToLua.pushnil(L);// 默认返回：nil
            fn.invoke(args);
            return 1;// 函数始终有返回值
        };
        addCallback(name, cb);
    }


    /**
     * 添加函数的返回值
     * 支持类型 String, boolean, int, float, double
     */
    public void push(String value) {
        ToLua.pushstring(L, value);
    }

    public void push(boolean value) {
        ToLua.pushboolean(L, value);
    }

    public void push(int value) {
        ToLua.pushnumber(L, value);
    }

    public void push(float value) {
        ToLua.pushnumber(L, value);
    }

    public void push(double value) {
        ToLua.pushnumber(L, value);
    }


    /**
     * 获取 lua 传来的参数值
     * index 从 1 开始
     */
    public String getString(int idx) {
        return ToLua.tostring(L, idx);
    }

    public boolean getBoolean(int idx) {
        return ToLua.toboolean(L, idx);
    }

    public double getNumber(int idx) {
        return ToLua.tonumber(L, idx);
    }

    //

    //

    /**
     * 关闭 jit
     */
    public void closeJIT() {
        doString(JIT_CLOSE);
    }


    /**
     * 在运行 lua 代码
     */
    public boolean doString(String s) {
        return doBuffer(s.getBytes(), "LuaState.java#doString()");
    }

    /**
     * 加载 lua 代码 buffer，并运行
     */
    public boolean doBuffer(byte[] buff, String name) {
        ToLua.pushtraceback(L);
        int oldTop = ToLua.gettop(L);
        if (ToLua.loadbuffer(L, buff, buff.length, "@" + name)) {
            if (ToLua.pcall(L, 0, ToLua.LUA_MULTRET, oldTop)) {
                ToLua.settop(L, oldTop - 1);
                return true;
            }
        }
        errorHandler();
        ToLua.settop(L, oldTop - 1);
        return false;
    }

    /**
     * 加载 lua 代码文件，并运行
     */
    public boolean doFile(String path) {
        byte[] buff = luaFileLoader.loadFile(path);
        if (buff == null) {
            errorHandler("[Java] " + "file:" + path + " not found");
            return false;
        }
        return doBuffer(buff, path);
    }

    /**
     * lua require()
     */
    public boolean loadFile(String path) {
        byte[] buff = luaFileLoader.loadFile(path);
        if (buff == null) {
            errorHandler("[Java] " + "file:" + path + " not found.");
            return false;
        }
        if (!ToLua.loadbuffer(L, buff, buff.length, "@" + path)) {
            errorHandler();
            return false;
        }
        return true;
    }


    /**
     * 获取 lua 函数
     */
    public LuaFunction getFunction(String name) {
        if (!fnList.containsKey(name))
            fnList.put(name, new LuaFunction(this, name));
        return fnList.get(name);
    }

    public void clearFunction(String name) {
        fnList.remove(name);
    }

    public void clearAllFunction() {
        fnList.clear();
    }


    /**
     * 销毁
     */
    public void destroy() {
        stateList.remove(L);
        ToLua.close(L);
    }


    //
}
