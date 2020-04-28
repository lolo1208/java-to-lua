package samples;

import com.sun.jna.Pointer;
import shibaInu.tolua.LuaState;


/**
 * 在 Lua 中调用 Java 函数
 * Created by LOLO on 2020/04/25.
 */
public class LuaCallJava {

    private LuaState state;

    public void run() {

        state = new LuaState();

        // 注册 lua 全局函数 out()，对应回调为 test_out()
        state.addStringCallback("out", this::test_out);
        state.addStringCallback("sum", this::test_sum);
        state.addStringCallback("max", this::test_max);

        // 也可以注册一个已知参数类型的函数，减少没必要的类型转换
        state.addCallback("say", this::test_say);


        //
        state.doString("" +
                " local retval = out(123, 444, true) " +
                " print('out() return:', retval) "
        );
        state.doFile("LuaCallJava");// LuaCallJava.lua
    }


    private void test_out(String[] args) {
        System.out.println("!!!!!! test_out args !!!!!!");
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        // 可以返回一个 String, boolean, int, float, double 类型的值。默认会返回：nil
//        state.push("--J#Diwjd@kkd(Dz--");
//        state.push(true);
//        state.push(123);
//        state.push(4.56);
    }


    private void test_sum(String[] args) {
        double val = 0;
        for (int i = 0; i < args.length; i++) {
            val += Double.parseDouble(args[i]);
        }
        state.push(val);// return val
    }

    private void test_max(String[] args) {
        double val = Double.MIN_VALUE;
        for (int i = 0; i < args.length; i++) {
            double v = Double.parseDouble(args[i]);
            if (v > val) val = v;
        }
        state.push(val);
    }


    /**
     * 已知参数类型的函数
     */
    private int test_say(Pointer L) {
        String arg1 = state.getString(1);// 参数 index 从 1 开始
        double arg2 = state.getNumber(2);
        int arg3 = (int) state.getNumber(3);
        boolean arg4 = state.getBoolean(4);
        System.out.printf("test_say: %s, %f, %d, %b %n", arg1, arg2, arg3, arg4);

        // 函数没有返回值时，return 0
//        return 0;

        // 函数有返回值时，push() 返回值，并 return 1
        state.push("OK!!!");
        return 1;
    }


    //
}
