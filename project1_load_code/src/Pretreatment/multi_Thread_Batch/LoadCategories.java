package Pretreatment.multi_Thread_Batch;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoadCategories implements Runnable {

  List<Post> posts;

  Map<String, Integer> categories;

  PreparedStatement stmtInsert;

  Connection con;
  static int cnt = 0;

  public LoadCategories(List<Post> posts, Map<String, Integer> categories, Connection con)
      throws SQLException {
    this.posts = posts;
    this.categories = categories;
    this.stmtInsert = con.prepareStatement(
        "INSERT INTO categories (category_id, category_name) VALUES (?,?)");
    this.con = con;
  }

  public static synchronized void count() {
    LoadCategories.cnt++;
  }

  @Override
  public void run() {
    for (String key : categories.keySet()) {
      try {
        stmtInsert.setInt(1, categories.get(key));
        stmtInsert.setString(2, key);
        stmtInsert.addBatch();
        count();
      } catch (SQLException e) {
        System.out.println(e);
      }
    }
    try {
      stmtInsert.executeBatch();
      con.commit();
      stmtInsert.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
