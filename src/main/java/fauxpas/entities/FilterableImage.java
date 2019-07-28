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

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * A wrapper for javafx.scene.image.Image to apply fauxpas.filters to.
 */
public class FilterableImage {

    private int width;
    private int height;
    private BufferedImage maintainedImage;
    private Renderer renderer;
    private Deque<Filter> filters;

    /**
     * Construct with dimensions.
     * @param width horizontal dimension
     * @param height vertical dimension
     */
    public FilterableImage(int width, int height) {
        this(ImageHelper.AllocateARGBBuffer(width, height));
    }

    /**
     * Construct by wrapping an existing image.
     * @param maintainedImage The image to wrap.
     */
    public FilterableImage(BufferedImage maintainedImage) {
        this(maintainedImage, ImageHelper::ARGBBufferRenderer);
    }

    public FilterableImage(BufferedImage maintainedImage, Renderer renderer) {
        this.width = maintainedImage.getWidth();
        this.height = maintainedImage.getHeight();
        this.maintainedImage = maintainedImage;
        this.renderer = renderer;
        this.filters = new ArrayDeque<Filter>() {
        };
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Access image
     * @return The image wrapped.
     */
    public BufferedImage getImage() {
        return this.maintainedImage;
    }

    /**
     * Set the wrapped image.
     * @param image replacement pixel source
     */
    public void setImage(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.maintainedImage = image;
    }

    /**
     * Apply an filter to the wrapped image.
     * @param filter to apply
     */
    public void applyFilter(Filter filter) {
        filters.addLast(filter);
    }

    public BufferedImage render(Selection selection) {
        if (this.maintainedImage == null) {
            this.maintainedImage = ImageHelper.AllocateARGBBuffer(this.width, this.height);
        }

        if (filters.size() == 0) {
            return this.maintainedImage;
        }

        Stack<Stream<Pixel>> image = new Stack<>();
        image.push(ImageHelper.SampleImage( this.maintainedImage, selection ));

        filters.stream().sorted().forEach( f -> {
            image.push( f.apply(image.pop()) ) ;
        } );


        return this.renderer.render(image.pop(), this.width, this.height);
    }

}