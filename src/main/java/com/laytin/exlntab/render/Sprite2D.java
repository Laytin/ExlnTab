package com.laytin.exlntab.render;

public class Sprite2D {
    public int x = 0;

    public int y = 0;

    public int width = 0;

    public int height = 0;

    public Sprite2D() {}

    public Sprite2D(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Sprite2D(float x, float y, float width, float height) {
        this((int)x, (int)y, (int)width, (int)height);
    }
}
