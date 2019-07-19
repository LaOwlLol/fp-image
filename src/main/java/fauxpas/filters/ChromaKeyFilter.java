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

import java.awt.Color;
import java.awt.image.BufferedImage;


/**
 * Filter the pixels that are within the threshold of the key color.
 *
 * Filtered pixels may be replaced by a source color or image, and if no source is provided filtered pixels have their opacity set to 0.0.
 *
 * If both the source image and source color are set, the source image will take precedence.
 */
public class ChromaKeyFilter implements Filter {

    private Color key_color;
    private Color source_color;
    private BufferedImage source_image;
    private float threshold;

    public ChromaKeyFilter() {
        this(Color.GREEN, null, null, 0.1f);
    }

    public ChromaKeyFilter(Color key_color, float threshold) {
        this(key_color, null, null, threshold);
    }

    public ChromaKeyFilter(Color key_color, Color source_color, float threshold) {
        this(key_color, source_color, null, threshold);
    }

    public ChromaKeyFilter(Color key_color, BufferedImage source_image, float threshold) {
        this(key_color, null, source_image, threshold);
    }

    private ChromaKeyFilter(Color key_color, Color source_color, BufferedImage source_image, float threshold) {
        this.key_color = key_color;
        this.source_color = source_color;
        this.source_image = ((source_image != null) ? source_image : null);
        this.threshold = threshold;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {

        BufferedImage buffer = ImageHelper.AllocateARGBBuffer(image.getWidth(), image.getHeight());

        new Sample().get(image).forEach( p -> {
            float delta = ColorHelper.EuclidianDistance(key_color, p.getColor());

            if (delta < threshold) {
                if ( source_image != null ) {
                    buffer.setRGB(p.x(), p.y(), source_image.getRGB(p.x(), p.y()));
                }
                else if (source_color != null) {
                    buffer.setRGB(p.x(), p.y(), source_color.getRGB());
                }
                else {
                    buffer.setRGB(p.x(), p.y(), ColorHelper.ColorValueFromRGBA(p.getRed(), p.getGreen(), p.getBlue(), 0));
                }
            }
            else {
                buffer.setRGB(p.x(), p.y(), p.getColor().getRGB());
            }
        });

        return buffer;
    }
}
