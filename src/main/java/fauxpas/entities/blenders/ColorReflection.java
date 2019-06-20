package fauxpas.entities.blenders;

import javafx.scene.paint.Color;

/**
 * Colors reflected, where the resulting color is the component wise product of color1 and color two with opacity of color1
 */
public class ColorReflection implements Blender {


    /**
     * Calculate color 2 reflected onto color 1.
     * @param color1 reflection surface.
     * @param color2 reflection light source.
     * @return new color from source color components multiplied with opacity of color 1.
     */
    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(
                Math.min(1.0, color1.getRed() * color2.getRed()),
                Math.min(1.0, color1.getGreen() * color2.getGreen()),
                Math.min(1.0, color1.getBlue() * color2.getBlue()),
                color1.getOpacity());
    }
}
