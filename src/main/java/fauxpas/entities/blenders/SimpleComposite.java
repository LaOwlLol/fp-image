package fauxpas.entities.blenders;

import javafx.scene.paint.Color;

/**
 * Composite colors with simple interpolation calculation.
 */
public class SimpleComposite implements Blender{

    private double bias;

    /**
     * Construct a composite blender with default bias of 0.5
     */
    public SimpleComposite() {
        this(0.5);
    }


    public SimpleComposite(double bias) {
        this.bias = bias;
    }

    /**
     * Calculate color composite with mid point calculation.
     * @param color1 starting point
     * @param color2 end point
     * @return color1.interpolate(color2, this.bias)
     */
    @Override
    public Color calc(Color color1, Color color2) {
        return color1.interpolate(color2, this.bias);
    }
}
