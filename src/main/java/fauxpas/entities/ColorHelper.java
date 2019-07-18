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

public class ColorHelper {

    public static int ColorValueFromRGBA(int r, int g, int b, int a ) {
        return new Color(r, g, b, a).getRGB();
    }
    public static Color ColorFromRGBValue(int color){ return new Color(color); }

    public static int Luminance(Color color, double redBalance, double greenBalance, double blueBalance) {
        return Math.min(255,(int) ColorMatrixBuilder.getColorColumnVector(color).dot(
            ColorMatrixBuilder.getColorColumnVector(redBalance, greenBalance, blueBalance) ) );
    }

    public static int Luminance(Color color) {
        return Luminance(color, 0.3, 0.59, 0.11);
    }

    public static float Brightness(Color color) {
        float[] r = new float[3];
        r = Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), r );
        return r[2];
    }

    public static float Brightness(int color) {
        return Brightness( ColorFromRGBValue(color) );
    }

    public static float Saturation(Color color) {
        float[] r = new float[3];
        r = Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), r );
        return r[1];
    }

    public static float Saturation(int color) {
        return Saturation( ColorFromRGBValue(color) );
    }

    public static float Hue(Color color) {
        float[] r = new float[3];
        r = Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), r );
        return r[0];
    }

    public static float Hue(int color) {
        return Hue( ColorFromRGBValue(color) );
    }

    public static int Alpha(int color) {
        return ColorFromRGBValue(color).getAlpha();
    }

    public static int FloatChannelToInt(float f) {
        return (int) (f * 255);
    }
}
