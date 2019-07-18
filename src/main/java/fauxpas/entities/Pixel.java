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


import java.awt.Color;

/**
 * A container for color, coordinate, and other properties of a single dot of an image.
 */
public class Pixel {

    private Color color;
    private Coordinate coordinate;

    /**
     * Construct a pixel from a coordinate and color
     * @param coordinate location of pixel
     * @param color pixel display values
     */
    public Pixel(Coordinate coordinate, int color) {
        this(coordinate, new Color( color ));
    }


    /**
     * Construct a pixel from a coordinate and color
     * @param coordinate location of pixel
     * @param color pixel display values
     */
    public Pixel(Coordinate coordinate, Color color) {
        this.coordinate = coordinate;
        this.color = color;
    }

    public int x() {
        return this.coordinate.x();
    }
    public int y() {
        return this.coordinate.y();
    }

    public int getRed() {
        return color.getRed();
    }

    public int getGreen() {
        return color.getGreen();
    }

    public int getBlue() {
        return color.getBlue();
    }

    public int getOpacity() {
        return color.getAlpha();
    }

    public Color getColor() { return color; }

}
