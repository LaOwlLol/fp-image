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

import fauxpas.filters.Filter;
import fauxpas.filters.noise.WhiteNoise;

import java.awt.image.BufferedImage;


/**
 * A wrapper for javafx.scene.image.Image to apply fauxpas.filters to.
 */
public class FilterableImage {

    private BufferedImage maintainedImage;

    /**
     * Construct with dimensions.
     * @param width horizontal dimension
     * @param height vertical dimension
     */
    public FilterableImage(int width, int height) {
        this.maintainedImage = ImageHelper.AllocateARGBBuffer(width, height);
        this.applyFilter(new WhiteNoise());
    }

    /**
     * Construct by wrapping an existing image.
     * @param maintainedImage The image to wrap.
     */
    public FilterableImage(BufferedImage maintainedImage) {
        this.maintainedImage = maintainedImage;
    }

    /**
     * Access image
     * @return The image wrapped.
     */
    public BufferedImage getImage() {
        return maintainedImage;
    }

    /**
     * Set the wrapped image.
     * @param image replacement pixel source
     */
    public void setImage(BufferedImage image) {
        this.maintainedImage = image;
    }

    /**
     * Get color of a pixel of the image.
     * @param x coordinate of the pixel to get
     * @param y coordinate of the pixel to get
     * @return color of pixel;
     */
    public int getPixelColor(int x, int y) {
        return maintainedImage.getRGB(x, y);
    }

    /**
     * Get color of a pixel in the image.
     * @param coordinate of the pixel to get
     * @return color of pixel;
     */
    public int getPixelColor(Coordinate coordinate) {
        return maintainedImage.getRGB(coordinate.x(), coordinate.y());
    }

    /**
     * Apply an filter to the wrapped image.
     * @param filter to apply
     */
    public void applyFilter(Filter filter) {
        this.maintainedImage = filter.apply(maintainedImage);
    }

}