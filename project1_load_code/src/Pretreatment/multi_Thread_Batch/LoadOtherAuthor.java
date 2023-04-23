package Pretreatment.multi_Thread_Batch;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LoadOtherAuthor implements Runnable {

  int BATCH_SIZE;

  List<Post> posts;

  Map<String, String> authorAndID;

  PreparedStatement stmtInsert;

  Connection con;

  static int cnt = 0;

  public LoadOtherAuthor(List<Post> posts, Map<String, String> authorAndID,
      Connection con,int BATCH_SIZE) throws SQLException {
    this.posts = posts;
    this.authorAndID = authorAndID;
    this.stmtInsert = stmtInsert;
    this.stmtInsert = con.prepareStatement(
        "INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (?,?,?,?);");
    this.con = con;
    this.BATCH_SIZE = BATCH_SIZE;
  }

  public static synchronized void count(){
    LoadOtherAuthor.cnt++;
  }

  @Override
  public void run() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Random random = new Random();
    authorAndID.forEach((s, id) -> {
      Long time = System.currentTimeMillis() - random.nextLong(100000000);
      String timeF = format.format(time);
      try {
        stmtInsert.setString(1, id);
        stmtInsert.setString(2, s);
        stmtInsert.setTimestamp(3, Timestamp.valueOf(timeF));
        stmtInsert.setNull(4, Types.BIGINT);
        stmtInsert.executeUpdate();
        count();
        if (LoadOtherAuthor.cnt % BATCH_SIZE == 0){
          stmtInsert.executeBatch();
        }
      } catch (SQLException e) {
        System.out.println(e);
      }
    });
    try {
      if (LoadOtherAuthor.cnt % BATCH_SIZE != 0){
        stmtInsert.executeBatch();
      }
      stmtInsert.close();
      con.commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
