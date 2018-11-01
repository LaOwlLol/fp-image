package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.jblas.DoubleMatrix;

public class GaussianBlur implements Filter {

    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;
    private double kernelValue;
    private double standardDeviation;
    private DoubleMatrix kernel;
    private int width;
    private final int mid;

    public GaussianBlur(int width, double standardDeviation) {

        //todo all good if no true center to kernel?
        if (width % 2 == 0) {
            System.err.println("GaussianBlur width requested is not an odd value. adding 1 to normalize things.");
            width = width+1;
        }

        this.standardDeviation = standardDeviation;
        this.width = width;
        this.kernel = new DoubleMatrix(this.width, this.width);

        this.mid = width/2;

        //pre calculate parts of gaussian equation that don't contain x,y
        double expDenom = Math.PI * Math.pow(this.standardDeviation,2);
        double outerDenom = 2.0 * expDenom;

        //initialize kernel
        this.kernelValue = 0.0;
        for (int y = 0; y < width; ++y) {
            for (int x = 0; x < width; ++x) {
                int i = x - mid;
                int j = y - mid;
                double expNumer = Math.pow(i, 2) + Math.pow(j, 2);
                double kvalue = (1.0/outerDenom) * Math.exp(expNumer/expDenom);
                this.kernel.put(x, y, kvalue);
                this.kernelValue += kvalue;
            }
        }
    }

    private DoubleMatrix getColorMatrix(Image target, ColorReader colorReader, int imageX, int imageY ) {
        DoubleMatrix colors = new DoubleMatrix(this.width, this.width);
        PixelReader targetReader = target.getPixelReader();

        for (int kernelY = 0; kernelY < this.width; ++kernelY ) {
            for (int kernelX = 0; kernelX < this.width; ++kernelX) {

                int i = kernelX - this.mid;
                int j = kernelY - this.mid;

                if ( ((imageX+i > 0) && (imageX+i < target.getWidth())) && ((imageY+j > 0) && (imageY+j < target.getHeight())) ) {
                    colors.put(kernelX, kernelY, colorReader.getColorProperty(targetReader.getColor(imageX+i, imageY+j)) );
                }

            }
        }

        return colors;
    }

    @Override
    public Image apply(Image target) {

        PixelReader targetReader = target.getPixelReader();
        WritableImage buffer = new WritableImage((int) target.getWidth(), (int) target.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        for (int imageY = 0; imageY < target.getHeight(); ++imageY) {
            for (int imageX = 0; imageX < target.getWidth(); ++imageX) {

                //apply
                bufferWriter.setColor(imageX, imageY, new Color(
                      getColorMatrix(target, Color::getRed, imageX, imageY).mul(this.kernel).sum()/this.kernelValue,
                      getColorMatrix(target, Color::getGreen, imageX, imageY).mul(this.kernel).sum()/this.kernelValue,
                      getColorMatrix(target, Color::getBlue, imageX, imageY).mul(this.kernel).sum()/this.kernelValue,
                      targetReader.getColor(imageX, imageY).getOpacity() ) );
            }
        }

        return buffer;
    }

}
