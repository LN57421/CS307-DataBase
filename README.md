# CS307-Project2 数据库应用程序API指南

### 主要贡献者、分工： 

12110416 刘家宝：搭建API框架，更改表结构以适应API，检查API

12112712 莫羡瑜：搭建Vue框架，结合前端更改后端API，检查API

实验课时：周四三四节     贡献比：50%/50%

## 前言及配置

本文档旨为带有API的数据库应用程序提供API接口描述，并描述高级功能的实现

使用语言：java 17;  导入数据：python 3.9.12;  前端语言：Js，css，html

项目整体框架：spring-boot + vue + mybatis-plus + postgresql/opengauss

## 所有API实现

### Homepage Controller：前端网页显示的API

1. **登录**
   - 参数：`String authorName`，表示作者的用户名；`String password`，表示作者的密码。
   - 返回值：`ResponseEntity<Object>`，表示登录操作的结果。如果用户名不存在，则返回状态码为204（No Content）。如果用户名存在且密码匹配，则返回作者的信息。如果用户名存在但密码不匹配，则返回状态码为403（Forbidden）。
2. **获取当前论坛所有帖子**
   - 参数：`String authorId`，表示要获取帖子的作者ID；`boolean login`，表示是否已登录。
   - 返回值：`ResponseEntity<Object>`，表示根据作者ID获取到的所有帖子列表。如果已登录且存在黑名单列表，则从帖子列表中移除被加入黑名单的作者的帖子。如果获取帖子成功，则返回帖子列表。
3. **获取热搜帖子**
   - 参数：`String authorId`，表示要获取帖子的作者ID；`boolean login`，表示是否已登录。
   - 返回值：`ResponseEntity<Object>`，表示获取到的热搜帖子列表。如果已登录且存在黑名单列表，则从帖子列表中移除被加入黑名单的作者的帖子。如果获取热搜帖子成功，则返回帖子列表。
4. **根据时间搜索帖子**

   - 参数：`Timestamp startTime`，表示起始时间；`Timestamp endTime`，表示结束时间；`String authorId`，表示作者ID；`boolean login`，表示是否已登录。

   - 返回值：`ResponseEntity<Object>`，表示根据时间范围搜索到的帖子列表。如果已登录且存在黑名单列表，则从帖子列表中移除被加入黑名单的作者的帖子。如果搜索成功，则返回帖子列表。
5. **按文章标题关键词搜索帖子**、**按文章内容关键词搜索帖子**

   - 参数：`String[] keyWords`，表示文章或者内容关键词数组；`String authorId`，表示作者ID；`boolean login`，表示是否已登录。

   - 返回值：`ResponseEntity<Object>`，表示根据关键词搜索到的帖子列表。如果已登录且存在黑名单列表，则从帖子列表中移除被加入黑名单的作者的帖子。如果搜索成功，则返回帖子列表。
6. **按文章类型搜索帖子**

   - 参数：`String[] Categories`，表示文章类型的数组；`String authorId`，表示作者ID；`boolean login`，表示是否已登录。

   - 返回值：`ResponseEntity<Object>`，表示根据文章类型搜索到的帖子列表。如果已登录且存在黑名单列表，则从帖子列表中移除被加入黑名单的作者的帖子。如果搜索成功，则返回帖子列表。
7. **获取单个帖子的所有具体信息**

   - 参数：`String authorId`，表示作者ID；`int id`，表示帖子ID。

   - 返回值：`ResponseEntity<Object>`，表示帖子的详细信息。返回结果包括帖子本身的信息、帖子的回复列表、回复的二级回复列表、当前用户对帖子的喜欢状态、当前用户对帖子的收藏状态以及当前用户与帖子的分享关系。
8. **返回收藏的帖子列表**、**返回喜欢的帖子列表**、**返回分享的帖子列表**

   - 参数：`String authorId`，表示作者ID。

   - 返回值：`ResponseEntity<Object>`，表示用户收藏的喜欢的或者分享的帖子列表。
9. **获取单个作者的信息**

   - 参数：`String authorId`，表示作者ID；`String followId`，表示关注者ID。

   - 返回值：`ResponseEntity<Object>`，表示关注状态。如果关注者ID在作者的关注列表中，返回`true`；否则返回`false`。
