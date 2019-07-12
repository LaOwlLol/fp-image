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

import fauxpas.entities.Range;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.jblas.DoubleMatrix;

/**
 * A utility for reducing noise in images, by averaging each pixel in an image with it's neighbors.
 */
public class GaussianBlur implements Filter {

    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;
    private double kernelValue;
    private double standardDeviation;
    private DoubleMatrix kernel;
    private int width;
    private final int mid;

    public GaussianBlur() {
        this(3, 1.0);
    }

    /**
     * Construct gaussian blur filter with custom convolution kernel.
     * @param width dimension of the convolution kernal.  Should be an odd number. Larger kernel means slower computation.
     * @param standardDeviation values used to intialize the convolution color. Higher values should mean more smoothing, no computation cost.
     */
    public GaussianBlur(int width, double standardDeviation) {

        //todo all good if no true center to kernel?
        if (width % 2 == 0) {
            System.err.println("GaussianBlur width requested is not an odd value. adding 1 to normalize things.");
            width = width+1;
        }

        this.standardDeviation = standardDeviation;
        this.width = width;
        this.kernel = new DoubleMatrix(this.width, this.width);

        this.mid = width/2;

        //pre calculate parts of gaussian equation that don't contain x,y
        double expDenom = Math.PI * Math.pow(this.standardDeviation,2);
        double outerDenom = 2.0 * expDenom;

        //initialize kernel
        this.kernelValue = 0.0;

        new Range(0, 3, 0, 3).get().forEach( c -> {
            int i = c.x() - mid;
            int j = c.y() - mid;
            double expNumer = Math.pow(i, 2) + Math.pow(j, 2);
            double kvalue = (1.0/outerDenom) * Math.exp(expNumer/expDenom);
            this.kernel.put(c.y(), c.x(),  kvalue);
            this.kernelValue += kvalue;
        } );
    }

    @Override
    public Image apply(Image target) {

        PixelReader targetReader = target.getPixelReader();
        WritableImage buffer = new WritableImage((int) target.getWidth(), (int) target.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        new Range(0, (int) target.getWidth(), 0, (int) target.getHeight() ).get().forEach( c -> {
            bufferWriter.setColor(c.x(), c.y(), new Color(
                    ColorMatrixBuilder.getNeighborColorMatrix(target, Color::getRed, this.width, c.x(), c.y()).mul(this.kernel).sum()/this.kernelValue,
                    ColorMatrixBuilder.getNeighborColorMatrix(target, Color::getGreen, this.width, c.x(), c.y()).mul(this.kernel).sum()/this.kernelValue,
                    ColorMatrixBuilder.getNeighborColorMatrix(target, Color::getBlue, this.width, c.x(), c.y()).mul(this.kernel).sum()/this.kernelValue,
                    targetReader.getColor(c.x(), c.y()).getOpacity() ) );
        }) ;

        return buffer;
    }

}
