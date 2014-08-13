package feedmanagement

import java.util.Date;

class Brand {
	
	Integer brandId
	String name
	Merchant merchant
	Date dateCreated

    static constraints = {
		brandId()
		name()
		merchant()
		dateCreated()
    }
	
	static mapping = {
		version false
		table name: "Brand"
	
		id name: 'brandId'
		autoTimestamp: true
	}
}
