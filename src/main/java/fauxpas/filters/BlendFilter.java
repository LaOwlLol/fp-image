package fauxpas.filters;

import fauxpas.entities.blenders.ColorProjection;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class BlendFilter implements Mixer{
    @Override
    public Image apply(Image s, Image p) {

        WritableImage buffer = new WritableImage((int)s.getWidth(), (int)s.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        PixelReader reader1 = s.getPixelReader();
        PixelReader reader2 = p.getPixelReader();

        ColorProjection blender = new ColorProjection();

        for (int y = 0; y < buffer.getHeight(); ++y) {
            for (int x = 0; x < buffer.getWidth(); ++x) {
                if (x < p.getWidth() && y < p.getHeight()) {


                    Color color1 = reader1.getColor(x, y);
                    Color color2 = reader2.getColor(x, y);

                    bufferWriter.setColor(x, y, blender.calc(color1, color2));
                }
            }
        }

        return buffer;

    }
}
