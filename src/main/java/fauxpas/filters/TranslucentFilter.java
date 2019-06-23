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

import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * A filter for transforming the luminance of a pixels to transparency.  Meaning pixels close to white, become transparent
 *
 * By default luminance is calculated with a gray scale filter color balance, but you can provide a custom color balance gray scale filter.
 */
public class TranslucentFilter implements Filter {

    GrayscaleFilter grayFilter;

    public TranslucentFilter() {
        this(new GrayscaleFilter());
    }

    public TranslucentFilter(GrayscaleFilter grayscaleFilter) {
        this.grayFilter = grayscaleFilter;
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        new Sample().get(grayFilter.apply(image)).forEach( p-> {

            bufferWriter.setColor(p.x(), p.y(), new Color(
                    p.getRed(),
                    p.getGreen(),
                    p.getBlue(),
                    Math.min( p.getOpacity(), 1.0-p.getRed() ) ));
        });

        return buffer;
    }

}
