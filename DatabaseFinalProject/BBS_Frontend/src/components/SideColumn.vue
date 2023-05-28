<template>
    <el-menu router :default-active="this.$router.path">
      <el-submenu index="1">
        <template slot="title"><i class="el-icon-message"></i>post</template>
        <el-menu-item-group>
          <el-menu-item index="1-1" @click="showPost" >Show Post</el-menu-item>
          <el-menu-item index="1-2" route="/create">Create Post</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-submenu index="2">
        <template slot="title"><i class="el-icon-menu"></i>History</template>
        <el-menu-item-group>
          <el-menu-item index="2-1" @click="like">Like</el-menu-item>
          <el-menu-item index="2-2" @click="favor">Favor</el-menu-item>
          <el-menu-item index="2-3" @click="share">Share</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-submenu index="3">
        <template slot="title"><i class="el-icon-s-custom"></i>User</template>
        <el-menu-item-group>
          <template slot="title">follow</template>
          <el-menu-item index="3-1" @click="follow">Following author</el-menu-item>
          <el-menu-item index="3-1" @click="black">Black author</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="Post">
          <el-menu-item index="3-2" @click="createdPost">Created Post</el-menu-item>
          <el-menu-item index="3-3" @click="repliedPost">Replied Post</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
</template>

<script>
export default {
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    showPost(){
      this.$router.push({ name: "postList", params: { model: "showPost", uid: this.$store.state.uid } })
    },
    like() {
      this.$router.push({ name: "likeList", params: { model: "like", uid: this.$store.state.uid } })
    },
    favor() {
      this.$router.push({ name: "favorList", params: { model: "favor", uid: this.$store.state.uid } })
    },
    share() {
      this.$router.push({ name: "shareList", params: { model: "share", uid: this.$store.state.uid } })
    },
    follow(){
      this.$router.push({name: "followingList", params: {uid: this.$store.state.uid}})
    },
    black(){
      this.$router.push({name: "blackList", params: {uid: this.$store.state.uid}})
    },
    createdPost(){
      this.$router.push({name: "createdPost", params: {model: "createdPost", uid: this.$store.state.uid}})
    },
    repliedPost(){
      this.$router.push({name: "repliedPost", params: {model: "repliedPost", uid: this.$store.state.uid}})
    },
  },
};
</script>

<style>
@import url("//unpkg.com/element-ui@2.15.13/lib/theme-chalk/index.css");

</style>