10. **获取作者关注列表**
    - 参数：`String authorId`，表示作者ID。
    - 返回值：`ResponseEntity<Object>`，表示作者关注的作者列表和对应的关注状态。返回结果包括关注的作者列表和一个布尔型数组，表示每个作者是否被当前作者关注。
11. **获取作者拉黑列表**
    - 参数：`String authorId`，表示作者ID。
    - 返回值：`ResponseEntity<Object>`，表示作者拉黑的作者列表和对应的拉黑状态。返回结果包括拉黑的作者列表和一个布尔型数组，表示每个作者是否被当前拉黑关注。
12. **获取个人创建的帖子**
    - 参数：`String authorId`，表示作者ID。

    - 返回值：`ResponseEntity<Object>`，表示作者创建的帖子列表。返回结果包括作者创建的帖子列表，每个帖子附带作者和城市信息。
13. **获取个人回复的帖子**

    - 参数：`String authorId`，表示作者ID。

    - 返回值：`ResponseEntity<Object>`，表示作者回复过的帖子列表。返回结果包括作者回复过的帖子列表，每个帖子附带作者和城市信息。

### Authors Controller

1. **获取所有authors**
   - 参数：无。
   - 返回值：`List<Author>`，包含所有作者的列表。每个作者对象包括作者的名称、密钥、注册时间和电话号码。
2. **创建新的author**
   - 参数：`Author authorNew`，包含新作者的信息。其中，`authorNew`对象包括作者的名称、密钥和电话号码。
   - 返回值：`ResponseEntity<Void>`，表示创建操作的结果。如果成功创建了新作者，则返回状态码为201（Created）。如果已存在同名的作者，则返回状态码为409（Conflict）。如果创建失败，则返回状态码为500（Internal Server Error）。

### Blacklist Controller 

1. **将用户加入黑名单**
   - 参数：`String authorId`，表示执行操作的用户ID；`String blockedAuthorId`，表示要加入黑名单的用户ID。
   - 返回值：`ResponseEntity<Void>`，表示加入黑名单操作的结果。如果成功加入黑名单，则返回状态码为201（Created）。如果加入黑名单操作失败，则返回状态码为500（Internal Server Error）。
2. **将用户从黑名单中移除**
   - 参数：`String blockedAuthorId`，表示要从黑名单中移除的用户ID。
   - 返回值：`ResponseEntity<Void>`，表示移除黑名单操作的结果。如果成功移除黑名单，则返回状态码为200（OK）。如果移除黑名单操作失败，则返回状态码为500（Internal Server Error）。
3. **获取某用户的黑名单列表**
   - 参数：`String authorId`，表示要获取黑名单列表的用户ID。
   - 返回值：`List<Blacklist>`，包含指定用户的黑名单列表。每个黑名单对象包括黑名单记录的ID、执行操作的用户ID和被加入黑名单的用户ID。

### Category Controller

1. **创建新的分类**
   - 参数：`String category_name`，表示要创建的分类名称。
   - 返回值：`ResponseEntity<Void>`，表示创建分类操作的结果。如果成功创建分类，则返回状态码为201（Created）。如果创建分类操作失败，则返回状态码为500（Internal Server Error）。
2. **获取所有分类**
   - 参数：无。
   - 返回值：`List<Category>`，包含所有的分类对象。
3. **根据ID获取分类**
   - 参数：无。
   - 返回值：`ResponseEntity<Category>`，表示根据ID获取分类的结果。如果成功获取分类，则返回状态码为200（OK）以及对应的分类对象。如果未找到分类，则返回状态码为404（Not Found）。
4. **删除某个分类**
   - 参数：`Integer categoryId`，表示要删除的分类ID。
   - 返回值：`ResponseEntity<Void>`，表示删除分类操作的结果。如果成功删除分类，则返回状态码为200（OK）。如果删除分类操作失败，则返回状态码为500（Internal Server Error）。

### City Controller

1. **创建一个新的城市**
   - 参数：`String cityName`，表示要创建的城市名称；`String cityState`，表示城市所属的州或省份名称。
   - 返回值：`ResponseEntity<Void>`，表示创建城市操作的结果。如果城市名称已存在，则返回状态码为409（Conflict）。如果成功创建城市，则返回状态码为201（Created）。如果创建城市操作失败，则返回状态码为500（Internal Server Error）。
