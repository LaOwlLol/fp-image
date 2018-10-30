package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class CannyFilter implements Filter, Convolution{

    private final double[][] horzKernal;
    private final double[][] vertKernal;
    private final double[][] posSlopeKernal;
    private final double[][] negSlopeKernal;
    private final int WIDTH = 3;
    private double lowerThreshHold;
    private double upperThreshHold;
    /*private double[] lowerThreshHold;
    private double[] upperThreshHold;
    private final int ORIENTATION_COUNT = 4;
    private final int HORIZONTAL = 0;
    private final int VERTICAL = 1;
    private final int POSITIVE_SLOPE = 2;
    private final int NEGATIVE_SLOPE = 3;*/

    public CannyFilter() {

        this.horzKernal = new double[WIDTH][WIDTH];
        this.horzKernal[0][0] = 0.0;
        this.horzKernal[1][0] = 1.0;
        this.horzKernal[2][0] = 0.0;
        this.horzKernal[0][1] = 0.0;
        this.horzKernal[1][1] = 0.0;
        this.horzKernal[2][1] = 0.0;
        this.horzKernal[0][2] = 0.0;
        this.horzKernal[1][2] = 1.0;
        this.horzKernal[2][2] = 0.0;

        this.vertKernal = new double[WIDTH][WIDTH];
        this.vertKernal[0][0] = 0.0;
        this.vertKernal[0][1] = 0.0;
        this.vertKernal[0][2] = 0.0;
        this.vertKernal[1][0] = 1.0;
        this.vertKernal[1][1] = 0.0;
        this.vertKernal[1][2] = 1.0;
        this.vertKernal[2][0] = 0.0;
        this.vertKernal[2][1] = 0.0;
        this.vertKernal[2][2] = 0.0;

        this.posSlopeKernal = new double[WIDTH][WIDTH];
        this.posSlopeKernal[0][0] = 0.0;
        this.posSlopeKernal[0][1] = 0.0;
        this.posSlopeKernal[0][2] = 1.0;
        this.posSlopeKernal[1][0] = 0.0;
        this.posSlopeKernal[1][1] = 0.0;
        this.posSlopeKernal[1][2] = 0.0;
        this.posSlopeKernal[2][0] = 1.0;
        this.posSlopeKernal[2][1] = 0.0;
        this.posSlopeKernal[2][2] = 0.0;

        this.negSlopeKernal = new double[WIDTH][WIDTH];
        this.negSlopeKernal[0][0] = 1.0;
        this.negSlopeKernal[0][1] = 0.0;
        this.negSlopeKernal[0][2] = 0.0;
        this.negSlopeKernal[1][0] = 0.0;
        this.negSlopeKernal[1][1] = 0.0;
        this.negSlopeKernal[1][2] = 0.0;
        this.negSlopeKernal[2][0] = 0.0;
        this.negSlopeKernal[2][1] = 0.0;
        this.negSlopeKernal[2][2] = 1.0;

        this.upperThreshHold = 0.15;
        this.lowerThreshHold = 0.0001;
    }

    public CannyFilter(double upperThreshHold, double lowerThreshHold) {
        this();
        this.upperThreshHold = upperThreshHold;
        this.lowerThreshHold = lowerThreshHold;
    }

    private double sumKernel(double[][][] tempKernal) {
        double sum = 0;

        for (int kernelY = 0; kernelY < WIDTH; ++kernelY ) {
            for (int kernelX = 0; kernelX < WIDTH; ++kernelX) {
                sum += tempKernal[kernelX][kernelY][0];
            }
        }
        return sum;
    }

    @Override
    public double[][][] computeKernel(Image target, PixelReader targetReader, double[][] convolution, int imageX, int imageY) {
        double[][][] tempKernel = new double[WIDTH][WIDTH][1];

        //multiply pass
        for (int kernelY = 0; kernelY < WIDTH; ++kernelY ) {
            for (int kernelX = 0; kernelX < WIDTH; ++kernelX) {

                int i = kernelX - (WIDTH/2);
                int j = kernelY - (WIDTH/2);

                if ((imageX+i) > 0 && (imageX+i) < target.getWidth() &&
                        (imageY+j) > 0 && (imageY+j) < target.getHeight()) {
                    tempKernel[kernelX][kernelY][0] = targetReader.getColor(imageX+i,imageY+j).getRed() *
                            convolution[kernelX][kernelY];
                }

            }
        }
        return tempKernel;
    }

    @Override
    public Image apply(Image target) {
        PixelReader targetReader = target.getPixelReader();
        WritableImage buffer = new WritableImage((int) target.getWidth(), (int) target.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();
        double orientation;

        double[][][] convolutionKernel = new double[WIDTH][WIDTH][1];

        double kernelSum;
        double gradient;

        for (int imageY = 0; imageY < target.getHeight(); ++imageY) {
            for (int imageX = 0; imageX < target.getWidth(); ++imageX) {
                if (targetReader.getColor(imageX, imageY).getBrightness() != 0.0) {

                    orientation = targetReader.getColor(imageX, imageY).getHue();
                    gradient = targetReader.getColor(imageX, imageY).getBrightness();

                    //horizontal line
                    if ( ((orientation > 337.5 && orientation <= 360 ) || ( orientation > 0 && orientation <= 22.5 )) ||
                            (orientation > 157.5 && orientation <= 202.5 )) {
                        convolutionKernel = computeKernel(target, targetReader, horzKernal, imageX, imageY);
                    }
                    //vertical line
                    else if ( (orientation > 67.5 && orientation >= 112.5) || ( orientation > 247.5 && orientation <= 292.5 ) ) {
                        convolutionKernel = computeKernel(target, targetReader, vertKernal, imageX, imageY);
                    }
                    //positive slope
                    else if ( (orientation > 22.5 && orientation >= 67.5) || ( orientation > 202.5 && orientation <= 247.5) ) {
                        convolutionKernel = computeKernel(target, targetReader, posSlopeKernal, imageX, imageY);
                    }
                    //negative slope
                    else if ( (orientation > 112.5 && orientation >= 157.5) || ( orientation > 292.5 && orientation <= 337.5) ) {
                        convolutionKernel = computeKernel(target, targetReader, negSlopeKernal, imageX, imageY);
                    }

                    //sum pass;
                    kernelSum = Math.abs(sumKernel(convolutionKernel));

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
