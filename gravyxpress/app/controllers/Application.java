package controllers;

import play.*;
import play.mvc.*;

import play.data.*;
import models.*;
import views.html.*;
import java.security.*;

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
      } 
      else {
          Restaurant.create(filledForm.get());

          DynamicForm data = DynamicForm.form().bindFromRequest();
          String resName = data.get("name");
          return redirect(controllers.routes.Application.show(resName));  
        }
    }  


// Authentication

  public static Result authenticate(){
      DynamicForm login = DynamicForm.form().bindFromRequest();
      String owner = login.get("login-owner");
      String password = login.get("login-password");
      Restaurant myRestaurant = Restaurant.by_owner(owner);
      if (myRestaurant != null){
          try {
              password = Restaurant.hashpw(password);
              if (myRestaurant.password.equals(password)){
                  session("owner", owner);
                  return redirect(controllers.routes.Protected.dashboard(String.valueOf(myRestaurant.id)));
              }
              else{
                return badRequest("Your password didn't match! myRestaurant.password= "+myRestaurant.password+
              "  Password= "+password);
                  }

              }
              catch (NoSuchAlgorithmException e){
               Logger.info(e.getMessage());
               return badRequest("Bad hash!");
              }
            }
      else{
              return redirect(controllers.routes.Application.login());
            }
        }
          
  

  public static Result login(){
    return ok(views.html.login.render());
  }

  public static Result logout(){ 
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.Application.index()
        );
  }
}

