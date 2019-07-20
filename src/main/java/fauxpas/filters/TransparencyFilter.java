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

import fauxpas.entities.Pixel;
import java.util.stream.Stream;

public class TransparencyFilter implements Filter {

    private float alpha;

    public TransparencyFilter() {
        this(0.5f);
    }

    public TransparencyFilter(float alpha) {
        this.alpha = Math.max(0f, Math.min(alpha, 1.0f));
    }

    @Override
    public Stream<Pixel> apply(Stream<Pixel> sample) {
        return sample.map(p -> new Pixel(p.getCoordinate(), p.getRed(), p.getGreen(), p.getBlue(), this.alpha));
    }
}
