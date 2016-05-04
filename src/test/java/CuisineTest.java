
import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

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
  public void getName_returnsCuisineName_true(){
    Cuisine myCuisine = new Cuisine("greasy");
    assertEquals(myCuisine.getName(), "greasy");
  }
}
