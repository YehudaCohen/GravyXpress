package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import java.security.*;
import play.Logger;

import javax.persistence.*;

@Entity
public class Category extends Model {
	@Id
	public Long id;

	@Required 
	public String name;

	@Required
	public Boolean enabled;

	@OneToMany (cascade = CascadeType.ALL)
	public List<MenuItem> items = new ArrayList();
	
	public static Finder<Long,Category> find = new Finder( Long.class, Category.class );
	
	public Category(String name){
		this.name = name;
		this.enabled = true;
	}

	public String display(){
		String disp = "<h3>"+this.name+ "</h3>";
		for (int i = 0; i < this.items.size(); i++){
			MenuItem menuItem = items.get(i);
			if (menuItem.enabled){
			disp += menuItem.display()+"<br>";
			}
		}
		disp += "<br>";
		return disp;

	}


	public void addItem(String name, Float price, String description){
		MenuItem newItem = new MenuItem(name,price,description);
		items.add(newItem);
	}
}