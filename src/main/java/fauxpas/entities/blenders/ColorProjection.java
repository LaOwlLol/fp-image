package fauxpas.entities.blenders;

import javafx.scene.paint.Color;

public class ColorProjection implements Blender{

    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(
                color1.getRed() - (color1.getRed() - color2.getRed()) / 2,
                color1.getGreen() - (color1.getGreen() - color2.getGreen()) / 2,
                color1.getBlue() - (color1.getBlue() - color2.getBlue()) / 2,
                1.0);
    }
}
