package controllers;

import play.*;
import play.mvc.*;

import play.data.*;
import models.*;
import views.html.*;

/*
 The Protected class is a controller class that implements all HTTP
requests that require a secure login. This is to protect a stranger from
altering a restaurant manager's restaurant information. 
*/


@Security.Authenticated(Secured.class)
public class Protected extends Controller {

	/* 
	The dashboard request returns the dashboard for a particular restaurant. 
	The restaurant's id is passed in as a parameter and the restaurant is then located and
	passed into a generic dashboard template. The template is rendered.

	Testing the case that a restaurant id did not exist, an error precipitated.
	A try-catch block was implemented to resolve this.

	Replacing the <br> with the \n is necessary when displaying in a textarea like the dashboard uses.
	The owner should not need to write in HTML
	*/
	public static Result dashboard(String id){
		if(Secured.isOwnerOf(Long.valueOf(id))) {
			try{
				Restaurant myRestaurant = Restaurant.find.byId(Long.valueOf(id));
				myRestaurant.about = myRestaurant.about.replace("<br>","\n"); // A method for updating about needs to be added to the Restaurant class.
				myRestaurant.hours = myRestaurant.hours.replace("<br>","\n"); // A method for updating hours needs to be added to the Restaurant class.
				Form<Restaurant> myRestaurantForm = Application.restaurantForm.fill(myRestaurant);
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

  /*
  The updateAbout method updates a restaurant's about field. and stores it to a database.
  It locates the restaurant entry using an ebean query and alters its about field before
  saving the changes to the database.

  It returns a redirect to the dashboard once changes have been made.

  If the cookie is no longer valid on the browser, a forbidden is returned.
  */	
  public static Result updateAbout(String id){
  	if(Secured.isOwnerOf(Long.valueOf(id))) {
	    DynamicForm requestForm = DynamicForm.form().bindFromRequest();
	    String about = requestForm.get("about");
	    about = about.replaceAll("(\r\n|\r|\n|\n\r)", "<br>"); //Saves all newlines as <br> to ease rendering on restaurant's homepage.
	    Restaurant myRestaurant = Restaurant.find.byId(Long.valueOf(id));
	    myRestaurant.about = about;
	    myRestaurant.save();

	    return redirect(controllers.routes.Protected.dashboard(id));
  	}
  	else{
  		return forbidden("You do not have permission to use this resource!");
  	}
  }

/*
Like update about just with the hours field.
*/
public static Result updateHours(String id){
  	if(Secured.isOwnerOf(Long.valueOf(id))) {
	    DynamicForm requestForm = DynamicForm.form().bindFromRequest();
	    String hours = requestForm.get("hours");
	    Restaurant myRestaurant = Restaurant.find.byId(Long.valueOf(id));
	    hours= hours.replaceAll("(\r\n|\r|\n|\n\r)","<br>");
	    myRestaurant.hours = hours;
	    myRestaurant.save();

	    return redirect(controllers.routes.Protected.dashboard(id));
  	}
  	else{
  		return forbidden("You do not have permission to use this resource!");
  	}
  }
}
