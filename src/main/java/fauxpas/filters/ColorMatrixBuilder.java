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

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import org.jblas.DoubleMatrix;

public class ColorMatrixBuilder {

    public static DoubleMatrix getColorMatrix(Image target, ColorReader colorReader, int kernelWidth, int imageX, int imageY ) {
        DoubleMatrix colors = new DoubleMatrix(kernelWidth, kernelWidth);
        PixelReader targetReader = target.getPixelReader();
        int midPoint = kernelWidth/2;

        for (int kernelY = 0; kernelY < kernelWidth; ++kernelY ) {
            for (int kernelX = 0; kernelX < kernelWidth; ++kernelX) {

                int i = kernelX - midPoint;
                int j = kernelY - midPoint;

                if ( ((imageX+i > 0) && (imageX+i < target.getWidth())) && ((imageY+j > 0) && (imageY+j < target.getHeight())) ) {
                    colors.put(kernelY, kernelX, colorReader.getColorProperty(targetReader.getColor(imageX+i, imageY+j)) );
                }
                else {
                    colors.put(kernelY, kernelX, colorReader.getColorProperty(targetReader.getColor(imageX, imageY)));
                }
            }
        }
        return colors;
    }

    public static DoubleMatrix getColorColumnVector(Image target, ColorReader colorReader, int vectorHeight, int imageX, int imageY ) {
        DoubleMatrix colors = new DoubleMatrix(vectorHeight, 1);
        PixelReader targetReader = target.getPixelReader();
        int midPoint = vectorHeight/2;


        for (int y = 0; y < vectorHeight; ++y) {

            int i = y - midPoint;

            if ((imageY+i > 0) && (imageY+i < target.getHeight())) {
                colors.put(y, 0, colorReader.getColorProperty(targetReader.getColor(imageX, imageY+i)) );
            }
            else {
                colors.put(y, 0, colorReader.getColorProperty(targetReader.getColor(imageX, imageY)));
            }
        }

        return colors;
    }
}
