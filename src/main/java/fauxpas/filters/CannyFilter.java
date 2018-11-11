package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.jblas.DoubleMatrix;

public class CannyFilter implements Filter{

    private final DoubleMatrix horzKernel;
    private final DoubleMatrix vertKernel;
    private final DoubleMatrix posSlopeKernel;
    private final DoubleMatrix negSlopeKernel;
    private final int WIDTH = 3;
    private final double lowerThreshHold;
    private final double upperThreshHold;

    public CannyFilter() {
        this(0.0001, 0.15 );
    }

    public CannyFilter(double lowerThreshHold, double upperThreshHold) {
        this.horzKernel = new DoubleMatrix(WIDTH, WIDTH);
        this.horzKernel.put(0,0, 0.0);
        this.horzKernel.put( 0, 1, 1.0);
        this.horzKernel.put(0,2, 0.0);
        this.horzKernel.put(1,0, 0.0);
        this.horzKernel.put(1,1, 0.0);
        this.horzKernel.put(1,2, 0.0);
        this.horzKernel.put(2,0, 0.0);
        this.horzKernel.put(2,1,1.0);
        this.horzKernel.put(2,2,0.0);

        this.vertKernel = new DoubleMatrix(WIDTH, WIDTH);
        this.vertKernel.put(0,0, 0.0);
        this.vertKernel.put(1,0,0.0);
        this.vertKernel.put(2,0,  0.0);
        this.vertKernel.put(0,1,  1.0);
        this.vertKernel.put(1,1,0.0);
        this.vertKernel.put(2,1,1.0);
        this.vertKernel.put(0,2, 0.0);
        this.vertKernel.put(1,2,  0.0);
        this.vertKernel.put(2,2,  0.0);

        this.posSlopeKernel = new DoubleMatrix(WIDTH, WIDTH);
        this.posSlopeKernel.put(0, 0, 0.0);
        this.posSlopeKernel.put(1,0,  0.0);
        this.posSlopeKernel.put(2,0,1.0);
        this.posSlopeKernel.put(0,1,  0.0);
        this.posSlopeKernel.put(1,1,  0.0);
        this.posSlopeKernel.put(2,1,  0.0);
        this.posSlopeKernel.put(0,2,  1.0);
        this.posSlopeKernel.put(1,2,  0.0);
        this.posSlopeKernel.put(2,2,   0.0);

        this.negSlopeKernel = new DoubleMatrix(WIDTH, WIDTH);
        this.negSlopeKernel.put(0,0,  1.0);
        this.negSlopeKernel.put(1,0,  0.0);
        this.negSlopeKernel.put(2,0,  0.0);
        this.negSlopeKernel.put(0,1,  0.0);
        this.negSlopeKernel.put(1,1,  0.0);
        this.negSlopeKernel.put(2,1,  0.0);
        this.negSlopeKernel.put(0,2,  0.0);
        this.negSlopeKernel.put(1,2,  0.0);
        this.negSlopeKernel.put(2,2,  1.0);

        this.upperThreshHold = upperThreshHold;
        this.lowerThreshHold = lowerThreshHold;
    }

