package Pretreatment.single_Thread;

import Data.Post;
import Data.Replies;
import com.alibaba.fastjson.JSON;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class Loader3Batch {

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

  private static void initDB() throws SQLException {
    stmt.execute("drop table if exists secondary_replies;" + "drop table if exists replies;"
        + "drop table if exists followed_authors;" + "drop table if exists favorite_posts;"
        + "drop table if exists shared_posts;" + "drop table if exists liked_posts;"
        + "drop table if exists post_categories;" + "drop table if exists posts;"
        + "drop table if exists authors;" + "drop table if exists cities;"
        + "drop table if exists categories;");
    stmt.execute("create table authors\n" + "(\n" + "    author_id         varchar primary key ,\n"
        + "    author_name       varchar(255) not null unique ,\n"
        + "    registration_time timestamp           not null,\n"
        + "    phone             bigint unique  --删除notnull，改变为bigint类型\n" + ");"
        + "create table cities\n" + "(\n" + "    city_name varchar(255) primary key ,--城市名字\n"
        + "    city_state varchar(255) not null --添加国家\n" + ");" + "create table posts\n" + "(\n"
        + "    post_id      integer unique primary key, --去掉自增\n"
        + "    author_id    varchar not null references authors (author_id), -- 更改为id，考虑到可扩展性（比如作者改名字）\n"
        + "    title        varchar(255) not null,\n" + "    content      text         not null,\n"
        + "    posting_time timestamp    not null,\n"
        + "    posting_city varchar(255) not null references cities(city_name) --post与城市是一对多的关系，一个post只能对应一个城市，但一个城市能对应多个post\n"
        + ");" + "create table replies\n" + "(\n"
        + "    reply_id    integer      not null primary key,\n"
        + "    content     text         not null,\n" + "    stars       integer      not null,\n"
        + "    author_id   varchar      not null references authors (author_id), -- 改为authorID\n"
        + "    post_id     integer      not null references posts (post_id)\n" + ");"
        + "create table secondary_replies\n" + "(\n"
        + "    secondary_reply_id integer      primary key,\n"
        + "    content            text         not null,\n"
        + "    stars              integer      not null,\n"
        + "    author_id          varchar      not null references authors (author_id),\n"
        + "    reply_id           integer      not null references replies (reply_id)\n" + ");"
        + "create table followed_authors\n" + "(\n"
        + "    author_id          varchar references authors (author_id) not null,\n"
        + "    follower_author_id varchar references authors(author_id), --改不改？\n"
        + "    constraint followed primary key (author_id, follower_author_id)\n" + ");\n"
        + "create table favorite_posts\n" + "(\n"
        + "    post_id             integer references posts (post_id),\n"
        + "    favorite_author_id  varchar not null,\n"
        + "    constraint favorite_pk primary key (post_id, favorite_author_id)\n" + ");\n"
        + "create table shared_posts\n" + "(\n"
        + "    post_id           integer references posts (post_id),\n"
        + "    sharing_author_id varchar not null,\n"
        + "    constraint shared_pk primary key (post_id, sharing_author_id)\n" + ");"
        + "create table liked_posts\n" + "(\n"
        + "    post_id          integer references posts (post_id),\n"
        + "    liking_author_id varchar not null,\n"
        + "    constraint liked_pk primary key (post_id, liking_author_id)\n" + ");"
        + "create table categories\n" + "(\n" + "    category_id   integer primary key,\n"
        + "    category_name varchar(255) unique not null\n" + ");"
        + "create table post_categories\n" + "(\n"
        + "    post_id     integer references posts (post_id) not null,\n"
        + "    category_id integer not null references categories (category_id),\n"
        + "    constraint post_categories_pk primary key (post_id, category_id)\n" + ");");
  }


  private static void closeDB() {
    if (con != null) {
      try {
        if (stmt != null) {
          stmt.close();
        }
        if (stmtInsert != null) {
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

  private static List<Post> loadPost(int i) {
    try {
      String jsonStringsPost;
      switch (i) {
        case 1:
          jsonStringsPost = Files.readString(Path.of("resource/posts.json"));
          break;
        case 2:
          jsonStringsPost = Files.readString(Path.of("resource/posts_5times.json"));
          break;
        case 3:
          jsonStringsPost = Files.readString(Path.of("resource/posts_10times.json"));
          break;
        default:
          jsonStringsPost = Files.readString(Path.of("resource/posts.json"));
      }
      List<Post> posts = JSON.parseArray(jsonStringsPost, Post.class);
      return posts;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<Replies> loadReplies(int i) {
    try {
      String jsonStringsReply;
      switch (i) {
        case 1:
          jsonStringsReply = Files.readString(Path.of("resource/replies.json"));
          break;
        case 2:
          jsonStringsReply = Files.readString(Path.of("resource/replies_5times.json"));
          break;
        case 3:
          jsonStringsReply = Files.readString(Path.of("resource/replies_10times.json"));
          break;
        default:
          jsonStringsReply = Files.readString(Path.of("resource/replies.json"));
      }
      List<Replies> replies = JSON.parseArray(jsonStringsReply, Replies.class);
      return replies;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Map<String, String> initOtherAuthorAndId(List<Post> posts, List<Replies> replies)
      throws SQLException {
    String sqlSelectName = "select author_name, author_id from authors;";
    rs = stmt.executeQuery(sqlSelectName);
    while (rs.next()) {
      String author_name = rs.getString("author_name");
      String author_id = rs.getString("author_id");
    }
    Random random = new Random();
    Map<String, String> authorAndID = new HashMap<>();
    List<String> IDExist = new ArrayList<>();
    for (Post post : posts) {
      IDExist.add(post.getAuthorID());
    }
    for (Post post : posts) {
      List<String> authors = new ArrayList<>();
      authors.addAll(post.getAuthorLiked());
      authors.addAll(post.getAuthorFavorite());
      authors.addAll(post.getAuthorShared());
      authors.addAll(post.getAuthorFollowedBy());
      authors.forEach(author -> {
        Long ID = random.nextLong(Long.MAX_VALUE);
        while (authorAndID.containsValue(String.valueOf(ID)) || IDExist.contains(ID)) {
          ID = random.nextLong(Long.MAX_VALUE);
        }
        authorAndID.put(author, String.valueOf(ID));
      });
    }
    for (Replies reply : replies) {
      Long ID = random.nextLong(Long.MAX_VALUE);
      while (authorAndID.containsValue(String.valueOf(ID)) || IDExist.contains(ID)) {
        ID = random.nextLong(Long.MAX_VALUE);
      }
      authorAndID.put(reply.getReplyAuthor(), String.valueOf(ID));
      ID = random.nextLong(Long.MAX_VALUE);
      while (authorAndID.containsValue(String.valueOf(ID))) {
        ID = random.nextLong(Long.MAX_VALUE);
      }
      authorAndID.put(reply.getSecondaryReplyAuthor(), String.valueOf(ID));
    }
    posts.forEach(post -> {
      authorAndID.remove(post.getAuthor());
    });
    return authorAndID;
  }

  private static Map<String, String> initAuthorAndID(List<Post> posts) {
    Map<String, String> authorAndID = new HashMap<>();
    posts.forEach(post -> {
      authorAndID.put(post.getAuthor(), post.getAuthorID());
    });
    return authorAndID;
  }

  private static Map<String, Integer> initCategories(List<Post> posts) throws SQLException {
    Map<String, Integer> initCategories = new HashMap<>();
    Map<String, Integer> categories = new HashMap<>();
    String sqlSelectCategories = "select category_name, category_id from categories";
    rs = stmt.executeQuery(sqlSelectCategories);
    while (rs.next()) {
      initCategories.put(rs.getString("category_name"), rs.getInt("category_id"));
    }
    categories.putAll(initCategories);
    posts.forEach(post -> {
      List<String> cats = post.getCategory();
      cats.forEach(cat -> {
        if (!categories.containsKey(cat)) {
          categories.put(cat, categories.size());
        }
      });
    });
    return categories;
  }

  private static Map<String, String> initCities(List<Post> posts) throws SQLException {
    Map<String, String> cities = new HashMap<>();
    String sqlSelectCities = "select city_name, city_state from cities";
    rs = stmt.executeQuery(sqlSelectCities);
    while (rs.next()) {
      cities.put(rs.getString("city_name"), rs.getString("city_state"));
    }
    posts.forEach(post -> {
      String cityAndNation = post.getPostingCity();
      String nation = cityAndNation.substring(cityAndNation.lastIndexOf(",") + 2);
      String city = cityAndNation.substring(0, cityAndNation.lastIndexOf(","));
      cities.put(city, nation);
    });
    return cities;
  }

  private static int replySize() throws SQLException {
    String sqlSelectReplySize = "select max(reply_id) from replies";
    rs = stmt.executeQuery(sqlSelectReplySize);
    rs.next();
    if (rs.getString("max") != null) {
      return Integer.valueOf(rs.getString("max"));
    } else {
      return 0;
    }
  }

  private static int secondReplySize() throws SQLException {
    String sqlSelectReplySize = "select max(secondary_reply_id) from secondary_replies";
    rs = stmt.executeQuery(sqlSelectReplySize);
    rs.next();
    if (rs.getString("max") != null) {
      return Integer.valueOf(rs.getString("max"));
    } else {
      return 0;
    }
  }

  private static int[] init(Map<String, String> otherAuthorAndID, Map<String, String> authorAndID,
      Map<String, Integer> categories, Map<String, String> cities, List<Post> posts,
      List<Replies> replies) throws SQLException {
    int[] replyAndSecondReplySize = new int[2];
    Map<String, String> initOtherAuthorAndID = initOtherAuthorAndId(posts, replies);
    otherAuthorAndID.putAll(initOtherAuthorAndID);
    Map<String, String> initAuthorAndID = initAuthorAndID(posts);
    authorAndID.putAll(initAuthorAndID);
    Map<String, Integer> initCategories = initCategories(posts);
    categories.putAll(initCategories);
    Map<String, String> initCities = initCities(posts);
    cities.putAll(initCities);
    replyAndSecondReplySize[0] = replySize();
    replyAndSecondReplySize[1] = secondReplySize();
    return replyAndSecondReplySize;
  }

  private static void loadOtherAuthor(List<Post> posts, Map<String, String> authorAndID)
      throws SQLException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    stmtInsert = con.prepareStatement(
        "INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (?,?,?,?);");
    Random random = new Random();
    authorAndID.forEach((s, id) -> {
      Long time = System.currentTimeMillis() - random.nextLong(100000000);
      String timeF = format.format(time);
      try {
        stmtInsert.setString(1, id);
        stmtInsert.setString(2, s);
        stmtInsert.setTimestamp(3, Timestamp.valueOf(timeF));
        stmtInsert.setNull(4, Types.BIGINT);
        stmtInsert.addBatch();
        cnt++;
      } catch (SQLException e) {
        System.out.println(e);
      }
    });
  }

  private static void loadPostAuthor(List<Post> posts) throws SQLException {
    stmtInsert = con.prepareStatement(
        "INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (?,?,?,?);");
    posts.forEach(post -> {
      try {
        stmtInsert.setString(1, post.getAuthorID());
        stmtInsert.setString(2, post.getAuthor());
        stmtInsert.setTimestamp(3, Timestamp.valueOf(post.getAuthorRegistrationTime()));
        stmtInsert.setLong(4, Long.parseLong(post.getAuthorPhone()));
        stmtInsert.addBatch();
        cnt++;
      } catch (Exception e) {
        System.out.println(e);
      }
    });
  }

  private static void loadCity(List<Post> posts, Map<String, String> cities) throws SQLException {
    stmtInsert = con.prepareStatement("INSERT INTO cities (city_name, city_state) VALUES (?,?)");
    for (String key : cities.keySet()) {
      try {
        stmtInsert.setString(1, key);
        stmtInsert.setString(2, cities.get(key));
        stmtInsert.addBatch();
        cnt++;
      } catch (SQLException e) {
        System.out.println(e);
      }
    }
  }

  private static void loadCategories(List<Post> posts, Map<String, Integer> categories)
      throws SQLException {
    stmtInsert = con.prepareStatement(
        "INSERT INTO categories (category_id, category_name) VALUES (?,?)");
    for (String key : categories.keySet()) {
      try {
        stmtInsert.setInt(1, categories.get(key));
        stmtInsert.setString(2, key);
        stmtInsert.addBatch();
        cnt++;
      } catch (SQLException e) {
        System.out.println(e);
      }
    }
  }

  private static void loadPostContent(List<Post> posts) throws SQLException {
    stmtInsert = con.prepareStatement(
        "INSERT INTO posts (post_id, author_id, title, content, posting_time, posting_city) VALUES (?,?,?,?,?,?);");
    for (Post post : posts) {
      String city = post.getPostingCity().substring(0, post.getPostingCity().lastIndexOf(","));
      try {
        stmtInsert.setInt(1, post.getPostID());
        stmtInsert.setString(2, post.getAuthorID());
        stmtInsert.setString(3, post.getTitle());
        stmtInsert.setString(4, post.getContent());
        stmtInsert.setTimestamp(5, Timestamp.valueOf(post.getPostingTime()));
        stmtInsert.setString(6, city);
        stmtInsert.addBatch();
        cnt++;
      } catch (SQLException e) {
        System.out.println(e);
      }
    }
  }

  private static void loadPostCategories(List<Post> posts, Map<String, Integer> categories)
      throws SQLException {
    stmtInsert = con.prepareStatement(
        "INSERT INTO post_categories (post_id, category_id) VALUES (?,?);");
    posts.forEach(post -> {
      post.getCategory().forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setInt(2, categories.get(s));
          stmtInsert.addBatch();
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
  }

  private static void loadFavor(List<Post> posts, Map<String, String> authorAndID)
      throws SQLException {
    stmtInsert = con.prepareStatement(
        "INSERT INTO favorite_posts (post_id, favorite_author_id) VALUES (?,?);");
    posts.forEach(post -> {
      List<String> authors = post.getAuthorFavorite();
      authors.forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setString(2, authorAndID.get(s));
          stmtInsert.addBatch();
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
  }

  private static void loadShare(List<Post> posts, Map<String, String> authorAndID)
      throws SQLException {
    stmtInsert = con.prepareStatement(
        "INSERT INTO shared_posts (post_id, sharing_author_id) VALUES (?,?);");
    posts.forEach(post -> {
      List<String> authors = post.getAuthorShared();
      authors.forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setString(2, authorAndID.get(s));
          stmtInsert.addBatch();
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
  }

  private static void loadLiked(List<Post> posts, Map<String, String> authorAndID)
      throws SQLException {
    stmtInsert = con.prepareStatement(
        "INSERT INTO liked_posts (post_id, liking_author_id) VALUES (?,?);");
    posts.forEach(post -> {
      List<String> authors = post.getAuthorLiked();
      authors.forEach(s -> {
        try {
          stmtInsert.setInt(1, post.getPostID());
          stmtInsert.setString(2, authorAndID.get(s));
          stmtInsert.addBatch();
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
  }

  private static void loadFollowedAuthors(List<Post> posts, Map<String, String> authorAndID)
      throws SQLException {
    stmtInsert = con.prepareStatement(
        "INSERT INTO followed_authors (author_id, follower_author_id) VALUES (?,?);");
    posts.forEach(post -> {
      List<String> authors = post.getAuthorFollowedBy();
      authors.forEach(s -> {
        try {
          stmtInsert.setString(1, authorAndID.get(post.getAuthor()));
          stmtInsert.setString(2, authorAndID.get(s));
          stmtInsert.addBatch();
          cnt++;
        } catch (SQLException e) {
          System.out.println(e);
        }
      });
    });
  }

  private static void loadReply(int replySize, int secondReplySize, List<Replies> replies,
      Map<String, String> authorAndID) throws SQLException {
    PreparedStatement stmtInsert1 = con.prepareStatement(
        "INSERT INTO replies (reply_id, content, stars, author_id, post_id) VALUES (?,?,?,?,?);");
    PreparedStatement stmtInsert2 = con.prepareStatement(
        "INSERT INTO secondary_replies (secondary_reply_id, content, stars, author_id, reply_id) VALUES (?,?,?,?,?);");
    int postId = replies.get(0).getPostID();
    String comparedReply =
        replies.get(0).getReplyAuthor() + replies.get(0).getReplyContent() + replies.get(0)
            .getReplyStars();
    try {
      stmtInsert1.setInt(1, replySize + 1);
      stmtInsert1.setString(2, replies.get(0).getReplyContent());
      stmtInsert1.setInt(3, replies.get(0).getReplyStars());
      stmtInsert1.setString(4, authorAndID.get(replies.get(0).getReplyAuthor()));
      stmtInsert1.setInt(5, postId);
      stmtInsert1.addBatch();
      replySize += 1;
      cnt++;
    } catch (SQLException e) {
      System.out.println(e);
    }
    for (Replies replies1 : replies) {
      String singleReply =
          replies1.getReplyAuthor() + replies1.getReplyContent() + replies1.getReplyStars();
      if (postId == replies1.getPostID()) {
        if (singleReply.equals(comparedReply)) {
          try {
            stmtInsert2.setInt(1, secondReplySize + 1);
            stmtInsert2.setString(2, replies1.getSecondaryReplyContent());
            stmtInsert2.setInt(3, replies1.getSecondaryReplyStars());
            stmtInsert2.setString(4, authorAndID.get(replies1.getSecondaryReplyAuthor()));
            stmtInsert2.setInt(5, replySize);
            stmtInsert2.addBatch();
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
            stmtInsert1.addBatch();
            cnt += 1;
            stmtInsert2.setInt(1, secondReplySize + 1);
            stmtInsert2.setString(2, replies1.getSecondaryReplyContent());
            stmtInsert2.setInt(3, replies1.getSecondaryReplyStars());
            stmtInsert2.setString(4, authorAndID.get(replies1.getSecondaryReplyAuthor()));
            stmtInsert2.setInt(5, replySize + 1);
            stmtInsert2.addBatch();
            secondReplySize += 1;
            replySize += 1;
            cnt += 1;
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
          stmtInsert1.addBatch();
          cnt += 1;
          stmtInsert2.setInt(1, secondReplySize + 1);
          stmtInsert2.setString(2, replies1.getSecondaryReplyContent());
          stmtInsert2.setInt(3, replies1.getSecondaryReplyStars());
          stmtInsert2.setString(4, authorAndID.get(replies1.getSecondaryReplyAuthor()));
          stmtInsert2.setInt(5, replySize + 1);
          stmtInsert2.addBatch();
          secondReplySize += 1;
          replySize += 1;
          cnt += 1;
        } catch (SQLException e) {
          System.out.println(e);
        }
      }
    }
    stmtInsert1.executeBatch();
    stmtInsert2.executeBatch();
    con.commit();
    stmtInsert1.close();
    stmtInsert2.close();
  }

  public static void exe() throws SQLException {
    stmtInsert.executeBatch();
    con.commit();
    stmtInsert.clearBatch();
  }

  public static void main(String[] args) throws SQLException {
    System.out.println("1: 1time; 2: 5times; 3: 10times");
    Scanner scanner = new Scanner(System.in);
    int choose = scanner.nextInt();
    System.out.println("waiting...");
    Properties prop = loadDBUser();
    List<Post> posts = loadPost(choose);
    List<Replies> replies = loadReplies(choose);
    int testCase = 3;
    long[] timeNeed = new long[testCase];
    openDB(prop);
    initDB();
    Map<String, String> initOtherAuthorAndID = new HashMap<>();
    Map<String, String> initAuthorAndID = new HashMap<>();
    Map<String, Integer> categories = new HashMap<>();
    Map<String, String> cities = new HashMap<>();
    int[] replyAndSecondReplySize = init(initOtherAuthorAndID, initAuthorAndID, categories, cities,
        posts, replies);
    Map<String, String> authorAndID = new HashMap<>();
    authorAndID.putAll(initOtherAuthorAndID);
    authorAndID.putAll(initAuthorAndID);
    for (int i = 0; i < testCase; i++) {
      initDB();
      long start = System.currentTimeMillis();
      loadOtherAuthor(posts, initOtherAuthorAndID);
      exe();
      loadPostAuthor(posts);
      exe();
      loadCategories(posts, categories);
      exe();
      loadCity(posts, cities);
      exe();
      loadPostContent(posts);
      exe();
      loadPostCategories(posts, categories);
      exe();
      loadFavor(posts, authorAndID);
      exe();
      loadShare(posts, authorAndID);
      exe();
      loadLiked(posts, authorAndID);
      exe();
      loadFollowedAuthors(posts, authorAndID);
      exe();
      loadReply(replyAndSecondReplySize[0], replyAndSecondReplySize[1], replies, authorAndID);
      con.commit();
      long end = System.currentTimeMillis();
      System.out.println(cnt + " records successfully loaded");
      System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");
      System.out.println(((double) (end - start) / 1000) + "/s");
      cnt = 0;
      timeNeed[i] = end - start;
    }
    long averageTime = 0;
    for (int i = 0; i < testCase; i++) {
      System.out.print("single import time: " + ((double) (timeNeed[i]) / 1000) + "/s" + " ");
      averageTime += timeNeed[i];
    }
    System.out.println();
    System.out.println("average import time: " + String.format("%.3f",
        ((double) (averageTime) / (1000 * testCase))) + "/s");
    closeDB();
  }
}

