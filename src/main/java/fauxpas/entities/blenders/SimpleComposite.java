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

package fauxpas.entities.blenders;


import fauxpas.entities.Pixel;

/**
 * Composite colors with simple interpolation calculation.
 */
public class SimpleComposite implements Blender{

    private float bias;

    /**
     * Construct a composite blender with default bias of 0.5
     */
    public SimpleComposite() {
        this(0.5f);
    }


    public SimpleComposite(float bias) {
        this.bias = bias;
    }

    /**
     * Calculate color composite with mid point calculation.
     * @param p1 starting point
     * @param p2 end point
     * @return (p2 - p1)*this.bias + p1
     */
    @Override
    public Pixel calc(Pixel p1, Pixel p2) {
        return new Pixel (
            p1.getCoordinate(),
            Math.max(Math.min(1.0f, (p2.getRed() - p1.getRed())*this.bias + p1.getRed()), 0f),
            Math.max(Math.min(1.0f, (p2.getGreen() - p1.getGreen())*this.bias + p1.getGreen()), 0f),
            Math.max(Math.min(1.0f, (p2.getBlue() - p1.getBlue())*this.bias + p1.getBlue()), 0f),
            Math.max(Math.min(1.0f, (p2.getAlpha() - p1.getAlpha())*this.bias + p1.getAlpha()), 0f)
        );
    }
}
