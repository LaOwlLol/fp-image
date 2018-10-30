package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class GaussianBlur implements Filter, Convolution {

    private static final int RED = 0;
    private static final int BLUE = 1;
    private static final int GREEN = 2;
    private double kernelValue;
    private double standardDeviation;
    private double [][] kernel;
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
        this.kernel = new double[this.width][this.width];

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
                this.kernel[x][y] = kvalue;
                this.kernelValue += kvalue;
            }
        }


    }

    @Override
    public Image apply(Image target) {

        PixelReader targetReader = target.getPixelReader();
        WritableImage buffer = new WritableImage((int) target.getWidth(), (int) target.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        double[][][] convolutionKernel;

        double [] sum;

        for (int imageY = 0; imageY < target.getHeight(); ++imageY) {
            for (int imageX = 0; imageX < target.getWidth(); ++imageX) {

                convolutionKernel = computeKernel(target, targetReader, this.kernel, imageX, imageY);

                //sum pass;
                sum = sumKernal(convolutionKernel);

                //apply

                bufferWriter.setColor(imageX, imageY,
                      new Color(sum[RED]/this.kernelValue,
                            sum[GREEN]/this.kernelValue,
                            sum[BLUE]/this.kernelValue,
                            targetReader.getColor(imageX, imageY).getOpacity())
                );
            }
        }

        return buffer;
    }

    private double[] sumKernal(double[][][] tempKernel) {
        double [] sum = new double[GREEN+1];
        sum[RED] = 0.0;
        sum[BLUE] = 0.0;
        sum[GREEN] = 0.0;
        for (int kernelY = 0; kernelY < this.width; ++kernelY ) {
            for (int kernelX = 0; kernelX < this.width; ++kernelX) {
                sum[RED] += tempKernel[kernelX][kernelY][RED];
                sum[BLUE] += tempKernel[kernelX][kernelY][BLUE];
                sum[GREEN] += tempKernel[kernelX][kernelY][GREEN];
            }
        }
        return sum;
    }

    @Override
    public double[][][] computeKernel(Image target, PixelReader targetReader, double[][] convolution, int imageX, int imageY) {
        double[][][] tempKernel = new double[this.width][this.width][GREEN+1];

        //multiply pass
        for (int kernelY = 0; kernelY < this.width; ++kernelY ) {
            for (int kernelX = 0; kernelX < this.width; ++kernelX) {

                int i = kernelX - this.mid;
                int j = kernelY - this.mid;

                if ((imageX+i) > 0 && (imageX+i) < target.getWidth() &&
                      (imageY+j) > 0 && (imageY+j) < target.getHeight()) {
                    tempKernel[kernelX][kernelY][RED] = targetReader.getColor(imageX+i,imageY+j).getRed() *
                          convolution[kernelX][kernelY];

                    tempKernel[kernelX][kernelY][BLUE] = targetReader.getColor(imageX+i,imageY+j).getBlue() *
                          convolution[kernelX][kernelY];

                    tempKernel[kernelX][kernelY][GREEN] = targetReader.getColor(imageX+i,imageY+j).getGreen() *
                          convolution[kernelX][kernelY];
                }

            }
        }
        return tempKernel;
    }
}
