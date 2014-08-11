package feedmanagement

class PushDataService {
	/*
	 * function to just add item into DataItemPush
	 *  - batch is current Batch
	 *  - item is a single item
	 */
	def addPushItem(batch, item){
		new DataItemPush(
			batch: batch,
			sku: batch.merchant.merchantId + '-' + item.SKU,
			productName: item.Name,
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
			gender: item.Gender,
			size: item.Size
		).save()
	}
	
	/*
	 * function push item to a give url using http-post
	 *  - url is the url to push data to
	 *  - item is a single item to be push (model:DataItemPush)
	 *  return true if push success
	 */
	def push(url, secret_key, item){
		//1. initial data
		def dataPost = [
			secret_code: secret_key,
			sku: item.sku,
			product_name: item.productName,
			description: item.description,
			url: item.url,
			original_url: item.originalUrl,
			image_url: item.imageUrl,
			price: item.price,
			brand: item.brand,
			colour: item.colour,
			currency_code: item.currency,
			delivery_cost: item.deliveryCost,
			gender: item.gender,
			size: item.size,
			mid: item.batch.merchant.merchantId
		]
		
		//2. push
		def http = new HttpService()
		if (http.post(url, dataPost) == 'OK'){
			//3. mark as pushed if it's OK
			item.datePushed = new Date()
			item.save()
			return true
		}
		else{
			return false
		}
		
	}

}
