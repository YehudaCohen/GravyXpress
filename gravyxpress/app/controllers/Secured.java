package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

public class Secured extends Security.Authenticator {
    /*
        The getUsername method in Security.Authenticator is overridden, so that instead of returning the username cookie
        it returns the owner cookie.
    */
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("owner");
    }
    
    /*
    onUnauthorized overrides the native onUnauthorized and demands a login rather than simply returning a forbidden.
    */
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }
    
    // Access rights


/*
isMemberOf is for future use testing whether employees belong to a particular restaurant.
For now it is idle, and is a similar version of isOwnerOf found below
*/
  /*  public static boolean isMemberOf(Long restaurant) {
        return Project.isMember(
            restaurant,
            Context.current().request().username()
        );
    }
    */

    /*
    isOwnerOf returns true if the logged in user is an owner of the restaurant.
    This can be used by controller objects when testing permissions.
    */
    public static boolean isOwnerOf(Long restaurant) {
        return Restaurant.isOwner(
            restaurant,
            Context.current().request().username()
        );
    }
    
}