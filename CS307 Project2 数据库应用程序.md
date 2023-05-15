# CS307 Project2 数据库应用程序（第二部分）

旨在为类似社交媒体的平台创建一套带有API的数据库应用程序

## 1. 基本要求 (70%)（性价比高且简单）

### 1.1 API规范：

为提供访问数据库系统的基本功能，需要构建一个后端库，该库公开一组**应用程序**（即使用python java等后端语言编写的程序）编程接口(API)。下面列出了每个API的一般描述。



不确定的地方：我觉得API接口对应的@PostMapping还需要调整

![image-20230515221955139](C:\Users\nian5\AppData\Roaming\Typora\typora-user-images\image-20230515221955139.png)

`@GetMapping("/users/{userId}/favoritePosts")` 是一个Spring MVC中的注解，表示该方法将处理HTTP的GET请求。

这个注解有两部分：

1. `@GetMapping`：这是Spring MVC中的一个注解，表示该方法将处理HTTP的GET请求。HTTP协议中有多种请求方法，包括GET、POST、PUT、DELETE等，其中GET通常用于请求服务器发送某些资源。
2. `"/users/{userId}/favoritePosts"`：这是URL路径模式，其中的`{userId}`是一个路径变量，可以在请求的URL中动态替换。比如，你可以发送一个请求到`/users/123/favoritePosts`，这个请求会被映射到这个方法，同时`{userId}`会被替换为`123`。

所以，`@GetMapping("/users/{userId}/favoritePosts")`的意思是，当你发送一个GET请求到`/users/{userId}/favoritePosts`这样的URL时（例如`/users/123/favoritePosts`），该请求将被映射到该方法。

在方法的参数列表中，你可以使用`@PathVariable Long userId`来获取路径变量`{userId}`的值。`@PathVariable`是Spring MVC的一个注解，表示该参数的值将从URL的路径中获取。

所以，这个注解定义的方法将处理这样的请求：获取指定用户ID的用户的所有收藏的帖子。当这个请求到达时，`userId`参数将被自动赋值为路径中的用户ID。


在这段代码中，`@RequestBody FavoritePost favoritePost` 表示该方法的 `favoritePost` 参数的值将从 HTTP 请求的 body 中获取。`@RequestBody` 是 Spring MVC 的一个注解，它用于将请求的 body 映射到方法的参数。

当你发送一个 POST 请求到 `/favorite` 这个 URL，并在请求的 body 中包含了一个 `FavoritePost` 对象（通常是 JSON 格式）时，Spring MVC 会自动将请求的 body 解析为 `FavoritePost` 对象，然后将这个对象作为参数传给这个方法。

`@Valid` 是一个 JSR-303 注解，它用于在方法被调用前对方法的参数进行验证。具体的验证规则是通过在 `FavoritePost` 类中的字段上添加注解来定义的。

所以，这个方法的作用是，当你发送一个包含了 `FavoritePost` 对象的 POST 请求到 `/favorite` 时，该方法将被调用，并将请求的 body 解析为 `FavoritePost` 对象，然后添加这个收藏的帖子。

---



