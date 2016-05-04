
import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.util.Date;

public class CuisineTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/best_restaurant_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteCuisinesQuery = "DELETE FROM cuisines *;";
      // String deleteRestaurantsQuery = "DELETE FROM restaurants *;";
      con.createQuery(deleteCuisinesQuery).executeUpdate();
      // con.createQuery(deleteRestaurantsQuery).executeUpdate();
    }
  }

  @Test
  public void Cuisine_instantiatesCorrectly_true(){
    Cuisine myCuisine = new Cuisine("sakdjas");
    assertTrue(myCuisine instanceof Cuisine);
  }

  @Test
  public void getName_returnsCuisineName_greasy(){
    Cuisine myCuisine = new Cuisine("greasy");
    assertEquals(myCuisine.getName(), "greasy");
  }

  @Test
  public void getCreatedAtAndgetUpdatedAt_returnTimestamps_hours(){
    Cuisine myCuisine = new Cuisine("greasy");
    Timestamp testCreatedAt = new Timestamp(new Date().getTime());
    assertEquals(myCuisine.getCreatedAt().getHours(), testCreatedAt.getHours());
    assertEquals(myCuisine.getUpdatedAt().getHours(), testCreatedAt.getHours());
  }

  @Test
  public void save_returnsCuisineName_greasy(){
    Cuisine myCuisine = new Cuisine("greasy");
    assertEquals(myCuisine.getName(), "greasy");
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Cuisine firstCuisine = new Cuisine("greasy");
    Cuisine secondCuisine = new Cuisine("greasy");
    assertTrue(firstCuisine.equals(secondCuisine));
  }

  @Test
  public void save_returnsTrueIfDescriptionsAretheSame_Cuisine() {
    Cuisine testCuisine = new Cuisine("greasy");
    testCuisine.save();
    assertTrue(Cuisine .all().get(0).equals(testCuisine));
  }

  @Test
  public void find_returnsCorrectCuisineSearchedFor_Cuisine() {
    Cuisine testCuisine = new Cuisine("greasy");
    testCuisine.save();
    assertEquals(Cuisine.find(testCuisine.getId()), testCuisine);
  }

}
