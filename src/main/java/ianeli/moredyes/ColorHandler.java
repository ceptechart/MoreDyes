package ianeli.moredyes;

import java.awt.Color;
import java.util.Random;

import com.scrtwpns.Mixbox;
import ianeli.moredyes.blocks.CustomGlazed;

public class ColorHandler {
    public static int mixColors(int c1, int c2) {
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = c1 & 0xFF;
        Color color1 = new Color(r1, g1, b1);

        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = c2 & 0xFF;
        Color color2 = new Color(r2, g2, b2);

        Color mixedColor = new Color(Mixbox.lerp(color1.getRGB(), color2.getRGB(), 0.5f));

        return (mixedColor.getRed() << 16) | (mixedColor.getGreen() << 8) | mixedColor.getBlue();
    }
    public static int getTerraCotta(int c1) {
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = c1 & 0xFF;
        Color color1 = new Color(r1, g1, b1);

        Color mixedColor = new Color(Mixbox.lerp(color1.getRGB(), new Color(155, 96, 69).getRGB(), 0.2f));

        return lighten(desaturate( (mixedColor.getRed() << 16) | (mixedColor.getGreen() << 8) | mixedColor.getBlue(),  0.2f), 0.1f);
    }

    public static int desaturate(int c1, float factor) {
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = c1 & 0xFF;
        float luminance = 0.299F * r1 + 0.587F * g1 + 0.114F * b1;
        int r2 = (int) (luminance + (r1 - luminance) * (1 - factor));
        int g2 = (int) (luminance + (g1 - luminance) * (1 - factor));
        int b2 = (int) (luminance + (b1 - luminance) * (1 - factor));

        return (r2 << 16) | (g2 << 8) | b2;
    }

    public static int lighten(int color, float factor) {
        // Extract RGB components
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        // Lighten each channel linearly
        int rLight = (int)(r + (255 - r) * factor);
        int gLight = (int)(g + (255 - g) * factor);
        int bLight = (int)(b + (255 - b) * factor);

        // Clamp values to 0-255 range (important for edge cases)
        rLight = Math.min(255, Math.max(0, rLight));
        gLight = Math.min(255, Math.max(0, gLight));
        bLight = Math.min(255, Math.max(0, bLight));

        // Recombine into packed int
        return (rLight << 16) | (gLight << 8) | bLight;
    }

    public static int getRandomSaturatedColor() {
        Random rand = new Random();
        float hue = rand.nextFloat() * 360;
        float saturation = 0.5f;
        float lightness = 0.4f;

        return hslToRgb(hue, saturation, lightness);
    }
    public static int getGlazedAccent(int seed) {
        Random rand = new Random(seed);
        float[] base = rgbToHsl(seed);

        float baseHue = base[0];
        float baseSat = base[1];
        float baseLight = base[2];

        // Choose one of a few possible harmonies
        int mode = rand.nextInt(3);
        float hue;
        switch (mode) {
            case 0: // Complementary
                hue = (baseHue + 180f) % 360f;
                break;
            case 1: // Analogous (split complement for more harmony)
                hue = (baseHue + 150f + rand.nextFloat() * 60f) % 360f;
                break;
            case 2: // Triadic
                hue = (baseHue + 120f) % 360f;
                break;
            default:
                hue = (baseHue + 180f) % 360f;
        }

        // Slight tweaks to make it visually more distinct
        float saturation = clamp(baseSat * (0.8f + rand.nextFloat() * 0.4f), 0f, 1f);
        float lightness = clamp(baseLight * (0.8f + rand.nextFloat() * 0.4f), 0f, 1f);

        return hslToRgb(hue, saturation, lightness);
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    // Convert HSL to RGB
    public static int hslToRgb(float hue, float saturation, float lightness) {
        float c = (1 - Math.abs(2 * lightness - 1)) * saturation;
        float x = c * (1 - Math.abs(((hue / 60) % 2) - 1));
        float m = lightness - c / 2;

        float rPrime = 0, gPrime = 0, bPrime = 0;

        if (hue >= 0 && hue < 60) {
            rPrime = c; gPrime = x; bPrime = 0;
        } else if (hue >= 60 && hue < 120) {
            rPrime = x; gPrime = c; bPrime = 0;
        } else if (hue >= 120 && hue < 180) {
            rPrime = 0; gPrime = c; bPrime = x;
        } else if (hue >= 180 && hue < 240) {
            rPrime = 0; gPrime = x; bPrime = c;
        } else if (hue >= 240 && hue < 300) {
            rPrime = x; gPrime = 0; bPrime = c;
        } else {
            rPrime = c; gPrime = 0; bPrime = x;
        }

        // Add m to match the lightness and convert to 0-255 range
        int r = Math.round((rPrime + m) * 255);
        int g = Math.round((gPrime + m) * 255);
        int b = Math.round((bPrime + m) * 255);

        // Combine RGB components into a single integer
        return (r << 16) | (g << 8) | b;
    }

    public static int hash32shift(int key)
    {
        key = ~key + (key << 15); // key = (key << 15) - key - 1;
        key = key ^ (key >>> 12);
        key = key + (key << 2);
        key = key ^ (key >>> 4);
        key = key * 2057; // key = (key + (key << 3)) + (key << 11);
        key = key ^ (key >>> 16);
        return key;
    }

    public static float[] rgbToHsl(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        float rNorm = r / 255f;
        float gNorm = g / 255f;
        float bNorm = b / 255f;

        float max = Math.max(rNorm, Math.max(gNorm, bNorm));
        float min = Math.min(rNorm, Math.min(gNorm, bNorm));
        float delta = max - min;

        float h = 0f, s, l = (max + min) / 2f;

        if (delta == 0) {
            h = 0f; // Achromatic
        } else if (max == rNorm) {
            h = 60f * (((gNorm - bNorm) / delta) % 6f);
        } else if (max == gNorm) {
            h = 60f * (((bNorm - rNorm) / delta) + 2f);
        } else if (max == bNorm) {
            h = 60f * (((rNorm - gNorm) / delta) + 4f);
        }

        if (h < 0) h += 360f;

        if (delta == 0) {
            s = 0f;
        } else {
            s = delta / (1f - Math.abs(2f * l - 1f));
        }

        return new float[]{h, s, l};
    }
}
