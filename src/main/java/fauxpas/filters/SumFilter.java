package fauxpas.filters;

import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class SumFilter implements Mixer {

    private double intensity1;
    private double intensity2;

    public SumFilter() {
        this(1.0, 1.0);
    }

    public SumFilter(double intensity1, double intensity2) {
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;
    }

    @Override
    public Image apply(Image f, Image s) {

            WritableImage buffer = new WritableImage((int)f.getWidth(), (int)f.getHeight());
            PixelWriter bufferWriter = buffer.getPixelWriter();

            PixelReader reader2 = s.getPixelReader();

            new Sample().get(f).filter( p ->  p.x() < s.getWidth() && p.y() < s.getHeight() ).forEach( p1 -> {
                Color p2 = reader2.getColor(p1.x(), p1.y());

                bufferWriter.setColor(p1.x(), p1.y(), new Color(
                        Math.min(1.0, (this.intensity1 * p1.getRed()) + (this.intensity2 * p2.getRed())),
                        Math.min(1.0, (this.intensity1 * p1.getGreen()) + (this.intensity2 * p2.getGreen())),
                        Math.min(1.0, (this.intensity1 * p1.getBlue()) + (this.intensity2 * p2.getBlue())),
                        Math.min(1.0, (this.intensity1 * p1.getOpacity()) + (this.intensity2 * p2.getOpacity()))
                ));
            });

            return buffer;
    }
}
