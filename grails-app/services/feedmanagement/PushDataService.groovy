package feedmanagement

class PushDataService {
	/*
	 * function to just add item into DataItemPush
	 *  - batch is current Batch
	 *  - item is a single item
	 */
	def addPushItem(batch, item){
		
		
		
	}
	
	/*
	 * function push item to a give url using http-post
	 *  - url is the url to push data to
	 *  - item is a single item to be push (model:DataItemPush)
	 */
	def push(url, item){
		//1. initial data
		//2. push
		
		//3. mark as pushed
		item.save()
	}

}
