package feedmanagement

import java.util.Date;

class Brand {
	
	Integer brandId
	String name
	Date dateCreated

    static constraints = {
		brandId()
		name()
		dateCreated()
    }
	
	static mapping = {
		version false
		table name: "Brand"
	
		id name: 'brandId'
		autoTimestamp: true
	}
}
