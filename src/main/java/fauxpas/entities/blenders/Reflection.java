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
 * Reflected, where the resulting color is the component wise product of incoming light (color1) and surface properties (color2), with opacity property of the surface
 */
public class Reflection implements Blender {


    /**
     * Calculate color 2 reflected onto color 1.
     * @param color1 incoming light color.
     * @param color2 reflection surface properties (how much light to reflect for each color channel).
     * @return reflected light color with surface (color2) opacity.
     */
    @Override
    public Color calc(Color color1, Color color2) {
        return new Color(
            Math.min(1.0, color1.getRed() * color2.getRed()),
            Math.min(1.0, color1.getGreen() * color2.getGreen()),
            Math.min(1.0, color1.getBlue() * color2.getBlue()),
            color2.getOpacity()
        );
    }
}
