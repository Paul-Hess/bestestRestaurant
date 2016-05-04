import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.util.Date;

public class RestaurantTest {

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

  @Test
  public void Restaurant_instantiatesCorrectly_true(){
    Restaurant myRestaurant = new Restaurant("sakdjas", 0);
    assertTrue(myRestaurant instanceof Restaurant);
  }

  @Test
  public void getName_returnsRestaurantName_greasy(){
    Restaurant myRestaurant = new Restaurant("greasy", 0);
    assertEquals(myRestaurant.getName(), "greasy");
  }

  @Test
  public void getRating_returnsRating_0(){
    Restaurant myRestaurant = new Restaurant("greasy", 0);
    assertEquals(myRestaurant.getRating(), 0);
  }

  @Test
  public void getCreatedAtAndgetUpdatedAt_returnTimestamps_hours(){
    Restaurant myRestaurant = new Restaurant("greasy", 0);
    Timestamp testCreatedAt = new Timestamp(new Date().getTime());
    assertEquals(myRestaurant.getCreatedAt().getHours(), testCreatedAt.getHours());
    assertEquals(myRestaurant.getUpdatedAt().getHours(), testCreatedAt.getHours());
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Restaurant firstRestaurant = new Restaurant("greasy", 0);
    Restaurant secondRestaurant = new Restaurant("greasy", 0);
    assertTrue(firstRestaurant.equals(secondRestaurant));
  }

  @Test
  public void save_returnsTrueIfDescriptionsAretheSame_Restaurant() {
    Restaurant testRestaurant = new Restaurant("greasy", 0);
    testRestaurant.save();
    assertTrue(Restaurant.all().get(0).equals(testRestaurant));
  }

  @Test
  public void findById_returnsCorrectRestaurantSearchedFor_Restaurant() {
    Restaurant testRestaurant = new Restaurant("greasy", 0);
    testRestaurant.save();
    assertEquals(Restaurant.findById(testRestaurant.getId()), testRestaurant);
  }

  @Test
  public void findByName_returnsCorrectRestaurantSearchedFor_Restaurant() {
    Restaurant testRestaurant = new Restaurant("greasy", 0);
    testRestaurant.save();
    assertEquals(Restaurant.findByName(testRestaurant.getName()), testRestaurant);
  }

}