import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.util.Date;

public class ReviewTest {

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


  @Test
  public void equals_returnsTrueIfReviewsAretheSame() {
    Review firstReview = new Review("test review", 2, 1);
    Review secondReview = new Review("test review", 2, 1);
    assertTrue(firstReview.equals(secondReview));
  }

  @Test
  public void Review_instantiatesCorrectly() {
      Review testReview = new Review("this a review", 4, 1);
      assertTrue(testReview instanceof Review);
  }

  @Test
  public void getReviewId_int() {
    Review testReview = new Review("this a review", 4, 1);
    assertEquals(testReview.getId(), 0);
  }

  @Test
  public void getRestaurantIdTest_int() {
    Review testReview = new Review("this a review", 4, 1);
    assertEquals(testReview.getRestaurantId(), 1);
  }

  @Test
  public void getReviewBodyTest_String() {
    Review testReview = new Review("this a review", 4, 1);
    assertEquals(testReview.getReviewBody(), "this a review");
  }

  @Test
  public void getRatingTest_int() {
    Review testReview = new Review("this a review", 4, 1);
    assertEquals(testReview.getReviewRating(), 4);
  }

  @Test
  public void all_returnsAllReviews_review() {
    Review testReview = new Review("this a review", 4, 1);
    testReview.save();
    assertEquals(Review.all().size(), 1);
  }

  @Test
  public void save_returnsTrueIfDescriptionsAretheSame_Review() {
    Review testReview = new Review("greasy", 0, 1);
    testReview.save();
    assertTrue(Review.all().get(0).equals(testReview));
  }

  @Test
  public void findById_returnsCorrectReviewSearchedFor_Review() {
    Review testReview = new Review("greasy", 0, 1);
    testReview.save();
    assertEquals(Review.findById(testReview.getId()), testReview);
  }


}
