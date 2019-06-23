package fauxpas.entities;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A two dimensional area that can produce streams of the coordinates covered.
 */
public class Range implements Selection {
    
    private int x_lower;
    private int x_upper;
    private int y_lower;
    private int y_upper;

    /**
     * Create a 2d range (x_lower to x_upper] by (y_lower, y_upper].
     * This range may represent a 2d rectangular area.
     * 
     * @param x_lower left bound
     * @param x_upper right bound
     * @param y_lower top
     * @param y_upper bottom
     */
    public Range(int x_lower, int x_upper, int y_lower, int y_upper) {
        this.x_lower = x_lower;
        this.x_upper = x_upper;
        this.y_lower = y_lower;
        this.y_upper = y_upper;
    }

    /**
     * @return Coordinate to the upper left corner of this range.
     */
    public Coordinate topLeft() {
        return new Coordinate(x_lower, y_lower);
    }

    /**
     * @return Coordinate to the upper right corner of this range.
     */
    public Coordinate topRight() {
        return new Coordinate( x_lower + ( x_upper - x_lower ) , y_lower);
    }

    /**
     * @return Coordinate to the lower left corner of this range.
     */
    public Coordinate bottomLeft() {
        return new Coordinate(x_lower, y_lower + ( y_upper - y_lower ) );
    }

    /**
     * @return Coordinate to the lower right corner of this range.
     */
    public Coordinate bottomRight() {
        return new Coordinate(x_lower + ( x_upper - x_lower ) , y_lower + ( y_upper - y_lower ));
    }

    /**
     * @return A stream of the coordinates covered by this range.
     */
    @Override
    public Stream<Coordinate> get() {
        return IntStream.range(x_lower, x_upper).mapToObj(
                x -> IntStream.range(y_lower, y_upper).mapToObj(
                        y -> new Coordinate(x, y)
                )
        ).flatMap(Function.identity());
    }

    /**
     * the area of this 2-d range.
     * @return (xMax - xMin) * (yMax - yMin)
     */
    public int size() {
        return (this.x_upper - this.x_lower) * ( this.y_upper - this.y_lower);
    }
}
