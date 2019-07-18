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

import org.jblas.DoubleMatrix;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorMatrixBuilder {

    public static DoubleMatrix getNeighborColorMatrix(BufferedImage target, ColorReader colorReader, int kernelWidth, int imageX, int imageY ) {
        DoubleMatrix colors = new DoubleMatrix(kernelWidth, kernelWidth);

        int midPoint = kernelWidth/2;

        for (int kernelY = 0; kernelY < kernelWidth; ++kernelY ) {
            for (int kernelX = 0; kernelX < kernelWidth; ++kernelX) {

                int i = kernelX - midPoint;
                int j = kernelY - midPoint;

                if ( ((imageX+i > 0) && (imageX+i < target.getWidth())) &&
                    ((imageY+j > 0) && (imageY+j < target.getHeight())) ) {

                    colors.put(kernelY, kernelX, colorReader.getColorProperty(
                        ColorHelper.ColorFromRGBValue(target.getRGB(imageX+i, imageY+j))
                    ));
                }
                else {
                    colors.put(kernelY, kernelX, colorReader.getColorProperty(
                        ColorHelper.ColorFromRGBValue(target.getRGB(imageX, imageY))
                    ));
                }
            }
        }
        return colors;
    }

    public static DoubleMatrix getNeighborColorColumnVector(BufferedImage target, ColorReader colorReader, int vectorHeight, int imageX, int imageY ) {
        DoubleMatrix colors = new DoubleMatrix(vectorHeight, 1);
        int midPoint = vectorHeight/2;


        for (int y = 0; y < vectorHeight; ++y) {

            int i = y - midPoint;

            if ((imageY+i > 0) && (imageY+i < target.getHeight())) {
                colors.put(y, 0, colorReader.getColorProperty(
                    ColorHelper.ColorFromRGBValue(target.getRGB(imageX,imageY+i))
                ));
            }
            else {
                colors.put(y, 0, colorReader.getColorProperty(
                    ColorHelper.ColorFromRGBValue(target.getRGB(imageX, imageY))
                ));
            }
        }

        return colors;
    }

    public static DoubleMatrix getColorColumnVector(Color color) {
        DoubleMatrix vector = new DoubleMatrix(3, 1);
        vector.put(0,0, color.getRed() );
        vector.put(1, 0, color.getGreen() );
        vector.put( 2, 0, color.getBlue() );
        return vector;
    }

    public static DoubleMatrix getColorRowVector(Color color) {
        DoubleMatrix vector = new DoubleMatrix(1, 3);
        vector.put(0,0, color.getRed() );
        vector.put(0, 1, color.getGreen() );
        vector.put( 0, 2, color.getBlue() );
        return vector;
    }

    public static DoubleMatrix getColorColumnVector(double redBalance, double greenBalance, double blueBalance) {
        DoubleMatrix vector = new DoubleMatrix(1, 3);
        vector.put(0,0, redBalance );
        vector.put(0, 1, greenBalance );
        vector.put( 0, 2, blueBalance );
        return vector;
    }
}
