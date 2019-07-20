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


    private Coordinate coordinate;
    private float red;
    private float green;
    private float blue;
    private float alpha;


    public Pixel(Coordinate coordinate, Color color) {
        this(coordinate, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Construct a pixel from a coordinate and color
     * @param coordinate location of pixel
     * @param red pixel display color channel value
     * @param green pixel display color channel value
     * @param blue pixel display color channel value
     */
    public Pixel(Coordinate coordinate, int red, int green, int blue, int alpha) {
        this(coordinate,
            ColorHelper.IntChannelToFloat( red ),
            ColorHelper.IntChannelToFloat( green ),
            ColorHelper.IntChannelToFloat( blue ),
            ColorHelper.IntChannelToFloat( alpha )
        );
    }

    /**
     * Construct a pixel from a coordinate and color
     * @param coordinate location of pixel
     * @param red pixel display color channel value
     * @param green pixel display color channel value
     * @param blue pixel display color channel value
     */
    public Pixel(Coordinate coordinate, float red, float green, float blue, float alpha) {
        this.coordinate = coordinate;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public int x() {
        return this.coordinate.x();
    }
    public int y() {
        return this.coordinate.y();
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Color getColor() {
        return ColorHelper.ColorFromRGBA(
            ColorHelper.FloatChannelToInt(red),
            ColorHelper.FloatChannelToInt(green),
            ColorHelper.FloatChannelToInt(blue),
            ColorHelper.FloatChannelToInt(alpha)
        );
    }

}
