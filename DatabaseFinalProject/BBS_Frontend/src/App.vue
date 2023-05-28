<template>
  <div id="app">
    <router-view name="Login"></router-view>
    <el-container>
      <el-aside width="200px">
        <router-view name="sideColumn"></router-view>
      </el-aside>
      <el-container>
        <el-header><router-view name="header"></router-view></el-header>
        <el-main>
          <router-view name="contain"></router-view>
          <router-view name="main" :key="$route.fullPath" style="float: left; width:70%"></router-view>
          <router-view name="sidebar" style="float: right; width:320px"></router-view>
        </el-main>
        <el-footer>
          <router-view name="bottom"></router-view>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "app",
  components: {},
};
</script>

<style>
html,body,#app, .el-container {
  background-size: cover;
  height: 100%;
  margin: 0px;
  padding: 0px;
}
#app{
  background:url("./assets/Background.jpg");
  background-color: #dfdfdf;
}
.el-header,
.el-footer {
  background-color: #3a3a3a;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #ffffff;
  text-align: center;
  line-height: 200px;
}

.el-main {
  color: #333;
  text-align: left;
  line-height: 160px;
}



body,
span,
a,
p,
ul,
li {
  margin: 0;
  padding: 0;
}
</style>
