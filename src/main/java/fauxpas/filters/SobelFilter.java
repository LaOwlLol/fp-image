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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

/**
 * A sobel operator filter.
 *
 * The resulting image will contain colored pixels where the contrast or color gradient is higher than the threshold.  The hue of these pixels will match the direction of the local gradient (gx/gy).  Pixels with gradient below the threshold are colored black.
 *
 * The preserve saturation option will apply the original image's saturation at a pixel colored by the sobel operator.
 *
 * If the manhattan option is set the threshold with be compared to the l1 norm gx+gy instead of the default l2 norm sqrt(gx^2+gy^2).  This can be an optimization but the resulting image will contain more color pixel which may not be the best result for passing to a canny edge detector.
 *
 * This filter should be run on the output of a smoothing or noise reduction filter like a GaussianBlur then a GrayScale filter.
 */
public class SobelFilter implements Mixer {

    private final int WIDTH = 3;

    private final float threshHold;
    private final FloatMatrix horzKernal;
    private final FloatMatrix vertKernal;

    private final boolean preserveSaturation;
    private final boolean manhattan;

    public SobelFilter() {
        this(0.2f, false, false);
    }

    public SobelFilter(float threshHold) {
        this(threshHold, false, false);

    }

    public SobelFilter(float threshHold, boolean preserveSaturation) {
        this(threshHold, preserveSaturation, false);

    }
    
    public SobelFilter(float threshHold, boolean preserveSaturation, boolean manhattan) {
        this.horzKernal = new FloatMatrix(WIDTH, WIDTH);
        this.horzKernal.put(0,0, -1.0f);
        this.horzKernal.put(0,1, -1.0f);
        this.horzKernal.put(0,2, -1.0f);
        this.horzKernal.put(2,0, 1.0f);
        this.horzKernal.put(2,1,1.0f);
        this.horzKernal.put(2,2,1.0f);

        this.vertKernal = new FloatMatrix(WIDTH,WIDTH);
        this.vertKernal.put(0,0, -1.0f);
        this.vertKernal.put(1,0,-1.0f);
        this.vertKernal.put(2,0, -1.0f);
        this.vertKernal.put(0,2, 1.0f);
        this.vertKernal.put(1,2,1.0f);
        this.vertKernal.put(2,2,1.0f);

        this.threshHold = threshHold;
        this.preserveSaturation = preserveSaturation;
        this.manhattan = manhattan;
    }

    @Override
    public Stream<Pixel> apply(Stream<Pixel> sample, BufferedImage image) {

        return sample.filter(p ->  p.x() < image.getWidth() && p.y() < image.getHeight()).map(p1 -> {

            float orientation;
            float horzSum;
            float vertSum;
            float gradient;

            //sum pass;
            horzSum = ColorMatrixBuilder.getNeighborColorMatrix(
                image,
                (color) -> ColorHelper.IntChannelToFloat( color.getGreen() ),
                WIDTH,
                p1.x(), p1.y()
            ).mul(horzKernal).sum();
            vertSum = ColorMatrixBuilder.getNeighborColorMatrix(
                image,
                (color) -> ColorHelper.IntChannelToFloat( color.getGreen() ),
                WIDTH,
                p1.x(), p1.y()
            ).mul(vertKernal).sum();

            orientation = (float) Math.atan( vertSum/horzSum );

            if (manhattan) {
                gradient = ColorHelper.GradientNormalize( vertSum + horzSum );
            }
            else {
                gradient = ColorHelper.GradientNormalize(  (float) Math.sqrt(Math.pow(vertSum, 2) + Math.pow(horzSum, 2)) );
            }

            //apply
            if ( gradient > this.threshHold ) {
                Color hsb = ColorHelper.ColorFromColorValue(
                    Color.HSBtoRGB(
                        orientation,
                        (preserveSaturation) ? ColorHelper.Saturation(image.getRGB(p1.x(), p1.y())) : 1.0f,
                        Math.min(1.0f, gradient )
                    )
                );
                return new Pixel(
                    p1.getCoordinate(),
                    hsb.getRed(),
                    hsb.getGreen(),
                    hsb.getBlue(),
                    ColorHelper.ColorFromColorValue( image.getRGB(p1.x(), p1.y()) ).getAlpha()
                );
            }
            else {
                return new Pixel(
                    p1.getCoordinate(),
                    0f, 0f, 0f,
                    ColorHelper.ColorFromColorValue( image.getRGB(p1.x(), p1.y()) ).getAlpha()
                );
            }
        });
    }
}
