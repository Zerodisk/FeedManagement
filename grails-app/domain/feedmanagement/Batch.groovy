package feedmanagement

import java.util.Date;

class Batch {
	
	Integer batchId
	Merchant merchant
	BatchStatus status
	Date dateCreated 
	
	
	enum BatchStatus {
		//NEW(0), FILE_DOWNLOADED(1), FILE_PROCESSED(2), PUSHED(3), DONE(99)
		NEW, FILE_DOWNLOADED, FILE_PROCESSED, PUSHED, DONE
	}
	

    static constraints = {
		batchId()
		merchant()
		status()
		dateCreated()
    }
	
	static mapping = {
		version false
		table name: "Batch"
	
		id name: 'batchId'
		autoTimestamp: true
		//status = [NEW: 0, FILE_DOWNLOADED:1, FILE_PROCESSED:2, PUSHED:3, DONE:4]
	}
}
