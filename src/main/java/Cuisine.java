import org.sql2o.*;
import java.sql.Timestamp;

public class Cuisine {

  private int id;
  private String cuisineName;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  public Cuisine(String cuisineName) {
    this.cuisineName = cuisineName;
  }

  public String getName() {
    return cuisineName;
  }
}
