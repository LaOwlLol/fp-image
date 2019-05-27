package fauxpas.filters;

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

        new Sample().get(f).filter( p -> p.x() < s.getWidth() && p.y() < s.getHeight() ).forEach( p1 -> {
            Color p2 = reader2.getColor(p1.x(), p1.y());

            bufferWriter.setColor(p1.x(), p1.y(), new Color(
                    p1.getRed() - (p1.getRed() - p2.getRed()) / 2,
                    p1.getGreen() - (p1.getGreen() - p2.getGreen()) / 2,
                    p1.getBlue() - (p1.getBlue() - p2.getBlue()) / 2,
                    p1.getOpacity() - (p1.getOpacity() - p2.getOpacity()) / 2));
        });

        return buffer;

    }
}
