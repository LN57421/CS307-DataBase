package NoPretreatment.multi_Thread_Batch;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoadPostCategories implements Runnable {

  int BATCH_SIZE;

  List<Post> posts;

  Map<String, Integer> categories;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public LoadPostCategories(List<Post> posts, Map<String, Integer> categories, Connection con,
      int BATCH_SIZE)
      throws SQLException {
    this.posts = posts;
    this.categories = categories;
    this.stmtInsert = con.prepareStatement(
        "INSERT INTO post_categories (post_id, category_id) VALUES (?,?);");
    this.con = con;
    this.BATCH_SIZE = BATCH_SIZE;
  }

  public static synchronized void count() {
    LoadPostCategories.cnt++;
  }

  @Override
  public void run() {
    posts.forEach(post -> {
      post.getCategory().forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setInt(2, categories.get(s));
          stmtInsert.executeUpdate();
          count();
          if (LoadPostCategories.cnt % BATCH_SIZE == 0){
            stmtInsert.executeBatch();
          }
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
    try {
      if (LoadPostCategories.cnt % BATCH_SIZE != 0){
        stmtInsert.executeBatch();
      }
      con.commit();
      stmtInsert.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
