package fauxpas.filters;

import fauxpas.entities.blenders.Reflection;
import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * A filter for simulating a reflection.  The first image will be used as the light source reflected onto a surface defined by the second image (color's of pixels in the second image are the color absorption properties of the surface, including opacity).
 *
 * In other words this mixer applies a fauxpas.entities.blenders.Reflection to compute the resulting image from the source images.
 */
public class ReflectionFilter implements Mixer{

    private Reflection reflection;

    public ReflectionFilter() {
        this.reflection = new Reflection();
    }

    @Override
    public Image apply(Image f, Image s) {

        WritableImage buffer = new WritableImage((int)f.getWidth(), (int)f.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        PixelReader reader2 = s.getPixelReader();

        new Sample().get(f).filter( p -> p.x() < s.getWidth() && p.y() < s.getHeight()).forEach( p1 -> {
            Color p2 = reader2.getColor(p1.x(), p1.y());

            bufferWriter.setColor(p1.x(), p1.y(), reflection.calc(p1.getColor(), p2));
        });

        return buffer;
    }
}
