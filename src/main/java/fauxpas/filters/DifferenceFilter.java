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

import fauxpas.entities.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;


/**
 * A filter for producing an image from the difference between two images.
 *
 * Each pixel of the resulting image is colored depending on the  similarity of corresponding pixels in the first and second images.
 *
 * If the two source pixels are with in the threshold of each other, they are colored 'equal'.  Otherwise they are colored 'diff'
 */
public class DifferenceFilter implements Mixer {
    private final float threshold;
    private Color equal;
    private Color diff;
    private boolean manhattan;
    private boolean appliedToEqual;


    /**
     * Default Difference filter with green for similar pixels (within 0.05) and red for different pixels.
     */
    public DifferenceFilter() {
        this(Color.GREEN, Color.RED, 0.05f, false);
    }

    /**
     * Make new Difference filter with custom threshold and normal type.
     * @param threshold  threshold for equality.
     * @param manhattan use manhattan distance (false is euclidean)
     */
    public DifferenceFilter(float threshold, boolean manhattan) {
        this(Color.GREEN, Color.RED, threshold, manhattan);
    }

    /**
     * Make new Difference filter with custom colors.
     * @param equal pixel color for equal.
     * @param diff pixel color for different.
     * @param threshold  threshold for equality.
     * @param manhattan use manhattan distance (false is euclidean)
     */
    public DifferenceFilter(Color equal, Color diff, float threshold, boolean manhattan) {
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
    public Stream<Pixel> apply(Stream<Pixel> f, BufferedImage s) {

        this.appliedToEqual = true;

        return f.filter( p ->  p.x() < s.getWidth() && p.y() < s.getHeight() ).map(p -> {
            float delta = ColorHelper.EuclidianDistance(p.getColor()
                , ColorHelper.ColorFromColorValue(s.getRGB(p.x(), p.y())),
                manhattan);

            if ( delta < threshold) {
                return new Pixel(p.getCoordinate(), equal);
            } else {
                this.appliedToEqual = false;
                return new Pixel(p.getCoordinate(), diff);
            }
        });
    }
}
