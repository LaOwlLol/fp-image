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


import java.awt.Color;

/**
 * Sum of two colors, with optional scalars.
 */
public class ColorSum implements Blender {

    /**
     * Construct a new sum blender.
     */
    public ColorSum() {

    }

    /**
     * Calculate the sum of two colors.
     * @param color1 a color to add.
     * @param color2 another color to add.
     * @return a new color from source color components, including opacity, added.
     */
    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(
            Math.min(255, color1.getRed() + color2.getRed()),
            Math.min(255, color1.getGreen() +  color2.getGreen()),
            Math.min(255, color1.getBlue() + color2.getBlue()),
            Math.min(255, color1.getAlpha() + color2.getAlpha())
        );
    }
}
