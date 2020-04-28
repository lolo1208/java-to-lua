package samples;

import com.sun.jna.Pointer;
import shibaInu.tolua.LuaState;
import shibaInu.tolua.ToLua;


/**
 * LUA / JIT / JAVA 运行效率测试
 * Created by LOLO on 2020/04/27.
 */
public class TestLuaJit {

    public void run() {

        LuaState state = new LuaState();
        state.addCallback("getTime", this::getTime);
        System.out.println();


        // jit 目前不支持在 MacOS 下运行
        if (!System.getProperty("os.name").startsWith("Mac OS")) {
            System.out.println("-----------[ JIT ]-----------");
            state.doFile("TestLuaJit_JitBytes");
            System.out.println();
        }


        // lua
        System.out.println("-----------[ LUA ]-----------");
        state.closeJIT();// 关闭 jit
        state.doFile("TestLuaJit");
        System.out.println();


        // java
        System.out.println("-----------[ JAVA ]----------");
        fn2();
        System.out.println();

    }

    // lua global function getTime()
    private int getTime(Pointer L) {
        ToLua.pushnumber(L, (int) System.currentTimeMillis());
        return 1;
    }


    // java

    private int[] arr1 = new int[999];
    private String[] arr2 = new String[]{"a", "b", "c", "d", "e"};

    private void fn1() {
        int n = 1;
        int len = arr1.length;
        int v1;
        String v2;
        for (int i = 0; i < 9999; i++) {
            if (i % 2 == 0) {
                n = n + 2;
                n = n * 2;
            } else {
                n = n - 2;
                n = n / 2;
            }

            for (int j = 0; j < len; j++) {
                v1 = arr1[j];
            }

            for (String v : arr2) {
                v2 = v;
            }
        }
    }

    private void fn2() {
        long t = 0;
        long time;
        for (int i = 0; i < 5; i++) {
            time = System.currentTimeMillis();
            fn1();
            t = System.currentTimeMillis() - time + t;
        }
        System.out.printf("5 times, ave: %sms%n", t / 5);
    }


    //
}