2. **删除一个城市**
   - 参数：`String cityName`，表示要删除的城市名称。
   - 返回值：`ResponseEntity<Void>`，表示删除城市操作的结果。如果未找到该城市名称，则返回状态码为404（Not Found）。如果成功删除城市，则返回状态码为200（OK）。如果删除城市操作失败，则返回状态码为500（Internal Server Error）。
3. **获取所有城市**
   - 参数：无。
   - 返回值：`ResponseEntity<List<City>>`，表示获取所有城市的结果。如果城市列表为空，则返回状态码为404（Not Found）。如果成功获取城市列表，则返回状态码为200（OK）以及包含城市对象的列表。

 ### FavoritePost Controller

1. **author收藏了某个帖子**
   - 参数：`int postId`，表示要收藏的帖子的ID；`String authorId`，表示收藏者的作者ID。
   - 返回值：`ResponseEntity<Void>`，表示收藏关系创建操作的结果。如果已经存在该收藏关系，则返回状态码为409（Conflict）。如果成功创建收藏关系，则返回状态码为201（Created）。如果创建收藏关系操作失败，则返回状态码为500（Internal Server Error）。
2. **author不收藏某个帖子**
   - 参数：`int postId`，表示要取消收藏的帖子的ID；`String authorId`，表示取消收藏的作者ID。
   - 返回值：`ResponseEntity<Void>`，表示取消收藏关系删除操作的结果。如果不存在该收藏关系，则返回状态码为409（Conflict）。如果成功删除取消收藏关系，则返回状态码为201（Created）。如果删除取消收藏关系操作失败，则返回状态码为500（Internal Server Error）。
3. **author收藏的所有帖子**
   - 参数：`String authorId`，表示要获取帖子列表的作者ID。
   - 返回值：`ResponseEntity<List<FavoritePost>>`，表示获取收藏帖子列表的结果。如果收藏帖子列表为空，则返回状态码为404（Not Found）。如果成功获取收藏帖子列表，则返回状态码为200（OK）以及包含帖子对象的列表。
4. **查找author有没有收藏某个帖子**
   - 参数：`int postId`，表示要查找的帖子的ID；`String authorId`，表示要查找的作者ID。
   - 返回值：`ResponseEntity<List<FavoritePost>>`，表示查找收藏帖子关系的结果。如果收藏帖子关系列表为空，则返回状态码为404（Not Found）。如果成功找到收藏帖子关系，则返回状态码为200（OK）以及包含帖子对象的列表。

### Follows Controller

1. **当前用户关注某个用户**
   - 参数：`String author_id`，表示当前用户的作者ID；`String follower_author_id`，表示要关注的用户的作者ID。
   - 返回值：`ResponseEntity<Void>`，表示关注关系创建操作的结果。如果已经存在该关注关系，则返回状态码为409（Conflict）。如果成功创建关注关系，则返回状态码为201（Created）。如果创建关注关系操作失败，则返回状态码为500（Internal Server Error）。
2. **当前用户不再关注某个用户**
   - 参数：`String author_id`，表示当前用户的作者ID；`String follower_author_id`，表示要取消关注的用户的作者ID。
   - 返回值：`ResponseEntity<Void>`，表示取消关注关系删除操作的结果。如果不存在该关注关系，则返回状态码为409（Conflict）。如果成功删除取消关注关系，则返回状态码为201（Created）。如果删除取消关注关系操作失败，则返回状态码为500（Internal Server Error）。
3. **查看当前用户的所有关注**
   - 参数：`String author_id`，表示要查看关注列表的用户的作者ID。
   - 返回值：`ResponseEntity<List<FollowedAuthor>>`，表示获取关注列表的结果。如果关注列表为空，则返回状态码为404（Not Found）。如果成功获取关注列表，则返回状态码为200（OK）以及包含关注用户对象的列表。
4. **查看当前用户是否有某个特定关注**
   - 参数：`String author_id`，表示要查看关注列表的用户的作者ID；`String follower_author_id`，表示要查看的关注用户的作者ID。
   - 返回值：`ResponseEntity<List<FollowedAuthor>>`，表示查找关注关系的结果。如果关注关系列表为空，则返回状态码为404（Not Found）。如果成功找到关注关系，则返回状态码为200（OK）以及包含关注用户对象的列表。

