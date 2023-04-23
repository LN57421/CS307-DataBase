package NoPretreatment.multi_Thread_Batch;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoadFavor implements Runnable{

  int BATCH_SIZE;

  List<Post> posts;

  Map<String, String> authorAndID;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public static synchronized void count(){
    LoadFavor.cnt++;
  }

  public LoadFavor(List<Post> posts, Map<String, String> authorAndID,
      Connection con, int BATCH_SIZE) throws SQLException {
    this.posts = posts;
    this.authorAndID = authorAndID;
    this.stmtInsert = con.prepareStatement("INSERT INTO favorite_posts (post_id, favorite_author_id) VALUES (?,?);");
    this.con = con;
    this.BATCH_SIZE = BATCH_SIZE;
  }

  @Override
  public void run() {
    posts.forEach(post -> {
      List<String> authors = post.getAuthorFavorite();
      authors.forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setString(2, authorAndID.get(s));
          stmtInsert.executeUpdate();
          count();
          if (LoadFavor.cnt % BATCH_SIZE == 0){
            stmtInsert.executeBatch();
          }
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
    try {
      if (LoadFavor.cnt % BATCH_SIZE != 0){
        stmtInsert.executeBatch();
      }
      con.commit();
      stmtInsert.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
