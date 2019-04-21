package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ChromaKeyFilter implements Filter {

    private Color key_color;
    private double threshold;

    public ChromaKeyFilter() {
        this(Color.GREEN,0.1);
    }

    public ChromaKeyFilter(Color key_color, double threshold) {
        this.key_color = key_color;
        this.threshold = threshold;
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        PixelReader imageReader = image.getPixelReader();

        for (int j = 0; j < image.getHeight(); ++j) {
            for (int i = 0; i < image.getWidth(); ++i) {
                Color imageColor = imageReader.getColor(i, j);

                double delta = Math.min(1.0, (Math.pow(key_color.getRed() - imageColor.getRed(), 2) +
                    Math.pow(key_color.getGreen() - imageColor.getGreen(), 2) +
                    Math.pow(key_color.getBlue() - imageColor.getBlue(), 2) ));

                if (delta <= threshold) {
                    bufferWriter.setColor(i, j, new Color(imageColor.getRed(), imageColor.getGreen(),
                        imageColor.getBlue(), 0.0));
                }
                else {
                    bufferWriter.setColor(i, j, new Color(imageColor.getRed(), imageColor.getGreen(),
                        imageColor.getBlue(), imageColor.getOpacity()));
                }
            }
        }

        return buffer;
    }
}
