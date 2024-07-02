package org.example;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CountryLocatorTest {

    private CountryLocator locator;

    // This method sets up the CountryLocator instance before each test
    @Before
    public void setUp() {
        String geoJsonFilePath = "src/main/resources/countries.geojson";
        locator = new CountryLocator(geoJsonFilePath);
    }

    // Test to verify that valid coordinates return the correct country code
    @Test
    public void testValidCoordinates() {
        // Test coordinates for Andorra
        String countryCode = locator.getCountryCode(1.601554, 42.546245);
        assertEquals("AD", countryCode);  // Example country code, replace with actual expected value

        // Test coordinates for Finland
        countryCode = locator.getCountryCode(25.748151, 61.92411);
        assertEquals("FI", countryCode);  // Example country code, replace with actual expected value
    }

    // Test to verify that invalid coordinates return null
    @Test
    public void testInvalidCoordinates() {
        // Test coordinates that are out of valid range
        String countryCode = locator.getCountryCode(200, 200.0);
        assertNull(countryCode);

        // Test more invalid coordinates
        assertNull(locator.getCountryCode(-91, 0));  // Invalid latitude
        assertNull(locator.getCountryCode(91, 0));   // Invalid latitude
        assertNull(locator.getCountryCode(0, -181)); // Invalid longitude
        assertNull(locator.getCountryCode(0, 181));  // Invalid longitude
    }

    // Test to verify edge cases return null
    @Test
    public void testEdgeCases() {
        // Test the origin point (0, 0)
        assertNull(locator.getCountryCode(0, 0));

        // Test the South Pole
        assertNull(locator.getCountryCode(-90, 0));

        // Test the North Pole
        assertNull(locator.getCountryCode(90, 0));

        // Test the International Date Line (West)
        assertNull(locator.getCountryCode(0, -180));

        // Test the International Date Line (East)
        assertNull(locator.getCountryCode(0, 180));
    }
}
