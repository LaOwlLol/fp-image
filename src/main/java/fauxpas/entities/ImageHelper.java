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

public class ImageHelper {
    public static BufferedImage AllocateARGBBuffer(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public static BufferedImage ARGBBufferRenderer(Stream<Pixel> sample, int width, int height) {
        BufferedImage buffer = ImageHelper.AllocateARGBBuffer(width, height);

        sample.filter(p -> p.x() < width && p.y() < height).forEach( p ->
            buffer.setRGB(p.x(), p.y(), p.getColor().getRGB())
        );

        return buffer;
    }

    public static Stream<Pixel> SampleImage(BufferedImage buffer) {
        return SampleImage(buffer, null);
    }

    public static Stream<Pixel> SampleImage(BufferedImage buffer, Selection selection) {
        return new Sample(selection).get(buffer);
    }
}
