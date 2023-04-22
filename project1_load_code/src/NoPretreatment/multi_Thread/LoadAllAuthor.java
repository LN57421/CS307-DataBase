package NoPretreatment.multi_Thread;

import Data.Post;
import Data.Replies;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LoadAllAuthor implements Runnable {

  List<Post> posts;

  List<Replies> replies;

  Map<String, String> authorAndID;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public LoadAllAuthor(List<Post> posts, Map<String, String> authorAndID, List<Replies> replies,
      Connection con) throws SQLException {
    this.posts = posts;
    this.replies = replies;
    this.authorAndID = authorAndID;
    this.stmtInsert = stmtInsert;
    this.stmtInsert = con.prepareStatement(
        "INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (?,?,?,?);");
    this.con = con;
  }

  public static synchronized void count(){
    LoadAllAuthor.cnt++;
  }

  @Override
  public void run() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Random random = new Random();
    List<String> authors = new ArrayList<>();
    posts.forEach(post -> {
      authors.addAll(post.getAuthorFollowedBy());
      authors.addAll(post.getAuthorLiked());
      authors.addAll(post.getAuthorFavorite());
      authors.addAll(post.getAuthorShared());
    });
    replies.forEach(replies1 -> {
      authors.add(replies1.getReplyAuthor());
      authors.add(replies1.getSecondaryReplyAuthor());
    });
    authors.forEach(s -> {
      if (!authorAndID.containsKey(s)) {
        Long ID = random.nextLong(Long.MAX_VALUE);
        while (authorAndID.containsValue(String.valueOf(ID))) {
          ID = random.nextLong(Long.MAX_VALUE);
        }
        Long time = System.currentTimeMillis() - random.nextLong(100000000);
        String timeF = format.format(time);
        authorAndID.put(s, String.valueOf(ID));
        try {
          stmtInsert.setLong(1, ID);
          stmtInsert.setString(2, s);
          stmtInsert.setTimestamp(3, Timestamp.valueOf(timeF));
          stmtInsert.setNull(4, Types.BIGINT);
          stmtInsert.executeUpdate();
          count();
        } catch (SQLException e) {
          System.out.println(e);
        }
      }
    });
    try {
      con.commit();
      stmtInsert.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
