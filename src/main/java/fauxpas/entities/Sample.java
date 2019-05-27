package fauxpas.entities;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Sample {

    private Selection area;

    public Sample() {
        area = null;
    }

    public Sample(Selection group) {
        this.area = group;
    }

    /**
     * Get a stream of pixels from this Sample's Range of a source image,  If this Range is unset full image is streamed.
     *
     * @param source Image to read pixels from.
     * @return Stream containing pixel from source image in the area (Range) of this Sample.
     */
    public Stream<Pixel> get(Image source) {
        PixelReader reader = source.getPixelReader();
        if (this.area == null) {
            this.area = new Range(0, (int )source.getWidth(), 0, (int) source.getHeight());
        }

        return this.area.get().filter( c -> (c.x() < source.getWidth() && c.y() < source.getHeight()) ).map(
                c -> new Pixel( c, reader.getColor(c.x(), c.y()) )
        );

    }

}
