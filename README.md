# fp-image
A simple general purpose image processing library for Java.

### Version 0.1.0
This API provides a filter features which can be applied to a wrap a JavaFX 8 Image called FilterableImage.

Filters can be defined by implementing the functional interface Filter.

The API also provides a way to Mix Filters with the functional interface Mixer.

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

#### Gaussian blur and Sobel edge detect

Gaussian blue and simple edge detection are available.

```java

FilterableImage myFilteredImage = new FilterableImage(myImage);

//arg1 = Convolution kernel width (rec. 3, larger means more compute time), arg2 = standard deviation. 
myFilteredImage.applyFilter(new GaussianBlur(3, 3));

myFilteredImage.applyFilter(new SobelFilter());
```
 
#### Blend or Sum noise values.

Although fairly verbose, you can mix two noise signals by applying a Mixer (which returns new filter to be applied) where the first filter argument is the redistribution filter with a power of 1.0. This is because pixel color value to the power 1 returns the original color value.

```java
FilterableImage noiseImage = new FilterableImage(800, 600);
noiseImage.applyFilter(new PerlinNoise());

// blend calculates pixel color channel as color1.getRed() - (color1.getRed() - color2.getRed())/2
noiseImage.applyFilter( BlendFilter().apply(new RedistributionFilter(1.0), new SimplexNoise(2)) )

noiseImage = new FilterableImage(800, 600);
noiseImage.applyFilter(new PerlinNoise());

// sum calculates pixel color channel as  (intensity1*color1.getRed()) + (intensity2*color2.getRed())
.applyFilter( SumFilter().apply(new RedistributionFilter(1.0), new SimplexNoise(2)) )
```




