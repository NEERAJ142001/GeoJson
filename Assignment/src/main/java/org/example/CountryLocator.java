package org.example;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CountryLocator {

    // Map to store country geometries with country code as the key
    private final Map<String, Geometry> countryGeometries = new HashMap<>();

    // Constructor to initialize the country geometries map
    public CountryLocator(String geoJsonFilePath) {
        loadCountryGeometries(geoJsonFilePath);
    }

    // Method to load country geometries from a GeoJSON file
    // path having the location of geojson file
    private void loadCountryGeometries(String filePath) {
        // Initialize a FeatureJSON object for reading GeoJSON data
        FeatureJSON featureJSON = new FeatureJSON();
        try {
            File file = new File(filePath);
            // Read the GeoJSON file and parse it into a SimpleFeatureCollection
            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) featureJSON.readFeatureCollection(file);

            // Iterate through the features and populate the country geometries map
            try (SimpleFeatureIterator features = featureCollection.features()) {
                while (features.hasNext()) {
                    SimpleFeatureImpl feature = (SimpleFeatureImpl) features.next();
                    System.out.println("Feature is"+ feature);
                    String countryCode = (String) feature.getAttribute("ISO_A2");

                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    countryGeometries.put(countryCode, geometry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get the country code for a given latitude and longitude
    public String getCountryCode(double latitude, double longitude) {
        Point point = createPoint(latitude, longitude);
        // Return null if the point is invalid
        if (point == null) {
            return null;
        }
        System.out.println("Point: " + point);
        // Check if the point is within any country's geometry
        for (Map.Entry<String, Geometry> entry : countryGeometries.entrySet()) {
            String countryCode = entry.getKey();
            Geometry geometry = entry.getValue();

            if (geometry.contains(point)) {
                return countryCode;
            }
        }
        return null;
    }

    // Method to create a Point from latitude and longitude
    private Point createPoint(double latitude, double longitude) {
        // Validate latitude and longitude
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            return null;
        }

        // Create a GeometryFactory object
        GeometryFactory geometryFactory = new GeometryFactory();

        // Create a Coordinate with the given longitude and latitude
        Coordinate coordinate = new Coordinate(latitude, longitude);

        // Create a Point using the GeometryFactory and the Coordinate
        Point point = geometryFactory.createPoint(coordinate);

        return point;
    }
    // Main method for testing
    public static void main(String[] args) {
        String geoJsonFilePath = "src/main/resources/countries.geojson";
        CountryLocator locator = new CountryLocator(geoJsonFilePath);
        String countryCode = locator.getCountryCode(-13.768752, -177.156097);
        System.out.println("Country Code: " + countryCode);
    }

}
