package fauxpas.filters;

import fauxpas.entities.blenders.ColorSum;
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
    public Image apply(Image s, Image p) {

            WritableImage buffer = new WritableImage((int)s.getWidth(), (int)s.getHeight());
            PixelWriter bufferWriter = buffer.getPixelWriter();

            PixelReader reader1 = s.getPixelReader();
            PixelReader reader2 = p.getPixelReader();

            ColorSum sum = new ColorSum(this.intensity1, this.intensity2);

            for (int y = 0; y < buffer.getHeight(); ++y) {
                for (int x = 0; x < buffer.getWidth(); ++x) {
                    if (x < p.getWidth() && y < p.getHeight()) {
                        Color color1 = reader1.getColor(x, y);
                        Color color2 = reader2.getColor(x, y);

                        bufferWriter.setColor(x, y, sum.calc(color1, color2));
                    }
                }
            }

            return buffer;
    }
}
