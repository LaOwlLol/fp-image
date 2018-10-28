package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Random;

public class WhiteNoise implements Filter {

    private Random random;

    public WhiteNoise() {
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        for (int j = 0; j < image.getHeight(); ++j) {
            for (int i = 0; i < image.getWidth(); ++i) {
                double color = (Math.sin(this.random.nextGaussian())/2) +0.5;
                bufferWriter.setColor(i, j, new Color(color, color, color, 1.0));
            }
        }

        return buffer;
    }
}
