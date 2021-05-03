package voteit.libs.json;

public class WrongTypeException extends JsonException {

    private static final long serialVersionUID = -678118423971148762L;

    public WrongTypeException(String message) {
        super(message);
    }

}
