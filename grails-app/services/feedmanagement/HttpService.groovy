package feedmanagement

//@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.2' )
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*


class HttpService {
	
	/*
	 * function do http-post
	 *  - url is the url to post to
	 *  - postData is map data (list of name and value)
	 *    postData =  [name: 'bob', title: 'construction worker']
	 *  return true if post success
	 */
	def post(url, postData){
		def http = new HTTPBuilder(url)
		
		http.post( path: '/', 
			       body: postData, 
				   requestContentType: URLENC ) { resp ->
					   return true
				   }
		return false
	}
	
	

}
