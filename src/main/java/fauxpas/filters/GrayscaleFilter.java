package fauxpas.filters;

import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Filter colors to gray.
 *
 * By default the color balance is 30% red, 59% green, and 11% blue, to approximate human or typical lighting. A constructor for adjusting these values is available.
 */
public class GrayscaleFilter implements Filter {

    private double redBalance;
    private double greenBalance;
    private double blueBalance;

    public GrayscaleFilter() {
        this(0.3, 0.59, 0.11);
    }

    /**
     * Create custom color balance for gray-scaling.
     * @param redBalance red bias
     * @param greenBalance green bias
     * @param blueBalance blue bias
     */
    public GrayscaleFilter(double redBalance, double greenBalance, double blueBalance) {
        this.redBalance = redBalance;
        this.greenBalance = greenBalance;
        this.blueBalance = blueBalance;
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        new Sample().get(image).forEach( p-> {
            double gray = Math.min(1.0, (redBalance*p.getRed()) +
                    (greenBalance * p.getGreen()) +
                    (blueBalance * p.getBlue()));

            bufferWriter.setColor(p.x(), p.y(), new Color(gray, gray, gray, p.getOpacity()));
        });

        return buffer;
    }

}
