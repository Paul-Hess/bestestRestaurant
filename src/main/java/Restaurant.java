import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Restaurant {

  private int id;
  private String restaurantName;
  private int rating;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private int cuisineId;

  public Restaurant(String restaurantName, int cuisineId) {
    this.restaurantName = restaurantName;
    this.cuisineId = cuisineId;
    this.rating = 0;
    createdAt = new Timestamp(new Date().getTime());
    updatedAt = new Timestamp(new Date().getTime());
  }

  public void save(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO restaurants ( restaurantName, rating, createdAt, updatedAt,  cuisineId) VALUES ( :restaurantName, :rating, :createdAt, :updatedAt, :cuisineId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("restaurantName", restaurantName)
        .addParameter("rating", rating)
        .addParameter("createdAt", createdAt)
        .addParameter("updatedAt", updatedAt)
        .addParameter("cuisineId", cuisineId)
        .executeUpdate()
        .getKey();
    }
  }

  public static Restaurant findByName(String search){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM restaurants WHERE restaurantName=:restaurantName";
      return con.createQuery(sql)
        .addParameter("restaurantName", search)
        .executeAndFetchFirst(Restaurant.class);
    }
  }

  public static Restaurant findById(int id){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM restaurants WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Restaurant.class);
    }
  }

  public static List<Restaurant> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id, restaurantName, createdAt, updatedAt, cuisineId FROM restaurants";
     return con.createQuery(sql).executeAndFetch(Restaurant.class);
    }
  }

  @Override
  public boolean equals(Object otherRestaurant) {
    if(!(otherRestaurant instanceof Restaurant)) {
      return false;
    } else {
      Restaurant newRestaurant = (Restaurant) otherRestaurant;
      return this.getName().equals(newRestaurant.getName()) &&
      this.getId() == newRestaurant.getId() &&
      this.getCuisineId() == newRestaurant.getCuisineId();
    }
  }

  //UPDATE update()
  public void updateName(String newName){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE restaurants SET restaurantName = :restaurantName WHERE id=:id";
       con.createQuery(sql)
        .addParameter("id", id)
        .addParameter("restaurantName", newName)
        .executeUpdate();
    }
  }


  //DELETE remove()
  public void remove() {
    try( Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM restaurants WHERE id=:id";
      con.createQuery(sql).addParameter("id", id).executeUpdate();
    }
  }

  public int getRating(){
    return rating;
  }

  public int getCuisineId() {
    return cuisineId;
  }

  public int getId(){
    return id;
  }

  public String getName() {
    return restaurantName;
  }

  public Timestamp getUpdatedAt(){
    return updatedAt;
  }

  public Timestamp getCreatedAt(){
    return createdAt;
  }

  // future implementation will set this as average rating from reviews associated with this restaurant.
  public void setAverageRating(int newRating) {
    this.rating = newRating;
  }
}
