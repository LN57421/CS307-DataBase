# 项目名称：2023年春季CS307课程项目

主要贡献者： 

刘家宝 莫羡瑜

### 初步ER图构建思路

   ###      主体表

1. 作者（Author）表：

   - Author's ID：主键、唯一、非空
   - Author：非空
   - Author Registration Time：非空
   - Author's Phone：唯一、非空

2. 文章（Post）表：

   - Post ID：主键、唯一、非空
   - Author ID：外键（引用作者表的Author's ID），非空
   - Title：非空
   - Content：非空
   - Posting Time：非空
   - Posting City：非空

3. 评论（Reply）表：

   - Reply ID：主键、唯一、非空
   - Reply Content：非空
   - Reply Stars：非空
   - Reply Author：非空，如果不在作者表中，需要为其生成ID及合理注册时间并添加至作者表
   - Post ID：外键（引用文章表的Post ID），非空

4. 次级评论（Secondary Reply）表：

   - Secondary Reply ID：主键、唯一、非空
   - Secondary Reply Content：非空
   - Secondary Reply Stars：非空
   - Secondary Reply Author：非空，如果不在作者表中，需要为其生成ID及合理注册时间并添加至作者表
   - Reply ID：外键（引用评论表的Reply ID），非空

5. 分类（Category）表

   - Category ID：主键、唯一、非空
   - Category：非空

   ### 关系表

6. 关注（Followed_Authors) 表

   - Author ID：主键、非空、外键（引用作者表的Author's ID）
   - Follower_Author_ID：非空、外键 （引用作者表的Author's ID）

7. 收藏（Favorited_Posts）表

   - Post ID：主键、外键（引用文章表的Post ID）、非空
   - Favorited_Author_ID：非空 、外键（引用作者表的Author's ID）

8. 分享（Shared_Posts) 表

   - Post ID：主键、外键（引用文章表的Post ID）、非空
   - Sharing_Author_ID：非空、外键（引用作者表的Author's ID）

9. 点赞（Liked_Posts）表

   - Post ID：主键、外键（引用文章表的Post ID）、非空
   - Liking_Author_ID：非空 、外键（引用作者表的Author's ID）

10. Post's Category 表

    - Post ID：主键、非空、外键（引用文章表的Post ID）
       - Category：非空、外键（引用分类表的Category ID）