### LikedPost Controller

1. **用户点赞某个帖子**
   - 参数：`int postId`，表示要点赞的帖子的ID；`String authorId`，表示点赞的用户的作者ID。
   - 返回值：`ResponseEntity<Void>`，表示点赞关系创建操作的结果。如果已经存在该点赞关系，则返回状态码为409（Conflict）。如果成功创建点赞关系，则返回状态码为201（Created）。如果创建点赞关系操作失败，则返回状态码为500（Internal Server Error）。
2. **用户取消点赞某个帖子**
   - 参数：`int postId`，表示要取消点赞的帖子的ID；`String authorId`，表示取消点赞的用户的作者ID。
   - 返回值：`ResponseEntity<Void>`，表示取消点赞关系删除操作的结果。如果不存在该点赞关系，则返回状态码为409（Conflict）。如果成功删除取消点赞关系，则返回状态码为201（Created）。如果删除取消点赞关系操作失败，则返回状态码为500（Internal Server Error）。
3. **查看用户点赞的所有帖子**
   - 参数：`String authorId`，表示要查看点赞列表的用户的作者ID。
   - 返回值：`ResponseEntity<List<LikedPost>>`，表示获取点赞列表的结果。如果点赞列表为空，则返回状态码为404（Not Found）。如果成功获取点赞列表，则返回状态码为200（OK）以及包含点赞帖子对象的列表。
4. **查看用户是否点赞某个特定帖子**
   - 参数：`int postId`，表示要查看的帖子的ID；`String authorId`，表示要查看点赞关系的用户的作者ID。
   - 返回值：`ResponseEntity<List<LikedPost>>`，表示查找点赞关系的结果。如果点赞关系列表为空，则返回状态码为404（Not Found）。如果成功找到点赞关系，则返回状态码为200（OK）以及包含点赞帖子对象的列表。

### PostCategory Controller

1. **创建文章分类**
   - 参数：`Integer postId`，表示要分类的文章的ID；`Integer categoryId`，表示文章分类的ID。
   - 返回值：`ResponseEntity<Void>`，表示文章分类创建操作的结果。如果成功创建文章分类，则返回状态码为201（Created）。如果创建文章分类操作失败，则返回状态码为500（Internal Server Error）。
2. **通过文章ID查找文章分类**
   - 参数：`Integer postId`，表示要查找分类的文章的ID。
   - 返回值：`List<PostCategory>`，表示根据文章ID获取到的文章分类列表。
3. **通过分类ID查找文章分类**
   - 参数：`Integer categoryId`，表示要查找的分类的ID。
   - 返回值：`List<PostCategory>`，表示根据分类ID获取到的文章分类列表。
4. **删除文章分类**
   - 参数：`Integer postId`，表示要删除分类的文章的ID；`Integer categoryId`，表示要删除的分类的ID。
   - 返回值：`ResponseEntity<Void>`，表示删除文章分类操作的结果。如果成功删除文章分类，则返回状态码为200（OK）。如果删除文章分类操作失败，则返回状态码为500（Internal Server Error）。

### Posts Controller

1. **创建新的帖子**
   - 参数：`String authorId`，表示帖子的作者ID；`String state`，表示帖子所属的州；`Post post`，表示要创建的帖子对象，包含标题、内容和发布城市等信息。
   - 返回值：`ResponseEntity<Void>`，表示帖子创建操作的结果。如果成功创建帖子，则返回状态码为201（Created）。如果创建帖子操作失败，则返回状态码为500（Internal Server Error）。
2. **查找用户创建的所有帖子**
   - 参数：`String authorId`，表示要查找帖子的作者ID。
   - 返回值：`List<Post>`，表示根据作者ID获取到的该用户创建的所有帖子列表。

### Replies Controller

1. **创建一条新回复**
   - 参数：`String authorId`，表示回复的作者ID；`Reply reply`，表示要创建的回复对象，包含回复内容和所属帖子ID等信息；`boolean is_anonymous`，表示回复是否匿名。
   - 返回值：`ResponseEntity<Void>`，表示回复创建操作的结果。如果成功创建回复，则返回状态码为201（Created）。如果创建回复操作失败，则返回状态码为500（Internal Server Error）。
