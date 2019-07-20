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

package fauxpas.entities;

import java.awt.image.BufferedImage;
import java.util.stream.Stream;

/**
 * A selection utility for producing a stream of pixels from an image.  By default a pixel stream produced will span the width and height of the source image.  A custom selection group may be passed to the constructor for sampling a subset of pixels in a source image.
 */
public class Sample {

    private Selection area;

    public Sample() {
        area = null;
    }

    /**
     * Construct a custom selection sample.
     * @param selection the selection to be used when producing a stream of pixels.
     */
    public Sample(Selection selection) {
        this.area = selection;
    }

    /**
     * Get a stream of pixels from this sample's selection,  If this selection is unset the full image is returned as a stream of pixels.
     *
     * @param source Image to read pixels from.
     * @return Stream containing pixel from source image in the area (Range) of this Sample.
     */
    public Stream<Pixel> get(BufferedImage source) {
        if (this.area == null) {
            this.area = new Range(0, source.getWidth(), 0, source.getHeight());
        }

        return this.area.get().filter( c -> (c.x() < source.getWidth() && c.y() < source.getHeight()) ).map(
            c -> new Pixel( c,  ColorHelper.ColorFromColorValue( source.getRGB(c.x(), c.y()) ) )
        );
    }

}
