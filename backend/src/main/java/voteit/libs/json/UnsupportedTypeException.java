package voteit.libs.json;

public class UnsupportedTypeException extends JsonException {

    private static final long serialVersionUID = 8446056321336847879L;

    public UnsupportedTypeException(String message) {
        super(message);
    }

}
