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

package fauxpas.filters.noise;

import fauxpas.entities.ColorHelper;
import fauxpas.entities.ImageHelper;
import fauxpas.entities.Range;
import fauxpas.filters.Filter;

import java.awt.image.BufferedImage;
import java.util.Random;

public class WhiteNoise implements Filter {

    private Random random;

    public WhiteNoise() {
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public BufferedImage apply(BufferedImage image) {


        BufferedImage buffer = ImageHelper.AllocateARGBBuffer(image.getWidth(), image.getHeight());

        new Range(0, image.getWidth(), 0, image.getHeight()).get().forEach(c -> {
                int color =  ColorHelper.FloatChannelToInt((float)(Math.sin(this.random.nextGaussian())/2) +0.5f);
                buffer.setRGB(c.x(), c.y(), ColorHelper.ColorValueFromRGBA(color, color, color, 255));
            }
        );

        return buffer;
    }
}
