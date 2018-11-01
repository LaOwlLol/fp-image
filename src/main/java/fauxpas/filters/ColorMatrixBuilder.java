package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import org.jblas.DoubleMatrix;

public class ColorMatrixBuilder {

    public static DoubleMatrix getColorMatrix(Image target, ColorReader colorReader, int kernelWidth, int imageX, int imageY ) {
        DoubleMatrix colors = new DoubleMatrix(kernelWidth, kernelWidth);
        PixelReader targetReader = target.getPixelReader();
        int midPoint = kernelWidth/2;

        for (int kernelY = 0; kernelY < kernelWidth; ++kernelY ) {
            for (int kernelX = 0; kernelX < kernelWidth; ++kernelX) {

                int i = kernelX - midPoint;
                int j = kernelY - midPoint;

                if ( ((imageX+i > 0) && (imageX+i < target.getWidth())) && ((imageY+j > 0) && (imageY+j < target.getHeight())) ) {
                    colors.put(kernelX, kernelY, colorReader.getColorProperty(targetReader.getColor(imageX+i, imageY+j)) );
                }
            }
        }
        return colors;
    }
}
