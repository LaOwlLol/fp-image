package fauxpas.filters;

import fauxpas.entities.Range;
import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.jblas.DoubleMatrix;

import java.awt.color.ColorSpace;
import java.util.Optional;

public class SobelFilter implements Filter {

    private final int WIDTH = 3;

    private final double threshHold;
    private final DoubleMatrix horzKernal;
    private final DoubleMatrix vertKernal;

    private final boolean preserveSaturation;
    private final boolean manhattan;

    public SobelFilter() {
        this(0.2, false, false);
    }

    public SobelFilter(double threshHold) {
        this(threshHold, false, false);

    }

    public SobelFilter(double threshHold, boolean preserveSaturation) {
        this(threshHold, preserveSaturation, false);

    }
    
    public SobelFilter(double threshHold, boolean preserveSaturation, boolean manhattan) {
        this.horzKernal = new DoubleMatrix(WIDTH, WIDTH);
        this.horzKernal.put(0,0, -1.0);
        this.horzKernal.put(0,1, -1);
        this.horzKernal.put(0,2, -1.0);
        this.horzKernal.put(2,0, 1.0);
        this.horzKernal.put(2,1,1);
        this.horzKernal.put(2,2,1.0);

        this.vertKernal = new DoubleMatrix(WIDTH,WIDTH);
        this.vertKernal.put(0,0, -1.0);
        this.vertKernal.put(1,0,-1);
        this.vertKernal.put(2,0, -1.0);
        this.vertKernal.put(0,2, 1.0);
        this.vertKernal.put(1,2,1);
        this.vertKernal.put(2,2,1.0);

        this.threshHold = threshHold;
        this.preserveSaturation = preserveSaturation;
        this.manhattan = manhattan;
    }

    @Override
    public Image apply(Image target) {

        //make sure it's gray.
        Image grayImage = new GrayscaleFilter().apply(target);

        WritableImage buffer = new WritableImage((int) target.getWidth(), (int) target.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();
        PixelReader targetReader = target.getPixelReader();

        new Range(0, (int) target.getWidth(), 0, (int) target.getHeight()).get().forEach(c -> {

            double orientation;
            double horzSum;
            double vertSum;
            double gradient;

            //sum pass;
            horzSum = ColorMatrixBuilder.getColorMatrix(grayImage, Color::getBlue, WIDTH, c.x(), c.y()).mul(
                    horzKernal).sum();
            vertSum = ColorMatrixBuilder.getColorMatrix(grayImage, Color::getBlue, WIDTH, c.x(), c.y()).mul(
                    vertKernal).sum();

            orientation = Math.atan( vertSum/horzSum );

            if (manhattan) {
                gradient = vertSum + horzSum;
            }
            else {
                gradient = Math.sqrt(Math.pow(vertSum, 2) + Math.pow(horzSum, 2));
            }

            //apply
            if ( gradient > this.threshHold ) {
                bufferWriter.setColor(c.x(), c.y(),
                        Color.hsb( Math.toDegrees(orientation),
                                preserveSaturation ? targetReader.getColor(c.x(), c.y()).getOpacity(): 1.0,
                                Math.min(1.0, gradient),
                                targetReader.getColor(c.x(), c.y()).getOpacity()
                        )
                );
            }
            else {
                bufferWriter.setColor(c.x(), c.y(),
                        new Color( 0.0, 0.0, 0.0, targetReader.getColor(c.x(), c.y()).getOpacity() )
                );
            }
        });

        return buffer;
    }
}
