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

import fauxpas.entities.*;
import org.jblas.FloatMatrix;

import java.awt.image.BufferedImage;
import java.util.stream.Stream;

/**
 * An canny edge filter which implements Non-maximum suppression, double threshold, and Edge tracking by hysteresis described at https://en.wikipedia.org/wiki/Canny_edge_detector
 * This filter should be applied to the output of a sobel operator.
 */
public class CannyFilter implements Mixer{

    private final FloatMatrix horzKernel;
    private final FloatMatrix vertKernel;
    private final FloatMatrix posSlopeKernel;
    private final FloatMatrix negSlopeKernel;
    private final int WIDTH = 3;
    private final float lowerThreshHold;
    private final float upperThreshHold;

    public CannyFilter() {
        this(0.05f, 0.35f );
    }

    public CannyFilter(float lowerThreshHold, float upperThreshHold) {
        this.horzKernel = new FloatMatrix(WIDTH, WIDTH);
        this.horzKernel.put(0,0, 0.0f);
        this.horzKernel.put( 0, 1, 1.0f);
        this.horzKernel.put(0,2, 0.0f);
        this.horzKernel.put(1,0, 0.0f);
        this.horzKernel.put(1,1, 0.0f);
        this.horzKernel.put(1,2, 0.0f);
        this.horzKernel.put(2,0, 0.0f);
        this.horzKernel.put(2,1,1.0f);
        this.horzKernel.put(2,2,0.0f);

        this.vertKernel = new FloatMatrix(WIDTH, WIDTH);
        this.vertKernel.put(0,0, 0.0f);
        this.vertKernel.put(1,0,0.0f);
        this.vertKernel.put(2,0,  0.0f);
        this.vertKernel.put(0,1,  1.0f);
        this.vertKernel.put(1,1, 0.0f);
        this.vertKernel.put(2,1, 1.0f);
        this.vertKernel.put(0,2, 0.0f);
        this.vertKernel.put(1,2,  0.0f);
        this.vertKernel.put(2,2,  0.0f);

        this.posSlopeKernel = new FloatMatrix(WIDTH, WIDTH);
        this.posSlopeKernel.put(0, 0, 0.0f);
        this.posSlopeKernel.put(1,0,  0.0f);
        this.posSlopeKernel.put(2,0, 1.0f);
        this.posSlopeKernel.put(0,1,  0.0f);
        this.posSlopeKernel.put(1,1,  0.0f);
        this.posSlopeKernel.put(2,1,  0.0f);
        this.posSlopeKernel.put(0,2,  1.0f);
        this.posSlopeKernel.put(1,2,  0.0f);
        this.posSlopeKernel.put(2,2,   0.0f);

        this.negSlopeKernel = new FloatMatrix(WIDTH, WIDTH);
        this.negSlopeKernel.put(0,0,  1.0f);
        this.negSlopeKernel.put(1,0,  0.0f);
        this.negSlopeKernel.put(2,0,  0.0f);
        this.negSlopeKernel.put(0,1,  0.0f);
        this.negSlopeKernel.put(1,1,  0.0f);
        this.negSlopeKernel.put(2,1,  0.0f);
        this.negSlopeKernel.put(0,2,  0.0f);
        this.negSlopeKernel.put(1,2,  0.0f);
        this.negSlopeKernel.put(2,2,  1.0f);

        this.upperThreshHold = upperThreshHold;
        this.lowerThreshHold = lowerThreshHold;
    }

    @Override
    public Stream<Pixel> apply(Stream<Pixel> sample, BufferedImage image) {

        return sample.filter(p ->  p.x() < image.getWidth() && p.y() < image.getHeight()).map( p1 -> {

            int color = image.getRGB(p1.x(), p1.y());

            if (ColorHelper.Brightness(color) != 0.0) {

                float orientation = ColorHelper.Hue( color );
                float gradient =  ColorHelper.Saturation( color ) ;
                float kernelSum = 0.0f;

                //NOTE: for the sake of speed we are ignoring float comparison errors.

                //horizontal line
                if ( ((orientation > 337.5 && orientation <= 360 ) || ( orientation > 0 && orientation <= 22.5 )) ||
                        (orientation > 157.5 && orientation <= 202.5 )) {
                    kernelSum = horzKernel.mul(ColorMatrixBuilder.getNeighborColorMatrix(image, ColorHelper::Brightness, WIDTH, p1.x(), p1.y())).sum();
                }
                //vertical line
                else if ( (orientation > 67.5 && orientation >= 112.5) || ( orientation > 247.5 && orientation <= 292.5 ) ) {
                    kernelSum  = vertKernel.mul(ColorMatrixBuilder.getNeighborColorMatrix(image, ColorHelper::Brightness, WIDTH, p1.x(), p1.y())).sum();
                }
                //positive slope
                else if ( (orientation > 22.5 && orientation >= 67.5) || ( orientation > 202.5 && orientation <= 247.5) ) {
                    kernelSum  = posSlopeKernel.mul(ColorMatrixBuilder.getNeighborColorMatrix(image, ColorHelper::Brightness, WIDTH, p1.x(), p1.y())).sum();
                }
                //negative slope
                else if ( (orientation > 112.5 && orientation >= 157.5) || ( orientation > 292.5 && orientation <= 337.5) ) {
                    kernelSum  = negSlopeKernel.mul(ColorMatrixBuilder.getNeighborColorMatrix(image, ColorHelper::Brightness, WIDTH, p1.x(), p1.y())).sum();
                }

                if (gradient > kernelSum) {
                    return new Pixel(p1.getCoordinate(),  gradient, gradient, gradient,  ColorHelper.Alpha(color) );
                }
                else if (gradient > this.lowerThreshHold) {
                    if (gradient > this.upperThreshHold) {
                        return new Pixel(p1.getCoordinate(), gradient, gradient, gradient, ColorHelper.Alpha(color) );
                    }
                    else if ( checkSurroundingPixels(image, p1.x(), p1.y()) ) {
                        return new Pixel(p1.getCoordinate(), gradient, gradient, gradient, ColorHelper.Alpha(color) );
                    }
                    else {
                        return new Pixel(p1.getCoordinate(), 0f, 0f, 0f, ColorHelper.Alpha(color) );
                    }
                }
                else {
                    return new Pixel(p1.getCoordinate(),0f, 0f, 0f, ColorHelper.Alpha(color) );
                }

            }
            else {
                return new Pixel(p1.getCoordinate(), 0f, 0f, 0f, ColorHelper.Alpha(color) );
            }

        });

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
