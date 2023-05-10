# CS307 Project2 数据库应用程序（第二部分）

旨在为类似社交媒体的平台创建一套带有API的数据库应用程序

## 1. 基本要求 (70%)（性价比高且简单）

### 1.1 API规范：

为提供访问数据库系统的基本功能，需要构建一个后端库，该库公开一组**应用程序**（即使用python java等后端语言编写的程序）编程接口(API)。下面列出了每个API的一般描述。

1. 注册新用户。
2. 用户可以收藏、分享和点赞帖子。
3. 用户可以查看他/她收藏、分享和点赞的帖子。
4. 用户可以关注或取消关注其他用户，还可以查看他/她已关注的用户列表。
5. 用户可以创建帖子。
6. 用户可以回复帖子或回复回复。
7. 用户可以查看他/她自己的帖子和回复。



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