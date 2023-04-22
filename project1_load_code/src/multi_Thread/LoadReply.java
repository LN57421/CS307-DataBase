package multi_Thread;

import Data.Replies;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoadReply implements Runnable{

  int replySize;

  int secondReplySize;

  List<Replies> replies;

  Map<String, String> authorAndID;

  PreparedStatement stmtInsert1;

  PreparedStatement stmtInsert2;

  Connection con;

  static int cnt = 0;

  public static synchronized void count(){
    LoadPostAuthor.cnt++;
  }

  public LoadReply(int replySize, int secondReplySize, List<Replies> replies,
      Map<String, String> authorAndID, Connection con) throws SQLException {
    this.replySize = replySize;
    this.secondReplySize = secondReplySize;
    this.replies = replies;
    this.authorAndID = authorAndID;
    this.stmtInsert1 = con.prepareStatement("INSERT INTO replies (reply_id, content, stars, author_id, post_id) VALUES (?,?,?,?,?);");
    this.stmtInsert2 = con.prepareStatement("INSERT INTO secondary_replies (secondary_reply_id, content, stars, author_id, reply_id) VALUES (?,?,?,?,?);");
    this.con = con;
  }

  @Override
  public void run() {
    int postId = replies.get(0).getPostID();
    String comparedReply = replies.get(0).getReplyAuthor() + replies.get(0).getReplyContent() + replies.get(0)
        .getReplyStars();
    try {
      stmtInsert1.setInt(1, replySize + 1);
      stmtInsert1.setString(2, replies.get(0).getReplyContent());
      stmtInsert1.setInt(3, replies.get(0).getReplyStars());
      stmtInsert1.setString(4, authorAndID.get(replies.get(0).getReplyAuthor()));
      stmtInsert1.setInt(5, postId);
      stmtInsert1.executeUpdate();
      replySize += 1;
      count();
    } catch (SQLException e) {
      System.out.println(e);
    }
    for (Replies replies1 : replies) {
      String singleReply = replies1.getReplyAuthor() + replies1.getReplyContent() + replies1.getReplyStars();
      if (postId == replies1.getPostID()) {
        if (singleReply.equals(comparedReply)) {
          try {
            stmtInsert2.setInt(1, secondReplySize + 1);
            stmtInsert2.setString(2, replies1.getSecondaryReplyContent());
            stmtInsert2.setInt(3, replies1.getSecondaryReplyStars());
            stmtInsert2.setString(4, authorAndID.get(replies1.getSecondaryReplyAuthor()));
            stmtInsert2.setInt(5, replySize);
            stmtInsert2.executeUpdate();
            secondReplySize += 1;
            count();
          } catch (SQLException e) {
            System.out.println(e);
          }
        } else {
          comparedReply = singleReply;
          try {
            stmtInsert1.setInt(1, replySize + 1);
            stmtInsert1.setString(2, replies1.getReplyContent());
            stmtInsert1.setInt(3, replies1.getReplyStars());
            stmtInsert1.setString(4, authorAndID.get(replies1.getReplyAuthor()));
            stmtInsert1.setInt(5, postId);
            stmtInsert1.executeUpdate();
            count();
            stmtInsert2.setInt(1, secondReplySize + 1);
            stmtInsert2.setString(2, replies1.getSecondaryReplyContent());
            stmtInsert2.setInt(3, replies1.getSecondaryReplyStars());
            stmtInsert2.setString(4, authorAndID.get(replies1.getSecondaryReplyAuthor()));
            stmtInsert2.setInt(5, replySize + 1);
            stmtInsert2.executeUpdate();
            secondReplySize += 1;
            replySize += 1;
            count();
          } catch (SQLException e) {
            System.out.println(e);
          }
        }
      } else {
        postId = replies1.getPostID();
        comparedReply = singleReply;
        try {
          stmtInsert1.setInt(1, replySize + 1);
          stmtInsert1.setString(2, replies1.getReplyContent());
          stmtInsert1.setInt(3, replies1.getReplyStars());
          stmtInsert1.setString(4, authorAndID.get(replies1.getReplyAuthor()));
          stmtInsert1.setInt(5, postId);
          stmtInsert1.executeUpdate();
          count();
          stmtInsert2.setInt(1, secondReplySize + 1);
          stmtInsert2.setString(2, replies1.getSecondaryReplyContent());
          stmtInsert2.setInt(3, replies1.getSecondaryReplyStars());
          stmtInsert2.setString(4, authorAndID.get(replies1.getSecondaryReplyAuthor()));
          stmtInsert2.setInt(5, replySize + 1);
          count();
          stmtInsert2.executeUpdate();
          secondReplySize += 1;
          replySize += 1;
        } catch (SQLException e) {
          System.out.println(e);
        }
      }
    }
    try {
      con.commit();
      stmtInsert1.close();
      stmtInsert2.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
