package multi_Thread;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoadPostCategories implements Runnable{

  List<Post> posts;

  Map<String, Integer> categories;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public LoadPostCategories(List<Post> posts, Map<String, Integer> categories, Connection con)
      throws SQLException {
    this.posts = posts;
    this.categories = categories;
    this.stmtInsert = con.prepareStatement("INSERT INTO post_categories (post_id, category_id) VALUES (?,?);");
    this.con = con;
  }

  public static synchronized void count(){
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
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
    try {
      con.commit();
      stmtInsert.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
