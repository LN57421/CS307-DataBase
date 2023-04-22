package multi_Thread;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoadFollowedAuthors implements Runnable{

  List<Post> posts;

  Map<String, String> authorAndID;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public static synchronized void count(){
    LoadPostAuthor.cnt++;
  }

  public LoadFollowedAuthors(List<Post> posts, Map<String, String> authorAndID,
      Connection con) throws SQLException {
    this.posts = posts;
    this.authorAndID = authorAndID;
    this.stmtInsert = con.prepareStatement("INSERT INTO followed_authors (author_id, follower_author_id) VALUES (?,?);");
    this.con = con;
  }
  @Override
  public void run() {
    posts.forEach(post -> {
      List<String> authors = post.getAuthorFollowedBy();
      authors.forEach(s -> {
        try {
          stmtInsert.setString(1, authorAndID.get(post.getAuthor()));
          stmtInsert.setString(2, authorAndID.get(s));
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
