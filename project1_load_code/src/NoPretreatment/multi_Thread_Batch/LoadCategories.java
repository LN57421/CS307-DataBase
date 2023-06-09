package NoPretreatment.multi_Thread_Batch;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoadCategories implements Runnable {

  int BATCH_SIZE;

  List<Post> posts;

  Map<String, Integer> categories;

  PreparedStatement stmtInsert;

  Connection con;
  static int cnt = 0;

  public LoadCategories(List<Post> posts, Map<String, Integer> categories, Connection con, int BATCH_SIZE)
      throws SQLException {
    this.posts = posts;
    this.categories = categories;
    this.stmtInsert = con.prepareStatement(
        "INSERT INTO categories (category_id, category_name) VALUES (?,?)");
    this.con = con;
    this.BATCH_SIZE = BATCH_SIZE;
  }

  public static synchronized void count() {
    LoadCategories.cnt++;
  }

  @Override
  public void run() {
    posts.forEach(post -> {
      List<String> category = post.getCategory();
      category.forEach(s -> {
        if (!categories.containsKey(s)) {
          categories.put(s, categories.size() + 1);
          try {
            stmtInsert.setInt(1, categories.size());
            stmtInsert.setString(2, s);
            stmtInsert.executeUpdate();
            count();
            if (LoadCategories.cnt % BATCH_SIZE == 0) {
              stmtInsert.executeBatch();
            }
          } catch (SQLException e) {
            System.out.println(e);
          }
        }
      });
    });
    try {
      if (LoadCategories.cnt % BATCH_SIZE != 0) {
        stmtInsert.executeBatch();
      }
      con.commit();
      stmtInsert.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