2. **查看所有的回复**
   - 参数：`String authorId`，表示要查找回复的作者ID。
   - 返回值：`List<Reply>`，表示根据作者ID获取到的该用户发表的所有回复列表。
3. **针对某一条回复进行删除**
   - 参数：`String authorId`，表示回复的作者ID；`int replyId`，表示要删除的回复ID。
   - 返回值：`ResponseEntity<Void>`，表示回复删除操作的结果。如果成功删除回复，则返回状态码为200（OK）。如果删除回复操作失败，则返回状态码为500（Internal Server Error）。

### Secondary Replies Controller

1. **创建一条新的次级回复**
   - 参数：`String authorId`，表示次级回复的作者ID；`SecondaryReply secondaryReply`，表示要创建的次级回复对象，包含次级回复的内容和所属回复ID等信息；`boolean is_anonymous`，表示次级回复是否匿名。
   - 返回值：`ResponseEntity<Void>`，表示次级回复创建操作的结果。如果成功创建次级回复，则返回状态码为201（Created）。如果创建次级回复操作失败，则返回状态码为500（Internal Server Error）。
2. **查看所有的次级回复**
   - 参数：`String authorId`，表示要查找次级回复的作者ID。
   - 返回值：`List<SecondaryReply>`，表示根据作者ID获取到的该用户发表的所有次级回复列表。
3. **删除一条次级回复**
   - 参数：`String authorId`，表示次级回复的作者ID；`int secondaryReplyId`，表示要删除的次级回复ID。
   - 返回值：`ResponseEntity<Void>`，表示次级回复删除操作的结果。如果成功删除次级回复，则返回状态码为200（OK）。如果删除次级回复操作失败，则返回状态码为500（Internal Server Error）。

### SharedPostController

1. **创建帖子分享关系**
   - 参数：`int postId`，表示要分享的帖子ID；`String authorId`，表示分享者的作者ID。
   - 返回值：`ResponseEntity<Void>`，表示帖子分享关系创建操作的结果。如果分享关系已存在，则返回状态码为409（Conflict）。如果成功创建帖子分享关系，则返回状态码为201（Created）。如果创建帖子分享关系操作失败，则返回状态码为500（Internal Server Error）。
2. **删除帖子分享关系**
   - 参数：`int postId`，表示要删除分享关系的帖子ID；`String authorId`，表示分享者的作者ID。
   - 返回值：`ResponseEntity<Void>`，表示帖子分享关系删除操作的结果。如果分享关系不存在，则返回状态码为409（Conflict）。如果成功删除帖子分享关系，则返回状态码为201（Created）。如果删除帖子分享关系操作失败，则返回状态码为500（Internal Server Error）。
3. **查找作者分享的所有帖子**
   - 参数：`String authorId`，表示要查找帖子分享关系的作者ID。
   - 返回值：`ResponseEntity<List<SharedPost>>`，表示根据作者ID获取到的该作者分享的所有帖子列表。如果帖子分享关系为空，则返回状态码为404（Not Found）。如果获取帖子分享关系成功，则返回帖子列表。
4. **查找作者分享的特定帖子**
   - 参数：`int postId`，表示要查找的特定帖子ID；`String authorId`，表示要查找帖子分享关系的作者ID。
   - 返回值：`ResponseEntity<List<SharedPost>>`，表示根据作者ID和帖子ID获取到的特定帖子的分享关系列表。如果帖子分享关系为空，则返回状态码为404（Not Found）。如果获取帖子分享关系成功，则返回帖子分享关系列表。

## 高级功能

### 使用open gauss

使用虚拟机实现，实现步骤如下：

1. 首先安装VMware
2. 在VMware安装centos7虚拟机
3. 在虚拟机中通过终端进行操作，安装docker
4. 在docker中拉取opengauss并配置到datagrip

### 后端服务器封装

后端使用SpringBoot框架 实现步骤如下：

1. 创建一个Java Spring Boot项目。
2. 在项目的`pom.xml`文件中添加必要的依赖，例如Spring Web, Spring Data JPA和PostgreSQL。
3. 在`application.properties`文件中配置数据库连接信息。
4. 为数据库中的表结构创建实体类 关系类。
5. 为实体类创建Mapper用于查询表，创建Controller用于封装API。
6. 测试API。

