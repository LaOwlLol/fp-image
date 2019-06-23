package fauxpas.filters;

import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Filter the pixels that are within the threshold of the key color.
 *
 * Filtered pixels may be replaced by a source color or image, and if no source is provided filtered pixels have their opacity set to 0.0.
 *
 * If both the source image and source color are set, the source image will take precedence.
 */
public class ChromaKeyFilter implements Filter {

    private Color key_color;
    private Color source_color;
    private PixelReader source_image;
    private double threshold;

    public ChromaKeyFilter() {
        this(Color.GREEN, null, null, 0.1);
    }

    public ChromaKeyFilter(Color key_color, double threshold) {
        this(key_color, null, null, threshold);
    }

    public ChromaKeyFilter(Color key_color, Color source_color, double threshold) {
        this(key_color, source_color, null, threshold);
    }

    public ChromaKeyFilter(Color key_color, Image source_image, double threshold) {
        this(key_color, null, source_image, threshold);
    }

    private ChromaKeyFilter(Color key_color, Color source_color, Image source_image, double threshold) {
        this.key_color = key_color;
        this.source_color = source_color;
        this.source_image = source_image.getPixelReader();
        this.threshold = threshold;
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        new Sample().get(image).forEach( p -> {
            double delta = Math.min(1.0, (Math.pow(key_color.getRed() - p.getRed(), 2) +
                    Math.pow(key_color.getGreen() - p.getGreen(), 2) +
                    Math.pow(key_color.getBlue() - p.getBlue(), 2) ));

            if (delta < threshold) {
                if ( source_image != null ) {
                    bufferWriter.setColor(p.x(), p.y(), source_image.getColor(p.x(), p.y()));
                }
                else if (source_color != null) {
                    bufferWriter.setColor(p.x(), p.y(), source_color);
                }
                else {
                    bufferWriter.setColor(p.x(), p.y(), new Color(p.getRed(), p.getGreen(), p.getBlue(), 0.0));
                }
            }
            else {
                bufferWriter.setColor(p.x(), p.y(), p.getColor());
            }
        });

        return buffer;
    }
}
