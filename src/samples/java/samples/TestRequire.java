package samples;


import shibaInu.tolua.LuaState;

/**
 * 测试 Lua 中的 require() 函数
 * Created by LOLO on 2020/04/27.
 */
public class TestRequire {


    public void run() {

        LuaState state = new LuaState();

        state.doFile("TestRequire");

    }


    //
}
