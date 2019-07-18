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
import fauxpas.entities.ColorMatrixBuilder;
import fauxpas.entities.ImageHelper;
import fauxpas.entities.Sample;

import java.awt.Color;
import java.awt.image.BufferedImage;


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
    private boolean appliedToEqual;


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
        this.appliedToEqual = false;
    }

    public boolean appliedToEqual() {
        return appliedToEqual;
    }

    @Override
    public BufferedImage apply(BufferedImage f, BufferedImage s) {
        BufferedImage buffer = ImageHelper.AllocateARGBBuffer(f.getWidth(), f.getHeight());

        this.appliedToEqual = true;

        new Sample().get(f).filter( p ->  p.x() < s.getWidth() && p.y() < s.getHeight() ).forEach(p -> {
            double delta;
            if (!manhattan) {
                delta = ColorMatrixBuilder.getColorColumnVector( p.getColor() )
                    .subi(
                        ColorMatrixBuilder.getColorColumnVector(
                            ColorHelper.ColorFromRGBValue(s.getRGB(p.x(), p.y()))
                        )
                    )
                    .norm2();
            }
            else {
                delta = ColorMatrixBuilder.getColorColumnVector( p.getColor() )
                    .subi(
                        ColorMatrixBuilder.getColorColumnVector(
                            ColorHelper.ColorFromRGBValue( s.getRGB(p.x(), p.y()) )
                        )
                    )
                    .norm1();
            }

            if ( delta < threshold) {
                buffer.setRGB(p.x(), p.y(), equal.getRGB());
            } else {
                buffer.setRGB(p.x(), p.y(), diff.getRGB());
                this.appliedToEqual = false;
            }
        });

        return buffer;
    }
}
