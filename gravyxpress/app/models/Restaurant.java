package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

@Entity
public class Restaurant extends Model {

	@Id
	public Long id;

	@Required
	public String name;

	@Required
	public String owner;


public static Finder<Long,Restaurant> find = new Finder( Long.class, Restaurant.class );

public static Restaurant by_name(String name) {
	List<Restaurant> matches = Restaurant.find.where().eq("name", name).findList();
	try{
	return matches.get(0);
}
	catch (IndexOutOfBoundsException e){
		 return null;
	}
}

public static void create(Restaurant restaurant){
	restaurant.save();
}

public static void delete(Long id) {
	find.ref(id).delete();
}

}