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

      model.put("template", "templates/home.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }

}
