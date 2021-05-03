package voteit.modules.exceptions;

public class InvalidFormatException extends Exception {
  private static final long serialVersionUID = 175400759L;

  public InvalidFormatException(String message) {
    super(message);
  }
}
