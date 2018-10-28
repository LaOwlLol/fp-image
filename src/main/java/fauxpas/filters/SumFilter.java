package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class SumFilter implements Mixer {

    private double intensity1;
    private double intensity2;

    public SumFilter() {
        this.intensity1 = 1.0;
        this.intensity1 = 1.0;
    }

    public SumFilter(double intensity1, double intensity2) {
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;
    }

    @Override
    public Filter apply(Filter f1, Filter f2) {
        return image -> {
            WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
            PixelWriter bufferWriter = buffer.getPixelWriter();

            Image image1 = f1.apply(image);
            PixelReader reader1 = image1.getPixelReader();
            Image image2 = f2.apply(image);
            PixelReader reader2 = image2.getPixelReader();

            for (int j = 0; j < image.getHeight(); ++j) {
                for (int i = 0; i < image.getWidth(); ++i) {
                    Color color1 = reader1.getColor(i, j);
                    Color color2 = reader2.getColor(i, j);

                    bufferWriter.setColor(i, j, new Color(
                          Math.min(1.0, (this.intensity1 * color1.getRed()) + (this.intensity2 * color2.getRed()) ),
                          Math.min(1.0, (this.intensity1 * color1.getGreen()) + (this.intensity2 * color2.getGreen())),
                          Math.min(1.0, (this.intensity1 * color1.getBlue()) + (this.intensity2 * color2.getBlue())),
                          1.0));
                }
            }

            return buffer;
        };
    }
}
