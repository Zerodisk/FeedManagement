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
		def brands = Brand.getAll()

		for (item in feedItems){
			if (brands.contains(item.Brand)){
				
			}
		}
	}
	
}