1. 注册新用户。

   你的原有代码已经实现了用户注册的基本功能：接收用户提交的信息并保存到数据库中。但是，这里还有一些重要的功能需要添加：

   1. **密码加密**：在保存用户信息到数据库之前，你应该先将用户的密码加密。这是一个很重要的安全措施，因为这样即使你的数据库被盗，攻击者也无法直接获取到用户的密码。你可以使用Spring Security提供的`BCryptPasswordEncoder`类来进行密码加密。

   2. **输入验证**：你应该在接收到用户提交的信息之后，首先进行输入验证。例如，你可以检查用户名是否已经被使用，密码是否符合复杂性要求，邮箱地址是否有效等等。你可以使用Spring的`@Valid`注解和Java的Bean Validation API来进行输入验证。、

      `@Valid` 注解可以用来在HTTP请求处理方法中开启对请求体（`@RequestBody`）对象的数据校验。

      在你的控制器类中，你可以在处理请求的方法参数前添加 `@Valid` 注解，示例如下：

      ```
      @PostMapping("/favorite")
      public FavoritePost addFavoritePost(@Valid @RequestBody FavoritePost favoritePost) {
          return favoritePostService.addFavoritePost(favoritePost);
      }
      
      @PostMapping("/share")
      public SharedPost addSharedPost(@Valid @RequestBody SharedPost sharedPost) {
          return sharedPostService.addSharedPost(sharedPost);
      }
      
      @PostMapping("/like")
      public LikedPost addLikedPost(@Valid @RequestBody LikedPost likedPost) {
          return likedPostService.addLikedPost(likedPost);
      }
      ```

      然后，你可以在你的实体类中（例如 `FavoritePost`、`SharedPost` 和 `LikedPost`）使用JSR 380（Bean Validation 2.0）提供的各种约束注解，如 `@NotNull`、`@Size`、`@Min`、`@Max` 等，来对属性进行约束。

      例如，如果你想确保 `FavoritePost` 的 `userId` 和 `postId` 属性都不为空，你可以这样做：

      ```
      public class FavoritePost {
          @NotNull
          private Long userId;
          
          @NotNull
          private Long postId;
          
          // 其他属性和方法
      }
      ```

      然后，当你调用 `addFavoritePost(@Valid @RequestBody FavoritePost favoritePost)` 方法时，如果请求体中的 `userId` 或 `postId` 为 `null`，就会抛出一个 `MethodArgumentNotValidException` 异常，你可以通过全局异常处理或者控制器内的异常处理方法来处理这个异常。

   3. **错误处理**：如果在用户注册过程中发生错误（例如用户名已经被使用，输入验证失败等），你应该返回一个包含错误信息的HTTP响应。你可以使用Spring的`ResponseEntity`类来创建这样的响应

---



2. 用户可以收藏、分享和点赞帖子。

   **实现要改四个地方 TableClass  ControllerClass ServiceClass Repository**

为了实现"用户可以收藏、分享和点赞帖子"的功能，你需要在相应的服务类（FavoritePostService, SharedPostService 和 LikedPostService）中添加相应的方法，并在对应的控制器类中添加API接口。

（注：在相应的API中的Controller中的@PostMapping中加入了对应的标识）

​	其次我们应该拥有错误处理

然后分享是可以多次的 点赞只能一次 收藏只能一次





---



3. 用户可以查看他/她收藏、分享和点赞的帖子。



为了实现这个功能，你需要在相应的Service类和Controller类中添加新的方法。具体来说：

**Service类**：

在FavoritePostService，SharedPostService，和LikedPostService中，你需要添加一个方法来获取用户收藏、分享和点赞的帖子。这个方法可能需要调用你的Repository类的查询方法，根据用户ID找到相应的收藏、分享和点赞的帖子。例如，在FavoritePostService中，你可以添加这样的一个方法：

```
public List<FavoritePost> getFavoritePostsByUserId(Long userId) {
    return favoritePostRepository.findByUserId(userId);
}
```

类似地，在SharedPostService和LikedPostService中，你也需要添加类似的方法。

**Controller类**：

在FavoritePostController，SharedPostController，和LikedPostController中，你需要添加一个新的API端点，用于处理用户请求查看他/她收藏、分享和点赞的帖子。这个API端点需要调用相应的Service类的方法，获取数据，并将数据返回给用户。例如，在FavoritePostController中，你可以添加这样的一个API端点：

```
@GetMapping("/users/{userId}/favoritePosts")
public ResponseEntity<List<FavoritePost>> getFavoritePostsByUserId(@PathVariable Long userId) {
    List<FavoritePost> favoritePosts = favoritePostService.getFavoritePostsByUserId(userId);
    return ResponseEntity.ok(favoritePosts);
}
```

类似地，在SharedPostController和LikedPostController中，你也需要添加类似的API端点。

注意：这里的代码假设你的Repository类有一个`findByUserId`方法，用于根据用户ID查找相应的收藏、分享和点赞的帖子。如果你的Repository类没有这个方法，你需要添加它。这个方法可能需要使用Spring Data JPA的查询方法，或者你自己定义的SQL查询。



但是还有一点要注意的就是 返回的值是空的时候要注意



如果没有找到对应的帖子，你可以选择返回一个空的列表或者一个特定的消息告诉用户没有找到他们收藏、分享或者点赞的帖子。

在 Service 类里，你可以这样处理：

