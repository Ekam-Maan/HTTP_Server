package HTTP;

public class HttpParsingException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final HttpStatusCode errorCode;
	
	public HttpParsingException (HttpStatusCode errorCode) {
		super(errorCode.MSG);
		this.errorCode = errorCode;
	}

	public HttpStatusCode getErrorCode() {
		return errorCode;
	}

}
