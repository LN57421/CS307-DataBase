import com.alibaba.fastjson.JSON;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Loader1Prepare {

  static int cnt;
  private static Connection con = null;
  private static Statement stmt = null;

  private static PreparedStatement stmtInsert = null;

  private static ResultSet rs = null;

  private static void openDB(Properties prop) {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (Exception e) {
      System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
      System.exit(1);
    }
    String url =
        "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
    try {
      con = DriverManager.getConnection(url, prop);
      con.setAutoCommit(false);
      stmt = con.createStatement();
    } catch (SQLException e) {
      System.err.println("Database connection failed");
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }


  private static void closeDB() {
    if (con != null) {
      try {
        if (stmt != null) {
          stmt.close();
        }
        if (stmtInsert != null){
          stmtInsert.close();
        }
        con.close();
        con = null;
      } catch (Exception ignored) {
      }
    }
  }

  private static Properties loadDBUser() {
    Properties properties = new Properties();
    try {
      properties.load(new InputStreamReader(new FileInputStream("dbUser.properties")));
      return properties;
    } catch (IOException e) {
      System.err.println("can not find db user file");
      throw new RuntimeException(e);
    }
  }

  private static List<Post> loadPost() {
    try {
      String jsonStringsPost = Files.readString(Path.of("resource/posts.json"));
      List<Post> posts = JSON.parseArray(jsonStringsPost, Post.class);
      return posts;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<Replies> loadReplies() {
    try {
      String jsonStringsReply = Files.readString(Path.of("resource/replies.json"));
      List<Replies> replies = JSON.parseArray(jsonStringsReply, Replies.class);
      return replies;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Map<String, String> initAuthorAndId() throws SQLException {
    Map<String, String> authorAndID = new HashMap<>();
    String sqlSelectName = "select author_name, author_id from authors;";
    rs = stmt.executeQuery(sqlSelectName);
    while (rs.next()) {
      String author_name = rs.getString("author_name");
      String author_id = rs.getString("author_id");
      authorAndID.put(author_name, author_id);
    }
    return authorAndID;
  }

  private static Map<String, Integer> initCategories() throws SQLException {
    Map<String, Integer> categories = new HashMap<>();
    String sqlSelectCategories = "select category_name, category_id from categories";
    rs = stmt.executeQuery(sqlSelectCategories);
    while (rs.next()) {
      categories.put(rs.getString("category_name"), rs.getInt("category_id"));
    }
    return categories;
  }

  private static int replySize() throws SQLException {
    String sqlSelectReplySize = "select max(reply_id) from replies";
    rs = stmt.executeQuery(sqlSelectReplySize);
    rs.next();
    if (rs.getString("max") != null) {
      return Integer.valueOf(rs.getString("max"));
    }else {
      return 0;
    }
  }

  private static int secondReplySize() throws SQLException {
    String sqlSelectReplySize = "select max(secondary_reply_id) from secondary_replies";
    rs = stmt.executeQuery(sqlSelectReplySize);
    rs.next();
    if (rs.getString("max") != null) {
      return Integer.valueOf(rs.getString("max"));
    }else {
      return 0;
    }
  }

  private static int[] init(Map<String, String> authorAndID, Map<String, Integer> categories)
      throws SQLException {
    int[] replyAndSecondReplySize = new int[2];
    authorAndID = initAuthorAndId();
    categories = initCategories();
    replyAndSecondReplySize[0] = replySize();
    replyAndSecondReplySize[1] = secondReplySize();
    return replyAndSecondReplySize;
  }

  private static void loadPostAuthor(List<Post> posts, Map<String, String> authorAndID)
      throws SQLException {
    stmtInsert = con.prepareStatement("INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (?,?,?,?);");
    posts.forEach(post -> {
      try {
        stmtInsert.setString(1, post.getAuthorID());
        stmtInsert.setString(2, post.getAuthor());
        stmtInsert.setTimestamp(3, Timestamp.valueOf(post.getAuthorRegistrationTime()));
        stmtInsert.setLong(4, Long.parseLong(post.getAuthorPhone()));
        stmtInsert.executeUpdate();
        authorAndID.put(post.getAuthor(), post.getAuthorID());
        cnt++;
      } catch (Exception e) {
        System.out.println(e);
      }
    });
  }

  private static void loadAllAuthor(List<Post> posts, List<Replies> replies, Map<String, String> authorAndID)
      throws SQLException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    stmtInsert = con.prepareStatement("INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (?,?,?,?);");
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
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      }
    });
  }

  private static void loadCity(List<Post> posts) throws SQLException{
    stmtInsert = con.prepareStatement("INSERT INTO cities (city_name, city_state) VALUES (?,?)");
    posts.forEach(post -> {
      String cityAndNation = post.getPostingCity();
      String nation = cityAndNation.substring(cityAndNation.lastIndexOf(",") + 2);
      String city = cityAndNation.substring(0, cityAndNation.lastIndexOf(","));
      try {
        stmtInsert.setString(1, city);
        stmtInsert.setString(2, nation);
        stmtInsert.executeUpdate();
        cnt++;
        con.commit();
      } catch (SQLException e) {
        System.out.println(e);
        try {
          con.rollback();
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
    });
  }

  private static void loadCategories(List<Post> posts, Map<String, Integer> categories)
      throws SQLException {
    stmtInsert = con.prepareStatement("INSERT INTO categories (category_id, category_name) VALUES (?,?)");
    posts.forEach(post -> {
      List<String> category = post.getCategory();
      category.forEach(s -> {
        if (!categories.containsKey(s)) {
          categories.put(s, categories.size() + 1);
          try {
            stmtInsert.setInt(1, categories.size());
            stmtInsert.setString(2, s);
            stmtInsert.executeUpdate();
            cnt++;
          } catch (SQLException e) {
            System.out.println(e);
          }
        }
      });
    });
  }

  private static void loadPostContent(List<Post> posts) throws SQLException {
    stmtInsert = con.prepareStatement("INSERT INTO posts (post_id, author_id, title, content, posting_time, posting_city) VALUES (?,?,?,?,?,?);");
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
  }

  private static void loadPostCategories(List<Post> posts, Map<String, Integer> categories) throws SQLException{
    stmtInsert = con.prepareStatement("INSERT INTO post_categories (post_id, category_id) VALUES (?,?);");
    posts.forEach(post -> {
      post.getCategory().forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setInt(2, categories.get(s));
          stmtInsert.executeUpdate();
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
  }

  private static void loadFavor(List<Post> posts, Map<String, String> authorAndID)
      throws SQLException {
    stmtInsert = con.prepareStatement("INSERT INTO favorite_posts (post_id, favorite_author_id) VALUES (?,?);");
    posts.forEach(post -> {
      List<String> authors = post.getAuthorFavorite();
      authors.forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setString(2, authorAndID.get(s));
          stmtInsert.executeUpdate();
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
  }

  private static void loadShare(List<Post> posts, Map<String, String> authorAndID)
      throws SQLException {
    stmtInsert = con.prepareStatement("INSERT INTO shared_posts (post_id, sharing_author_id) VALUES (?,?);");
    posts.forEach(post -> {
      List<String> authors = post.getAuthorShared();
      authors.forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setString(2, authorAndID.get(s));
          stmtInsert.executeUpdate();
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
  }

  private static void loadLiked(List<Post> posts, Map<String, String> authorAndID)
      throws SQLException {
    stmtInsert = con.prepareStatement("INSERT INTO liked_posts (post_id, liking_author_id) VALUES (?,?);");
    posts.forEach(post -> {
      List<String> authors = post.getAuthorLiked();
      authors.forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setString(2, authorAndID.get(s));
          stmtInsert.executeUpdate();
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
  }

  private static void loadFollowedAuthors(List<Post> posts, Map<String, String> authorAndID)
      throws SQLException {
    stmtInsert = con.prepareStatement("INSERT INTO followed_authors (author_id, follower_author_id) VALUES (?,?);");
    posts.forEach(post -> {
      List<String> authors = post.getAuthorFollowedBy();
      authors.forEach(s -> {
        try {
          stmtInsert.setString(1, authorAndID.get(post.getAuthor()));
          stmtInsert.setString(2, authorAndID.get(s));
          stmtInsert.executeUpdate();
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
  }

  private static void loadReply(int replySize, int secondReplySize, List<Replies> replies,
      Map<String, String> authorAndID) throws SQLException {
    PreparedStatement stmtInsert1 = con.prepareStatement("INSERT INTO replies (reply_id, content, stars, author_id, post_id) VALUES (?,?,?,?,?);");
    PreparedStatement stmtInsert2 = con.prepareStatement("INSERT INTO secondary_replies (secondary_reply_id, content, stars, author_id, reply_id) VALUES (?,?,?,?,?);");
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
      cnt++;
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
            cnt++;
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
            stmtInsert2.setInt(1, secondReplySize + 1);
            stmtInsert2.setString(2, replies1.getSecondaryReplyContent());
            stmtInsert2.setInt(3, replies1.getSecondaryReplyStars());
            stmtInsert2.setString(4, authorAndID.get(replies1.getSecondaryReplyAuthor()));
            stmtInsert2.setInt(5, replySize + 1);
            stmtInsert2.executeUpdate();
            secondReplySize += 1;
            replySize += 1;
            cnt += 2;
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
          stmtInsert2.setInt(1, secondReplySize + 1);
          stmtInsert2.setString(2, replies1.getSecondaryReplyContent());
          stmtInsert2.setInt(3, replies1.getSecondaryReplyStars());
          stmtInsert2.setString(4, authorAndID.get(replies1.getSecondaryReplyAuthor()));
          stmtInsert2.setInt(5, replySize + 1);
          stmtInsert2.executeUpdate();
          secondReplySize += 1;
          replySize += 1;
          cnt+=2;
        } catch (SQLException e) {
          System.out.println(e);
        }
      }
    }
    stmtInsert1.close();
    stmtInsert2.close();
  }

  public static void main(String[] args) throws SQLException {
    Properties prop = loadDBUser();
    List<Post> posts = loadPost();
    List<Replies> replies = loadReplies();
    openDB(prop);
    Map<String, String> authorAndID = new HashMap<>();
    Map<String, Integer> categories = new HashMap<>();
    long start = System.currentTimeMillis();
    int[] replyAndSecondReplySize = init(authorAndID, categories);
    loadPostAuthor(posts, authorAndID);
    loadAllAuthor(posts, replies, authorAndID);
    loadCategories(posts, categories);
    loadCity(posts);
    loadPostContent(posts);
    loadPostCategories(posts, categories);
    loadFavor(posts, authorAndID);
    loadShare(posts, authorAndID);
    loadLiked(posts, authorAndID);
    loadFollowedAuthors(posts, authorAndID);
    loadReply(replySize(), secondReplySize(), replies, authorAndID);
    con.commit();
    long end = System.currentTimeMillis();
    System.out.println(cnt + " records successfully loaded");
    System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");
    System.out.println(((double) (end - start) / 1000) + "/s");
    System.out.println(authorAndID.size());
    closeDB();
  }
}

