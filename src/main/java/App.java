import org.sql2o.*;
import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (req, res) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      if(Cuisine.all().size() > 0) {
        model.put("cuisines", Cuisine.all());
      }
      model.put("template", "templates/home.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/", (req, res) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String cuisineName = req.queryParams("cuisine-name");
      Cuisine newCuisine = new Cuisine(cuisineName);
      newCuisine.save();
      if(Cuisine.all().size() > 0) {
        model.put("cuisines", Cuisine.all());
      }
      model.put("template", "templates/home.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisines/:cuisine_id", (req, res) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(req.params("cuisine_id"));
      Cuisine currentCuisine = Cuisine.find(id);
      model.put("cuisine", currentCuisine);
      if(currentCuisine.restaurantList().size() > 0) {
        model.put("restaurants", currentCuisine.restaurantList());
      }
      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines/:cuisine_id",(req, res) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(req.params("cuisine_id"));
      Cuisine currentCuisine = Cuisine.find(id);
      String restoName = req.queryParams("restaurant-name");
      Restaurant newRestaurant = new Restaurant(restoName, id);
      newRestaurant.save();
      model.put("restaurants", currentCuisine.restaurantList());
      model.put("cuisine", currentCuisine);
      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisines/:cuisine_id/restaurants/:restaurant_id", (req, res) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(req.params("restaurant_id"));
      Restaurant currentRestaurant = Restaurant.findById(id);
      model.put("restaurant", currentRestaurant);
      model.put("template", "templates/restaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines/restaurant/:restaurant_Id/delete",(req, res) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(req.params("restaurant_id"));
      Restaurant currentRestaurant = Restaurant.findById(id);
      int thisRestrauntsCuisine = currentRestaurant.getCuisineId();
      currentRestaurant.remove();
      res.redirect("/cuisines/" + thisRestrauntsCuisine);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/restaurant/:restaurant_id/edit",(req, res) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(req.params("restaurant_id"));
      Restaurant currentRestaurant = Restaurant.findById(id);
      int thisRestrauntsCuisine = currentRestaurant.getCuisineId();
      String newName = req.queryParams("changeName");
      currentRestaurant.update("restaurantName", newName);

      res.redirect(String.format("/cuisines/%s/restaurants/%s", thisRestrauntsCuisine, id));
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines/:cuisine_id/delete",(req, res) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(req.params("cuisine_id"));
      Cuisine currentCuisine = Cuisine.find(id);
      currentCuisine.remove();
      res.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines/:cuisine_id/edit",(req, res) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(req.params("cuisine_id"));
      Cuisine currentCuisine = Cuisine.find(id);


      String newName = req.queryParams("changeCusineName");
      currentCuisine.update("cuisineName", newName);

      res.redirect(String.format("/cuisines/%s", id));
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
