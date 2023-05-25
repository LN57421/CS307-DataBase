<template>
  <div v-if = "isCreate">
    <el-dialog :visible="isCreate" title="Create successfully!" @close="closeDialog" ></el-dialog>
  </div>
  <div v-else>
    <el-dialog :visible="dialogVisible" title="Please login first!" @close="closeDialogLoading" ></el-dialog>
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="Title">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="City">
        <el-select v-model="form.region" placeholder="Choose your city">
          <el-option label="shanghai" value="shanghai"></el-option>
          <el-option label="beijing" value="beijing"></el-option>
          <el-option label="tokyo" value="tokyo"></el-option>
        </el-select>
        <el-select v-model="form.state" placeholder="Choose your state">
          <el-option label="China" value="China"></el-option>
          <el-option label="Japan" value="Japan"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Tag">
        <el-checkbox-group v-model="form.type">
          <el-checkbox label="Food" name="food"></el-checkbox>
          <el-checkbox label="Sport" name="Sport"></el-checkbox>
          <el-checkbox label="Nature" name="Nature"></el-checkbox>
          <el-checkbox label="Music" name="Music"></el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="Content">
        <el-input type="textarea" v-model="form.content"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">Create post</el-button>
        <el-button>Cancel</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      form: {
        name: "",
        region: "",
        type: [],
        resource: "",
        content: "",
        state: "",
      },
      isCreate: false,
      dialogVisible: false,
    };
  },
  methods: {
    onSubmit() {
      console.log("submit!");
      this.$http({
        url: `${this.$store.state.uid}/posts/create/${this.form.state}`,
        method: 'post',
        headers: {
          "Content-Type": "application/json",
        },
        data:{
          title: this.form.name,
          content: this.form.content,
          postingCity: this.form.region,
          authorId: null,
          author: null,
          postingTime: null,
          city: null,
          dialogVisible: false,
        },
      })
      .then( (response) => {
        if (response.status === 201){
          this.isCreate = true;
        }
        console.log(response.status)
      })
      .catch(function (error) {
        console.log(error);
      });
    },
    showDialogLoading() {
      this.dialogVisible = true; // 显示弹窗
    },
    closeDialogLoading() {
      this.dialogVisible = false; // 关闭弹窗
      this.$router.push({ name: "postList", params: { model: "showPost", uid: this.$store.state.uid } })
    },
    closeDialog() {
      this.isCreate = false;
      this.$router.push({ name: "postList", params: { model: "showPost", uid: this.$store.state.uid } })
    },
  },
  beforeMount() {
    if (this.$store.state.uid == null){
      this.showDialogLoading();
      return;
    }
  }
};
</script>

<style scoped>
@import url("//unpkg.com/element-ui@2.15.13/lib/theme-chalk/index.css");
.el-form {
  background-color: rgb(255, 255, 255, 0.7);
  background-size: 100%;
}
</style>
