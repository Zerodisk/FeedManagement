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
			if (brands.findIndexOf{brand -> brand.name.toLowerCase() == item.Brand.toLowerCase()} >= 0){
				
				//get previous batch
				def batchPrevious = Batch.findByBatchId(batch.batchId - 1)
				//try to find previous item
				def itemOld = null
				if (batchPrevious != null){
					itemOld = DataItemRaw.findBySkuAndBatch(batch.merchant.merchantId + '-' + item.SKU, batchPrevious)
				}
				
				//brand is matched to insert
				addRawItem(batch, item)
				
				if (shouldAddToPush(item, itemOld)){
					//add to push item
					def pushSrv = new PushDataService()
					pushSrv.addPushItem(batch, item)
					//println 'added new to push, sku: ' + batch.merchant.merchantId + '-' + item.SKU
				}
			}
			
		}
		
		return true;
	}
	
	/*
	 * function to check if new item should be added to DataItemPush
	 *  - itemNew is json item
	 *  - itemOld is matched item on old item [DateItemRaw]
	 *  return true if should add, return false to ignore
	 */
	def shouldAddToPush(itemNew, itemOld){
		def result = false
		
		if (itemOld == null){
			//not found old match sku, so will add new to push
			result = true
		}
		else{
			//found old matched sku, so check if info still the same
			if ( 
				  (itemNew.Name        != itemOld.name) ||
				  (itemNew.Description != itemOld.description) ||
				  (itemNew.Image       != itemOld.imageUrl) ||
				  (itemNew.Url         != itemOld.url) ||
				  (itemNew.Price.toFloat() != itemOld.price)
			   ){
			   result = true
			}
		}
		
		return result
	}
	
	/*
	 * function to just add item into DataItemRaw
	 *  - batch is current Batch
	 *  - item is a single item
	 */
	def addRawItem(batch, item){
		new DataItemRaw(
			batch: batch,
			sku: batch.merchant.merchantId + '-' + item.SKU,
			name: item.Name,
			category: item.Category,
			description: item.Description,
			url: item.Url,
			originalUrl: item.OriginalUrl,
			imageUrl: item.Image,
			price: item.Price,
			brand: item.Brand,
			colour: item.Colour,
			currency: item.Currency,
			deliveryCost: item.DeliveryCost,
			deliveryTime: item.DeliveryTime,
			gender: item.Gender,
			size: item.Size,
			stockLevel: item.StockLevel
		).save()
	}
	
	
}
