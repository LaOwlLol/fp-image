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

package fauxpas.filters.noise;

import fauxpas.entities.Pixel;
import fauxpas.fastnoise.FastNoise;
import fauxpas.filters.Filter;

import java.util.Random;
import java.util.stream.Stream;

public class SimplexNoise implements Filter {

    private FastNoise fastNoise;
    private float frequencyX;
    private float frequencyY;

    public SimplexNoise() {
        this.fastNoise = new FastNoise(new Random(System.currentTimeMillis()).nextInt());
        this.fastNoise.SetNoiseType(FastNoise.NoiseType.Simplex);
        this.frequencyX = 1.0f;
        this.frequencyY = 1.0f;
    }

    public SimplexNoise(float frequency) {
        this();
        this.frequencyX = frequency;
        this.frequencyY = frequency;
    }

    public SimplexNoise(float frequencyX, float frequencyY) {
        this();
        this.frequencyX = frequencyX;
        this.frequencyY = frequencyY;
    }

    @Override
    public Stream<Pixel> apply(Stream<Pixel> sample) {
        return sample.map( p -> {
                float color = (this.fastNoise.GetNoise(this.frequencyX * p.x(), this.frequencyY * p.y())/2)+0.5f;
                return new Pixel(p.getCoordinate(), color, color, color, 1.0f);
            }
        );
    }
}
