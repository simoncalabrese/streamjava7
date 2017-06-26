package interfaces;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class Optional<T> {

    private T elem;

    public Optional(T elem) {
        this.elem = elem;
    }

    public static<T> Optional<T> of(T elem) {
        return new Optional<>(elem);
    }

    public Boolean isPresent(){
        return elem != null;
    }

    public T get() {
        return elem;
    }

    public T orElse(T defaultElem) {
        if(isPresent()) {
            return get();
        } else {
            return defaultElem;
        }
    }
}
