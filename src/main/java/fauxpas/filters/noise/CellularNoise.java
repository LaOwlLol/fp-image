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

import fauxpas.entities.ColorHelper;
import fauxpas.entities.ImageHelper;
import fauxpas.entities.Range;
import fauxpas.fastnoise.FastNoise;
import fauxpas.filters.Filter;

import java.awt.image.BufferedImage;
import java.util.Random;

public class CellularNoise implements Filter {

    private FastNoise fastNoise;
    private float frequencyX;
    private float frequencyY;

    public CellularNoise() {
        this.fastNoise = new FastNoise(new Random(System.currentTimeMillis()).nextInt());
        this.fastNoise.SetNoiseType(FastNoise.NoiseType.Cellular);
        this.frequencyX = 1.0f;
        this.frequencyY = 1.0f;
    }

    public CellularNoise(float frequency) {
        this();
        this.frequencyX = frequency;
        this.frequencyY = frequency;
    }

    public CellularNoise(float frequencyX, float frequencyY) {
        this();
        this.frequencyX = frequencyX;
        this.frequencyY = frequencyY;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {

        BufferedImage buffer = ImageHelper.AllocateARGBBuffer(image.getWidth(), image.getHeight());

        new Range(0, image.getWidth(), 0, image.getHeight()).get().forEach( c -> {
                int color =  ColorHelper.FloatChannelToInt((this.fastNoise.GetNoise(this.frequencyX * c.x(), this.frequencyY * c.y())/2)+0.5f);
                buffer.setRGB(c.x(), c.y(), ColorHelper.ColorValueFromRGBA(color, color, color, 255));
            }
        );

        return buffer;
    }

}
