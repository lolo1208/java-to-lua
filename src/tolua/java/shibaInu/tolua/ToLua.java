package shibaInu.tolua;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;


/**
 * 封装 ToLua 接口的工具类
 * Created by LOLO on 2020/04/23.
 */
public class ToLua {


    //
    // interface
    public interface StringCallback {
        void invoke(String[] args);
    }


    //
    // enum
    public enum LUA_TYPE {
        NIL, BOOLEAN, LIGHTUSERDATA, NUMBER, STRING, TABLE, FUNCTION, USERDATA, THREAD
    }


    //
    // static
    public static String[] LUA_TYPE_NAME = {"nil", "boolean", "lightuserdata", "number", "string", "table", "function", "userdata", "thread"};
    public static int LUA_MULTRET = -1;
    public static int LUA_REGISTRYINDEX = -10000;
    public static int LUA_GLOBALSINDEX = -10002;


    /**
     * Error Handler
     */
    private static void errorHandler(Pointer L) {
        LuaState.getState(L).errorHandler();
    }


    //
    // luaL
    public static Pointer newstate() {
        return ToLuaLib.INSTANCE.luaL_newstate();
    }


    //
    // lua
    public static IntByReference lua_atpanic(Pointer L, ToLuaLib.LibCallback panicf) {
        return ToLuaLib.INSTANCE.lua_atpanic(L, panicf);
    }

    public static void close(Pointer L) {
        ToLuaLib.INSTANCE.lua_close(L);
    }

    public static boolean pcall(Pointer L, int nargs, int nresults, int msgh) {
        return ToLuaLib.INSTANCE.lua_pcall(L, nargs, nresults, msgh) == 0;
    }

    public static void settop(Pointer L, int top) {
        ToLuaLib.INSTANCE.lua_settop(L, top);
    }

    public static void rawgeti(Pointer L, int idx, int n) {
        ToLuaLib.INSTANCE.lua_rawgeti(L, idx, n);
    }

    public static void rawseti(Pointer L, int tableIndex, int index) {
        ToLuaLib.INSTANCE.lua_rawseti(L, tableIndex, index);
    }

    public static int gettop(Pointer L) {
        return ToLuaLib.INSTANCE.lua_gettop(L);
    }


    public static boolean isnil(Pointer L, int idx) {
        return LUA_TYPE.NIL.ordinal() == ToLuaLib.INSTANCE.lua_type(L, idx);
    }

    public static boolean isboolean(Pointer L, int idx) {
        int t = ToLuaLib.INSTANCE.lua_type(L, idx);
        return t == LUA_TYPE.BOOLEAN.ordinal() || LUA_TYPE.NIL.ordinal() == t;
    }

    public static boolean isstring(Pointer L, int idx) {
        return ToLuaLib.INSTANCE.lua_isstring(L, idx) == 1; // true = 1
    }

    public static String typename(Pointer L, int stackPos) {
        return LUA_TYPE_NAME[ToLuaLib.INSTANCE.lua_type(L, stackPos)];
    }


    public static boolean toboolean(Pointer L, int idx) {
        return ToLuaLib.INSTANCE.lua_toboolean(L, idx);
    }

    public static double tonumber(Pointer L, int idx) {
        return ToLuaLib.INSTANCE.lua_tonumber(L, idx);
    }

    public static IntByReference topointer(Pointer L, int idx) {
        return ToLuaLib.INSTANCE.lua_topointer(L, idx);
    }


    public static void pushnil(Pointer L) {
        ToLuaLib.INSTANCE.lua_pushnil(L);
    }

    public static void pushstring(Pointer L, String str) {
        ToLuaLib.INSTANCE.lua_pushstring(L, str);
    }

    public static void pushboolean(Pointer L, boolean value) {
        ToLuaLib.INSTANCE.lua_pushboolean(L, value ? 1 : 0);
    }

    public static void pushnumber(Pointer L, int value) {
        ToLuaLib.INSTANCE.lua_pushnumber(L, value);
    }

    public static void pushnumber(Pointer L, float value) {
        ToLuaLib.INSTANCE.lua_pushnumber(L, value);
    }

    public static void pushnumber(Pointer L, double value) {
        ToLuaLib.INSTANCE.lua_pushnumber(L, value);
    }


    //
    // luaopen
    public static int luaopen_cjson(Pointer L) {
        return ToLuaLib.INSTANCE.luaopen_cjson(L);
    }

    public static int luaopen_cjson_safe(Pointer L) {
        return ToLuaLib.INSTANCE.luaopen_cjson_safe(L);
    }



    //
    // tolua
    public static void openlibs(Pointer L) {
        ToLuaLib.INSTANCE.tolua_openlibs(L);
    }

    public static void pushcfunction(Pointer L, ToLuaLib.LibCallback fn) {
        ToLuaLib.INSTANCE.tolua_pushcfunction(L, fn);
    }

    public static void pushtraceback(Pointer L) {
        ToLuaLib.INSTANCE.tolua_pushtraceback(L);
    }

    public static boolean loadbuffer(Pointer L, byte[] buff, int sz, String name) {
        return ToLuaLib.INSTANCE.tolua_loadbuffer(L, buff, sz, name) == 0;
    }


    public static void getfield(Pointer L, int idx, String key) {
        if (ToLuaLib.INSTANCE.tolua_getfield(L, idx, key) != 0)
            errorHandler(L);
    }

    public static void setfield(Pointer L, int idx, String key) {
        if (ToLuaLib.INSTANCE.tolua_setfield(L, idx, key) != 0)
            errorHandler(L);
    }

    public static void getglobal(Pointer L, String key) {
        getfield(L, LUA_GLOBALSINDEX, key);
    }

    public static void setglobal(Pointer L, String key) {
        setfield(L, LUA_GLOBALSINDEX, key);
    }

    public static void pop(Pointer L, int amount) {
        settop(L, -(amount) - 1);
    }


    public static int objlen(Pointer L, int stackPos) {
        return ToLuaLib.INSTANCE.tolua_objlen(L, stackPos);
    }


    public static String tostring(Pointer L, int idx) {
        IntByReference len = new IntByReference();
        if (len.getPointer() != Pointer.NULL) {
            return ToLuaLib.INSTANCE.tolua_tolstring(L, idx, len);
        }
        return null;
    }


    public static int beginpcall(Pointer L, int fnRef) {
        return ToLuaLib.INSTANCE.tolua_beginpcall(L, fnRef);
    }


    //
    // toluaL
    public static int ref(Pointer L) {
        return ToLuaLib.INSTANCE.toluaL_ref(L);
    }


    //
}
