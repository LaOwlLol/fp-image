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

import fauxpas.entities.Sample;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Filter colors to gray.
 *
 * By default the color balance is 30% red, 59% green, and 11% blue, to approximate human or typical lighting. A constructor for adjusting these values is available.
 */
public class GrayscaleFilter implements Filter {

    private double redBalance;
    private double greenBalance;
    private double blueBalance;

    public GrayscaleFilter() {
        this(0.3, 0.59, 0.11);
    }

    /**
     * Create custom color balance for gray-scaling.
     * @param redBalance red bias
     * @param greenBalance green bias
     * @param blueBalance blue bias
     */
    public GrayscaleFilter(double redBalance, double greenBalance, double blueBalance) {
        this.redBalance = redBalance;
        this.greenBalance = greenBalance;
        this.blueBalance = blueBalance;
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        new Sample().get(image).forEach( p-> {
            double gray = Math.min(1.0, (redBalance*p.getRed()) +
                    (greenBalance * p.getGreen()) +
                    (blueBalance * p.getBlue()));

            bufferWriter.setColor(p.x(), p.y(), new Color(gray, gray, gray, p.getOpacity()));
        });

        return buffer;
    }

}
