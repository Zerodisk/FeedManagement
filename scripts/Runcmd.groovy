import org.junit.Before;

println '........\n....\n...\n..\n.\nScript starting..'

def push_url = 'http://www.afrofunk.com.au/store/remote/productUpdate'
def push_url_finalise = 'http://localhost:8081/store/remote/doFinaliseUpdate'
def remote_secret_key = 'i2J9pLeC1-28DekxaDuvm'

def needFinalised = false
def pendingBatches = feedmanagement.Batch.where{status != feedmanagement.Batch.BatchStatus.DONE && merchant.isActive == true}
println 'Number of pending batches: ' + pendingBatches.size()
for (batch in pendingBatches){
	
	def d = new feedmanagement.DownloadService();
	
	if (batch.status == feedmanagement.Batch.BatchStatus.NEW){
		//download the file
		println 'Downloading file: ' + batch.merchant.urlFeed
		if ( d.download(batch.merchant.urlFeed, batch.merchant.fileStoreLocal) ){
			println 'Download successed, file saved'
			batch.status = feedmanagement.Batch.BatchStatus.FILE_DOWNLOADED
			batch.save()
		}
		else{
			println 'File download failed !!!'
			return
		}
	}
	
	if (batch.status == feedmanagement.Batch.BatchStatus.FILE_DOWNLOADED){
		//process the raw file
		// - read xml/json file ("/home/tan/Documents/Developments/workspaces/temp/datafeed.json")
		def jsonObj = d.parseJson(batch.merchant.fileStoreLocal)
		println 'Number of raw items: ' + jsonObj.size()
		
		// - save only the brand needed
		def feed = new feedmanagement.RawDataService()
		if (feed.addFeedItems(batch, jsonObj)){
			println 'Process raw data file successed'
			batch.status = feedmanagement.Batch.BatchStatus.FILE_PROCESSED
			batch.save()
		}
		else{
			println 'File processing failed !!!'
			return
		}
	}
	
	if (batch.status == feedmanagement.Batch.BatchStatus.FILE_PROCESSED){
		//start pushing
		
		//get pending push
		
		//loop through and push
		
		
	
	}

	if (batch.status == feedmanagement.Batch.BatchStatus.PUSHED){
		// batch finish, change to done
		batch.status = feedmanagement.Batch.BatchStatus.DONE
		batch.save()
		needFinalised = true
	}
	
}

if (needFinalised){
	//do finalise push here
	def http = new feedmanagement.HttpService()
	if (http.post(push_url_finalise, [secret_code:remote_secret_key]) == 'OK'){
		println 'Push finalise finish..  Script end here..'
	}
	else{
		println 'Push finalise failed!!'
	}
	
}






