package fauxpas.entities.blenders;

import javafx.scene.paint.Color;

/**
 * Sum of two colors, with optional scalars.
 */
public class ColorSum implements Blender {

    /**
     * Construct a new sum blender.
     */
    public ColorSum() {

    }

    /**
     * Calculate the sum of two colors.
     * @param color1 a color to add.
     * @param color2 another color to add.
     * @return a new color from source color components, including opacity, added.
     */
    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(
            Math.min(1.0, color1.getRed() + color2.getRed()),
            Math.min(1.0, color1.getGreen() +  color2.getGreen()),
            Math.min(1.0, color1.getBlue() + color2.getBlue()),
            Math.min(1.0, color1.getOpacity() + color2.getOpacity())
        );
    }
}
