<template>
  <div class="Header">
    <router-link :to="{ name: 'root' }">
      <img src="../assets/cnodejs_light.svg" />
    </router-link>
    <router-link :to="{ name: 'login' }">
      <button id="login">
        {{ userStatus ? "Hello, " + username : "Login" }}
        <el-avatar src="https://www.dmoe.cc/random.php" ></el-avatar>
      </button>
    </router-link>
  </div>
</template>

<script>
import { mapState } from "vuex";
// 在应用程序初始化时从本地存储中还原状态

export default {
  name: "Header",
  computed: mapState(["username", "userStatus"]),
  data() {
    return {};
  },
  created() {

  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.Header {
  background: #3a3a3a;
  height: 100%;
}
a {
  text-decoration: none;
  color: #cbc9c9;
}
img {
  max-width: 120px;
  margin-left: 50px;
  margin-top: 10px;
}
#login {
  float: right;
  margin-right: 50px;
  margin-top: 10px;
  margin-bottom: 5px;
  background-color: #1a1818;
  font-size: 20px;
  color: #ffffff;
  border-radius: 5px;
}
</style>
