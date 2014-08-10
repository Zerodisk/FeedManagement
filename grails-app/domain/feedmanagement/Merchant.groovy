package feedmanagement

class Merchant {
	
	Integer merchantId
	String  name
	String  description
	String  urlFeed
	String  fileStoreLocal
	Boolean isActive

    static constraints = {
		merchantId()
		name()
		description()
		urlFeed()
		fileStoreLocal()
		isActive()
    }
	
	static mapping = {
		version false
		table name: "Merchant"
	
		id generator: 'assigned', name: 'merchantId'
	}
}
