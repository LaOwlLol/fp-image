package fauxpas.filters;

import javafx.scene.image.Image;

/**
 *  A filter defines a transformation from one image to another.
 */
public interface Filter {

    Image apply(Image i);

}
