package NoPretreatment.multi_Thread_Batch;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoadCity implements Runnable{

  int BATCH_SIZE;

  List<Post> posts;

  Map<String, String> cities;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public LoadCity(List<Post> posts, Map<String, String> cities, Connection con, int BATCH_SIZE)
      throws SQLException {
    this.posts = posts;
    this.cities = cities;
    this.stmtInsert = con.prepareStatement("INSERT INTO cities (city_name, city_state) VALUES (?,?)");
    this.con = con;
    this.BATCH_SIZE = BATCH_SIZE;
  }

  public static synchronized void count(){
    LoadCity.cnt++;
  }

  @Override
  public void run() {
    for (Post post: posts) {
      String cityAndNation = post.getPostingCity();
      String nation = cityAndNation.substring(cityAndNation.lastIndexOf(",") + 2);
      String city = cityAndNation.substring(0, cityAndNation.lastIndexOf(","));
      if (!cities.containsKey(city)) {
        try {
          stmtInsert.setString(1, city);
          stmtInsert.setString(2, nation);
          stmtInsert.executeUpdate();
          cities.put(city, nation);
          count();
          if (LoadCity.cnt % BATCH_SIZE == 0){
            stmtInsert.executeBatch();
          }
        } catch (SQLException e) {
          System.out.println(e);
        }
      }
    }
    try {
      if (LoadCity.cnt % BATCH_SIZE != 0){
        stmtInsert.executeBatch();
      }
      con.commit();
      stmtInsert.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
