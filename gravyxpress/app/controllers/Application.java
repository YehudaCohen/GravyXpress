package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import models.*;
import views.html.*;

public class Application extends Controller {
	static Form<Restaurant> restaurantForm = Form.form(Restaurant.class);
	
	public static Result show(String restaurant) {

	if (Restaurant.by_name(restaurant) == null){
		return badRequest("Error 404: We could not find the restaurant you requested!");
	}
	else{
		return ok("The restaurant named " + restaurant + " has been successfully created!");
	}

  }

    public static Result index() {
        return ok(views.html.index.render("",restaurantForm));
    }

    public static Result createRestaurant() {
    Form<Restaurant> filledForm = restaurantForm.bindFromRequest();
  		if(filledForm.hasErrors()) {
    	return badRequest(
      		views.html.index.render("", filledForm)
    );
  } else {
    Restaurant.create(filledForm.get());

    DynamicForm data = DynamicForm.form().bindFromRequest();
    String resName = data.get("name");
    return redirect(controllers.routes.Application.show(resName));  
  }
  }  
  
}

