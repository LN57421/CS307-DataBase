<template>
  <div class="SideBar">
    <div class="loading" v-if="loading">
      <el-container v-loading="true"></el-container>
      <el-dialog :visible="dialogVisible" title="Please login first!" @close="closeDialog" ></el-dialog>
    </div>
    <div v-else class="autherinfo">
      <div v-for="(FollowAuthor, index) in follows" :key="index">
        <div class="authersummay">
          <p
            style="background-size:cover; background-image:url(https://img.paulzzh.com/touhou/random);font-size:20px">
            author</p>
          <router-link :to="{ name: 'user_info', params: { name: FollowAuthor.authorName }}">
            <el-avatar
              src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
          </router-link>
          <router-link :to="{ name: 'user_info', params: { name: FollowAuthor.authorName }}"
                       :title="FollowAuthor.authorName">
            {{ FollowAuthor.authorName }}
          </router-link>
          <div v-if="isFollow[index]">
            <el-button type="warning" @click="unFollowClick(index, FollowAuthor.authorId)">unFollow<i
              class="el-icon-circle-plus el-icon--right"></i></el-button>
          </div>
          <div v-else>
            <el-button type="primary" @click="followClick(index, FollowAuthor.authorId)">Follow<i
              class="el-icon-circle-plus el-icon--right"></i></el-button>
          </div>
          <section>
            phone: {{ FollowAuthor.phone }}
          </section>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SideBar',
  data() {
    return {
      follows: null,
      isFollow: [],
      loading: false,
      dialogVisible:false,
    }
  },
  methods: {
    getData() {
      //获取用户信息
      this.$http({
        url: `${this.$route.params.uid}/getFollowList`,
        method: 'get',
      })
      .then((response) => {
        this.follows = response.data[0];
        this.isFollow = response.data[1];
        this.loading = false;
        console.log(response)
        console.log(response.data[0])
        console.log(response.data[1])
      })
      .catch(function (error) {
        console.log(error);
      });
    },
    followClick(index, authorId) {
      this.$http({
        url: `${authorId}/follows/create/${this.$route.params.uid}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "post",
      }).then((response => {
        this.$set(this.isFollow, index, true);
        console.log(this.isFollow[index])
      })).catch(error => {
        console.log(error);
        window.alert("error code: " + error.response.status + this.isFollow[index]);
      })
    },
    unFollowClick(index, authorId) {
      this.$http({
        url: `${authorId}/follows/delete/${this.$route.params.uid}`, //ES6语法，引入组件内的 route object（路由信息对象）
        method: "delete",
      }).then((response => {
        this.$set(this.isFollow, index, false); //必须得加这个，不然页面不响应变化
      })).catch(error => {
        console.log(error);
        window.alert("error code: " + error.response.status);
      })
    },
    showDialog() {
      this.dialogVisible = true; // 显示弹窗
    },
    closeDialog() {
      this.dialogVisible = false; // 关闭弹窗
    },

  },
  beforeMount() {
    this.loading = true
    if (this.$route.params.uid == null){
      this.showDialog();
      return;
    }
    this.getData();
  },
  //监控同级路由下复用附件，进行数据更新
  watch: {
    $route() {
      this.getData();
    }
  }
}
</script>

<style scoped>
.SideBar {
  float: left;
  width: 24%;
  box-sizing: border-box;
  margin-right: 3%;
  padding: 0.8rem 0.4rem;
}

.SideBar .autherinfo {
  background: white;
}

.SideBar > div p {
  padding: 12px 0 12px 12px;
  background-color: rgba(212, 205, 205, 0.17);
  font-size: 0.75rem;
}

.SideBar img {
  width: 3.5rem;
  display: inline-block;
  padding: 0 0 0 6px;
  border-radius: 3px;
}

.SideBar a {
  text-decoration: none;
  color: #a8a3a3;
}

.SideBar .authersummay > a {
  display: inline-block;
  padding: 12px 0 0 6px;
  vertical-align: top;
}

.SideBar .authersummay > a[title] {
  margin-top: 20px;
  margin-left: 2px;
}

.SideBar section {
  padding: 12px 0 12px 12px;
  font-size: 0.75rem;
}

.SideBar li {
  list-style: none;
}
</style>
