package voteit.libs.serverhttp;

public interface Handler {
    abstract public Response handle(Context context);
}
