package fauxpas.entities.blenders;

import javafx.scene.paint.Color;

public class ColorSum implements Blender {

    double intensity1;
    double intensity2;

    public ColorSum(double i1, double i2) {
        this.intensity1 = i1;
        this.intensity2 = i2;
    }

    public ColorSum() {
        this(1.0, 1.0);
    }

    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(Math.min(1.0, (this.intensity1 * color1.getRed()) + (this.intensity2 * color2.getRed())),
                Math.min(1.0, (this.intensity1 * color1.getGreen()) + (this.intensity2 * color2.getGreen())),
                Math.min(1.0, (this.intensity1 * color1.getBlue()) + (this.intensity2 * color2.getBlue())),
                Math.min(1.0, (this.intensity1 * color1.getOpacity()) + (this.intensity2 * color2.getOpacity())));
    }
}
