package com.devengine.test;

import com.devengine.core.EngineManager;
import com.devengine.core.WindowsManager;
import com.devengine.core.utils.Consts;
import org.lwjgl.Version;

public class Main {

    private static WindowsManager window;
    private static TestGame game;
//    private static EngineManager engine;

    public static void main(String[] args) {
        System.out.println(Version.getVersion());
        window = new WindowsManager(Consts.TITLE, 0, 0, false);
        game = new TestGame();
        EngineManager engine = new EngineManager();

        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static WindowsManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }
}
