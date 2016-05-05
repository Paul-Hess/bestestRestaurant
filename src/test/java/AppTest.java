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

import static org.assertj.core.api.Assertions.*;

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
      String deleteReviewsQuery = "DELETE FROM reviews *;";
      con.createQuery(deleteCuisinesQuery).executeUpdate();
      con.createQuery(deleteRestaurantsQuery).executeUpdate();
      con.createQuery(deleteReviewsQuery).executeUpdate();
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
  public void viewsReviewTest() {
    Cuisine greasy = new Cuisine("greasy");
    greasy.save();
    Restaurant greezyGrill = new Restaurant("greezyGrill", greasy.getId());
    greezyGrill.save();
    Review testReview = new Review("test review", 4, greezyGrill.getId());
    testReview.save();
    goTo("http://localhost:4567/cuisines/" + greasy.getId() + "/restaurants/" + greezyGrill.getId());
    assertThat(pageSource()).contains("test review");
  }

  @Test
  public void createReviewTest() {
    Cuisine greasy = new Cuisine("greasy");
    greasy.save();
    Restaurant greezyGrill = new Restaurant("greezyGrill", greasy.getId());
    greezyGrill.save();
    goTo("http://localhost:4567/cuisines/" + greasy.getId() + "/restaurants/" + greezyGrill.getId());
    fill("#addReviewBodyIn").with("test review");
    fill("#addReviewRatingIn").with("5");
    submit("button", withText("add review."));
    assertThat(pageSource()).contains("test review");
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
  public void viewRestaurant() {
    Cuisine greasy = new Cuisine("greasy");
    greasy.save();
    Restaurant greezyGrill = new Restaurant("greezyGrill", greasy.getId());
    greezyGrill.save();
    goTo("http://localhost:4567/cuisines/" + greasy.getId() + "/restaurants/" + greezyGrill.getId());
    assertThat(pageSource()).contains("greezyGrill");
  }

  @Test
  public void deleteRestaurants() {
    Cuisine greasy = new Cuisine("greasy");
    greasy.save();
    Restaurant jimmysFishSticks = new Restaurant("jimmysFishSticks", greasy.getId());
    jimmysFishSticks.save();
    goTo("http://localhost:4567/cuisines/" + greasy.getId());
    submit("button", withText("remove"));
    assertThat(pageSource()).contains("greasy");
    assertThat(pageSource()).doesNotContain("jimmysFishSticks");
  }

  @Test
  public void updateRestaurant() {
    Cuisine greasy = new Cuisine("greasy");
    greasy.save();
    Restaurant greezyGrill = new Restaurant("greezyGrill", greasy.getId());
    greezyGrill.save();
    goTo("http://localhost:4567/cuisines/" + greasy.getId() + "/restaurants/" + greezyGrill.getId());
    fill("#changeName").with("jimmysFishSticks");
    submit("button", withText("change"));
    assertThat(pageSource()).contains("jimmysFishSticks");
  }

  @Test
  public void deleteCuisines() {
    Cuisine greasy = new Cuisine("greasy");
    greasy.save();
    goTo("http://localhost:4567/");
    submit("button", withText("remove cuisine and the restaurants that fit it."));
    assertThat(pageSource()).doesNotContain("greasy");
  }

  @Test
  public void updateCuisine() {
    Cuisine greasy = new Cuisine("greasy");
    greasy.save();
    goTo("http://localhost:4567/cuisines/" + greasy.getId());
    fill("#changeCusineName").with("Fish Sticks");
    submit("button", withText("change that cuisine name."));
    assertThat(pageSource()).contains("Fish Sticks");
  }

}
