package fauxpas.entities;

import java.util.stream.Stream;

/**
 * A selection produces a stream of coordinates.
 */
public interface Selection {

    /**
     * @return Coordinates included in the selection.
     */
    Stream<Coordinate> get();

}
