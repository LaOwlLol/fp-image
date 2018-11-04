package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.jblas.DoubleMatrix;

public class CannyFilter implements Filter{

    private final DoubleMatrix horzKernal;
    private final DoubleMatrix vertKernal;
    private final DoubleMatrix posSlopeKernal;
    private final DoubleMatrix negSlopeKernal;
    private final int WIDTH = 3;
    private double lowerThreshHold;
    private double upperThreshHold;

    public CannyFilter() {

        this.horzKernal = new DoubleMatrix(WIDTH, WIDTH);
        this.horzKernal.put(0,0, 0.0);
        this.horzKernal.put(1, 0, 1.0);
        this.horzKernal.put(2,0, 0.0);
        this.horzKernal.put(0,1, 0.0);
        this.horzKernal.put(1,1, 0.0);
        this.horzKernal.put(2, 1,0.0);
        this.horzKernal.put(0,2, 0.0);
        this.horzKernal.put(1,2,1.0);
        this.horzKernal.put(2,2,0.0);

        this.vertKernal = new DoubleMatrix(WIDTH, WIDTH);
        this.vertKernal.put(0,0, 0.0);
        this.vertKernal.put(0,1,0.0);
        this.vertKernal.put(0, 2, 0.0);
        this.vertKernal.put(1, 0, 1.0);
        this.vertKernal.put(1,1,0.0);
        this.vertKernal.put(1,2,1.0);
        this.vertKernal.put(2, 0,0.0);
        this.vertKernal.put(2, 1, 0.0);
        this.vertKernal.put(2, 2, 0.0);

        this.posSlopeKernal = new DoubleMatrix(WIDTH, WIDTH);
        this.posSlopeKernal.put(0, 0, 0.0);
        this.posSlopeKernal.put(0, 1, 0.0);
        this.posSlopeKernal.put(0,2,1.0);
        this.posSlopeKernal.put(1, 0, 0.0);
        this.posSlopeKernal.put(1, 1, 0.0);
        this.posSlopeKernal.put(1, 2, 0.0);
        this.posSlopeKernal.put(2, 0, 1.0);
        this.posSlopeKernal.put(2, 1, 0.0);
        this.posSlopeKernal.put(2, 2,  0.0);

        this.negSlopeKernal = new DoubleMatrix(WIDTH, WIDTH);
        this.negSlopeKernal.put(0, 0, 1.0);
        this.negSlopeKernal.put(0, 1, 0.0);
        this.negSlopeKernal.put(0, 2, 0.0);
        this.negSlopeKernal.put(1, 0, 0.0);
        this.negSlopeKernal.put(1, 1, 0.0);
        this.negSlopeKernal.put(1, 2, 0.0);
        this.negSlopeKernal.put(2, 0, 0.0);
        this.negSlopeKernal.put(2, 1, 0.0);
        this.negSlopeKernal.put(2, 2, 1.0);

        this.upperThreshHold = 0.15;
        this.lowerThreshHold = 0.0001;
    }

    public CannyFilter(double upperThreshHold, double lowerThreshHold) {
        this();
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
                        kernelSum = horzKernal.mul(ColorMatrixBuilder.getColorMatrix(target, Color::getBlue, WIDTH, imageX, imageY)).sum();
                    }
                    //vertical line
                    else if ( (orientation > 67.5 && orientation >= 112.5) || ( orientation > 247.5 && orientation <= 292.5 ) ) {
                        kernelSum  = vertKernal.mul(ColorMatrixBuilder.getColorMatrix(target, Color::getBlue, WIDTH, imageX, imageY)).sum();
                    }
                    //positive slope
                    else if ( (orientation > 22.5 && orientation >= 67.5) || ( orientation > 202.5 && orientation <= 247.5) ) {
                        kernelSum  = posSlopeKernal.mul(ColorMatrixBuilder.getColorMatrix(target, Color::getBlue, WIDTH, imageX, imageY)).sum();
                    }
                    //negative slope
                    else if ( (orientation > 112.5 && orientation >= 157.5) || ( orientation > 292.5 && orientation <= 337.5) ) {
                        kernelSum  = negSlopeKernal.mul(ColorMatrixBuilder.getColorMatrix(target, Color::getBlue, WIDTH, imageX, imageY)).sum();
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
            if (targetReader.getColor(imageX-1, imageY-1).getBrightness() > this.upperThreshHold ) {
                return true;
            }
        }
        else if (imageY - 1 > 0) {
            if (targetReader.getColor(imageX, imageY-1).getBrightness() > this.upperThreshHold ) {
                return true;
            }
        }
        else if (imageX + 1 < target.getWidth() && imageY -1 > 0) {
            if (targetReader.getColor(imageX+1, imageY-1).getBrightness() > this.upperThreshHold ) {
                return true;
            }
        }
        else if (imageX + 1 < target.getWidth()) {
            if (targetReader.getColor(imageX+1, imageY).getBrightness() > this.upperThreshHold ) {
                return true;
            }
        }
        else if (imageX + 1 < target.getWidth() && imageY + 1 < target.getHeight()) {
            if (targetReader.getColor(imageX-1, imageY-1).getBrightness() > this.upperThreshHold ) {
                return true;
            }
        }
        else if (imageY + 1 < target.getHeight()) {
            if (targetReader.getColor(imageX, imageY+1).getBrightness() > this.upperThreshHold ) {
                return true;
            }
        }
        else if (imageX - 1 > 0 && imageY + 1 < target.getHeight()) {
            if (targetReader.getColor(imageX-1, imageY+1).getBrightness() > this.upperThreshHold ) {
                return true;
            }
        }
        else if (imageX - 1 > 0 ) {
            if (targetReader.getColor(imageX-1, imageY).getBrightness() > this.upperThreshHold ) {
                return true;
            }
        }

        return false;
    }
}
