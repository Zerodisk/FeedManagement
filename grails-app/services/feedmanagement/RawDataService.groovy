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
		
	}
	
}
