package feedmanagement

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.URLENC


class HttpService {
	
	/*
	 * function do http-post
	 *  - url is the url to post to
	 *  - postData is map data (list of name and value)
	 *    postData =  [name: 'bob', title: 'construction worker']
	 *  return data in response body
	 */ 
	def post(url, postData){

		try{
			def http = new HTTPBuilder( url ) 
			http.post( body: postData,
			           requestContentType: URLENC ) { resp, reader ->
			 
			  //println "POST Success: ${resp.statusLine}"
			  return reader
			}
		}
		catch(Exception e){
			return e.getMessage()
		}
		
	}
	
	

}
