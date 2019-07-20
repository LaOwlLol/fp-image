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
import fauxpas.entities.Pixel;
import fauxpas.entities.blenders.SimpleComposite;

import java.awt.image.BufferedImage;
import java.util.stream.Stream;

/**
 * Utility for composting two images with the SimpleComposite Blender.
 *
 * A composite can have a bias where 0.0 is fully the the first image, 1.0 is fully the second image, or by default pixels are interpolated half way between the two images.
 */
public class CompositeFilter implements Mixer{

    SimpleComposite blender;

    public CompositeFilter() {
        this(0.5f);
    }

    public CompositeFilter(float bias) {
         blender = new SimpleComposite(bias);
    }

    @Override
    public Stream<Pixel> apply(Stream<Pixel> f, BufferedImage s) {
        return f.filter( p -> p.x() < s.getWidth() && p.y() < s.getHeight()).map( p1 -> blender.calc(
            p1,
            new Pixel( p1.getCoordinate(), ColorHelper.ColorFromColorValue( s.getRGB(p1.x(),p1.y()) ))
        ));
    }
}
