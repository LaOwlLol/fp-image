package fauxpas.filters;

import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class TranslucentFilter implements Filter {

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        new Sample().get(image).forEach( p-> {
            double alpha = Math.min(1.0, (0.3*p.getRed()) +
                    (0.59 * p.getGreen()) +
                    (0.11 * p.getBlue()));

            bufferWriter.setColor(p.x(), p.y(), new Color(
                    p.getRed(),
                    p.getGreen(),
                    p.getBlue(),
                    Math.min( p.getOpacity(), 1.0-alpha ) ));
        });

        return buffer;
    }

}
