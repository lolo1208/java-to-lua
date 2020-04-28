package samples;

import shibaInu.tolua.LuaFileLoader;


public class Main {

    public static void main(String[] args) {

        // 设置全局 LuaFileLoader 实例的 lua 文件目录列表
        LuaFileLoader.instance.dirs = new String[]{"./src/samples/lua/", "./lua/", "./"};

//        args = new String[] {"5"};
        boolean all = args.length == 0;

        if (all || args[0].equals("1")) {
            System.out.println("\n---------- [DoSomething] ----------\n");
            new DoSomething().run();
        }

        if (all || args[0].equals("2")) {
            System.out.println("\n---------- [TestRequire] ----------\n");
            new TestRequire().run();
        }

        if (all || args[0].equals("3")) {
            System.out.println("\n---------- [LuaCallJava] ----------\n");
            new LuaCallJava().run();
        }

        if (all || args[0].equals("4")) {
            System.out.println("\n---------- [JavaCallLua] ----------\n");
            new JavaCallLua().run();
        }

        if (all || args[0].equals("5")) {
            System.out.println("\n----------- [TestLuaJit] ----------\n");
            new TestLuaJit().run();
        }

        if (!all && args[0].equals("6")) {
            System.out.println("\n----------- [LuaThread] -----------\n");
            for (int i = 0; i < 10; i++) {
                new LuaThread("LuaState " + i).start();
            }
        }

    }
}
