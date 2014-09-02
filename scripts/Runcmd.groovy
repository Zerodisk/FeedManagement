import org.junit.Before;

def line = ''
BufferedReader br = new BufferedReader(new InputStreamReader(System.in))

while (line != '4'){
	runMenu()
	print 'Please select the menu number: '
	line = br.readLine()
	switch (line){
		case "1":
			runAddBatch()
			break
		case "2":
			runMain()
			break
		case "3":
			runCheckPending()
			break
	}
}

println '\n\n\n\n......\n.....\n...\n..\nbye\n................\n\n\n'

def runCheckPending(){
	def pendingBatches = feedmanagement.Batch.where{status != feedmanagement.Batch.BatchStatus.DONE && merchant.isActive == true}
	println '\n\nNumber of pending batches: ' + pendingBatches.size()
	for (batch in pendingBatches){
		println '- ' + batch.merchant.name + ' (status: ' + batch.status + ')'
	}
}

def runAddBatch(){	
	def selMerchant = feedmanagement.Merchant.findByMerchantId(100)
	new feedmanagement.Batch(merchant: selMerchant, status: feedmanagement.Batch.BatchStatus.NEW).save(flush: true)
	println '\n\nnew batched is added successfully.'
}

def runMenu(){
	println '\n\n\n'
	println '*******************  menu *********************'
	println '*                                             *'
	println '*   Press the following menu number           *'
	println '*                                             *'
	println '*   1. add the new batch (theiconics)         *'
	println '*   2. start the script                       *'
	println '*   3. check the pending batch                *'
	println '*   4. exit                                   *'
	println '*                                             *'
	println '***********************************************'
	println ''
}

def runMain(){
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
				batch.save(flush: true)
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
			println 'Processing..... this could take a while.'
			if (feed.addFeedItems(batch, jsonObj)){
				println 'Process raw data file successed'
				batch.status = feedmanagement.Batch.BatchStatus.FILE_PROCESSED
				batch.save(flush: true)
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
				print 'Pushing ' + numCount + ' of ' + pushPending.size()
				if (pushSrv.push(push_url, remote_secret_key, pushItem)){
					println '     ..push# ' + numCount + ' has successed'
				}
				else{
					println '     ..push# ' + numCount + ' has failed !!!!!! press ctrl-c to stop script..'
				}
				numCount = numCount + 1
				sleep(300)
			}
			
			batch.status = feedmanagement.Batch.BatchStatus.PUSHED
			batch.save(flush: true)
		}
	
		if (batch.status == feedmanagement.Batch.BatchStatus.PUSHED){
			//--------- batch finish, change to done
			batch.status = feedmanagement.Batch.BatchStatus.DONE
			batch.save(flush: true)
			needFinalised = true
		}
		
	}
	
	if (needFinalised){
		//------------ do finalise push here
		def http = new feedmanagement.HttpService()
		if (http.post(push_url_finalise, [secret_code:remote_secret_key]) == 'OK'){
			println 'Push finalise finish..'
		}
		else{
			println 'Push finalise failed!!'
		}
	}
	
	println '\n\nScript end here..\n\n...'
	return ''
}







