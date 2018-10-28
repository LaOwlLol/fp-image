package fauxpas.filters;

public interface Mixer {

    Filter apply(Filter f1, Filter f2);

}
