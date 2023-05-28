<template>
  <div id="desk">
    <el-header style="background-color: transparent;font-size: 30px">Top10!</el-header>
    <el-carousel :interval="4000" type="card" height="300px">
      <el-carousel-item v-for="(item, index) in posts" :key="index" >
        <div @click="seeMore(item.postId, item.author)" style="width: 100%;height: 100%">
          <h2 class="medium">{{ item.title }}</h2>
          <h4 class="medium">{{ "author: " + item.author.authorName }}</h4>
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  name: "DashBoard",
  data(){
    return{
      posts:{}
    }
  },
  methods:{
    getData(){
      this.$http({
        url: `${this.$store.state.uid}/HostPosts/${this.$store.state.userStatus ? true : false}`,
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
  },
  beforeMount() {
    this.getData();
  }
}
</script>

<style scoped>
@import url("//unpkg.com/element-ui@2.15.13/lib/theme-chalk/index.css");

.el-carousel__item h4{
  color: #475669;
  opacity: 0.75;
  line-height: 20px;
  text-align: center;
  margin-top: 20px;
}
.el-carousel__item h2{
  color: #475669;
  opacity: 0.75;
  line-height: 20px;
  text-align: center;
  margin-top:20%;
}

.el-carousel__item:nth-child(2n) {
  background-color: rgba(255, 255, 255, 0.95);
}

.el-carousel__item:nth-child(2n+1) {
  background-color: rgba(250, 235, 215, 0.9);
}
</style>
