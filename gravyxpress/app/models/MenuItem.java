package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import java.security.*;
import play.Logger;

import javax.persistence.*;

@Entity
public class MenuItem extends Model {
	@Id
	public Long id;

	@Required
	public String name;

	@Required
	public Float price;

	@Column
	public String description;

	@Required
	public Boolean enabled;

	public static Finder<Long,MenuItem> find = new Finder( Long.class, MenuItem.class );

	public MenuItem(String name, Float price, String description){
		this.name = name;
		this.price = price;
		this.description = description;
		this.enabled = true;
	}

	public String display(){
		return this.name+ "\t\t\t$" + String.valueOf(this.price)+"<br>"+
					this.description+"<br>";
	}
	
}