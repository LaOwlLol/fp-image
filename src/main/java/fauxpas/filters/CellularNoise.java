package fauxpas.filters;

import fauxpas.fastnoise.FastNoise;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        for (int j = 0; j < image.getHeight(); ++j) {
            for (int i = 0; i < image.getWidth(); ++i) {
                double color =  (this.fastNoise.GetNoise(this.frequencyX * i, this.frequencyY * j)/2)+0.5;
                bufferWriter.setColor(i, j, new Color(color, color, color, 1.0));
            }
        }

        return buffer;
    }

}
