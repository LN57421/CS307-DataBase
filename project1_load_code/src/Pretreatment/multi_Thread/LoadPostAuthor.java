package Pretreatment.multi_Thread;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class LoadPostAuthor implements Runnable{

  List<Post> posts;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public static synchronized void count(){
    LoadPostAuthor.cnt++;
  }

  public LoadPostAuthor(List<Post> posts, Connection con)
      throws SQLException {
    this.posts = posts;
    stmtInsert = con.prepareStatement("INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (?,?,?,?);");
    this.con = con;
  }

  @Override
  public void run() {
    posts.forEach(post -> {
      try {
        stmtInsert.setString(1, post.getAuthorID());
        stmtInsert.setString(2, post.getAuthor());
        stmtInsert.setTimestamp(3, Timestamp.valueOf(post.getAuthorRegistrationTime()));
        stmtInsert.setLong(4, Long.parseLong(post.getAuthorPhone()));
        stmtInsert.executeUpdate();
        count();
      } catch (SQLException e) {
        System.out.println(e);
      }
    });
    try {
      stmtInsert.close();
      con.commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
