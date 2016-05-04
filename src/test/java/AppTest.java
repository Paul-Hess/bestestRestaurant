import org.sql2o.*;
import org.fluentlenium.adapter.FluentTest;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }


  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/best_restaurant_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteCuisinesQuery = "DELETE FROM cuisines *;";
      String deleteRestaurantsQuery = "DELETE FROM restaurants *;";
      con.createQuery(deleteCuisinesQuery).executeUpdate();
      con.createQuery(deleteRestaurantsQuery).executeUpdate();
    }
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("bestestRest ...aurants!");
  }

  @Test
  public void createCuisine() {
    goTo("http://localhost:4567/");
    fill("#cuisine-name").with("greasy");
    submit("#cuisine-btn");
    assertThat(pageSource()).contains("greasy");
  }

  @Test
  public void viewCuisine() {
    goTo("http://localhost:4567/");
    fill("#cuisine-name").with("greasy");
    submit("#cuisine-btn");
    click("a", withText("greasy"));
    assertThat(pageSource()).contains("greasy restaurants:");
  }

  @Test
  public void listRestaurants() {
    Cuisine greasy = new Cuisine("greasy");
    greasy.save();
    Restaurant greezyGrill = new Restaurant("greezyGrill", greasy.getId());
    greezyGrill.save();
    goTo("http://localhost:4567/cuisines/" + greasy.getId());
    assertThat(pageSource()).contains("greezyGrill");
  }

  @Test
  public void viewRestaurants() {
    Cuisine greasy = new Cuisine("greasy");
    greasy.save();
    Restaurant greezyGrill = new Restaurant("greezyGrill", greasy.getId());
    greezyGrill.save();
    goTo("http://localhost:4567/cuisines/" + greasy.getId() + "/restaurants/" + greezyGrill.getId());
    assertThat(pageSource()).contains("greezyGrill");
  }


}
