package com.civica.resources;

//enum is a special class in java which has a collection of constants and methods
public enum ApiResources
{
    //This is invoked using valueOf
    //E.G. ApiResources.valueOf(resources);
    //If resource = addPlaceAPI, the it will pass the value of addPlaceAPI to the constructor
    //Then the constructor sets the class variable resource to the same value
    //Finally, that resource value is returned by the getResource() method
    //E.G. If resource is addPlace then ApiResources.valueOf(resource) = "/maps/api/place/add/json"
    //This is passed to ApiResources() constructor
    //the class variable private String resource is set to this value.
    //getResouce() then returns value "/maps/api/place/add/json"

    //The syntax for the enum method declaration is to separate them with commas and have a
    //semi-colon at the end of the method declarations.
    //This is a method thta will return the string value "/maps/api/place/add/json"
    addPlaceAPI("/maps/api/place/add/json"),
    //This is a method that will return the string value "/maps/api/place/delete/json"
    getAddPlaceAPI("/maps/api/place/delete/json"),
    //This is a method that will return the string value "/maps/api/place/delete/json"
    deletePlaceAPI("/maps/api/place/delete/json");

    private String resource;

    //This is the constructor for the APIResources enum (class)
    //Since the methods accept 1 string, so does the constructor
    ApiResources(String resource)
    {
        //This sets the class level resource variable to the resource parameter passed in
        this.resource=resource;
    }


    public String getResource()
    {
        //Seems the constructed resource cannot be returned directly as its scope is local to the constructor
        //so, you need to assign it to another variable at class level and return that instead.
        return resource;
    }


}
