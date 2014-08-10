package feedmanagement

import groovy.json.JsonSlurper

class DownloadService {

	/*
	 * function for download
	 *  - address is the url to download
	 *  - fileName is full path filename to store on disk
	 */
	def download(address, fileName)
	{
		def file = new FileOutputStream(fileName)
		try{
			def out = new BufferedOutputStream(file)
			out << new URL(address).openStream()
			out.close()
			return true		//successful download and save
		}
		catch (all){
			return false	//fail
		}
	}
	
	/*
	 * function parse json 
	 *  - jsonFileName is full path file name for json
	 */
	def parseJson(jsonFileName){
		def json = new JsonSlurper()
		def file = new File(jsonFileName)
		return json.parse(file);
	}
}
