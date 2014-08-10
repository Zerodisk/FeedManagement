package feedmanagement


class RawDataService {

	/*
	 * function to process feed
	 *  - batch is the batch object
	 *  - feedItems is the list of all item raw data
	 *  return true if success, false when failed
	 */
	def addFeedItems(batch, feedItems){
		// - get all brand
		def brands = new Brand()
		brands = brands.getAll()
		
		for (item in feedItems){			
			if (brands.findIndexOf{brand -> brand.name == item.Brand} >= 0){
				//brand is matched to insert
				
				
			}
			
		}
		
		return true;
		
	}
	
	/*
	 * function to check if new item should be added to DataItemPush
	 *  - batch is a current Batch
	 *  - item to check
	 *  return true if should add, return false to ignore
	 */
	def shouldAddToPush(batch, item){
		
	}
	
	/*
	 * function to just add item into DataItemRaw
	 *  - batch is current Batch
	 *  - item is a single item
	 */
	def addRawItem(batch, item){
		
	}
	
	
}
