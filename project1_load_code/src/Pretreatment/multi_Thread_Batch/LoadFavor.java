package Pretreatment.multi_Thread_Batch;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoadFavor implements Runnable{

  List<Post> posts;

  Map<String, String> authorAndID;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public static synchronized void count(){
    LoadFavor.cnt++;
  }

  public LoadFavor(List<Post> posts, Map<String, String> authorAndID,
      Connection con) throws SQLException {
    this.posts = posts;
    this.authorAndID = authorAndID;
    this.stmtInsert = con.prepareStatement("INSERT INTO favorite_posts (post_id, favorite_author_id) VALUES (?,?);");
    this.con = con;
  }

  @Override
  public void run() {
    posts.forEach(post -> {
      List<String> authors = post.getAuthorFavorite();
      authors.forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setString(2, authorAndID.get(s));
          stmtInsert.addBatch();
          count();
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
    try {
      stmtInsert.executeBatch();
      con.commit();
      stmtInsert.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
