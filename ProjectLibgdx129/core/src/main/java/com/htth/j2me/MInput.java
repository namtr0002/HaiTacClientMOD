package com.htth.j2me;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MInput {
    public static boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }

    public static int mapJ2MEKeyToGdx(int j2meKey) {
        switch (j2meKey) {
            case -1: return Input.Keys.UP;
            case -2: return Input.Keys.DOWN;
            case -3: return Input.Keys.LEFT;
            case -4: return Input.Keys.RIGHT;
            case -5: return Input.Keys.ENTER;
            default: return j2meKey;
        }
    }
}


