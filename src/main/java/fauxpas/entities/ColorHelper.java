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
    public static Color ColorFromColorValue(int color){ return new Color(color); }
    public static int ColorValueFromColor(Color color) { return color.getRGB(); }
    public static Color ColorFromRGBA(int r, int g, int b, int a) {
        return ColorFromColorValue( ColorValueFromRGBA(r, g, b, a) );
    }

    public static float Luminance(Color color, float redBalance, float greenBalance, float blueBalance) {
        return Math.min(1.0f ,ColorMatrixBuilder.getColorColumnVector(color).dot(
            ColorMatrixBuilder.getColorColumnVector(redBalance, greenBalance, blueBalance) ) );
    }

    public static float Luminance(Color color) {
        return Luminance(color, 0.3f, 0.59f, 0.11f);
    }

    public static float Brightness(Color color) {
        float[] r = new float[3];
        r = Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), r );
        return r[2];
    }

    public static float Brightness(int color) {
        return Brightness( ColorFromColorValue(color) );
    }

    public static float Saturation(Color color) {
        float[] r = new float[3];
        r = Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), r );
        return r[1];
    }

    public static float Saturation(int color) {
        return Saturation( ColorFromColorValue(color) );
    }

    public static float Hue(Color color) {
        float[] r = new float[3];
        r = Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), r );
        return r[0];
    }

    public static float Hue(int color) {
        return Hue( ColorFromColorValue(color) );
    }

    public static float Alpha(int color) {
        return  IntChannelToFloat( ColorFromColorValue(color).getAlpha() );
    }

    public static float EuclidianDistance(Color a, Color b, boolean manhattan) {
        /*return (float) ( Math.pow( IntChannelToFloat( a.getRed() - b.getRed() ), 2) +
            Math.pow( IntChannelToFloat( a.getGreen() - b.getGreen() ), 2) +
            Math.pow( IntChannelToFloat( a.getBlue() - b.getBlue() ), 2) );*/
        if (manhattan) {
            return ColorMatrixBuilder.getColorColumnVector(a)
                .subi(ColorMatrixBuilder.getColorColumnVector(b))
                .norm1();
        }
        else {
            return ColorMatrixBuilder.getColorColumnVector(a)
                .subi(ColorMatrixBuilder.getColorColumnVector(b))
                .norm2();
        }
    }

    public static float IntChannelToFloat(int i) {
        return i/255.0f;
    }

    public static int FloatChannelToInt(float f) {
        return (int) (f * 255);
    }

    public static float GradientNormalize(float v) {
        return ((1.0f / (1.0f + (float) Math.exp(-v))) * 2.0f) - 1.0f;
    }

}
