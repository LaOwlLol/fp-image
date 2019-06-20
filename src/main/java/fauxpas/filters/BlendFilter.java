package fauxpas.filters;

import fauxpas.entities.blenders.ColorProjection;
import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class BlendFilter implements Mixer{
    @Override
    public Image apply(Image f, Image s) {

        WritableImage buffer = new WritableImage((int)f.getWidth(), (int)f.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        PixelReader reader2 = s.getPixelReader();

        ColorProjection blender = new ColorProjection();

        new Sample().get(f).filter( p -> p.x() < s.getWidth() && p.y() < s.getHeight() ).forEach( p1 -> {
            Color p2 = reader2.getColor(p1.x(), p1.y());

            bufferWriter.setColor(p1.x(), p1.y(), blender.calc(p1.getColor(), p2));
        });

        return buffer;
    }
}
