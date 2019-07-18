/*
 * fp-image an image manipulation API.
 *     Copyright (C) 2019 Nate G. - LaOwlLol
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fauxpas.filters;

import fauxpas.entities.ColorHelper;
import fauxpas.entities.ColorMatrixBuilder;
import fauxpas.entities.ImageHelper;
import fauxpas.entities.Range;
import org.jblas.DoubleMatrix;

import java.awt.image.BufferedImage;

/**
 * An canny edge filter which implements Non-maximum suppression, Double threshold, and Edge tracking by hysteresis described at https://en.wikipedia.org/wiki/Canny_edge_detector
 * This filter should be applied to the output of a sobel operator.
 */
public class CannyFilter implements Filter{

    private final DoubleMatrix horzKernel;
    private final DoubleMatrix vertKernel;
    private final DoubleMatrix posSlopeKernel;
    private final DoubleMatrix negSlopeKernel;
    private final int WIDTH = 3;
    private final double lowerThreshHold;
    private final double upperThreshHold;

    public CannyFilter() {
        this(0.05, 0.35 );
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
    public BufferedImage apply(BufferedImage image) {

        BufferedImage buffer = ImageHelper.AllocateARGBBuffer(image.getWidth(), image.getHeight());

        new Range(0, image.getWidth(), 0, image.getHeight()).get().forEach( c -> {

            int color = image.getRGB(c.x(), c.y());

            if (ColorHelper.Brightness(color) != 0.0) {

                float orientation = ColorHelper.Hue( color );
                float gradient =  ColorHelper.Saturation( color ) ;
                double kernelSum = 0.0;
                int gray = ColorHelper.FloatChannelToInt(gradient);

                //NOTE: for the sake of speed we are ignoring double comparison errors.

                //horizontal line
                if ( ((orientation > 337.5 && orientation <= 360 ) || ( orientation > 0 && orientation <= 22.5 )) ||
                        (orientation > 157.5 && orientation <= 202.5 )) {
                    kernelSum = horzKernel.mul(ColorMatrixBuilder.getNeighborColorMatrix(image, ColorHelper::Brightness, WIDTH, c.x(), c.y())).sum();
                }
                //vertical line
                else if ( (orientation > 67.5 && orientation >= 112.5) || ( orientation > 247.5 && orientation <= 292.5 ) ) {
                    kernelSum  = vertKernel.mul(ColorMatrixBuilder.getNeighborColorMatrix(image, ColorHelper::Brightness, WIDTH, c.x(), c.y())).sum();
                }
                //positive slope
                else if ( (orientation > 22.5 && orientation >= 67.5) || ( orientation > 202.5 && orientation <= 247.5) ) {
                    kernelSum  = posSlopeKernel.mul(ColorMatrixBuilder.getNeighborColorMatrix(image, ColorHelper::Brightness, WIDTH, c.x(), c.y())).sum();
                }
                //negative slope
                else if ( (orientation > 112.5 && orientation >= 157.5) || ( orientation > 292.5 && orientation <= 337.5) ) {
                    kernelSum  = negSlopeKernel.mul(ColorMatrixBuilder.getNeighborColorMatrix(image, ColorHelper::Brightness, WIDTH, c.x(), c.y())).sum();
                }

                if (gradient > kernelSum) {
                    buffer.setRGB(c.x(), c.y(), ColorHelper.ColorValueFromRGBA( gray, gray, gray, ColorHelper.Alpha(color) ));
                }
                else if (gradient > this.lowerThreshHold) {
                    if (gradient > this.upperThreshHold) {
                        buffer.setRGB(c.x(), c.y(), ColorHelper.ColorValueFromRGBA( gray, gray, gray, ColorHelper.Alpha(color) ));
                    }
                    else if ( checkSurroundingPixels(image, c.x(), c.y()) ) {
                        buffer.setRGB(c.x(), c.y(), ColorHelper.ColorValueFromRGBA( gray, gray, gray, ColorHelper.Alpha(color) ));
                    }
                    else {
                        buffer.setRGB(c.x(), c.y(), ColorHelper.ColorValueFromRGBA( 0, 0, 0, ColorHelper.Alpha(color) ));
                    }
                }
                else {
                    buffer.setRGB(c.x(), c.y(), ColorHelper.ColorValueFromRGBA( 0, 0, 0, ColorHelper.Alpha(color) ));
                }

            }
            else {
                buffer.setRGB(c.x(), c.y(), ColorHelper.ColorValueFromRGBA( 0, 0, 0, ColorHelper.Alpha(color)) );
            }

        });

        return buffer;
    }

    private boolean checkSurroundingPixels(BufferedImage target, int imageX, int imageY) {

        if (imageX - 1 > 0 && imageY -1 > 0) {
            return ColorHelper.Brightness(target.getRGB(imageX, imageY)) > this.lowerThreshHold;
        }
        else if (imageY - 1 > 0) {
            return ColorHelper.Brightness(target.getRGB(imageX, imageY)) > this.lowerThreshHold;
        }
        else if (imageX + 1 < target.getWidth() && imageY -1 > 0) {
            return ColorHelper.Brightness(target.getRGB(imageX, imageY)) > this.lowerThreshHold;
        }
        else if (imageX + 1 < target.getWidth()) {
            return ColorHelper.Brightness(target.getRGB(imageX, imageY)) > this.lowerThreshHold;
        }
        else if (imageX + 1 < target.getWidth() && imageY + 1 < target.getHeight()) {
            return ColorHelper.Brightness(target.getRGB(imageX, imageY)) > this.lowerThreshHold;
        }
        else if (imageY + 1 < target.getHeight()) {
            return ColorHelper.Brightness(target.getRGB(imageX, imageY)) > this.lowerThreshHold;
        }
        else if (imageX - 1 > 0 && imageY + 1 < target.getHeight()) {
            return ColorHelper.Brightness(target.getRGB(imageX, imageY)) > this.lowerThreshHold;
        }
        else if (imageX - 1 > 0 ) {
            return ColorHelper.Brightness(target.getRGB(imageX, imageY)) > this.lowerThreshHold;
        }

        return false;
    }
}