```
public List<FavoritePost> getFavoritePostsByUserId(Long userId) {
    return Optional.ofNullable(favoritePostRepository.findByUserId(userId))
            .orElse(Collections.emptyList());
}
```

在这段代码中，`Optional.ofNullable()` 方法用于处理可能为 null 的值。如果 `favoritePostRepository.findByUserId(userId)` 返回 null，那么 `orElse()` 方法将会返回一个空的列表。

在 Controller 类里，你需要返回 HTTP 状态码来告知用户请求的处理结果。如果列表为空，你可以选择返回 HTTP 200 状态码和一个空列表，或者返回一个特定的消息告诉用户没有找到他们收藏、分享或者点赞的帖子。例如：

```
@GetMapping("/users/{userId}/favoritePosts")
public ResponseEntity<?> getFavoritePostsByUserId(@PathVariable Long userId) {
    List<FavoritePost> favoritePosts = favoritePostService.getFavoritePostsByUserId(userId);
    if (favoritePosts.isEmpty()) {
        return ResponseEntity.status(HttpStatus.OK).body("No favorite posts found for user with ID: " + userId);
    } else {
        return ResponseEntity.ok(favoritePosts);
    }
}
```

在这段代码中，如果 `favoritePosts` 列表为空，那么我们返回一个 HTTP 200 状态码和一个消息告知用户没有找到他们的收藏帖子。否则，我们返回 HTTP 200 状态码和收藏帖子的列表。

对于 SharedPostController 和 LikedPostController，你可以用同样的方式处理。





---

4. 用户可以关注或取消关注其他用户，还可以查看他/她已关注的用户列表。

这个功能涉及到 `FollowedAuthor` 的 `Service` 类和 `Controller` 类。

首先，你需要在 `FollowedAuthorService` 类中添加以下方法：

1. `followAuthor`: 当一个用户想要关注另一个用户时，这个方法会被调用。它接受一个 `FollowedAuthor` 对象作为参数，并将其保存到数据库中。
2. `unfollowAuthor`: 当一个用户想要取消关注另一个用户时，这个方法会被调用。它接受一个 `FollowedAuthorsId` 对象作为参数，并从数据库中删除对应的记录。
3. `findFollowedByAuthor`: 这个方法会返回一个用户关注的所有用户的列表。它接受一个 `Author` 对象作为参数，然后从数据库中找到所有由这个 `Author` 关注的 `FollowedAuthor` 对象。

然后，在 `FollowedAuthorController` 类中，你需要添加以下API端点：

1. `POST /followedAuthor`: 这个端点接受一个 `FollowedAuthor` 对象，并调用 `followAuthor` 方法将其保存到数据库中。
2. `DELETE /followedAuthor`: 这个端点接受一个 `FollowedAuthorsId` 对象，并调用 `unfollowAuthor` 方法从数据库中删除对应的记录。
3. `GET /followedAuthor/{authorId}`: 这个端点接受一个 `Author` 的 `id`，并调用 `findFollowedByAuthor` 方法来返回这个 `Author` 关注的所有用户的列表。

这就是实现 "用户可以关注或取消关注其他用户，还可以查看他/她已关注的用户列表" 这个功能的大体步骤。这个过程可能需要根据你的具体需求进行一些调整，比如你可能需要添加一些错误处理的逻辑，或者在保存和删除 `FollowedAuthor` 对象前进行一些权限检查。





注：这里我这几个类可能暂时实现没有实现完全同步

---



5. 用户可以创建帖子。


在你的服务层（Service layer）和控制层（Controller layer）中，你需要添加新的方法来实现用户创建帖子的功能。

**服务层（Service layer）**

首先，在你的PostService类中，你已经有了一个方法 `createPost` 用于保存新的帖子。这个方法将会被我们在控制层调用。

```
public Post createPost(Post post) {
    return postRepository.save(post);
}
```

**控制层（Controller layer）**

然后，在PostController类中，你需要添加一个新的方法来处理HTTP POST请求，以创建新的帖子。这个方法将会接受一个Post对象作为请求体（request body），然后调用`createPost`方法来保存新的帖子。之后，它将返回一个HTTP响应，表示帖子是否已经成功创建。

这个方法可能会像下面这样：

