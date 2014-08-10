package feedmanagement

class DataItemRaw {
	
	Integer id
	Batch batch
	
	String sku
	String name
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
	String deliveryTime
	String feature
	String gender
	String size
	String stockLevel
		
	Date dateCreated

    static constraints = {
		id()
		batch()
		sku()
		name()
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
		deliveryTime (nullable: true, blank: true)
		feature (nullable: true, blank: true)
		gender (nullable: true, blank: true)
		size (nullable: true, blank: true)
		stockLevel (nullable: true, blank: true)
    }
	
	static mapping = {
		version false
		table name: "DataItemRaw"
		autoTimestamp: true
				
		description column: 'description', sqlType: "varchar(500)"
	}
}
