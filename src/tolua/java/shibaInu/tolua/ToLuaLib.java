package shibaInu.tolua;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;


/**
 * ToLua 动态链接库接口
 * Created by LOLO on 2020/04/20.
 */
public interface ToLuaLib extends Library {


    //
    ToLuaLib INSTANCE = Native.load("tolua", ToLuaLib.class);


    //
    Pointer luaL_newstate();


    //
    IntByReference lua_atpanic(Pointer L, LibCallback panicf);

    void lua_close(Pointer L);

    int lua_gettop(Pointer L);

    void lua_settop(Pointer L, int top);

    int lua_pcall(Pointer L, int nargs, int nresults, int msgh);

    int lua_isstring(Pointer L, int idx);

    int lua_type(Pointer L, int idx);

    boolean lua_toboolean(Pointer L, int idx);

    double lua_tonumber(Pointer L, int idx);

    IntByReference lua_topointer(Pointer L, int idx);

    void lua_rawgeti(Pointer L, int idx, int n);

    void lua_rawseti(Pointer L, int tableIndex, int index);


    void lua_pushnil(Pointer L);

    void lua_pushstring(Pointer L, String str);

    void lua_pushboolean(Pointer L, int value);

    void lua_pushnumber(Pointer L, double number);


    //
    int luaopen_cjson(Pointer L);

    int luaopen_cjson_safe(Pointer L);


    //
    int tolua_loadbuffer(Pointer L, byte[] buff, int sz, String name);

    void tolua_openlibs(Pointer L);

    void tolua_pushtraceback(Pointer L);

    void tolua_pushcfunction(Pointer L, LibCallback fn);

    int tolua_setfield(Pointer L, int idx, String key);

    int tolua_getfield(Pointer L, int idx, String key);

    int tolua_objlen(Pointer L, int stackPos);

    String tolua_tolstring(Pointer L, int idx, IntByReference strLen);

    int tolua_beginpcall(Pointer L, int fnRef);


    //
    int toluaL_ref(Pointer L);


    //
    interface LibCallback extends Callback {
        int invoke(Pointer L);
    }


    //
}
