package fauxpas.entities.blenders;

import javafx.scene.paint.Color;

/**
 * Composite colors with simple calculation.
 */
public class SimpleComposite implements Blender{

    /**
     * Calculate color composite with mid point calculation.
     * @param color1 starting point
     * @param color2 end point
     * @return subtractive composite c1 - (c1-c2)/2.
     */
    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(
                Math.max(0.0, color1.getRed() - (color1.getRed() - color2.getRed()) / 2),
                Math.max(0.0, color1.getGreen() - (color1.getGreen() - color2.getGreen()) / 2),
                Math.max(0.0, color1.getBlue() - (color1.getBlue() - color2.getBlue()) / 2),
                Math.max(0.0, color1.getOpacity() - (color1.getOpacity() - color2.getOpacity()) / 2) );
    }
}
