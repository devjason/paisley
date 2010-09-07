package com.devjason.svnlog.domain

import grails.test.*

class BranchTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testBranchName() {
		Branch branch = new Branch(name:'trunk')
		assertEquals "trunk", branch.name
    }
}
