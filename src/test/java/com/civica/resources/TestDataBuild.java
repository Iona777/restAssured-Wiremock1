package com.civica.resources;

import com.civica.pojos.AddPlace;
import com.civica.pojos.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * This class contains methods that are used to return test data, in this case payloads.
=
 *    *@param foo  the foo parameter
 *    * @param bar  the bar parameter
 *    * @return the baz content
 */
public class TestDataBuild
{

    /**
     * Creates a new payload for AddPlace API using input parameters
     * Uses POJO classes internally to create the data
     * @param name the name of the location
     * @param language the language used in the location
     * @param address the address of the location
     * @return returns AddPlace type
     */
    public AddPlace addPlacePayload(String name, String language, String address)
    {
        AddPlace place = new AddPlace(); // This is a POJO class to define data
        List<String> typesList = new ArrayList<>();
        typesList.add("car park");
        typesList.add("shop");
        Location loc = new Location(); //This is a POJO class to define data
        loc.setLat(-38.383494); //Uses the setter methods of the location class
        loc.setLng(33.427362);
        place.setLocation(loc); //Uses the setter methods of the place class
        place.setAccuracy(50);
        place.setName(name); //This is set from the input parameter rather than hard coding
        place.setPhone_number("0113 123456");
        place.setAddress(address); //This is set from the input parameter rather than hard coding
        place.setWebsite("http://google.com");
        place.setLanguage(language); //This is set from the input parameter rather than hard coding

        return  place;
    }

    /**
     * Creates a new payload for AddPlace API using input parameters
     * Uses HashMaps internally to create the data
     * @param name the name of the location
     * @param language the language used in the location
     * @param address the address of the location
     * @return AddPlace type
     */
    public HashMap<String, Object> addPlacePayloadHashMap(String name, String language, String address)
    {
     HashMap<String, Object> place = new HashMap<>(); //These are HashMaps to define data
     HashMap<String, Object> loc = new HashMap<>();
     List<String> typesList = new ArrayList<>(); //Seems to be difficult to use a HashMap for a list.

     typesList.add("car park");
     typesList.add("shop");

     //Using loc HashMap
        loc. put("Lat", -38.383494 );
        loc.put("Lng",33.427362);

        //Usi place hashmap
        place.put("Location", loc); //Sets location object to the loc Hashmap
        place.put("Accuracy", 50);
        place.put("Name",name); //This is set from the input parameter
        place.put("Phone_Number","0113 123456");
        place.put("Address",address); //This is set from the input parameter
        place.put("Website","http://google.com");
        place.put("Language",language); //This is set from the input parameter

     return  place;

    }

    /**
     * Sets value of place_id to be deleted
     * @param placeId id of the place
     * @return HashMap type
     */
    public HashMap<String, Object> deletePlacePayload(String placeId)
    {
        //Using Hashmap, return HashMap<String, Object> if using this
        HashMap<String, Object> jsonHashMap = new HashMap<>();
        jsonHashMap.put("place_id", placeId);

        return jsonHashMap;
    }
}
