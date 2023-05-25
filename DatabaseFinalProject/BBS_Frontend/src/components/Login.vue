<template>
  <div class="login-register">
    <!--    contai为主容器-->
    <div class="contain">
      <!--      big contain为主部件-->
      <!--      big-box为实现页面跳转，通过active和islogin实现-->
      <div class="big-box" :class="{ active: isLogin }">
        <!--        登录处理-->
        <div class="big-contain" v-if="isLogin">
          <div class="btitle">Login</div>
          <div class="bform">
            <input type="text" placeholder="Username" v-model="form.username" />
            <input
              type="password"
              placeholder="Password"
              v-model="form.password"
            />
          </div>
          <button class="bbutton" @click="login">Enter</button>
        </div>
        <!--注册处理-->
        <div class="big-contain" v-else>
          <el-dialog :visible="dialogVisible" title="create account successfully!" @close="closeDialog" ></el-dialog>
          <div class="btitle">Creat your account</div>
          <div class="bform">
            <input type="text" placeholder="Username" v-model="form.username" />
            <input
              type="text"
              placeholder="Phone_number"
              v-model="form.phone"
            />
            <span class="errTips" v-if="existed">* username has existed!*</span>
            <input
              type="password"
              placeholder="Password"
              v-model="form.password"
            />
            <input
              type="password"
              placeholder="Password_again"
              v-model="rePassword"
              style="float: left"
            />
            <div v-if="form.password !== rePassword && rePassword !== ''">
              <p style="color: red">invalid</p>
            </div>
            <div v-else-if="form.password === rePassword && rePassword !== ''">
              <p style="color: lawngreen">valid</p>
            </div>
            <div v-else></div>
          </div>

          <button class="bbutton" @click="register" style="display: inline">
            Register</button
          >:
          <button class="bbutton" @click="returnhome" style="display: inline">
            Back
          </button>
        </div>
        <!--        页面翻转-->
      </div>
      <div class="small-box" :class="{ active: isLogin }">
        <div class="small-contain" v-if="isLogin">
          <div class="stitle">Welcome SUSFORUM!</div>
          <p class="scontent">no account? register first!</p>
          <button class="sbutton" @click="changeType">REGISTER</button>
        </div>
        <div class="small-contain" v-else>
          <div class="stitle">Welcome SUSFORUM!</div>
          <p class="scontent">no account? register first!</p>
          <button class="sbutton" @click="changeType">Enter</button>
        </div>
      </div>
    </div>
  </div>
</template>



