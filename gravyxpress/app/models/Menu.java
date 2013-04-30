package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import java.security.*;
import play.Logger;

import javax.persistence.*;

@Entity
public class Menu extends Model {
	@Id
	public Long id;

	@OneToMany(cascade = CascadeType.ALL)
	public List<Category> categories = new ArrayList();

	public void addCategory(String name){
		Category newCat = new Category(name);
		categories.add(newCat);
	}

		public String display(){
		String disp = "<h2>Menu</h2>";
		for (int i = 0; i < this.categories.size(); i++){
			Category cat = categories.get(i);
			if (cat.enabled){
			disp += cat.display();
			}
		}
		disp += "<br>";
		return disp;

	}

}