package samples;

import shibaInu.tolua.LuaState;


/**
 * 随便测测
 * Created by LOLO on 2020/04/25.
 */
public class DoSomething {


    public void run() {

        LuaState state = new LuaState();

        // print value
        state.doString("print(1, 'stringgg')");
        state.doString("print(2, 123)");
        state.doString("print(3, 45.567)");
        state.doString("print(4, true)");
        state.doString("print('5', false)");
        state.doString("print(6, nil)");
        state.doString("print(7, {})");
        state.doString("local tbl = {} print(8, tbl)");
        state.doString("local fn = function() end print(9, fn)");
        state.doString("print('--')");


        // create & call function
        state.doString("function LuaGlobalFn() print('Global Function!!') end");
        state.doString("LuaGlobalFn()");

        state.doString("" +
                " local function LuaLocalFn() " +
                "   print('Local Function!!') " +
                " end " +
                " LuaLocalFn() "// 同一段代码块才能访问 local 变量和函数
        );
        state.doString("print('LuaLocalFn:', LuaLocalFn)");// 已经是另一段代码块了，无法访问 LuaLocalFn


        // do require()
        state.doString("require('DoSomething')");// DoSomething.lua
    }


    //
}
