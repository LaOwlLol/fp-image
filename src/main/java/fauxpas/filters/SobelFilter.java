package fauxpas.filters;

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

    double threshHold;
    private DoubleMatrix horzKernal;
    private DoubleMatrix vertKernal;

    private boolean preserveSaturation;

    public SobelFilter() {
        this.horzKernal = new DoubleMatrix(WIDTH, WIDTH);
        this.horzKernal.put(0,0, -0.5);
        this.horzKernal.put(1,0, -1);
        this.horzKernal.put(2,0, -0.5);
        this.horzKernal.put(0,2, 0.5);
        this.horzKernal.put(1,2,1);
        this.horzKernal.put(2,2,0.5);

        this.vertKernal = new DoubleMatrix(WIDTH,WIDTH);
        this.vertKernal.put(0,0, -0.5);
        this.vertKernal.put(0,1,-1);
        this.vertKernal.put(0,2, -0.5);
        this.vertKernal.put(2,0, 0.5);
        this.vertKernal.put(2,1,1);
        this.vertKernal.put(2,2,0.5);

        this.threshHold = 0.25;
        this.preserveSaturation = true;
    }

    public SobelFilter(double threshHold) {
        this();
        this.threshHold = threshHold;
    }

    @Override
    public Image apply(Image target) {

        //make sure it's gray.
        Image grayImage = new GrayscaleFilter().apply(target);

        PixelReader targetReader = grayImage.getPixelReader();
        WritableImage buffer = new WritableImage((int) target.getWidth(), (int) target.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        double orientation;
        double horzSum;
        double vertSum;
        double gradient;

        for (int imageY = 0; imageY < target.getHeight(); ++imageY) {
            for (int imageX = 0; imageX < target.getWidth(); ++imageX) {

                //sum pass;
                horzSum = ColorMatrixBuilder.getColorMatrix(target, Color::getBlue, WIDTH, imageX, imageY).mul(
                        horzKernal).sum();
                vertSum = ColorMatrixBuilder.getColorMatrix(target, Color::getBlue, WIDTH, imageX, imageY).mul(
                        vertKernal).sum();

                orientation = Math.atan( vertSum/horzSum );
                gradient = Math.sqrt( Math.pow( vertSum, 2) + Math.pow(horzSum, 2));

                //apply
                if ( gradient > this.threshHold ) {
                    bufferWriter.setColor(imageX, imageY, Color.hsb( Math.toDegrees(orientation), 1.0,
                                    Math.min(1.0, gradient),
                                    preserveSaturation ? targetReader.getColor(imageX, imageY).getOpacity() : 1.0 )
                    );
                }
                else {
                    bufferWriter.setColor(imageX, imageY,
                            new Color( 0.0, 0.0, 0.0, targetReader.getColor(imageX, imageY).getOpacity())
                    );
                }
            }
        }

        return buffer;
    }
}
