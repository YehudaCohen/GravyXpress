package controllers;

import play.*;
import play.mvc.*;

import play.data.*;
import models.*;
import views.html.*;
import java.security.*;

// This controller class contains the main request handlers. Request handlers requiring user authentication
// are a part of Protected controller.
public class Application extends Controller {


	static Form<Restaurant> restaurantForm = Form.form(Restaurant.class);
	
  /*
  Tests whether passed in restaurant exists. If it does returns a 200 and renders the restaurant's home page
  otherwise, returns a 404.
  */
	public static Result show(String restaurant) {
    Restaurant myRestaurant = Restaurant.by_name(restaurant);
	if (myRestaurant == null){
		  return badRequest("Error 404: We could not find the restaurant you requested!");
	 }
	 else{
		  return ok(views.html.restauranthome.render(myRestaurant));
	 }

  }


    /*
    displays the index page
    */
    public static Result index() {
        return ok(views.html.index.render("",restaurantForm));
      }

      /*
      Index binds restaurant form from Request, determines whether errors exist in the newly bound
      object. If they do, a bad request is returned and the index page renders again.

      Otherwise, a new restaurant is created. The relevant restaurant name is retrieved and the user
      is redirected to the restaurant's home page.
      */
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
/*
Binds the login form from HTTP request. Locates the relevant restaurant.
Compares hash of password from login form to hash stored in database.
If equal, stores a cookie in the web browser of client with a hash
of the owner of the restaurant. Then redirects to relevant
dashboard Result in Protected controller.

If passwords don't match, return bad request.

redirects to login page if login fails.
*/
  public static Result authenticate(){
      DynamicForm login = DynamicForm.form().bindFromRequest();
      String owner = login.get("login-owner");
      String password = login.get("login-password");
      Restaurant myRestaurant = Restaurant.by_owner(owner);
      if (myRestaurant != null){
          try { // this entire block should be moved into a function down the line. I shouldn't need to handle this
          //exception every time.
              password = Restaurant.hashpw(password);
              if (myRestaurant.password.equals(password)){
                  session("owner", owner);
                  return redirect(controllers.routes.Protected.dashboard(String.valueOf(myRestaurant.id)));
              }
              else{
                return badRequest("Your password didn't match! myRestaurant.password= "+myRestaurant.password+
              "  Password= "+password); // Displays hashes of passwords for testing purposes.
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
          
  
// renders login page
  public static Result login(){
    return ok(views.html.login.render());
  }

// clears cookies in browser and redirects to index page.
  public static Result logout(){ 
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.Application.index()
        );
  }
}

