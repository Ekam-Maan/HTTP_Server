package HTTP;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpParser  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
	private static final int SP = 32;//0x20; //32
	private static final int CR = 13;//0x80; //13
	private static final int LF = 10;// 0x0A; //10
	
	public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
		//System.out.println("inside HTTP paerse");
		InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
		
		HttpRequest request = new HttpRequest();
		
		try {
			parseRequestLine(reader, request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parseHeaders(reader, request);
		parseBody(reader,request);
		return request;
	}



	

	private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
		
		
		int _byte;
		String body = "";
		
    	
    	 
		boolean methodParsed = false;
		boolean requestTargetParsed = false;
		boolean versionParsed=false;
		
		StringBuilder processingDataBuffer = new StringBuilder();
		while((_byte= reader.read()) >= 0) {
			
		//	System.out.println("inside while: "+ _byte+" : "+ (char)_byte);
			if (_byte == CR) {
				_byte = reader.read();
				if(_byte == LF) {
					
					if(!methodParsed || ! requestTargetParsed) { // checking if the request line is incomplete or empty
						throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
					}
					System.out.println();
					LOGGER.debug("Request Line HTTP version to Process: {}", processingDataBuffer.toString());
					
						try {
							if(!versionParsed) {
								request.setHttpVersion(processingDataBuffer.toString());
								versionParsed = true;
							}
							
						} catch (BadHttpVersionException e) {
							// TODO Auto-generated catch block
							throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
						} catch (HttpParsingException e) {
							// TODO Auto-generated catch block
							throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
						}
						System.out.println();
					if(methodParsed && request.getMethod().name().equals("GET")) {
						return;
					}else if (methodParsed && requestTargetParsed && request.getMethod().name().equals("POST")) {
						_byte = reader.read();
						if(_byte == CR) {
							processingDataBuffer.append((char)_byte);
							_byte = reader.read();
							if(_byte == LF) {
								System.out.println("Reached body......");
								boolean bodyLeft = true;
								// parsing body of the request
								while(bodyLeft) {
									_byte= reader.read();
									System.out.println((char)_byte);
									 body += (char)_byte;
									 if((char)_byte == '}') {
											bodyLeft = false;
										}
								}
								request.setBody(body);
								System.out.println("body"+body);
								return;
							}
							else {
								processingDataBuffer.append((char)_byte);
							}
						}
					}
					
					
					
					
				}else {
					throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
				}
			}
			
			if(_byte == SP ) {
				if(!methodParsed) {
					System.out.println();
					LOGGER.debug("\nRequest Line METHOD to Process: {}", processingDataBuffer.toString());
					
					//System.out.println("there 1"+ processingDataBuffer.toString() );
					request.setMethod(processingDataBuffer.toString());
					//System.out.print(processingDataBuffer.toString());
					//System.out.println("there");
					processingDataBuffer.delete(0, processingDataBuffer.length());
					methodParsed = true;
				} else if(!requestTargetParsed) {
					System.out.println();
					LOGGER.debug("\nRequest Line REQUEST TARGET to Process: {}", processingDataBuffer.toString());
					request.setRequestTarget(processingDataBuffer.toString());
					//System.out.print(processingDataBuffer.toString());
					processingDataBuffer.delete(0, processingDataBuffer.length());
					requestTargetParsed = true;
				}else if (request.getMethod().name().equals("POST")) {
					processingDataBuffer.append((char)_byte);
				}
				else {// checking if the request line has more than two spaces, i.e. more than two items
					throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
				}
				
			}else {
				processingDataBuffer.append((char)_byte);
				System.out.print((char)_byte);
				// checking if the length of the method is not too long ; otherwise first it will first get stored in the 
				// the buffer and then it will be compared, which is not a good approach
				if(!methodParsed && processingDataBuffer.length()> HttpMethod.MAX_LENGTH) {
					//System.out.println("LEnght: "+processingDataBuffer.toString());
					throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
				}
			}
		}
		
		
	}
	private void parseHeaders(InputStreamReader reader, HttpRequest request) {
		
		
	}
	private void parseBody(InputStreamReader reader, HttpRequest request) {
		
	}
		
		
		
	
	

}
