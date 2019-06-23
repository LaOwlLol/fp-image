package fauxpas.entities.blenders;

import javafx.scene.paint.Color;

/**
 * Reflected, where the resulting color is the component wise product of incoming light (color1) and surface properties (color2), with opacity property of the surface
 */
public class Reflection implements Blender {


    /**
     * Calculate color 2 reflected onto color 1.
     * @param color1 incoming light color.
     * @param color2 reflection surface properties (how much light to reflect for each color channel).
     * @return reflected light color with surface (color2) opacity.
     */
    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(
            Math.min(1.0, color1.getRed() * color2.getRed()),
            Math.min(1.0, color1.getGreen() * color2.getGreen()),
            Math.min(1.0, color1.getBlue() * color2.getBlue()),
            color2.getOpacity()
        );
    }
}
