package com.htth.j2me;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MGraphics {
    private final SpriteBatch batch;
    private final BitmapFont font;

    public MGraphics(SpriteBatch batch, BitmapFont font) {
        this.batch = batch;
        this.font = font;
    }

    public void drawImage(TextureRegion region, float x, float y) {
        batch.draw(region, x, y);
    }

    public void drawString(String text, float x, float y, Color color) {
        font.setColor(color);
        font.draw(batch, text, x, y);
    }
}


