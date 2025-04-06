package ianeli.moredyes.Entity;

import net.minecraft.client.render.VertexConsumer;

public class ColorableVertexConsumer implements VertexConsumer {
    private final VertexConsumer parent;
    private final int color;
    private float red, green, blue, alpha;

    public ColorableVertexConsumer(VertexConsumer parent, int color) {
        this.parent = parent;
        this.color = color;
        this.red = (color >> 16 & 0xFF) / 255f;
        this.green = (color >> 8 & 0xFF) / 255f;
        this.blue = (color & 0xFF) / 255f;
        this.alpha = (color >> 24 & 0xFF) / 255f;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        /*return parent.color(
                (int)(red * this.red),
                (int)(green * this.green),
                (int)(blue * this.blue),
                (int)(alpha * this.alpha)
        );*/
        return parent.color(red, green, blue, alpha);
    }

    @Override
    public VertexConsumer vertex(float x, float y, float z) {
        return parent.vertex(x, y, z);
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        return parent.texture(u, v);
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        return parent.overlay(u, v);
    }

    @Override
    public VertexConsumer light(int u, int v) {
        return parent.light(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return parent.normal(x, y, z);
    }

}
