package fauxpas.filters;

import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * A filter for transforming the luminance of a pixels to transparency.  Meaning pixels close to white, become transparent
 *
 * By default luminance is calculated with a gray scale filter color balance, but you can provide a custom color balance gray scale filter.
 */
public class TranslucentFilter implements Filter {

    GrayscaleFilter grayFilter;

    public TranslucentFilter() {
        this(new GrayscaleFilter());
    }

    public TranslucentFilter(GrayscaleFilter grayscaleFilter) {
        this.grayFilter = grayscaleFilter;
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        new Sample().get(grayFilter.apply(image)).forEach( p-> {

            bufferWriter.setColor(p.x(), p.y(), new Color(
                    p.getRed(),
                    p.getGreen(),
                    p.getBlue(),
                    Math.min( p.getOpacity(), 1.0-p.getRed() ) ));
        });

        return buffer;
    }

}