### 完善API设计

在基本API的基础上 实现以下API

1. 用户可以在注册后登录 也可以以游客方式访问 但游客只能浏览帖子不能点赞收藏回复等操作

   实现方式：为每个网页API添加log的标识 非log情况下用户无法使用某些特定API

2. 用户登陆后可以在进行回复或者二级回复时进行匿名发言

   实现方式：为回复和二级回复的表增添匿名列，默认为false，对于每个回复，创建回复或二级回复时用户需要标定其是否为匿名回复

3. 用户登陆后能够拉黑多个用户 用户可以查看其拉黑人员 并且取消拉黑

   实现方式：创建拉黑用户对应的表 实体类 以及其对应的Controller和Mapper 并实现相应方法

4. 实现了热搜榜 热搜榜实时显示点赞数最高的十个帖子

   实现方式：在Mapper中增添搜索方法 根据点赞数降序排序 同时前端进行实时更新 返回结果 

5. 用户可以通过时间区间对帖子进行搜索

   实现方式：在Mapper中增添搜索方法 用户输入参数为起始和结束的TimeStamp 返回结果

6. 用户可以进行标题和内容中包含的关键词进行搜索 关键词搜索不限个数

   实现方式：在Mapper中增添搜索方法 返回某个关键词对应的Post的list; 用户输入以逗号分隔的多个关键词 在Controller中对每个关键词对应的帖子进行搜索并加入Set 最后删除拉黑用户对应的帖子并返回

7. 用户可以通过类别进行搜索 类别不限个数

   实现方式：在Mapper中增添搜索方法 返回某个类别对应的Post的list; 用户输入以逗号分隔的多个关键词后 在Controller中对每个类别对应的帖子进行搜索并加入Set 最后删除拉黑用户对应的帖子并返回

8. 对某些API添加了安全性检验 并通过返回状态码来显示是否创建成功

   实现方式：在方法中封装HTTP响应的信息，包括响应状态码、响应头和响应体

9. 增加专门的API文档网址，为用户提供便捷的查询文档，内部集成所有API，包括对API的注释。同时为开发人员提供测试场所

   网址在localhost:9090/swagger-ui.html

### 前端网站设计

vue是一款封装了js，html与css的网页设计框架。此次项目使用vue作为前端GUI以及用户交互的框架

1.网页结构

​	使用html原生组件，如容器div，段落p等。

​	使用element官方提供的组件，例如按钮、走马灯等。

​	使用vue框架下的路由实现组件跳转

​	使用vue框架下的store存储相关信息

2.网页行为

​	使用axios向后端发送请求各个请求（get,post,delete），调用后端restfulAPI接口，获取已经处理好的json数据等

​	使用vue原生行为模式，解析json数据并呈现到前端页面上。

​	使用vue中<script></script>模块，在其中定义各种函数，并绑定到前端各个控件上，使用户操作更简便

3.网页表现

​	使用vue中css模块作为美化工具，使用element提供的美化包或自定义布局、背景等。为用户提供颇具美感的数据展示

​	使用github部分开源美化代码。

### 使用数据库连接池

通过添加依赖项我们可以很方便的使用数据库连接池 在这次项目中 我们使用到了`HikariCP`作为连接池

首先就是对数据源的配置 如下所示：

```xml
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>2.5.1</version>
</dependency>
```

接下来在`resource/application.properties`中添加如下的配置：

```ini
spring.datasource.hikari.connection-timeout=20000  //超时连接时间
spring.datasource.hikari.minimum-idle=5            // 最小空闲连接
spring.datasource.hikari.maximum-pool-size=12     // 最大连接数
spring.datasource.hikari.idle-timeout=300000     // 连接允许在池中闲置的最长时间  
spring.datasource.hikari.max-lifetime=1200000    // 连接最大生命周期
spring.datasource.hikari.auto-commit=true   // 自动提交从池中返回的连接 
```
## 总结

在本次项目中，我们进一步加深了对于数据库在实际项目中的运用，同时自主学习和使用了前后端分离的SpringBoot和Vue框架，这让我们自己对于往后能够进一步处理数据库甚至其他课程的项目有了更强的自信。希望能够在以后的项目中运用到这次项目和课程中学习到的知识，不断进步！

本次文档到此结束，感谢您的阅读！
