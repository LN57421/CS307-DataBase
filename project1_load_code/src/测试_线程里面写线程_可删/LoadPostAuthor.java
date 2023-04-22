package 测试_线程里面写线程_可删;

import Data.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadPostAuthor implements Runnable{
  List<Post> posts;

  Map<String, String> authorAndID;

  private static final int THREAD_POOL_SIZE = 4;

  Connection con;

  static int cnt = 0;

  public static synchronized void count(){
    LoadPostAuthor.cnt++;
  }

  public LoadPostAuthor(List<Post> posts, Map<String, String> authorAndID, Connection con) {
    this.posts = posts;
    this.authorAndID = authorAndID;
    this.con = con;
  }

  @Override
  public void run() {
    ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    try {
      for (int i = 0; i < THREAD_POOL_SIZE; i++) {
        int start = i * posts.size() / THREAD_POOL_SIZE;
        int end = (i + 1) * posts.size() / THREAD_POOL_SIZE;
        List<Post> sublist = posts.subList(start, end);
        Runnable task = new Task(sublist, authorAndID, con);
        executor.execute(task);
      }
      executor.shutdown();
      while (!executor.isTerminated()){}
      con.commit();
    }catch (SQLException e){
      System.out.println(e);
    }
  }

  private static class Task implements Runnable{

    List<Post> posts;

    Map<String, String> authorAndID;

    PreparedStatement stmtInsert;

    public Task(List<Post> posts, Map<String, String> authorAndID, Connection con)
        throws SQLException {
      this.posts = posts;
      this.authorAndID = authorAndID;
      this.stmtInsert = con.prepareStatement("INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (?,?,?,?);");
    }

    private synchronized void put(String name, String ID){
      authorAndID.put(name, ID);
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
          put(post.getAuthor(), post.getAuthorID());
          count();
        }catch (SQLException e){
          System.out.println(e);
        }
      });
      try {
        stmtInsert.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
