package fauxpas.entities;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.util.stream.Stream;

/**
 * A selection utility for producing a stream of pixels from an image.  By default a pixel stream produced will span the width and height of the source image.  A custom selection group may be passed to the constructor for sampling a subset of pixels in a source image.
 */
public class Sample {

    private Selection area;

    public Sample() {
        area = null;
    }

    /**
     * Construct a custom selection sample.
     * @param selection the selection to be used when producing a stream of pixels.
     */
    public Sample(Selection selection) {
        this.area = selection;
    }

    /**
     * Get a stream of pixels from this sample's selection,  If this selection is unset the full image is returned as a stream of pixels.
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
