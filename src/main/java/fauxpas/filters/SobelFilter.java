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

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A sobel operator filter.
 *
 * The resulting image will contain colored pixels where the contrast or color gradient is higher than the threshold.  The hue of these pixels will match the direction of the local gradient (gx/gy).  Pixels with gradient below the threshold are colored black.
 *
 * The preserve saturation option will apply the original image's saturation at a pixel colored by the sobel operator.
 *
 * If the manhattan option is set the threshold with be compared to the l1 norm gx+gy instead of the default l2 norm sqrt(gx^2+gy^2).  This can be an optimization but the resulting image will contain more color pixel which may not be the best result for passing to a canny edge detector.
 *
 * This filter should be run on the output of a smoothing or noise reduction filter like a GaussianBlur.
 */
public class SobelFilter implements Filter {

    private final int WIDTH = 3;

    private final double threshHold;
    private final DoubleMatrix horzKernal;
    private final DoubleMatrix vertKernal;

    private final boolean preserveSaturation;
    private final boolean manhattan;

    public SobelFilter() {
        this(0.2, false, false);
    }

    public SobelFilter(double threshHold) {
        this(threshHold, false, false);

    }

    public SobelFilter(double threshHold, boolean preserveSaturation) {
        this(threshHold, preserveSaturation, false);

    }
    
    public SobelFilter(double threshHold, boolean preserveSaturation, boolean manhattan) {
        this.horzKernal = new DoubleMatrix(WIDTH, WIDTH);
        this.horzKernal.put(0,0, -1.0);
        this.horzKernal.put(0,1, -1);
        this.horzKernal.put(0,2, -1.0);
        this.horzKernal.put(2,0, 1.0);
        this.horzKernal.put(2,1,1);
        this.horzKernal.put(2,2,1.0);

        this.vertKernal = new DoubleMatrix(WIDTH,WIDTH);
        this.vertKernal.put(0,0, -1.0);
        this.vertKernal.put(1,0,-1);
        this.vertKernal.put(2,0, -1.0);
        this.vertKernal.put(0,2, 1.0);
        this.vertKernal.put(1,2,1);
        this.vertKernal.put(2,2,1.0);

        this.threshHold = threshHold;
        this.preserveSaturation = preserveSaturation;
        this.manhattan = manhattan;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {

        //make sure it's gray.
        BufferedImage grayImage = new GrayscaleFilter().apply(image);

        BufferedImage buffer = ImageHelper.AllocateARGBBuffer(image.getWidth(), image.getHeight());

        new Range(0, image.getWidth(), 0, image.getHeight()).get().forEach(c -> {

            float orientation;
            float horzSum;
            float vertSum;
            float gradient;

            //sum pass;
            horzSum = (float) ColorMatrixBuilder.getNeighborColorMatrix(grayImage, (color) -> color.getBlue(), WIDTH, c.x(), c.y()).mul(
                    horzKernal).sum();
            vertSum = (float) ColorMatrixBuilder.getNeighborColorMatrix(grayImage, (color) -> color.getBlue(), WIDTH, c.x(), c.y()).mul(
                    vertKernal).sum();

            orientation = (float) Math.atan( vertSum/horzSum );

            if (manhattan) {
                gradient = ColorHelper.GradientNormalize( vertSum + horzSum );
            }
            else {
                gradient =  ColorHelper.GradientNormalize((float) Math.sqrt(Math.pow(vertSum, 2) + Math.pow(horzSum, 2)));
            }

            //apply
            if ( gradient > this.threshHold ) {
                buffer.setRGB(c.x(), c.y(),
                    Color.HSBtoRGB( orientation,
                        (preserveSaturation) ? ColorHelper.Saturation(image.getRGB(c.x(), c.y())) : 1.0f,
                        Math.min(1.0f, gradient )
                    )
                );
            }
            else {
                buffer.setRGB(c.x(), c.y(),
                        ColorHelper.ColorValueFromRGBA(
                            0, 0, 0,
                            ColorHelper.ColorFromRGBValue( image.getRGB(c.x(), c.y()) ).getAlpha()
                        )
                );
            }
        });

        return buffer;
    }
}
