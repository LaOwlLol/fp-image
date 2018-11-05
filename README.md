# fp-image
A simple general purpose image processing library for Java.

### Version 0.1.0
This API provides a filter feature which can be applied to a JavaFX 8 Image wrapped in an object called FilterableImage.

This API provides implementations of Gaussian Blur, Sobel edges, and Canny edges filters. Custom filters can be defined by implementing the functional interface Filter.

The API also provides a way to mix/blend filtered images with the functional interface Mixer.

#### Define an FilterableImage:

FilterableImage wraps a JavaFX 8 Image.

```java
//blank image
FilterableImage myFilteredImage = new FilterableImage(800, 600);

//use existing image constructor
myFilteredImage = new FilterableImage(myImage);
```

#### Generate a height map from noise filters:

Several noise filters are available, also fastNoise can be used to create new noise filters.

```java
FilterableImage noiseImage = new FilterableImage(800, 600);
noiseImage.applyFilter(new PerlinNoise());
```

#### Gaussian blur and Sobel edge detect:

Gaussian blur and simple edge detection are available.

```java

FilterableImage myFilteredImage = new FilterableImage(myImage);

//arg1 = Convolution kernel width (rec. 3, larger means more compute time), arg2 = standard deviation. 
myFilteredImage.applyFilter(new GaussianBlur(3, 3));

myFilteredImage.applyFilter(new SobelFilter());
```
 
#### Blend or Sum noise values:

Mixers blended or sum the color values for the results of two filters. Be sure the return images are the same dimensions.

```java
FilterableImage noiseImage = new FilterableImage(800, 600);
noiseImage.applyFilter(new PerlinNoise());

// blend calculates pixel color channel as color1.getRed() - (color1.getRed() - color2.getRed())/2
noiseImage.applyFilter( BlendFilter().apply(image -> image , new SimplexNoise(2)) )

noiseImage = new FilterableImage(800, 600);
noiseImage.applyFilter(new PerlinNoise());

// sum calculates pixel color channel as  (intensity1*color1.getRed()) + (intensity2*color2.getRed())
.applyFilter( SumFilter().apply(image -> image, new SimplexNoise(2)) )
```

#### Sobel Edges:

It's always recommended to blur an images before detect images. Sobel filter internally uses a grayscale filter, so there is no need to do this on your own (although this may change in the future). 

The parameter to Sobel filter is a threshold value to further reduce noisy edges in the image. A very small value (about 0.02 - 0.01 or lower, even 0.0) seems to work well, but fine tuning images is probably required.

```java
myFilteredImage.applyFilter(new GaussianBlur(3,3));
myFilteredImage.applyFilter(new SobelFilter(0.02));
```

#### Canny Edges: 

CannyFilter works on the output of a SobelFilter (it does not internally call sobel at this time).  CannyFilter constructor can take an upper and low threshold values for double threshold and edge tracking by hysteresis (see wikipedia).  If not thresholds are provided the default value are 0.0001 and 0.15.

```java
myFilteredImage.applyFilter(new GaussianBlur(3,3));
myFilteredImage.applyFilter(new SobelFilter(0.00001));
myFilteredImage.applyFilter(new CannyFilter());
```







