package fauxpas.filters;

import fauxpas.entities.Sample;
import javafx.scene.image.Image;
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

        new Sample().get(image).forEach( p -> {
            double delta = Math.min(1.0, (Math.pow(key_color.getRed() - p.getRed(), 2) +
                    Math.pow(key_color.getGreen() - p.getGreen(), 2) +
                    Math.pow(key_color.getBlue() - p.getBlue(), 2) ));

            if (delta <= threshold) {
                bufferWriter.setColor(p.x(), p.y(), new Color(p.getRed(), p.getGreen(),
                        p.getBlue(), 0.0));
            }
            else {
                bufferWriter.setColor(p.x(), p.y(), new Color(p.getRed(), p.getGreen(),
                        p.getBlue(), p.getOpacity()));
            }
        });

        return buffer;
    }
}
