package fauxpas.filters;

import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * A utility filter for manipulating the contrast in an image.
 */
public class RedistributionFilter implements Filter {

    private final double pow;

    public RedistributionFilter() {
        this(2.0);
    }

    /**
     *
     * @param power values should be very close to 1.0. power > 1.0 will washout decrease contrast. power < 1.0 will darken the image.
      */
    public RedistributionFilter(double power) {
        this.pow = power;
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        new Sample().get(image).forEach( p -> {
            bufferWriter.setColor(p.x(), p.y(), new Color(
                Math.min(1.0, Math.pow( 255*p.getRed(), this.pow )/255),
                Math.min(1.0, Math.pow( 255*p.getGreen(), this.pow )/255),
                Math.min(1.0, Math.pow( 255*p.getBlue(), this.pow )/255),
                p.getOpacity()
            ));
        });

        return buffer;
    }
}
