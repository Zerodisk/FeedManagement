import org.junit.Before;

println '........\n....\n...\n..\n.\nScript starting..'

def push_url = 'http://localhost:88/store/remote/productUpdate'
def push_url_finalise = 'http://localhost:88/store/remote/doFinaliseUpdate'
def remote_secret_key = 'i2J9pLeC1-28DekxaDuvm'

def needFinalised = false
def pendingBatches = feedmanagement.Batch.where{status != feedmanagement.Batch.BatchStatus.DONE && merchant.isActive == true}
println 'Number of pending batches: ' + pendingBatches.size()
for (batch in pendingBatches){
	
	def d = new feedmanagement.DownloadService();
	
	if (batch.status == feedmanagement.Batch.BatchStatus.NEW){
		//------- download the file
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
		//------- process the raw file
		// - read xml/json file ("/home/tan/Documents/Developments/workspaces/temp/datafeed.json")
		println 'Processing raw data: ' + batch.merchant.fileStoreLocal
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
		//-------- start pushing
		//get pending push
		def pushPending = feedmanagement.DataItemPush.findAllByBatchAndDatePushed(batch, null)
		
		//loop through and push
		def pushSrv = new feedmanagement.PushDataService()
		def numCount = 1
		for (pushItem in pushPending){
			println 'Pushing ' + numCount + ' of ' + pushPending.size()
			if (pushSrv.push(push_url, remote_secret_key, pushItem)){
				println '  ..push# ' + numCount + ' has successed'
			}
			else{
				println '  ..push# ' + numCount + ' has failed !!!!!! press ctrl-c to stop script..'
			}
			numCount = numCount + 1
		}
		
		batch.status = feedmanagement.Batch.BatchStatus.PUSHED
		batch.save()
	}

	if (batch.status == feedmanagement.Batch.BatchStatus.PUSHED){
		//--------- batch finish, change to done
		batch.status = feedmanagement.Batch.BatchStatus.DONE
		batch.save()
		needFinalised = true
	}
	
}

if (needFinalised){
	//------------ do finalise push here
	def http = new feedmanagement.HttpService()
	if (http.post(push_url_finalise, [secret_code:remote_secret_key]) == 'OK'){
		println 'Push finalise finish..  Script end here..'
	}
	else{
		println 'Push finalise failed!!'
	}
	
}






