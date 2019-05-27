package fauxpas.entities;

import javafx.scene.paint.Color;

public class Pixel {

    private Color color;
    private Coordinate coordinate;

    public Pixel(Coordinate coordinate, Color color) {
        this.coordinate = coordinate;
        this.color = color;
    }

    public int x() {
        return this.coordinate.x();
    }
    public int y() {
        return this.coordinate.y();
    }


    public double getRed() {
        return color.getRed();
    }

    public double getGreen() {
        return color.getGreen();
    }

    public double getBlue() {
        return color.getBlue();
    }

    public double getOpacity() {
        return color.getOpacity();
    }
}
