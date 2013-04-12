package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import java.security.*;
import play.Logger;

import javax.persistence.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;


/*
Restaurant is a class that doubles up as a database table. Attributes id, name, owner, password hours, and about 
are all attributes. hours and about are not required fields, but all other attributes are.
*/
@Entity
public class Restaurant extends Model {

	@Id
	public Long id;

	@Required
	public String name;

	@Required
	public String owner;

	@Required
	public String password;

	@Column(columnDefinition = "TEXT")
	public String hours;

	@Column(columnDefinition = "TEXT")
	public String about;

// Find is a object that indexes the restaurant class and allows you to search for it by Long id.
public static Finder<Long,Restaurant> find = new Finder( Long.class, Restaurant.class );

// Query restaurant by name. Return restaurant object.
public static Restaurant by_name(String name) {
	List<Restaurant> matches =  Restaurant.find.where().eq("name", name).findList();
	try{
		return matches.get(0);
}
	catch (IndexOutOfBoundsException e){ // no elements in list.
		 return null;
	}
}

// Query restaurant by owner. Return restaurant object.
public static Restaurant by_owner(String owner) {
	List<Restaurant> matches = Restaurant.find.where().eq("owner", owner).findList();
	try{
		return matches.get(0);
	}
	catch (IndexOutOfBoundsException e){ // no elements in list.
		 return null;
	}
}

// Hash a password and return a password hash. 
// This secures the password ensuring it is never exposed in storage.
public static String hashpw(String password) throws NoSuchAlgorithmException{
	MessageDigest pwhasher = MessageDigest.getInstance("SHA-256");
	pwhasher.update(password.getBytes());
	byte [] pwhash = pwhasher.digest();
	return new String(pwhash);
	}

/*
Get date to automatically assign to restaurant.about. This code I found and did not write myself:
http://stackoverflow.com/questions/2942857/how-to-convert-current-date-into-string-in-java
*/
public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

/*
Upon clean up this method will be moved to another class.

now() returns a formatted string of the current datetime.
*/
public static String now() {
Calendar cal = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
return sdf.format(cal.getTime());
}

/*
This collects a new restaurant object, adds an empty hours string formatted as html.
It calls now() to find the time that the restaurant was created and adds that to about.
It then adds the restaurant to the database.
*/
public static void create(Restaurant restaurant){
	try{
		restaurant.password = Restaurant.hashpw(restaurant.password);
	}
	catch (NoSuchAlgorithmException e){ //This should never be called, given that SHA-256 is a valid algorithm.
		// as such we can safely call restaurant.save() later without worrying about a vulnerable password being stored.
		Logger.info(e.getMessage());
	}
	String hours = "Monday: \nTuesday: \nWednesday: \nThursday: \nFriday: \nSaturday: \nSunday: \n";
	hours = hours.replaceAll("(\r\n|\r|\n|\n\r)","<br>");
	restaurant.hours = hours; 
	restaurant.about = "Created: "+ now();
	restaurant.save();
}

public static void delete(Long id) {
	find.ref(id).delete();
}

  /**
     * Check if a user is the owner of this restaurant
     */
    public static boolean isOwner(Long restaurant, String user) {
        return find.where()
            .eq("owner", user)
            .eq("id", restaurant)
            .findRowCount() > 0;
    }
}