```
@PostMapping("/posts")
public ResponseEntity<Post> createPost(@RequestBody Post post) {
    try {
        Post _post = postService.createPost(post);
        return new ResponseEntity<>(_post, HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

注意，这只是一个基本的实现，你可能需要根据你的具体需求来修改这些方法。例如，你可能需要验证请求中的数据，处理可能出现的错误，或者添加权限控制来限制谁可以创建帖子。





---



6. 用户可以回复帖子或回复回复。

实现用户回复帖子或回复回复的功能涉及到`Reply`和`SecondaryReply`这两个实体。首先，我们需要明确用户回复帖子或回复回复的业务逻辑。

1. **用户回复帖子**：当用户回复帖子时，我们需要创建一个新的`Reply`实体，这个实体需要包含用户ID，帖子ID，以及回复内容。这个过程会涉及到`ReplyService`中的新方法（例如，`addReply`），该方法需要接受一个新的`Reply`实体，并将其保存到数据库中。
2. **用户回复回复**：当用户回复另一个回复时，我们需要创建一个新的`SecondaryReply`实体。这个实体需要包含用户ID，原始回复ID，以及回复内容。这个过程会涉及到`SecondaryReplyService`中的新方法（例如，`addSecondaryReply`），该方法需要接受一个新的`SecondaryReply`实体，并将其保存到数据库中。

在控制器层面，你需要为这两个操作创建新的API端点。例如，在`ReplyController`中，你可能需要添加一个POST请求的API端点（例如，`/replies`），用于接收客户端的新回复请求。在`SecondaryReplyController`中，你可能需要添加一个类似的API端点（例如，`/secondaryreplies`），用于接收客户端的新回复请求。

在处理这些请求时，你需要从请求体中提取出必要的信息（例如，用户ID，帖子ID或回复ID，以及回复内容），然后使用这些信息来创建新的`Reply`或`SecondaryReply`实体，并调用相应的服务类方法将这些实体保存到数据库中。





好像没有什么报错可以考虑的？



一个基本的实现，它假设客户端会发送一个包含所有必要信息的请求体，服务端会将这些信息直接保存到数据库中。在实际的应用中，你可能需要对这个过程进行更多的控制和验证，例如，检查用户的身份，确保他们有权限执行这个操作，检查帖子和回复的存在性等。



7. 用户可以查看他/她自己的帖子和回复。

好的，以下是对应的服务类和控制器类的更新代码：

首先，我们在`AuthorService`类中添加两个新的方法：

```
javaCopy code@Service
public class AuthorService {
    // ...existing fields and methods...

    public List<Post> findPostsByAuthor(Author author) {
        return postRepository.findByAuthor(author);
    }

    public List<Reply> findRepliesByAuthor(Author author) {
        return replyRepository.findByAuthor(author);
    }

    public List<SecondaryReply> findSecondaryRepliesByAuthor(Author author) {
        return secondaryReplyRepository.findByAuthor(author);
    }
}
```

然后，我们在`AuthorController`类中添加两个新的API接口：

```
javaCopy code@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    // ...existing fields and methods...

    @GetMapping("/{id}/posts")
    public List<Post> findPostsByAuthor(@PathVariable Long id) {
        Author author = authorService.findById(id);
        return authorService.findPostsByAuthor(author);
    }

    @GetMapping("/{id}/replies")
    public List<Reply> findRepliesByAuthor(@PathVariable Long id) {
        Author author = authorService.findById(id);
        return authorService.findRepliesByAuthor(author);
    }

    @GetMapping("/{id}/secondaryReplies")
    public List<SecondaryReply> findSecondaryRepliesByAuthor(@PathVariable Long id) {
        Author author = authorService.findById(id);
        return authorService.findSecondaryRepliesByAuthor(author);
    }
}
```

这样，通过URL `/api/authors/{id}/posts`、`/api/authors/{id}/replies` 和 `/api/authors/{id}/secondaryReplies`，我们就可以分别获取到指定作者的帖子、回复和二级回复。

以上代码中，我们假设`PostRepository`、`ReplyRepository` 和 `SecondaryReplyRepository` 类已经实现了 `findByAuthor(Author author)` 方法，这些方法用于获取指定作者的帖子、回复和二级回复。如果还没有实现，可以在对应的接口中添加这些方法：

```
javaCopy codepublic interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(Author author);
}

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByAuthor(Author author);
}

