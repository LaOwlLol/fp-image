package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.color.ColorSpace;
import java.util.Optional;

public class SobelFilter implements Filter, Convolution {

    private static final int RED = 0;
    private static final int BLUE = 1;
    private static final int GREEN = 2;
    private final int WIDTH = 3;
    double threshHold;
    double[][] horzKernal;
    double[][] vertKernal;
    double[][] orientation;

    public SobelFilter() {
        this.horzKernal = new double[WIDTH][WIDTH];
        this.horzKernal[0][0] = -0.5;
        this.horzKernal[1][0] = -1;
        this.horzKernal[2][0] = -0.5;
        this.horzKernal[0][2] = 0.5;
        this.horzKernal[1][2] = 1;
        this.horzKernal[2][2] = 0.5;

        this.vertKernal = new double[WIDTH][WIDTH];
        this.vertKernal[0][0] = -0.5;
        this.vertKernal[0][1] = -1;
        this.vertKernal[0][2] = -0.5;
        this.vertKernal[2][0] = 0.5;
        this.vertKernal[2][1] = 1;
        this.vertKernal[2][2] = 0.5;

        this.threshHold = 0.25;
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
        this.orientation = new double[(int) target.getWidth()][(int) target.getHeight()];

        double[][][] horzConvolutionKernel;
        double[][][] vertConvolutionKernel;

        double horzSum;
        double vertSum;

        for (int imageY = 0; imageY < target.getHeight(); ++imageY) {
            for (int imageX = 0; imageX < target.getWidth(); ++imageX) {

                horzConvolutionKernel = computeKernel(grayImage, targetReader, horzKernal, imageY, imageX);
                vertConvolutionKernel = computeKernel(grayImage, targetReader, vertKernal, imageY, imageX);

                //sum pass;
                horzSum = sumKernel(horzConvolutionKernel);
                vertSum = sumKernel(vertConvolutionKernel);

                //orientation[imageX][imageY] = ((Math.atan( vertSum/horzSum ) / Math.PI) + 0.5) * Math.PI ;
                orientation[imageX][imageY] = Math.abs(Math.atan(vertSum/horzSum));

                //apply
                //
                    bufferWriter.setColor(imageX, imageY,
                            Color.hsb( Math.toDegrees( orientation[imageX][imageY] ),
                                    1.0,
                                    1.0,
                                    targetReader.getColor(imageX, imageY).getOpacity())
                    );
                /*}
                else {
                    bufferWriter.setColor(imageX, imageY,
                            new Color( 0.0, 0.0, 0.0, targetReader.getColor(imageX, imageY).getOpacity())
                    );
                }
                */
            }
        }

        return buffer;
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
    public double[][][] computeKernel(Image target, PixelReader targetReader, double[][] convolution, int imageY, int imageX) {
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
                    /*tempKernel[kernelX][kernelY][BLUE] = targetReader.getColor(imageX+i,imageY+j).getBlue() *
                          convolution[kernelX][kernelY];*/

                    /*tempKernel[kernelX][kernelY][GREEN] = targetReader.getColor(imageX+i,imageY+j).getGreen() *
                          convolution[kernelX][kernelY];*/
                }

            }
        }
        return tempKernel;
    }
}
