package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashMap;
import java.util.Map;

import static org.example.App.*;


/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public void testGetBestCustomer(){
        assertEquals("ID: 94884341; Name: Erika Musterfrau", getBestCustomer(1, 2025));
        assertEquals("No Customer Found", getBestCustomer(9, 2001));
    }

    public void testMakeRequest(){
        // not sure how to best test an api result
        assertTrue(makeRequest("Contact").length() > 100);
        assertTrue(makeRequest("Invoice").length() > 100);

        try{
            makeRequest("This route does not exist");
        }catch (RuntimeException e){
            assertEquals("Error with the HTTP Request", e.getMessage());
        }
    }

    public void testAddOrUpdateAmount(){

        Map<String, Integer> testMap = new HashMap<>();
        assertEquals(0, testMap.size());

        addOrUpdateAmount(testMap, "First Id", 42);
        assertEquals(1, testMap.size());
        assertEquals(42, (int) testMap.get("First Id"));

        addOrUpdateAmount(testMap, "Second Id", 69);
        assertEquals(2, testMap.size());
        assertEquals(69, (int) testMap.get("Second Id"));

        addOrUpdateAmount(testMap, "First Id", 1337);
        assertEquals(2, testMap.size());
        assertEquals(1379, (int) testMap.get("First Id"));
    }
}
