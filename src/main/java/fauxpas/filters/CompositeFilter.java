package fauxpas.filters;

import fauxpas.entities.blenders.SimpleComposite;
import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


/**
 * Utility for composting two images with the SimpleComposite Blender.
 *
 * A composite can have a bias where 0.0 is fully the the first image, 1.0 is fully the second image, or by default pixels are interpolated half way between the two images.
 */
public class CompositeFilter implements Mixer{

    SimpleComposite blender;

    public CompositeFilter() {
        this(0.5);
    }

    public CompositeFilter(double bias) {
         blender = new SimpleComposite(bias);
    }

    @Override
    public Image apply(Image f, Image s) {

        WritableImage buffer = new WritableImage((int)f.getWidth(), (int)f.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();
        PixelReader reader2 = s.getPixelReader();

        new Sample().get(f).filter( p -> p.x() < s.getWidth() && p.y() < s.getHeight() ).forEach( p1 -> {
            Color p2 = reader2.getColor(p1.x(), p1.y());

            bufferWriter.setColor(p1.x(), p1.y(), blender.calc(p1.getColor(), p2));
        });

        return buffer;
    }
}
