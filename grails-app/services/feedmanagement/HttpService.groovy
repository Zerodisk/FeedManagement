package feedmanagement

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.GET


class HttpService {
	
	/*
	 * function do http-post
	 *  - url is the url to post to
	 *  - postData is map data (list of name and value)
	 *    postData =  [name: 'bob', title: 'construction worker']
	 *  return data in response body
	 */
	def post(url, postData){
		println 'hey! i am here ' + url
		//def http = new HTTPBuilder(url)
		//println 'hey! i am here 2'
		try{
			/*
			http.post( body: postData,
					   requestContentType: URLENC ) { resp ->
						   return resp.data.text					   
					   }
					   */
			new HTTPBuilder(url).request( POST ) { req ->				
			  response.success = { resp ->
				println 'request was successful'
				//return resp.data.text
			  }
			
			  response.failure = { resp ->
				println 'request failed'
				//return resp.status
			  }
			}
		}
		catch(Exception e){
			return e.getMessage()
		}
	}
	
	

}
