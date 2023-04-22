package multi_Thread;

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
import Data.Post;
import Data.Replies;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Loader4TransactionThread {

  private static Connection[] con = new Connection[11];

  private static Properties prop;

  private static void openDB() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (Exception e) {
      System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
      System.exit(1);
    }
    String url =
        "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
    try {
      for (int i = 0; i < con.length; i++) {
        con[i] = DriverManager.getConnection(url, prop);
        con[i].setAutoCommit(false);
      }

    } catch (SQLException e) {
      System.err.println("Database connection failed");
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }

  private static void initDB() throws SQLException {
    Statement stmt = con[0].createStatement();
    stmt.execute("drop table if exists secondary_replies;"
        + "drop table if exists replies;"
        + "drop table if exists followed_authors;"
        + "drop table if exists favorite_posts;"
        + "drop table if exists shared_posts;"
        + "drop table if exists liked_posts;"
        + "drop table if exists post_categories;"
        + "drop table if exists posts;"
        + "drop table if exists authors;"
        + "drop table if exists cities;"
        + "drop table if exists categories;");
    con[0].commit();
    stmt.execute("create table authors\n"
        + "(\n"
        + "    author_id         varchar primary key ,\n"
        + "    author_name       varchar(255) not null unique ,\n"
        + "    registration_time timestamp           not null,\n"
        + "    phone             bigint unique  --删除notnull，改变为bigint类型\n"
        + ");"
        + "create table cities\n"
        + "(\n"
        + "    city_name varchar(255) primary key ,--城市名字\n"
        + "    city_state varchar(255) not null --添加国家\n"
        + ");"
        + "create table posts\n"
        + "(\n"
        + "    post_id      integer unique primary key, --去掉自增\n"
        + "    author_id    varchar not null references authors (author_id), -- 更改为id，考虑到可扩展性（比如作者改名字）\n"
        + "    title        varchar(255) not null,\n"
        + "    content      text         not null,\n"
        + "    posting_time timestamp    not null,\n"
        + "    posting_city varchar(255) not null references cities(city_name) --post与城市是一对多的关系，一个post只能对应一个城市，但一个城市能对应多个post\n"
        + ");"
        + "create table replies\n"
        + "(\n"
        + "    reply_id    integer      not null primary key,\n"
        + "    content     text         not null,\n"
        + "    stars       integer      not null,\n"
        + "    author_id   varchar      not null references authors (author_id), -- 改为authorID\n"
        + "    post_id     integer      not null references posts (post_id)\n"
        + ");"
        + "create table secondary_replies\n"
        + "(\n"
        + "    secondary_reply_id integer      primary key,\n"
        + "    content            text         not null,\n"
        + "    stars              integer      not null,\n"
        + "    author_id          varchar      not null references authors (author_id),\n"
        + "    reply_id           integer      not null references replies (reply_id)\n"
        + ");"
        + "create table followed_authors\n"
        + "(\n"
        + "    author_id          varchar references authors (author_id),\n"
        + "    follower_author_id varchar references authors(author_id), --改不改？\n"
        + "    constraint followed primary key (author_id, follower_author_id)\n"
        + ");\n"
        + "create table favorite_posts\n"
        + "(\n"
        + "    post_id             integer references posts (post_id),\n"
        + "    favorite_author_id  varchar not null,\n"
        + "    constraint favorite_pk primary key (post_id, favorite_author_id)\n"
        + ");\n"
        + "create table shared_posts\n"
        + "(\n"
        + "    post_id           integer references posts (post_id),\n"
        + "    sharing_author_id varchar not null,\n"
        + "    constraint shared_pk primary key (post_id, sharing_author_id)\n"
        + ");"
        + "create table liked_posts\n"
        + "(\n"
        + "    post_id          integer references posts (post_id),\n"
        + "    liking_author_id varchar not null,\n"
        + "    constraint liked_pk primary key (post_id, liking_author_id)\n"
        + ");"
        + "create table categories\n"
        + "(\n"
        + "    category_id   integer primary key,\n"
        + "    category_name varchar(255) unique not null\n"
        + ");"
        + "create table post_categories\n"
        + "(\n"
        + "    post_id     integer references posts (post_id),\n"
        + "    category_id integer not null references categories (category_id),\n"
        + "    constraint post_categories_pk primary key (post_id, category_id)\n"
        + ");"
    );
    con[0].commit();
    stmt.close();
  }


  private static void closeDB() {
    if (con != null) {
      try {
        for (int i = 0; i < con.length; i++) {
          con[i].close();
          con[i] = null;
        }
      } catch (Exception ignored) {
        System.out.println(ignored);
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

  private static void initAuthorAndId(Map<String, String> authorAndID) throws SQLException {
    Statement stmt = con[0].createStatement();
    String sqlSelectName = "select author_name, author_id from authors;";
    ResultSet rs = stmt.executeQuery(sqlSelectName);
    while (rs.next()) {
      String author_name = rs.getString("author_name");
      String author_id = rs.getString("author_id");
      authorAndID.put(author_name, author_id);
    }
    stmt.close();
  }

  private static void initCategories(Map<String, Integer> categories) throws SQLException {
    Statement stmt = con[1].createStatement();
    String sqlSelectCategories = "select category_name, category_id from categories";
    ResultSet rs = stmt.executeQuery(sqlSelectCategories);
    while (rs.next()) {
      categories.put(rs.getString("category_name"), rs.getInt("category_id"));
    }
    stmt.close();
  }

  private static void initCities(Map<String, String> cities) throws SQLException {
    Statement stmt = con[2].createStatement();
    String sqlSelectCities = "select city_name, city_state from cities";
    ResultSet rs = stmt.executeQuery(sqlSelectCities);
    while (rs.next()) {
      cities.put(rs.getString("city_name"), rs.getString("city_state"));
    }
    stmt.close();
  }

  private static void replySize(int[] replyAndSecondReplySize) throws SQLException {
    Statement stmt = con[3].createStatement();
    String sqlSelectReplySize = "select max(reply_id) from replies";
    ResultSet rs = stmt.executeQuery(sqlSelectReplySize);
    rs.next();
    if (rs.getString("max") != null) {
      replyAndSecondReplySize[0] = Integer.valueOf(rs.getString("max"));
    } else {
      replyAndSecondReplySize[0] = 0;
    }
    stmt.close();
  }

  private static void secondReplySize(int[] replyAndSecondReplySize) throws SQLException {
    Statement stmt = con[4].createStatement();
    String sqlSelectReplySize = "select max(secondary_reply_id) from secondary_replies";
    ResultSet rs = stmt.executeQuery(sqlSelectReplySize);
    rs.next();
    if (rs.getString("max") != null) {
      replyAndSecondReplySize[1] = Integer.valueOf(rs.getString("max"));
    } else {
      replyAndSecondReplySize[1] = 0;
    }
    stmt.close();
  }

  private static int[] init(Map<String, String> authorAndID, Map<String, Integer> categories,
      Map<String, String> cities)
      throws InterruptedException {
    int[] replyAndSecondReplySize = new int[2];
    Thread T1 = new Thread(() -> {
      try {
        initAuthorAndId(authorAndID);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    Thread T2 = new Thread(() -> {
      try {
        initCategories(categories);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    Thread T3 = new Thread(() -> {
      try {
        initCities(cities);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    Thread T4 = new Thread(() -> {
      try {
        replySize(replyAndSecondReplySize);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    Thread T5 = new Thread(() -> {
      try {
        secondReplySize(replyAndSecondReplySize);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    T1.start();
    T2.start();
    T3.start();
    T4.start();
    T5.start();
    T1.join();
    T2.join();
    T3.join();
    T4.join();
    T5.join();
    return replyAndSecondReplySize;
  }

  public static int setCon() {
    int count = LoadAllAuthor.cnt + LoadCategories.cnt + LoadCity.cnt + LoadFavor.cnt
        + LoadFollowedAuthors.cnt + LoadLiked.cnt + LoadPostAuthor.cnt + LoadPostCategories.cnt
        + LoadPostContent.cnt + LoadReply.cnt + LoadShare.cnt;
    LoadAllAuthor.cnt = 0;
    LoadCategories.cnt = 0;
    LoadCity.cnt = 0;
    LoadFavor.cnt = 0;
    LoadFollowedAuthors.cnt = 0;
    LoadLiked.cnt = 0;
    LoadPostAuthor.cnt = 0;
    LoadPostCategories.cnt = 0;
    LoadPostContent.cnt = 0;
    LoadReply.cnt = 0;
    LoadShare.cnt = 0;
    return count;
  }

  public static void main(String[] args) throws SQLException, InterruptedException {
    prop = loadDBUser();
    List<Post> posts = loadPost();
    List<Replies> replies = loadReplies();
    int testCase = 3;
    long[] timeNeed = new long[testCase];
    openDB();
    for (int i = 0; i < testCase; i++) {
      int cnt = 0;
      initDB();
      Map<String, String> authorAndID = new HashMap<>();
      Map<String, Integer> categories = new HashMap<>();
      Map<String, String> cities = new HashMap<>();
      long start = System.currentTimeMillis();
      int[] replyAndSecondReplySize = init(authorAndID, categories, cities);
      ExecutorService service1 = Executors.newFixedThreadPool(3);//导入postAuthor,city,category
      ExecutorService service2 = Executors.newFixedThreadPool(2);//导入其他author,post,
      ExecutorService service3 = Executors.newFixedThreadPool(6);//导入follow，like，share，favor，reply，P_C
      service1.execute(new LoadPostAuthor(posts, authorAndID, con[0]));
      service1.execute(new LoadCity(posts, cities, con[1]));
      service1.execute(new LoadCategories(posts, categories, con[2]));
      service1.shutdown();
      while (!service1.isTerminated()) {
      }
      service2.execute(new LoadAllAuthor(posts, authorAndID, replies, con[3]));
      service2.execute(new LoadPostContent(posts, con[4]));
      service2.shutdown();
      while (!service2.isTerminated()) {
      }
      service3.execute(new LoadFavor(posts, authorAndID, con[5]));
      service3.execute(new LoadLiked(posts, authorAndID, con[6]));
      service3.execute(new LoadShare(posts, authorAndID, con[7]));
      service3.execute(new LoadFollowedAuthors(posts, authorAndID, con[8]));
      service3.execute(new LoadPostCategories(posts, categories, con[9]));
      service3.execute(
          new LoadReply(replyAndSecondReplySize[0], replyAndSecondReplySize[1], replies,
              authorAndID, con[10]));
      service3.shutdown();
      while (!service3.isTerminated()) {
      }
      long end = System.currentTimeMillis();
      cnt = setCon();
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
    System.out.println(
        "average import time: " + String.format("%.3f", ((double) (averageTime) / (1000 * testCase))) + "/s");
    closeDB();
  }

}

