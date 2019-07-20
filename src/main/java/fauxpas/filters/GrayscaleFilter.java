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

import fauxpas.entities.ColorHelper;
import fauxpas.entities.Pixel;
import java.util.stream.Stream;

/**
 * Filter colors to gray.
 *
 * By default the color balance is 30% red, 59% green, and 11% blue, to approximate human or typical lighting. A constructor for adjusting these values is available.
 */
public class GrayscaleFilter implements Filter {

    private float redBalance;
    private float greenBalance;
    private float blueBalance;

    public GrayscaleFilter() {
        this(0.3f, 0.59f, 0.11f);
    }

    /**
     * Create custom color balance for gray-scaling.
     * @param redBalance red bias
     * @param greenBalance green bias
     * @param blueBalance blue bias
     */
    public GrayscaleFilter(float redBalance, float greenBalance, float blueBalance) {
        this.redBalance = redBalance;
        this.greenBalance = greenBalance;
        this.blueBalance = blueBalance;
    }

    @Override
    public Stream<Pixel> apply(Stream<Pixel> sample) {
        return sample.map( p-> {
            float gray = ColorHelper.Luminance( p.getColor(), this.redBalance, this.greenBalance, this.blueBalance );

            return new Pixel(p.getCoordinate(), gray, gray, gray, p.getAlpha() );
        });
    }

}
