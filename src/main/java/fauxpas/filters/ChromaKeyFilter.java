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
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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
    private PixelReader source_image;
    private double threshold;

    public ChromaKeyFilter() {
        this(Color.GREEN, null, null, 0.1);
    }

    public ChromaKeyFilter(Color key_color, double threshold) {
        this(key_color, null, null, threshold);
    }

    public ChromaKeyFilter(Color key_color, Color source_color, double threshold) {
        this(key_color, source_color, null, threshold);
    }

    public ChromaKeyFilter(Color key_color, Image source_image, double threshold) {
        this(key_color, null, source_image, threshold);
    }

    private ChromaKeyFilter(Color key_color, Color source_color, Image source_image, double threshold) {
        this.key_color = key_color;
        this.source_color = source_color;
        this.source_image = source_image.getPixelReader();
        this.threshold = threshold;
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        new Sample().get(image).forEach( p -> {
            double delta = Math.min(1.0, (Math.pow(key_color.getRed() - p.getRed(), 2) +
                    Math.pow(key_color.getGreen() - p.getGreen(), 2) +
                    Math.pow(key_color.getBlue() - p.getBlue(), 2) ));

            if (delta < threshold) {
                if ( source_image != null ) {
                    bufferWriter.setColor(p.x(), p.y(), source_image.getColor(p.x(), p.y()));
                }
                else if (source_color != null) {
                    bufferWriter.setColor(p.x(), p.y(), source_color);
                }
                else {
                    bufferWriter.setColor(p.x(), p.y(), new Color(p.getRed(), p.getGreen(), p.getBlue(), 0.0));
                }
            }
            else {
                bufferWriter.setColor(p.x(), p.y(), p.getColor());
            }
        });

        return buffer;
    }
}
