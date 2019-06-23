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

import javafx.scene.paint.Color;

/**
 * Composite colors with simple interpolation calculation.
 */
public class SimpleComposite implements Blender{

    private double bias;

    /**
     * Construct a composite blender with default bias of 0.5
     */
    public SimpleComposite() {
        this(0.5);
    }


    public SimpleComposite(double bias) {
        this.bias = bias;
    }

    /**
     * Calculate color composite with mid point calculation.
     * @param color1 starting point
     * @param color2 end point
     * @return color1.interpolate(color2, this.bias)
     */
    @Override
    public Color calc(Color color1, Color color2) {
        return color1.interpolate(color2, this.bias);
    }
}
