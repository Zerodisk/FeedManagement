import grails.test.AbstractCliTestCase

class RuncmdTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testRuncmd() {

        execute(["runcmd"])

        assertEquals 0, waitForProcess()
        verifyHeader()
    }
}
