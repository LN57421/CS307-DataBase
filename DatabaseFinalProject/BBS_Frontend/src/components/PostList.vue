<template>
  <div v-if="loading">
    <el-container v-loading="true"></el-container>
    <el-dialog :visible="dialogVisible" title="Please login first!" @close="closeDialog" ></el-dialog>
  </div>
  <div v-else>
    <el-collapse accordion>
      <el-collapse-item v-for="(post, index) in posts" :key="index">
        <el-descriptions
          class="margin-top"
          title="Detail"
          :column="3"
          border
        >
          <template slot="extra">
            <el-button type="primary" size="small" @click="seeMore(post.postId, post.author)"
              >See more</el-button
            >
          </template>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-user"></i>
              Author's name
            </template>
            {{ post.author.authorName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-location-outline"></i>
              City
            </template>
            {{ post.city.cityName + ", " + post.city.cityState }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-timer"></i>
              Posting time
            </template>
            {{ post.postingTime }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-chat-line-square"></i>
              Content
            </template>
            {{ post.content }}
          </el-descriptions-item>
        </el-descriptions>
        <template slot="title">
          <el-avatar src="https://www.dmoe.cc/random.php"></el-avatar>
          {{ post.author.authorName + ": " + post.title
          }}<i class="header-icon el-icon-info"></i>
        </template>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
export default {
  name: "PostList",
  data() {
    return {
      posts: {},
      loading: false,
      dialogVisible:false,
      likeUrl:"/{authorId}/showLike/true",
      favorUrl:"/{authorId}/showFavor/true",
      shareUrl:"/{authorId}/showShare",
      createdUrl:"/{authorId}/showPost",
      repliedUrl:"/{authorId}/showReplied"
    };
  },
  methods: {
    getData(realUrl) {
      this.$http({
        url: realUrl,
        method: "get",
      })
        .then((response) => {
          console.log(response);
          this.posts = response.data;
          this.loading = false;
        })
        .catch(function (error) {
          console.log(error);
        });
    },
    seeMore(postId, author) {
      this.$router.push({ name: "post_content", params: { postId: postId, uid: this.$store.state.uid , author: author} });
    },
    showDialog() {
      this.dialogVisible = true; // 显示弹窗
    },
    closeDialog() {
      this.dialogVisible = false; // 关闭弹窗
    },
  },
  beforeMount() {
    this.loading = true;
    var realUrl = "";
    if(this.$route.params.model === "showPost"){
      realUrl = `${this.$store.state.uid != null ? this.$store.state.uid : -1}/AllPosts/${this.$store.state.userStatus ? true : false}`;
      this.getData(realUrl);
    }else if(this.$route.params.model === "like"){
      if(this.$route.params.uid == null){
        this.showDialog();
        return;
      }
      realUrl = this.likeUrl.replace("{authorId}",this.$route.params.uid);
      this.getData(realUrl);
    }else if(this.$route.params.model === "favor"){
      if(this.$route.params.uid == null){
        this.showDialog();
        return;
      }
      realUrl = this.favorUrl.replace("{authorId}",this.$route.params.uid);
      this.getData(realUrl);
    }else if(this.$route.params.model === "share"){
      if(this.$route.params.uid == null){
        this.showDialog();
        return;
      }
      realUrl = this.shareUrl.replace("{authorId}",this.$route.params.uid);
      this.getData(realUrl);
    }else if(this.$route.params.model === "createdPost"){
      if(this.$route.params.uid == null){
        this.showDialog();
        return;
      }
      realUrl = this.createdUrl.replace("{authorId}",this.$route.params.uid);
      this.getData(realUrl);
    }else if(this.$route.params.model === "repliedPost"){
      if(this.$route.params.uid == null){
        this.showDialog();
        return;
      }
      realUrl = this.repliedUrl.replace("{authorId}",this.$route.params.uid);
      this.getData(realUrl);
    }
  },
};
</script>

<style>
@import url("//unpkg.com/element-ui@2.15.13/lib/theme-chalk/index.css");
</style>