    @Override
    public Image apply(Image target) {
        PixelReader targetReader = target.getPixelReader();
        WritableImage buffer = new WritableImage((int) target.getWidth(), (int) target.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();
        double orientation;

        double kernelSum;
        double gradient;

        for (int imageY = 0; imageY < target.getHeight(); ++imageY) {
            for (int imageX = 0; imageX < target.getWidth(); ++imageX) {
                if (targetReader.getColor(imageX, imageY).getBrightness() != 0.0) {

                    orientation = targetReader.getColor(imageX, imageY).getHue();
                    gradient = targetReader.getColor(imageX, imageY).getBrightness();
                    kernelSum = 0.0;

                    //horizontal line
                    if ( ((orientation > 337.5 && orientation <= 360 ) || ( orientation > 0 && orientation <= 22.5 )) ||
                            (orientation > 157.5 && orientation <= 202.5 )) {
                        kernelSum = horzKernel.mul(ColorMatrixBuilder.getColorMatrix(target, Color::getBrightness, WIDTH, imageX, imageY)).sum();
                    }
                    //vertical line
                    else if ( (orientation > 67.5 && orientation >= 112.5) || ( orientation > 247.5 && orientation <= 292.5 ) ) {
                        kernelSum  = vertKernel.mul(ColorMatrixBuilder.getColorMatrix(target, Color::getBrightness, WIDTH, imageX, imageY)).sum();
                    }
                    //positive slope
                    else if ( (orientation > 22.5 && orientation >= 67.5) || ( orientation > 202.5 && orientation <= 247.5) ) {
                        kernelSum  = posSlopeKernel.mul(ColorMatrixBuilder.getColorMatrix(target, Color::getBrightness, WIDTH, imageX, imageY)).sum();
                    }
                    //negative slope
                    else if ( (orientation > 112.5 && orientation >= 157.5) || ( orientation > 292.5 && orientation <= 337.5) ) {
                        kernelSum  = negSlopeKernel.mul(ColorMatrixBuilder.getColorMatrix(target, Color::getBrightness, WIDTH, imageX, imageY)).sum();
                    }

                    if (gradient > kernelSum) {
                        bufferWriter.setColor(imageX, imageY, Color.gray(gradient, targetReader.getColor(imageX, imageY).getOpacity()));
                    }
                    else if (gradient > this.lowerThreshHold) {
                        if (gradient > this.upperThreshHold) {
                            bufferWriter.setColor(imageX, imageY, Color.gray(gradient, targetReader.getColor(imageX, imageY).getOpacity()));
                        }
                        else if ( checkSurroundingPixels(target, targetReader, imageX, imageY) ) {
                            bufferWriter.setColor(imageX, imageY, Color.gray(gradient, targetReader.getColor(imageX, imageY).getOpacity()));
                        }
                        else {
                            bufferWriter.setColor(imageX, imageY, Color.gray(0.0, targetReader.getColor(imageX, imageY).getOpacity()));
                        }
                    }
                    else {
                        bufferWriter.setColor(imageX, imageY, Color.gray(0.0, targetReader.getColor(imageX, imageY).getOpacity()));
                    }

                }
                else {
                    bufferWriter.setColor(imageX, imageY, Color.gray(0.0, targetReader.getColor(imageX, imageY).getOpacity()));
                }
            }
        }

        return buffer;
    }

    public boolean checkSurroundingPixels(Image target, PixelReader targetReader, int imageX, int imageY) {

        if (imageX - 1 > 0 && imageY -1 > 0) {
            if (targetReader.getColor(imageX-1, imageY-1).getBrightness() > this.lowerThreshHold ) {
                return true;
            }
        }
        else if (imageY - 1 > 0) {
            if (targetReader.getColor(imageX, imageY-1).getBrightness() > this.lowerThreshHold ) {
                return true;
            }
        }
        else if (imageX + 1 < target.getWidth() && imageY -1 > 0) {
            if (targetReader.getColor(imageX+1, imageY-1).getBrightness() > this.lowerThreshHold ) {
                return true;
            }
        }
        else if (imageX + 1 < target.getWidth()) {
            if (targetReader.getColor(imageX+1, imageY).getBrightness() > this.lowerThreshHold ) {
                return true;
            }
        }
        else if (imageX + 1 < target.getWidth() && imageY + 1 < target.getHeight()) {
            if (targetReader.getColor(imageX-1, imageY-1).getBrightness() > this.lowerThreshHold ) {
                return true;
            }
        }
        else if (imageY + 1 < target.getHeight()) {
            if (targetReader.getColor(imageX, imageY+1).getBrightness() > this.lowerThreshHold ) {
                return true;
            }
        }
        else if (imageX - 1 > 0 && imageY + 1 < target.getHeight()) {
            if (targetReader.getColor(imageX-1, imageY+1).getBrightness() > this.lowerThreshHold ) {
                return true;
            }
        }
        else if (imageX - 1 > 0 ) {
            if (targetReader.getColor(imageX-1, imageY).getBrightness() > this.lowerThreshHold ) {
                return true;
            }
        }

        return false;
    }
}
