package fauxpas.filters;

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

        for (int y = 0; y < buffer.getHeight(); ++y) {
            for (int x = 0; x < buffer.getWidth(); ++x) {
                if (x < p.getWidth() && y < p.getHeight()) {
                    Color color1 = reader1.getColor(x, y);
                    Color color2 = reader2.getColor(x, y);


                    bufferWriter.setColor(x, y, new Color(
                            color1.getRed() - (color1.getRed() - color2.getRed()) / 2,
                            color1.getGreen() - (color1.getGreen() - color2.getGreen()) / 2,
                            color1.getBlue() - (color1.getBlue() - color2.getBlue()) / 2,
                            color1.getOpacity() - (color1.getOpacity() - color2.getOpacity()) / 2));
                }
            }
        }

        return buffer;

    }
}
