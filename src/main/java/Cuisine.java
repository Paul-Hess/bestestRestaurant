import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Cuisine {

  private int id;
  private String cuisineName;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  public Cuisine(String cuisineName) {
    this.cuisineName = cuisineName;
    createdAt = new Timestamp(new Date().getTime());
    updatedAt = new Timestamp(new Date().getTime());
  }

  public void save(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO cuisines ( cuisineName, createdAt, updatedAt) VALUES ( :cuisineName, :createdAt, :updatedAt)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("cuisineName", cuisineName)
        .addParameter("createdAt", createdAt)
        .addParameter("updatedAt", updatedAt)
        .executeUpdate()
        .getKey();
    }
  }

  @Override
  public boolean equals(Object otherCuisine) {
    if(!(otherCuisine instanceof Cuisine)) {
      return false;
    } else {
      Cuisine newCuisine = (Cuisine) otherCuisine;
      return this.getName().equals(newCuisine.getName()) &&
      this.getId() == newCuisine.getId();
    }
  }


  public static Cuisine find(int id){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM cuisines WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Cuisine.class);
    }
  }

  public static List<Cuisine> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id, cuisineName, createdAt, updatedAt FROM cuisines";
     return con.createQuery(sql).executeAndFetch(Cuisine.class);

    }
  }

  public List<Restaurant> restaurantList() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id, restaurantName, createdAt, updatedAt, cuisineId FROM restaurants WHERE cuisineId=:cuisineId";
     return con.createQuery(sql)
        .addParameter("cuisineId", this.id)
        .executeAndFetch(Restaurant.class);
    }
  }


  //UPDATE update()
  public void updateName(String newName){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE cuisines SET cuisineName = :cuisineName WHERE id=:id";
       con.createQuery(sql)
        .addParameter("id", id)
        .addParameter("cuisineName", newName)
        .executeUpdate();
    }
  }


  //DELETE remove()
  public void remove() {
    try( Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM cuisines WHERE id=:id";
      con.createQuery(sql).addParameter("id", id).executeUpdate();
    }
  }

  public int getId(){
    return id;
  }

  public String getName() {
    return cuisineName;
  }

  public Timestamp getUpdatedAt(){
    return updatedAt;
  }

  public Timestamp getCreatedAt(){
    return createdAt;
  }

}
