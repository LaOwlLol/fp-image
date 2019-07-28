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

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * A wrapper for javafx.scene.image.Image to apply fauxpas.deck to.
 */
public class FilterableImage {

    private int width;
    private int height;
    private BufferedImage maintainedImage;
    private Renderer renderer;
    private ArrayList<Filter> deck;

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
        this.deck = new ArrayList<Filter>() {
        };
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Stream<Filter> getFilterDeck() {
        return deck.stream();
    }

    public Stream<Pixel> sample(Selection selection) {
        return ImageHelper.SampleImage(this.maintainedImage, selection);
    }

    public Stream<Pixel> sample() {
        return this.sample(null);
    }

    /**
     * Apply an filter to the wrapped image.
     * @param filter to apply
     */
    public void addFilter(Filter filter) {
        deck.add(filter);
    }

    public BufferedImage render() {
        return render(null);
    }

    public BufferedImage render(Selection selection) {
        if (this.maintainedImage == null) {
            this.maintainedImage = ImageHelper.AllocateARGBBuffer(this.width, this.height);
        }

        if (deck.size() == 0) {
            return this.maintainedImage;
        }

        Stack<Stream<Pixel>> resultBuffer = new Stack<>();
        resultBuffer.push(this.sample(selection));

        this.getFilterDeck().forEachOrdered(f -> {
            resultBuffer.push( f.apply(resultBuffer.pop()) ) ;
        } );


        return this.renderer.render(resultBuffer.pop(), this.width, this.height);
    }

}