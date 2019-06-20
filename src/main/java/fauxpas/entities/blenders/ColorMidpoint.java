package fauxpas.entities.blenders;

import javafx.scene.paint.Color;

/**
 * Mid point between two colors, with opacity of the more opaque color.
 */
public class ColorMidpoint implements Blender{

    /**
     * Calculate the mid point between two colors.
     * @param color1 starting point
     * @param color2 end point
     * @return new color where each component is starting point values minus half the difference between start and end values, and opacity of the more opaque color.
     */
    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(
                color1.getRed() - (color1.getRed() - color2.getRed()) / 2,
                color1.getGreen() - (color1.getGreen() - color2.getGreen()) / 2,
                color1.getBlue() - (color1.getBlue() - color2.getBlue()) / 2,
                (color1.getOpacity() < color2.getOpacity()) ? color2.getOpacity() : color1.getOpacity() );
    }
}
