package fauxpas.filters;

import fauxpas.entities.blenders.ColorSum;
import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * A utility filter for adding two images by summing their pixel colors channels.
 */
public class SumFilter implements Mixer {

    private ColorSum sum;

    public SumFilter() {
        sum = new ColorSum();
    }

    @Override
    public Image apply(Image f, Image s) {

        WritableImage buffer = new WritableImage((int)f.getWidth(), (int)f.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        PixelReader reader2 = s.getPixelReader();

        new Sample().get(f).filter( p ->  p.x() < s.getWidth() && p.y() < s.getHeight() ).forEach( p1 -> {
            Color p2 = reader2.getColor(p1.x(), p1.y());

            bufferWriter.setColor(p1.x(), p1.y(), sum.calc(p1.getColor(), p2));
        });

        return buffer;
    }
}
