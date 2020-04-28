package samples;

import shibaInu.tolua.LuaFunction;
import shibaInu.tolua.LuaState;


/**
 * 在 Java 中调用 Lua 函数
 * Created by LOLO on 2020/04/25.
 */
public class JavaCallLua {


    public void run() {

        LuaState state = new LuaState();


        // 在 lua 中 创建 luaGlobalFn() 全局函数
        state.doString("" +
                " function luaGlobalFn(v) " +
                "   print('luaGlobalFn:', v) " +
                " end "
        );
        // 在 java 中调用函数
        LuaFunction luafn = state.getFunction("luaGlobalFn");
        luafn.begin();
        luafn.push("使用 LuaFunction 对象调用");
        luafn.call();
        luafn.end();
        // doString() 方式调用
        state.doString("luaGlobalFn('使用 doString() 调用' .. 123)");


        // require JavaCallLua.lua
        state.doFile("JavaCallLua");

        // 获取 JavaCallLua.lua 中定义的 min_max() 全局函数
        LuaFunction min_max = state.getFunction("min_max");
        min_max.begin();
        min_max.push(true);// arg1
//        min_max.push(false);
        min_max.push(123);// arg2
        min_max.push(456);// arg3
        min_max.call();
        int retval = (int) min_max.getNumber();// 获取函数返回值
        min_max.end();

        System.out.println(retval);
    }


    //
}
