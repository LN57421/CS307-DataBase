package multi_Thread;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class LoadPostContent implements Runnable{

  List<Post> posts;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public LoadPostContent(List<Post> posts, Connection con) throws SQLException {
    this.posts = posts;
    this.stmtInsert = con.prepareStatement("INSERT INTO posts (post_id, author_id, title, content, posting_time, posting_city) VALUES (?,?,?,?,?,?);");
    this.con = con;
  }

  public static synchronized void count(){
    LoadPostContent.cnt++;
  }

  @Override
  public void run() {
    for(Post post : posts) {
      String city = post.getPostingCity().substring(0, post.getPostingCity().lastIndexOf(","));
      try {
        stmtInsert.setInt(1, post.getPostID());
        stmtInsert.setString(2, post.getAuthorID());
        stmtInsert.setString(3, post.getTitle());
        stmtInsert.setString(4, post.getContent());
        stmtInsert.setTimestamp(5, Timestamp.valueOf(post.getPostingTime()));
        stmtInsert.setString(6, city);
        stmtInsert.executeUpdate();
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
