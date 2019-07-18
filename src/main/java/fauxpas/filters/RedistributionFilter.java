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
import fauxpas.entities.ImageHelper;
import fauxpas.entities.Sample;

import java.awt.image.BufferedImage;

/**
 * A utility filter for manipulating the contrast in an image.
 */
public class RedistributionFilter implements Filter {

    private final double pow;

    public RedistributionFilter() {
        this(2.0);
    }

    /**
     *
     * @param power values should be very close to 1.0. power greater than 1.0 will washout decrease contrast. power less than 1.0 will darken the image.
      */
    public RedistributionFilter(double power) {
        this.pow = power;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {

        BufferedImage buffer = ImageHelper.AllocateARGBBuffer(image.getWidth(), image.getHeight());

        new Sample().get(image).forEach(p -> buffer.setRGB(p.x(), p.y(),
            ColorHelper.ColorValueFromRGBA(
                Math.min(255, (int) Math.pow( p.getRed(), this.pow )),
                Math.min(255, (int) Math.pow( p.getGreen(), this.pow )),
                Math.min(255, (int) Math.pow( p.getBlue(), this.pow )),
                p.getOpacity()
            )
        ));

        return buffer;
    }
}
