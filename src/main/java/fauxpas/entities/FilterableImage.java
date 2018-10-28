package fauxpas.entities;

import fauxpas.filters.Filter;
import fauxpas.filters.SimplexNoise;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FilterableImage {

    private Image maintainedImage;

    public FilterableImage(int width, int height) {
        this.maintainedImage = new WritableImage(width, height);
        this.applyFilter(new SimplexNoise());
    }

    public FilterableImage(Image maintainedImage) {
        this.maintainedImage = maintainedImage;
    }

    public Image getImage() {
        return maintainedImage;
    }

    public void setImage(Image image) {
        this.maintainedImage = image;
    }
    /**
     * Get the intensity value (synonymous with depth) of a pixel
     * @param x coordinate of pixel to get
     * @param y coordinate of pixel to get
     * @return intensity of pixel;
     */
    public Color getPixel(int x, int y) {
        return maintainedImage.getPixelReader().getColor(x, y);
    }

    public void applyFilter(Filter filter) {
        this.maintainedImage = filter.apply(maintainedImage);
    }

}