public interface SecondaryReplyRepository extends JpaRepository<SecondaryReply, Long> {
    List<SecondaryReply> findByAuthor(Author author);
}
```

以上代码中，我们使用了Spring Data JPA的查询方法命名规则自动生成查询，`findByAuthor` 方法会根据传入的 `Author` 对象查询出所有相关的帖子、回复或二级回复。

这些代码只是一个基础的示例，你可能需要根据实际的业务需求和数据库模型进行相应的调整和扩展。







还有一些高级功能： - 匿名回复 - 支持图片和视频播放（需要页面显示） - 屏蔽或拉黑功能 - 热门搜索列表功能 - 多参数搜索功能。任何参数可以为空或非空。例如：按时间段、关键词和类型搜索帖子。





为了实现这些功能，你需要在你的服务类和控制器类中添加新的方法。下面是一个概览：

**服务类**：

1. 在AuthorService中，添加一个方法来创建新的Author（注册新用户）。
2. 在FavoritePostService, SharedPostService 和 LikedPostService中，添加方法来处理用户收藏、分享、和点赞帖子的操作。
3. 在同样的服务类中，添加方法来查找用户收藏、分享、和点赞的帖子。
4. 在FollowedAuthorService中，添加方法来处理用户关注和取消关注其他用户的操作，以及查找用户关注的其他用户的列表。
5. 在PostService中，添加一个方法来创建新的Post。
6. 在ReplyService 和 SecondaryReplyService中，添加方法来创建新的Reply和SecondaryReply。
7. 在AuthorService, PostService, ReplyService 和 SecondaryReplyService中，添加方法来查找用户的帖子和回复。

**控制器类**：

1. 在AuthorController中，添加一个API端点来处理用户注册的请求。
2. 在FavoritePostController, SharedPostController, 和 LikedPostController中，添加API端点来处理用户收藏、分享、和点赞帖子的请求。
3. 在同样的控制器中，添加API端点来返回用户收藏、分享、和点赞的帖子。
4. 在FollowedAuthorController中，添加API端点来处理用户关注和取消关注其他用户的请求，以及返回用户关注的其他用户的列表。
5. 在PostController中，添加一个API端点来处理用户创建帖子的请求。
6. 在ReplyController 和 SecondaryReplyController中，添加API端点来处理用户创建回复的请求。
7. 在AuthorController, PostController, ReplyController, 和 SecondaryReplyController中，添加API端点来返回用户的帖子和回复。

对于更高级的功能，如匿名回复，支持图片和视频播放，屏蔽或拉黑功能，热门搜索列表功能，和多参数搜索功能，你可能需要进行更深入的设计和实现。例如，你可能需要修改你的实体类和数据库模式，添加新的服务类和控制器方法，甚至可能需要使用更复杂的技术，如全文搜索引擎。

---

以下实现数据库API接口的简要步骤：

1. **创建一个Java Spring Boot项目。**√

   Java 17

2. **在项目的`pom.xml`文件中添加必要的依赖，例如Spring Web, Spring Data JPA和PostgreSQL。**√ 

   可以随时进行修改

   

   在您提供的`pom.xml`文件中，您添加了以下依赖项和插件：

   依赖项：

   1. `spring-boot-starter`：这是一个核心Spring Boot依赖，包括自动配置支持、Spring Boot配置文件处理和应用程序启动器。
   2. `spring-boot-starter-test`：这个依赖项用于编写Spring Boot应用程序的测试，包括JUnit、Spring Test和其他用于测试的库。

   插件：

   1. `spring-boot-maven-plugin`：这个插件为Spring Boot应用程序提供了Maven支持，包括创建可执行的jar文件、提供内置的应用程序服务器等。

   您当前正在使用的Spring Boot版本是`3.0.6`，Java版本是`17`。如果您需要添加其他依赖项，例如与数据库交互、创建RESTful API等，可以继续修改`pom.xml`文件。

   

   以下是一个汇总后的依赖项列表，涵盖了您提到的高级要求：

   1. `spring-boot-starter-web`：用于构建web应用程序，包括RESTful应用程序。
   2. `spring-boot-starter-data-jpa`：用于简化数据访问层（如数据库操作），包括对JPA的支持。
   3. PostgreSQL驱动程序：用于连接到PostgreSQL数据库。
   4. ~~OpenGauss驱动程序：用于连接到OpenGauss数据库。~~
   5. `spring-boot-starter-thymeleaf`：用于创建基于网页的应用程序，使用Thymeleaf模板引擎。
   6. `spring-boot-starter-security`：用于添加安全性和权限管理功能。
   7. `spring-boot-starter-cache`：用于实现缓存功能。
   8. `spring-boot-starter-mail`：用于发送邮件功能。
   9. `spring-boot-starter-actuator`：用于监控和管理应用程序。
   10. `HikariCP`：用于数据库连接池。

   

3. **在`application.properties`文件中配置数据库连接信息。**√ 

   最后进行账户和密码的修改

   

4. **为数据库中的表结构创建实体类 关系类。**√ 

   Getters and setters 还没有添加

   

   Q：我注意到你加了Getters and setters的注释？

   A：是的，我在这些实体类示例中添加了“Getters and setters”的注释，以提醒您需要为这些实体类添加相应的getter和setter方法。getter和setter方法是Java类中的一种常用编程范式，用于访问和修改对象的属性。

   例如，对于Author实体类中的`authorName`属性，您需要添加以下getter和setter方法：

   ```
   javaCopy codepublic String getAuthorName() {
       return authorName;
   }
   
   public void setAuthorName(String authorName) {
       this.authorName = authorName;
   }
   ```

   同样，您需要为其他实体类中的属性添加相应的getter和setter方法。这样，您可以在其他类中访问和修改这些实体类的属性，同时保持良好的封装性。在某些情况下，您可能还需要添加一些其他方法，以便在实体类之间执行特定的操作或逻辑。

   

   在Hibernate和JPA中，一个**嵌入类**（Embeddable class）或复合主键类是一个特殊的类，它作为实体类的一部分被持久化。这种类不会单独映射到数据库表，而是它的属性会映射到包含它的实体类的数据库表中。

   这种情况常常出现在你有一个实体，它的一部分属性共享相同的关联或嵌入类型。例如，你可能有一个“用户”实体，用户有家庭地址和办公地址，这两个地址都可以用相同的“地址”类型来表示。在这种情况下，你可以创建一个嵌入的“地址”类，并在“用户”类中包含两个地址实例。

   嵌入类在复合主键的情况下也非常有用。在你的例子中，你有多个实体类，它们的主键由多个属性组成。例如，“FollowedAuthors”类的主键由“authorId”和“followerAuthorId”两个属性组成。你可以将这两个属性放入一个嵌入类中，并在“FollowedAuthors”类中使用这个嵌入类作为主键。

   所以，当我们说嵌入类被“内嵌”到其他类中，我们是指嵌入类的属性被直接映射到包含它的实体类的数据库表中，而不是映射到它自己的表中。

   

5. **为实体类创建Spring Data JPA存储库，以处理CRUD操作**。√ 

   

   (注：似乎这样就不用 Getters and setters ？)

   Q：创建Spring Data JPA存储库来处理实体类的CRUD操作。这个是干嘛的

   Spring Data JPA存储库（Repository）是一个抽象层，用于处理实体类的CRUD（创建、读取、更新、删除）操作。存储库可以帮助你简化数据访问层的代码，因为它们自动实现了许多常见的数据操作。这样，你可以专注于编写业务逻辑，而不是手动编写SQL查询和数据访问代码。

   在Spring Data JPA中，存储库通常通过扩展`JpaRepository`接口来定义。`JpaRepository`是一个泛型接口，接受两个类型参数：

   1. 实体类的类型（例如，`Author`）。
   2. 实体类主键的类型（例如，`String`）。

   例如，对于`Author`实体类，你可以创建一个名为`AuthorRepository`的接口，并扩展`JpaRepository`：

   ```
   javaCopy codeimport org.springframework.data.jpa.repository.JpaRepository;
   
   public interface AuthorRepository extends JpaRepository<Author, String> {
   }
   ```

   通过扩展`JpaRepository`，`AuthorRepository`接口会自动获得大量用于操作`Author`实体的方法，例如`save()`（用于创建和更新实体）、`findById()`（用于查找实体）和`delete()`（用于删除实体）。这些方法提供了基本的CRUD操作，因此你不需要手动实现它们。

   要使用存储库，只需在服务类中注入一个存储库实例，然后在业务逻辑中调用存储库方法。例如：

   ```
   javaCopy codeimport org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Service;
   
   @Service
   public class AuthorService {
   
       @Autowired
       private AuthorRepository authorRepository;
   
       public Author registerNewUser(Author author) {
           // 在这里添加业务逻辑，例如检查用户名是否已经存在等
           return authorRepository.save(author);
       }
   }
   ```

   在这个示例中，`registerNewUser()`方法使用`AuthorRepository`的`save()`方法来保存`Author`实体。`save()`方法会根据需要创建或更新实体，所以你不需要手动编写SQL插入或更新语句。类似地，你可以使用`AuthorRepository`的其他方法来实现其他CRUD操作。

   

6. **创建服务类，处理业务逻辑**。√

   Q:

   为实体类创建Spring Data JPA存储库，以处理CRUD操作的接口需要传入服务类吗

   A:

   实际上，Spring Data JPA存储库（Repository）是被服务类（Service）引用和使用的，而不是反过来。存储库主要负责数据的访问和操作，而服务类则包含业务逻辑，并通过调用存储库的方法来与数据库交互。因此，存储库并不需要知道服务类。

   例如，当你在服务类中调用一个CRUD操作（如`save()`，`delete()`，`findById()`等）时，实际上你是在调用存储库接口中定义的这些方法。Spring Data JPA会自动提供这些方法的实现，因此你不需要手动编写它们的实现代码。

   这就是为什么你会在服务类中看到如下代码：

   ```
   javaCopy code@Autowired
   private AuthorRepository authorRepository;
   ```

   这行代码的意思是，Spring框架会自动寻找并注入一个`AuthorRepository`的实例，你可以直接使用这个实例调用其方法，而无需手动创建这个实例。

   总的来说，服务类需要存储库来执行数据库操作，但存储库并不需要服务类。

   

   

   现在我们已经为所有实体类创建了相应的服务类，完成了基本的 CRUD 操作。当然，这只是最基本的服务类，如果有更复杂的业务逻辑需求，还需要在这些基础上进行扩展。

   

   对于每个服务类，我们确实需要一个构造函数来注入对应的存储库。对于上述的每个服务类，我们应该添加一个构造函数来实现这一点。以下是对AuthorService的修改：

   ```
   javaCopy code@Service
   public class AuthorService {
       private final AuthorRepository authorRepository;
   
       @Autowired
       public AuthorService(AuthorRepository authorRepository) {
           this.authorRepository = authorRepository;
       }
   
       // 其他方法...
   }
   ```

   同样，其他的服务类也应该添加类似的构造函数来注入相应的存储库。这是因为我们依赖于Spring的依赖注入（Dependency Injection）功能来提供所需的存储库实例。这样，当Spring创建一个服务类的实例时，它会自动提供一个存储库实例，我们可以在服务类中使用这个实例来访问数据库。

7. **创建控制器类，处理HTTP请求**。√

   HTTP（超文本传输协议）是一种用于传输超媒体文档（如HTML）的应用层协议。它是为Web浏览器与Web服务器之间的通信而设计的，但也可以用于其他目的。HTTP遵循经典的客户端-服务器模型，客户端打开一个连接以发出请求，然后等待直到收到服务器的响应。

   来自客户端的HTTP请求就是从客户端（例如，一个Web浏览器或者其它的HTTP客户端）发出去的请求。这个请求可以是获取某个页面的内容（GET请求），提交表单数据（POST请求），或者其它的HTTP方法（如PUT、DELETE等）。

   在我们的上下文中，客户端可能是一个前端应用，例如一个使用React或Vue.js编写的Web应用，或者是一个移动应用。这个应用会发送HTTP请求到我们的后端服务（这里是Spring Boot应用）来获取数据（例如获取所有作者的列表），或者修改数据（例如添加一个新的作者，或者更新一个已有的作者的信息）。

   

   

8. **在控制器中实现具体的API接口，并调用相应的服务类方法**。

   是的，你已经定义了服务类和控制器类，下一步就是在控制器类中实现具体的API接口。在这些接口中，你将调用服务类的方法来实现你的业务逻辑。

   在Spring框架中，控制器类负责处理HTTP请求，并调用服务类中的方法。服务类则负责处理业务逻辑，通常会调用数据存储库类来访问和修改数据库。

   你已经在控制器类中定义了基本的CRUD操作的接口。如果你的应用有更复杂的业务需求，你可能需要在服务类和控制器类中定义更多的方法。

   例如，如果你想实现一个获取所有在特定城市发布的帖子的接口，你可能需要在`PostService`类中定义一个新的方法，例如`findAllPostsByCity`。然后在`PostController`类中定义一个新的API接口，例如`/posts/city/{cityName}`，并在这个接口的处理函数中调用`PostService`的`findAllPostsByCity`方法。

9. **根据需要，在服务类中添加业务逻辑**。

   是的，服务类（Service Class）是在Spring框架中处理业务逻辑的地方。这些业务逻辑可能包括数据验证、计算和转换，以及调用存储库（Repository）方法进行数据库操作等。

   例如，你可能会有一个服务类方法，该方法需要先检查输入的数据是否满足某些条件，然后再调用存储库方法进行数据库操作。或者，你可能需要在服务类中实现某些复杂的计算逻辑，这些计算逻辑需要使用到存储库中的多个方法。

   需要注意的是，服务类应该尽量避免直接处理HTTP请求和响应。这是因为这些任务通常由控制器类（Controller Class）来处理。服务类应该主要关注业务逻辑，而不是HTTP协议的细节。

   这样的设计有助于保持代码的清晰和可维护。如果你的业务逻辑和HTTP处理逻辑混在一起，可能会导致代码难以理解和修改。通过将这两部分逻辑分开，可以使代码更容易测试和复用。

这些步骤将帮助你实现基本的数据库API接口。你可能需要根据项目需求调整或扩展这些步骤。



---



### 1.2 功能要求：

需要使用通用编程语言与数据库交互，以满足1.1节中提到的所有要求。为了测试所有API，并提供必要的输入数据和显示结果集，您应该提供一种与您的程序交互的接口。接口可以是：（这里接口四选一即可 卷了多接口没有额外分数）

- 基于命令行的应用程序，用于输入和输出。

- HTTP/RESTful接口服务。√

  

- 基于GUI的桌面应用程序。

- 基于网页的。

准备测试数据：将项目1中的两个json文件作为测试数据，并将它们存储在项目2的数据库中。（包含project1）

## 2. 高级要求 (30%/61%)

如果您想获得任何高级要求的满分，请尝试通过提供高质量的解决方案来展示自己。

- 使用open gauss数据库完成项目（最多5%）。（安装上虚拟机完成项目）
  https://edu.hicomputing.huawei.com/teaching
  https://www.modb.pro/db/611481

- 在Section 1中的基本要求的基础上，进一步提高API的可用性，以接受更灵活类型的请求。您可以考虑比Section 2中提出的更多要求，并实现新的要求。例如（最多15%）：

  - 匿名回复
  - 支持图片和视频播放（需要页面显示）
  - 屏蔽或拉黑功能
  - 热门搜索列表功能
  - 多参数搜索功能。任何参数可以为空或非空。例如：按时间段、关键词和类型搜索帖子。
  - 其他

- 封装功能并实现一个真正的后端服务器，而不是几个独立的脚本或程序。服务器应提供基于套接字的通信或HTTP/RESTful Web服务（最多10%）。

- 应用数据库连接池（最多2%）。

  - 大数据管理（最多4%）。
  - 页面显示设计（最多4%）。
  - 用于数据展示的实用且美观的GUI设计或网页设计（最多4%）。
  - 基于命令行的输入和输出格式显示精美（最多2%）。
  - 合理使用用户权限、存储过程、索引和视图（最多5%）。
  - 支持高并发并进行适当的压力测试（最多10%）。

  ## 3. 提交内容

  请在2022年6月2日23:55之前提交您的报告和附件，包括：

  - 您编写的所有脚本。
  - 不超过12页的报告。报告中应包括以下内容：
    - 小组基本信息：请遵循项目1中描述的相同要求。
    - 代码的API规范：请描述接口的目的和用途（为防止报告过长，每个API可能只需用1-2句话描述）。另外，您需要说明参数和返回值的类型及含义。您可以参考成熟开源项目的任何API文档，了解如何组织接口规范。
    - 如果您完成了任何高级要求，请描述您完成了什么以及如何完成的。