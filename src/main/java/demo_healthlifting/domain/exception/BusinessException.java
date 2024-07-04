package demo_healthlifting.domain.exception;

public class BusinessException extends Exception {

	private static final long serialVersionUID = -6666714642586995974L;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

}
