package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import java.security.*;
import play.Logger;

import javax.persistence.*;

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
	public String about;

public static Finder<Long,Restaurant> find = new Finder( Long.class, Restaurant.class );


public static Restaurant by_name(String name) {
	List<Restaurant> matches =  Restaurant.find.where().eq("name", name).findList();
	try{
		return matches.get(0);
}
	catch (IndexOutOfBoundsException e){
		 return null;
	}
}

public static Restaurant by_owner(String owner) {
	List<Restaurant> matches = Restaurant.find.where().eq("owner", owner).findList();
	try{
		return matches.get(0);
	}
	catch (IndexOutOfBoundsException e){
		 return null;
	}
}

public static String hashpw(String password) throws NoSuchAlgorithmException{
	MessageDigest pwhasher = MessageDigest.getInstance("SHA-256");
	pwhasher.update(password.getBytes());
	byte [] pwhash = pwhasher.digest();
	return new String(pwhash);
	}


public static void create(Restaurant restaurant){
	try{
		restaurant.password = Restaurant.hashpw(restaurant.password);
	}
	catch (NoSuchAlgorithmException e){
		Logger.info(e.getMessage());
	}
	restaurant.about = "";
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