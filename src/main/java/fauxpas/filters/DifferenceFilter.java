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
 * A filter for producing an image from the difference between two images.
 *
 * Each pixel of the resulting image is colored depending on the  similarity of corresponding pixels in the first and second images.
 *
 * If the two source pixels are with in the threshold of each other, they are colored 'equal'.  Otherwise they are colored 'diff'
 */
public class DifferenceFilter implements Mixer {
    private final double threshold;
    private Color equal;
    private Color diff;
    private boolean manhattan;


    /**
     * Default Difference filter with green for similar pixels (within 0.05) and red for different pixels.
     */
    public DifferenceFilter() {
        this(Color.GREEN, Color.RED, 0.05, false);
    }

    /**
     * Make new Difference filter with custom threshold and normal type.
     * @param threshold  threshold for equality.
     * @param manhattan use manhattan distance (false is euclidean)
     */
    public DifferenceFilter(double threshold, boolean manhattan) {
        this(Color.GREEN, Color.RED, threshold, manhattan);
    }

    /**
     * Make new Difference filter with custom colors.
     * @param equal pixel color for equal.
     * @param diff pixel color for different.
     * @param threshold  threshold for equality.
     * @param manhattan use manhattan distance (false is euclidean)
     */
    public DifferenceFilter(Color equal, Color diff, double threshold, boolean manhattan) {
        this.equal = equal;
        this.diff = diff;
        this.threshold = threshold;
        this.manhattan = manhattan;
    }

    @Override
    public Image apply(Image f, Image s) {
        WritableImage buffer = new WritableImage((int)f.getWidth(), (int)f.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();
        PixelReader sr = s.getPixelReader();

        new Sample().get(f).forEach(p -> {
            double delta;
            if (!manhattan) {
                delta = ColorMatrixBuilder.getColorColumnVector(p.getColor())
                    .subi(ColorMatrixBuilder.getColorColumnVector(sr.getColor(p.x(), p.y())))
                    .norm2();
            }
            else {
                delta = ColorMatrixBuilder.getColorColumnVector(p.getColor())
                    .subi(ColorMatrixBuilder.getColorColumnVector(sr.getColor(p.x(), p.y())))
                    .norm1();
            }

            if ( delta < threshold) {
                bufferWriter.setColor(p.x(), p.y(), equal);
            } else {
                bufferWriter.setColor(p.x(), p.y(), diff);
            }
        });

        return buffer;
    }
}
