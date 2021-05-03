package voteit.modules.exceptions;

public class ResourceNotFoundException extends Exception {
  public ResourceNotFoundException(String resource) {
    super(resource + " not found");
  }
}
