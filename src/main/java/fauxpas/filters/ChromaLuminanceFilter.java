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

import fauxpas.entities.Pixel;

import java.util.stream.Stream;

/**
 * A filter for transforming the luminance of a pixels to transparency.  Meaning pixels close to white (or the gray balance provided by a GrayscaleFilter), become transparent
 *
 * By default luminance is calculated with a gray scale filter color balance, but you can provide a custom color balance gray scale filter.
 */
public class ChromaLuminanceFilter implements Filter {

    GrayscaleFilter grayFilter;

    public ChromaLuminanceFilter() {
        this(new GrayscaleFilter());
    }

    public ChromaLuminanceFilter(GrayscaleFilter grayscaleFilter) {
        this.grayFilter = grayscaleFilter;
    }

    @Override
    public Stream<Pixel> apply(Stream<Pixel> sample) {

        return grayFilter.apply(sample).map( p-> new Pixel(
            p.getCoordinate(),
            p.getRed(),
            p.getGreen(),
            p.getBlue(),
            Math.min( p.getAlpha(), 1.0f-p.getRed() )
        ));
    }

}
