package fauxpas.entities.blenders;

import javafx.scene.paint.Color;

public class ColorReflection implements Blender {
    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(
                Math.min(1.0, color1.getRed() * color2.getRed()),
                Math.min(1.0, color1.getGreen() * color2.getGreen()),
                Math.min(1.0, color1.getBlue() * color2.getBlue()),
                Math.min(1.0, color1.getOpacity() * color2.getOpacity()));
    }
}
