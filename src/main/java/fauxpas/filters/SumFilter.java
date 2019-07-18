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
import fauxpas.entities.blenders.ColorSum;
import fauxpas.entities.Sample;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * A utility filter for adding two images by summing their pixel colors channels.
 */
public class SumFilter implements Mixer {

    private ColorSum sum;

    public SumFilter() {
        sum = new ColorSum();
    }

    @Override
    public BufferedImage apply(BufferedImage f, BufferedImage s) {

        BufferedImage buffer = ImageHelper.AllocateARGBBuffer(f.getWidth(), f.getHeight());

        new Sample().get(f).filter( p ->  p.x() < s.getWidth() && p.y() < s.getHeight() ).forEach( p1 -> {
            Color p2 = ColorHelper.ColorFromRGBValue( s.getRGB(p1.x(), p1.y()) );

            buffer.setRGB(p1.x(), p1.y(), sum.calc(p1.getColor(), p2).getRGB());
        });

        return buffer;
    }
}
