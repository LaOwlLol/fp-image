package fauxpas.entities;

import fauxpas.filters.Filter;
import fauxpas.filters.noise.WhiteNoise;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * A wrapper for javafx.scene.image.Image to apply fauxpas.filters to.
 */
public class FilterableImage {

    private Image maintainedImage;

    /**
     * Construct with dimensions.
     * @param width
     * @param height
     */
    public FilterableImage(int width, int height) {
        this.maintainedImage = new WritableImage(width, height);
        this.applyFilter(new WhiteNoise());
    }

    /**
     * Construct by wrapping an existing image.
     * @param maintainedImage The image to wrap.
     */
    public FilterableImage(Image maintainedImage) {
        this.maintainedImage = maintainedImage;
    }

    /**
     * Access image
     * @return The image wrapped.
     */
    public Image getImage() {
        return maintainedImage;
    }

    /**
     * Set the wrapped image.
     * @param image
     */
    public void setImage(Image image) {
        this.maintainedImage = image;
    }

    /**
     * Get color of a pixel of the image.
     * @param x coordinate of the pixel to get
     * @param y coordinate of the pixel to get
     * @return color of pixel;
     */
    public Color getPixelColor(int x, int y) {
        return maintainedImage.getPixelReader().getColor(x, y);
    }

    /**
     * Get color of a pixel in the image.
     * @param coordinate of the pixel to get
     * @return color of pixel;
     */
    public Color getPixelColor(Coordinate c) {
        return maintainedImage.getPixelReader().getColor(c.x(), c.y());
    }

    /**
     * Apply an filter to the wrapped image.
     * @param filter
     */
    public void applyFilter(Filter filter) {
        this.maintainedImage = filter.apply(maintainedImage);
    }

}