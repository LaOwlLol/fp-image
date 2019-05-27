package fauxpas.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Range {
    
    private int x_lower;
    private int x_upper;
    private int y_lower;
    private int y_upper;

    public Range() {
        this(0,0,0,0);
    }

    /**
     * Create a 2d range (x_lower to x_upper] by (y_lower, y_upper]
     * 
     * @param x_lower
     * @param x_upper
     * @param y_lower
     * @param y_upper
     */
    public Range(int x_lower, int x_upper, int y_lower, int y_upper) {
        this.x_lower = x_lower;
        this.x_upper = x_upper;
        this.y_lower = y_lower;
        this.y_upper = y_upper;
    }

    public Range(int x_lower, int x_upper) {
        this(x_lower, x_upper, 0, 1);
    }

    public Coordinate topLeft() {
        return new Coordinate(x_lower, y_lower);
    }

    public Coordinate topRight() {
        return new Coordinate(x_upper-1, y_lower);
    }

    public Coordinate bottomLeft() {
        return new Coordinate(x_lower, y_upper-1);
    }

    public Coordinate bottomRight() {
        return new Coordinate(x_upper-1, y_upper-1);
    }

    public Stream<Coordinate> get() {
        ArrayList list = new ArrayList((x_upper - x_lower) * ( y_upper - y_lower ));
        IntStream.range(x_lower, x_upper).forEach( (x) -> {
            IntStream.range( y_lower, y_upper ).forEach( (y) -> list.add(new Coordinate(x, y)));
        } );

        return list.stream();
    }

}
