package feedmanagement

import java.util.Date;

class DataItemPush {
	Integer id
	Batch batch
	
	String sku
	String productName
	String category
	String description
	String url
	String originalUrl
	String imageUrl
	
	float price
	
	String brand
	String colour
	String currency
	String deliveryCost
	String gender
	String size
		
	Date dateCreated
	Date datePushed

	static constraints = {
		id()
		batch()
		sku()
		productName()
		category (nullable: true, blank: true)
		description (nullable: true, blank: true)
		url (nullable: true, blank: true)
		originalUrl (nullable: true, blank: true)
		imageUrl (nullable: true, blank: true)
		price (nullable: true, blank: true)
		brand (nullable: true, blank: true)
		colour (nullable: true, blank: true)
		currency (nullable: true, blank: true)
		deliveryCost (nullable: true, blank: true)
		gender (nullable: true, blank: true)
		size (nullable: true, blank: true)
		dateCreated()
		datePushed(nullable: true)
	}
	
	static mapping = {
		version false
		table name: "DataItemPush"
		autoTimestamp: true
				
		description column: 'description', sqlType: "varchar(500)"
	}

}
