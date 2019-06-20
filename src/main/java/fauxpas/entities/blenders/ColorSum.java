package fauxpas.entities.blenders;

import javafx.scene.paint.Color;

/**
 * Sum of two colors, with optional scalars.
 */
public class ColorSum implements Blender {

    double scalar1;
    double scalar2;

    /**
     * Construct a new sum blender with scalars.
     * @param scalar1 multiplier for first color arguments.
     * @param scalar2 multiplier for second color arguments.
     */
    public ColorSum(double scalar1, double scalar2) {
        this.scalar1 = scalar1;
        this.scalar2 = scalar2;
    }

    /**
     * Construct a new sum blender.
     */
    public ColorSum() {
        this(1.0, 1.0);
    }

    /**
     * Calculate the sum of two colors.
     * @param color1 a color to add.
     * @param color2 another color to add.
     * @return a new color from source color components, including opacity, added.
     */
    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(Math.min(1.0, (this.scalar1 * color1.getRed()) + (this.scalar2 * color2.getRed())),
                Math.min(1.0, (this.scalar1 * color1.getGreen()) + (this.scalar2 * color2.getGreen())),
                Math.min(1.0, (this.scalar1 * color1.getBlue()) + (this.scalar2 * color2.getBlue())),
                Math.min(1.0, (this.scalar1 * color1.getOpacity()) + (this.scalar2 * color2.getOpacity())));
    }
}
