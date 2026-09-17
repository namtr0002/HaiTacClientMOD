package com.htth.j2me;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MImage {
    private final Texture texture;
    private final TextureRegion region;

    public MImage(String internalPath) {
        this.texture = new Texture(Gdx.files.internal(internalPath));
        this.region = new TextureRegion(texture);
    }

    public TextureRegion getRegion() {
        return region;
    }

    public void dispose() {
        texture.dispose();
    }
}


