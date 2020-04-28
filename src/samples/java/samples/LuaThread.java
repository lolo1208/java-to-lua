package samples;


import shibaInu.tolua.LuaState;

import java.util.Random;


/**
 * lua state 在多线程环境下运行
 */
public class LuaThread extends Thread {


    public LuaThread(String name) {
        this.setName(name);
    }

    @Override
    public void run() {

        LuaState state = new LuaState();

        state.doFile("LuaThread");
        state.doString(String.format("SetName('%s')", getName()));

        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(Math.abs(new Random().nextLong() % 200));
                state.doString("Next()");
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        System.out.println(getName() + "    done.");
        state.destroy();
    }


    //
}