<script>
export default {
  name: "login-register",
  data() {
    return {
      isLogin: false,
      existed: false,
      rePassword: "",
      token: "",
      uid: "",
      dialogVisible: false,
      form: {
        username: "",
        password: "",
        phone: "",
      },
    };
  },
  created() {
    // const self = this;
    // if (self.$store.state.token !== "" && self.$store.state.token !== null) {
    //   alert("已登录，请确认是否重新登录");
    // }
  },
  methods: {
    changeType() {
      this.isLogin = !this.isLogin;
      this.form.username = "";
      this.form.password = "";
    },
    returnhome() {
      this.$router.push("/");
    },
    login() {
      const self = this;
      if (self.form.username === "" && self.form.password === "") {
        window.alert("please write your username or password!");
        // return;
      } else if (this.validID() === false) {
        window.alert("please input the valid username");
        // return;
      } else if (this.finiteLengthPassword() === false) {
        window.alert("please input the valid password");
        // return;
      } else {
        this.$http
          .post(
            "/Login?" +
              "authorName=" +
              self.form.username +
              "&password=" +
              self.form.password
          )
          .then((response) => {
            console.log(response.status);
            if (response.status === 200) {
              this.$store.commit("Login", {
                authorName: response.data.authorName,
                authorKey: response.data.authorKey,
                authorId: response.data.authorId,
                userStatus: true,
              });
              console.log(response);
              console.log(this.$store.state.username);
              console.log(this.$store.state.uid);
              this.$router.push("/");
            } else if (response.status === 204) {
              window.alert("no such user");
            }
            // 请求成功的处理逻辑
          })
          .catch((error) => {
            console.log(error);
            if (error.response.status === 403) {
              window.alert("username or password is error");
            } else {
              window.alert("ERROR");
            }
            // 请求失败的处理逻辑
          });
      }
    },
    register() {
      const self = this;
      //此步为跳转，应该在注册后执行，先放在这
      // this.$router.push("/index")
      if (this.validID() === false) {
        window.alert("please input vaild name");
        return;
      } else if (this.finiteLengthPassword() === false) {
        window.alert("password is not invaild: cannot be null or too long");
        return;
      } else if (this.consistentPassword() === false) {
        window.alert("Two different passwords");
        return;
      } else if (self.form.username === "" && self.form.password === "") {
        window.alert("username cannot be null");
        return;
      } else {
        self
          .$http({
            method: "post",
            url: "api/Authors/create",
            headers: {
              "Content-Type": "application/json",
            },
            data: {
              authorName: self.form.username,
              authorKey: self.form.password,
              phone: self.form.phone,
              registrationTime: null,
              authorId:null
            },
          })
          .then((res) => {
            self.existed = false;
            self.showDialog();
          })
          .catch((err) => {
            console.log(err);
            try {
              if (err.response.status === 409) {
                self.existed = true;
              } else if (err.response.status === 500) {
                self.existed = false;
                window.alert("error:" + err.response.status + " creation failed");
              }
            } catch (error) {
              window.alert(error + " net work error");
            }
          });
      }
    },
    validID() {
      var length1 = 0;
      if (this.form.username) {
        length1 = this.form.username.length;
      }
      console.log(length1);
      if (length1 === 0) {
        return false;
      } else {
        return true;
      }
    },
    finiteLengthPassword() {
      var length1 = 0;
      if (this.form.username != null) {
        length1 = this.form.username.length;
      }
      if (length1 >= 32) {
        return false;
      } else if (length1 === 0) {
        return false;
      }
      return true;
    },
    consistentPassword() {
      if (this.form.password != this.rePassword) {
        return false;
      } else {
        return true;
      }
    },
    showDialog() {
      this.dialogVisible = true; // 显示弹窗
    },
    closeDialog() {
      this.dialogVisible = false; // 关闭弹窗
    },
  },
};
</script>

<style scoped="scoped">
.login-register {
  width: 100vw;
  height: 100vh;
  box-sizing: border-box;
  background-color: antiquewhite;
}
.contain {
  width: 60%;
  height: 60%;
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  border-radius: 20px;
  box-shadow: 0 0 3px #f0f0f0, 0 0 6px #f0f0f0;
}
.big-box {
  width: 70%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 30%;
  transform: translateX(0%);
  transition: all 1s;
}
.big-contain {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.btitle {
  font-size: 1.5em;
  font-weight: bold;
  color: rgb(57, 167, 176);
}
.bform {
  width: 100%;
  height: 40%;
  padding: 2em 0;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}
.bform .errTips {
  display: block;
  width: 50%;
  text-align: left;
  color: red;
  font-size: 0.7em;
  margin-left: 1em;
}
.bform input {
  width: 50%;
  height: 30px;
  border: none;
  outline: none;
  border-radius: 10px;
  padding-left: 2em;
  background-color: #f0f0f0;
}
.bbutton {
  width: 20%;
  height: 40px;
  border-radius: 24px;
  border: none;
  outline: none;
  background-color: rgb(57, 167, 176);
  color: #fff;
  font-size: 0.9em;
  cursor: pointer;
}
.small-box {
  width: 30%;
  height: 100%;
  background: linear-gradient(135deg, rgb(57, 167, 176), rgb(56, 183, 145));
  position: absolute;
  top: 0;
  left: 0;
  transform: translateX(0%);
  transition: all 1s;
  border-top-left-radius: inherit;
  border-bottom-left-radius: inherit;
}
.small-contain {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.stitle {
  font-size: 1.5em;
  font-weight: bold;
  color: #fff;
}
.scontent {
  font-size: 0.8em;
  color: #fff;
  text-align: center;
  padding: 2em 4em;
  line-height: 1.7em;
}
.sbutton {
  width: 60%;
  height: 40px;
  border-radius: 24px;
  border: 1px solid #fff;
  outline: none;
  background-color: transparent;
  color: #fff;
  font-size: 0.9em;
  cursor: pointer;
}
.big-box.active {
  left: 0;
  transition: all 2s;
}
.small-box.active {
  left: 100%;
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
  border-top-right-radius: inherit;
  border-bottom-right-radius: inherit;
  transform: translateX(-100%);

  transition: all 2s;
}
</style>
