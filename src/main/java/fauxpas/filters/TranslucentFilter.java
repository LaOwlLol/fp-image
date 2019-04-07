package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class TranslucentFilter implements Filter {

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        PixelReader imageReader = image.getPixelReader();

        for (int j = 0; j < image.getHeight(); ++j) {
            for (int i = 0; i < image.getWidth(); ++i) {
                Color imageColor = imageReader.getColor(i, j);

                double alpha = Math.min(1.0, (0.3*imageColor.getRed()) +
                        (0.59 * imageColor.getGreen()) +
                        (0.11 * imageColor.getBlue()));

                bufferWriter.setColor(i, j, new Color(
                        imageColor.getRed(),
                        imageColor.getGreen(),
                        imageColor.getBlue(),
                        1.0-alpha));
            }
        }

        return buffer;
    }

}
