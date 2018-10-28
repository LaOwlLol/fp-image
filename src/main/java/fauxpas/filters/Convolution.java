package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public interface Convolution {

    double[][][] computeKernel(Image target, PixelReader targetReader, double[][] convolution, int imageY, int imageX);
}
