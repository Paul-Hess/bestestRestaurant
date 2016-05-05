import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Review {

  private int id;
  private String reviewBody;
  private int reviewRating;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private int restaurantId;

  public Review(String reviewBody, int reviewRating, int restaurantId){
    this.reviewBody =  reviewBody;
    this.reviewRating = reviewRating;
    createdAt = new Timestamp(new Date().getTime());
    updatedAt = new Timestamp(new Date().getTime());
    this.restaurantId = restaurantId;
  }

  public void save(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO reviews( reviewBody, reviewRating, createdAt, updatedAt, restaurantId) VALUES (:reviewBody, :reviewRating, :createdAt, :updatedAt, :restaurantId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("reviewBody", reviewBody)
        .addParameter("reviewRating", reviewRating)
        .addParameter("createdAt", createdAt)
        .addParameter("updatedAt", updatedAt)
        .addParameter("restaurantId", restaurantId)
        .executeUpdate()
        .getKey();
    }
  }

  public static Review findById(int id){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM reviews WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Review.class);
    }
  }

  public static Review findByRestaurantId(int id){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM reviews WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Review.class);
    }
  }

  public static List<Review> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM reviews";
      return con.createQuery(sql)
        .executeAndFetch(Review.class);
    }
  }

  @Override
  public boolean equals(Object otherReview) {
    if(!(otherReview instanceof Review)) {
      return false;
    } else {
      Review newReview = (Review) otherReview;
      return this.getReviewBody().equals(newReview.getReviewBody()) &&
      this.getId() == newReview.getId() &&
      this.getRestaurantId() == newReview.getRestaurantId();
    }
  }

  public int getId(){
    return this.id;
  }

  public String getReviewBody(){
    return this.reviewBody;
  }

  public int getReviewRating(){
    return this.reviewRating;
  }

  public Timestamp getUpdatedAt(){
    return updatedAt;
  }

  public Timestamp getCreatedAt(){
    return createdAt;
  }

  public int getRestaurantId(){
    return this.restaurantId;
  }
}
