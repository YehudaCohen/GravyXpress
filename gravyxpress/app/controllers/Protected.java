package controllers;

import play.*;
import play.mvc.*;

import play.data.*;
import models.*;
import views.html.*;


@Security.Authenticated(Secured.class)
public class Protected extends Controller {
	public static Result dashboard(String id){
		if(Secured.isOwnerOf(Long.valueOf(id))) {
			try{
				Form<Restaurant> myRestaurantForm = Application.restaurantForm.fill(Restaurant.find.byId(Long.valueOf(id)));
				return ok(views.html.dashboard.render(myRestaurantForm));
			}
	    	catch(RuntimeException e){
	    		return TODO;
	    	}
	    }
	    else{
	    	return forbidden("You do not have permission to use this resource!");
	    }
    	
  	}

  public static Result updateAbout(String id){
  	if(Secured.isOwnerOf(Long.valueOf(id))) {
	    DynamicForm requestForm = DynamicForm.form().bindFromRequest();
	    String about = requestForm.get("about");
	    Restaurant myRestaurant = Restaurant.find.byId(Long.valueOf(id));
	    myRestaurant.about = about;
	    myRestaurant.save();

	    return redirect(controllers.routes.Protected.dashboard(id));
  	}
  	else{
  		return forbidden("You do not have permission to use this resource!");
  	}
  }

}
