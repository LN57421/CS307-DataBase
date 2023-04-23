package Pretreatment.multi_Thread;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoadCity implements Runnable{

  List<Post> posts;

  Map<String, String> cities;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public LoadCity(List<Post> posts, Map<String, String> cities, Connection con)
      throws SQLException {
    this.posts = posts;
    this.cities = cities;
    this.stmtInsert = con.prepareStatement("INSERT INTO cities (city_name, city_state) VALUES (?,?)");
    this.con = con;
  }

  public static synchronized void count(){
    LoadCity.cnt++;
  }

  @Override
  public void run() {
    for (String key : cities.keySet()){
      try {
        stmtInsert.setString(1, key);
        stmtInsert.setString(2, cities.get(key));
        stmtInsert.executeUpdate();
        count();
      } catch (SQLException e) {
        System.out.println(e);
      }
    }
    try {
      con.commit();
      stmtInsert.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
