<template>
  <div class="ArticleSection">
    <div class="loading" v-if="loading">
      <el-container v-loading="true"></el-container>
    </div>
    <div class="article" v-else>
      <el-dialog :visible="dialogVisible" title="Please login first!" @close="closeDialog" ></el-dialog>
      <div slot="header" class="clearfix">
            <el-button type="info" style="float: left" plain @click="reply">Reply</el-button>
            <div v-if="share"><el-tooltip class="item" effect="dark" content="cancel" placement="top-start"><el-button type="danger" icon="el-icon-share" style="float: right" circle @click="disshareClick"></el-button></el-tooltip></div>
            <div v-else><el-button type="primary" icon="el-icon-share" style="float: right" circle @click="shareClick"></el-button></div>
            <div v-if="favor"><el-button type="success" icon="el-icon-folder-remove" style="float: right" circle @click="disfavorClick"></el-button></div>
            <div v-else><el-button type="success" icon="el-icon-folder-add" style="float: right" circle @click="favorClick"></el-button></div>
            <div v-if="like"><el-button type="warning" icon="el-icon-star-on" style="float: right" circle @click="dislikeClick"></el-button></div>
            <div v-else><el-button type="warning" icon="el-icon-star-off" style="float: right" circle @click="likeClick"></el-button></div>
      </div>
      <h1>{{ post.title }}</h1>
      <ul>
        <li>• tags:{{}}</li>
        <li>• posting time:{{ post.postingTime }}</li>
        <li>
          • author name:
          <router-link
            :to="{
              name: 'user_info',
              params: { name: post.author.authorName },
            }"
            >{{ post.author.authorName }}</router-link
          >
        </li>
      </ul>
      <div v-html="post.content" id="content"></div>
      <div v-if="isReply">
        <el-input
          type="textarea"
          placeholder="input your reply"
          v-model="firstReplyContent"
          maxlength="100"
          show-word-limit
        >
        </el-input>
        <div style="margin: 20px 0;">
          <el-button type="primary" plain @click="sendFirstReply(false)">Send</el-button>
          <el-button type="primary" @click="sendFirstReply(true)">Send_Anonymous</el-button>
        </div>
      </div>
    </div>

    <div id="reply">
      <div v-for="(key, index) in firstReply" :key="index" class="replySec">
        <el-card class="box-card">
          <el-button type="info" style="float: right" plain @click="secondReplyToReply(index)">Reply to reply</el-button>
          <el-avatar src="https://www.dmoe.cc/random.php"></el-avatar >
         {{ key.author.authorName }}
          <el-descriptions class="margin-top" :column="2" border>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-user"></i>
                Author's name
              </template>
              {{ key.author.authorName }}
            </el-descriptions-item>
                      <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-star-on"></i>
                stars
              </template>
            <section class="thumbsClass"><span>❤ {{ key.stars }}</span></section>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-chat-line-square"></i>
                Content
              </template>
              {{ key.content }}
            </el-descriptions-item>
          </el-descriptions>
          <div v-if="isSecondReply[index]">
            <el-input
              type="textarea"
              placeholder="input your reply"
              v-model="secondReplyContent[index]"
              maxlength="100"
              show-word-limit
            >
            </el-input>
            <div style="margin: 20px 0;">
              <el-button type="primary" plain @click="SendSecondReply(key.replyId, index,false)" >Send</el-button>
              <el-button type="primary" @click="SendSecondReply(key.replyId, index,true)" >Send_Anonymous</el-button>
            </div>

          </div>
          <h3>Second reply</h3>
          <div v-for="(secondReplies, secondIndex) in dealingSecondReply[index]" :key="secondIndex" class="text item">
            <el-descriptions class="margin-top" :column="2" border>
              <el-descriptions-item label="author"> {{ secondReplies.author.authorName}}</el-descriptions-item>
              <el-descriptions-item label="secondReply"> {{ secondReplies.content}}<span>❤ {{ secondReplies.stars }}</span></el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Article",
  data() {
    return {
      post: {
        author: {
          loginname: "temp", //设置默认值，防止Vue在axios未被调用前报错
        },
      },
      firstReply: {},
      secondReply: {
        type:Array//这里定义Arraytype
      },
      dealingSecondReply: {},
      loading: false,
      like: false,
      favor: false,
      share: false,
      postId: "",
      isReply:false,
      isSecondReply:[],
      firstReplyContent:"",
      secondReplyContent:[],
      dialogVisible:false,
    };
  },
  methods: {
    getData() {
      //获取文章信息
      this.$http({
        url: `${this.$route.params.uid}/postContent/${this.$route.params.postId}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "get",
      })
        .then((response) => {
          this.post = response.data[0];
          this.firstReply = response.data[1];
          this.secondReply = response.data[2];
          this.like = response.data[3];
          this.favor = response.data[4];
          this.share = response.data[5];
          console.log(response.data);
          console.log(this.firstReply);
          console.log(this.secondReply);
          console.log(this.like);
          console.log(this.favor);
          console.log(this.share);
          this.dealing();
          console.log(this.dealingSecondReply);
          this.loading = false;
          this.postId = this.$route.params.postId;
          this.isReply = false;
          this.isSecondReply=[];
          this.firstReplyContent="";
          this.secondReplyContent=[];
        })
        .catch(function (error) {
          console.log(error);
          window.alert("error code: " + error.response.status);
        });
    },
    showDialog() {
      this.dialogVisible = true; // 显示弹窗
    },
    closeDialog() {
      this.dialogVisible = false; // 关闭弹窗
    },
    dealing() {
      var point = 0;
      for(var i = 0; i < this.secondReply.length; i = i + 1){
        this.dealingSecondReply[i] = this.secondReply[i];
        // console.log(data)
        // console.log(this.secondReply[i]['replyId'])
      }
      console.log(this.dealingSecondReply['0'])
    },
    likeClick(){
      if (this.$store.state.uid == null){
        this.showDialog();
        return;
      }
      this.$http({
        url: `${this.$route.params.uid}/likedPosts/create/${this.$route.params.postId}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "post",
      }).then((response => {
        if(response.status === 201){
          this.like = true;
        }
      })).catch(error =>{
        console.log(error);
        window.alert("error code: " + error.response.status);
      })
    },
    dislikeClick(){
      this.$http({
        url: `${this.$route.params.uid}/likedPosts/delete/${this.$route.params.postId}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "delete",
      }).then((response => {
        if(response.status === 201){
          this.like = false;
        }
      })).catch(error =>{
        console.log(error);
        window.alert("error code: " + error.response.status);
      })
    },
    favorClick(){
      if (this.$store.state.uid == null){
        this.showDialog();
        return;
      }
      this.$http({
        url: `${this.$route.params.uid}/favoritePosts/create/${this.$route.params.postId}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "post",
      }).then((response => {
        if(response.status === 201){
          this.favor = true;
        }
      })).catch(error =>{
        console.log(error);
        window.alert("error code: " + error.response.status);
      })
    },
    disfavorClick(){
      this.$http({
        url: `${this.$route.params.uid}/favoritePosts/delete/${this.$route.params.postId}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "delete",
      }).then((response => {
        if(response.status === 201){
          this.favor = false;
        }
      })).catch(error =>{
        console.log(error);
        window.alert("error code: " + error.response.status);
      })
    },
    shareClick(){
      if (this.$store.state.uid == null){
        this.showDialog();
        return;
      }
      this.$http({
        url: `${this.$route.params.uid}/sharedPosts/create/${this.$route.params.postId}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "post",
      }).then((response => {
        if(response.status === 201){
          this.share = true;
        }
      })).catch(error =>{
        console.log(error);
        window.alert("error code: " + error.response.status);
      })
    },
    disshareClick(){
      this.$http({
        url: `${this.$route.params.uid}/sharedPosts/delete/${this.$route.params.postId}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "delete",
      }).then((response => {
        if(response.status === 201){
          this.share = false;
        }
      })).catch(error =>{
        console.log(error);
        window.alert("error code: " + error.response.status);
      })
    },
    reply(model, id){
      if (this.$store.state.uid == null){
        this.showDialog();
        return;
      }
      this.isReply = true;
    },
    secondReplyToReply(index){
      if (this.$store.state.uid == null){
        this.showDialog();
        return;
      }
      this.$set(this.isSecondReply, index, true);
    },
    sendFirstReply(anonymous){
      this.$http({
        url: `${this.$route.params.uid}/replies/create/${anonymous}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "post",
        headers: {
          "Content-Type": "application/json",
        },
        data: {
          replyId: null,
          content:this.firstReplyContent,
          stars:null,
          author:null,
          authorId:null,
          postId:this.postId,
          post: null,
        },
      }).then((res) =>{
        if (res.status == 201){
          this.getData();
          this.$forceUpdate();
        }
      })
    },
    SendSecondReply(replyId, index, anonymous){
      this.$http({
        url: `${this.$route.params.uid}/secondaryReplies/create/${anonymous}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "post",
        headers: {
          "Content-Type": "application/json",
        },
        data: {
          secondaryReplyId: null,
          content:this.secondReplyContent[index],
          stars:null,
          author:null,
          authorId:null,
          replyId:replyId,
          reply:null,
          is_anonymous:false,
        },
      }).then((res) =>{
        if (res.status == 201){
          this.getData();
          this.$forceUpdate();
        }
      })
    }
  },
  beforeMount() {
    this.loading = true;
    this.getData();
  },
  watch: {
    $route() {
      this.getData();
    },
  },
};
</script>

<style scoped>
/*scoped属性导致css仅对当前组件生效，而html绑定渲染出的内容可以理解为是子组件的内容，子组件不会被加上对应的属性，所以不会应用css.
解决的话把scoped属性去掉就行了*/

@import url("//unpkg.com/element-ui@2.15.13/lib/theme-chalk/index.css");

.ArticleSection #content {
  padding: 2rem 1rem 2rem 1rem;
  line-height: 1.6;
  padding-bottom: 1rem;
}
.ArticleSection > h1 {
  font-size: 0.1rem;
}
.article {
  background: white;
  margin-bottom: 10px;
  padding-left: 20px;
  padding-top: 10px;
}
.article h1 {
  font-size: 1.2rem;
  font-weight: 600;
}
.article > ul li {
  display: inline-block;
  font-size: 0.8rem;
  color: #a8a3a3;
  padding-left: 5px;
}
.article li a {
  color: inherit;
  text-decoration: none;
}

.thumbsClass {
  float: right;
}
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}
.info{
  text-decoration: none;
}

.box-card {
}
</style>
