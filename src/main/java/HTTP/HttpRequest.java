package HTTP;

public class HttpRequest extends HttpMessage {
	
	private HttpMethod method;
	private String requestTarget;
	private String originalHttpVersion;
	private HttpVersion bestCompatibleHttpversion;
	private String body;
   
    public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "HttpRequest [method=" + method + ", requestTarget=" + requestTarget + ", bestCompatibleHttpversion="
				+ bestCompatibleHttpversion.getLITERAL() + "]";
	}
	HttpRequest(){
		
	}
	public HttpMethod getMethod() {
		return method;
	}

    public void setMethod( String methodName) throws HttpParsingException {
    	for (HttpMethod method: HttpMethod.values()) {
    		if(methodName.equals(method.name())) {
    			this.method = method;
    			return;
    		}
    	}
		throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
	}

	
	

	public String getRequestTarget() {
		// TODO Auto-generated method stub
		return this.requestTarget;
	}

	public void setRequestTarget(String requestTarget) throws HttpParsingException {
		
		if(requestTarget == null || requestTarget.length() == 0) {
			throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
		}
		this.requestTarget = requestTarget;
		// TODO Auto-generated method stub
		
	}
	
	public HttpVersion getHttpVersion() {
		return this.bestCompatibleHttpversion;
	}
	
	public String getOriginalVersion() {
		return this.originalHttpVersion;
	}
	public void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
		this.originalHttpVersion= originalHttpVersion;
		this.bestCompatibleHttpversion= HttpVersion.getBestCompatibleVersion(originalHttpVersion);
		if(bestCompatibleHttpversion == null) {
			throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
