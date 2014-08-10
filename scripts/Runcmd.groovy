import org.junit.Before;

println 'Script starting..'

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
		
		// - save only the brand needed
		def feed = new feedmanagement.RawDataService()
		if (feed.addFeedItems(batch, feed)){
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
	
}

/*
def m = feedmanagement.Merchant.findAllByMerchantId(1)
new feedmanagement.Batch(merchant: m, status: feedmanagement.Batch.BatchStatus.NEW).save()
println 'save ok'
*/




