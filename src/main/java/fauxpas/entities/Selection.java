package fauxpas.entities;

import java.util.stream.Stream;

public interface Selection {

    Stream<Coordinate> get();

    int size();

}
