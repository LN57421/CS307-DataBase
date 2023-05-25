<template>
	<div class="SideBar">
	    <div class="loading" v-if="loading">
	        Loading...
	    </div>
		<div v-else class="autherinfo">
			<div class="authersummay">
				<p style="background-size:cover; background-image:url(https://img.paulzzh.com/touhou/random);font-size:20px" >author</p>
				<router-link  :to="{ name: 'user_info', params: { name: userinfo.authorName }}">
					      <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
				</router-link>
				<router-link  :to="{ name: 'user_info', params: { name: userinfo.authorName }}" :title="userinfo.authorName">
					{{userinfo.authorName}}
				</router-link>
				<div v-if="isFollow"><el-button type="warning" @click="unFollowClick">unFollow<i class="el-icon-circle-plus el-icon--right" ></i></el-button></div>
            	<div v-else><el-button type="primary" @click="followClick">Follow<i class="el-icon-circle-plus el-icon--right" ></i></el-button></div>
				<section>
					phone: {{userinfo.phone}}
				</section>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'SideBar',
		data () {
		    return {
		      userinfo:null,
			  isFollow: false,
		      loading:false
		    }
		},
		methods: {
		  	getData(){
				//获取用户信息
				this.$http({
		            url: `${this.$route.params.author.authorId}/getFollow/${this.$route.params.uid}`,
		            method: 'get',
		        })
				.then( (response) => {
					this.userinfo = this.$route.params.author;
          this.$set(this, 'isFollow', response.data)
          this.loading = false;
					console.log(response.data)
				})
				.catch(function (error) {
				  	console.log(error);
				});
			},
			followClick(){
			this.$http({
				url: `${this.$route.params.author.authorId}/follows/create/${this.$route.params.uid}`, //ES6语法，引入组件内的 route object（路由信息对象）
				method: "post",
			}).then((response => {
				if(response.status === 201){
				this.isFollow = true;
				}
			})).catch(error =>{
				console.log(error);
				window.alert("error code: " + error.response.status);
			})
			},
			unFollowClick(){
			this.$http({
				url: `${this.$route.params.author.authorId}/follows/delete/${this.$route.params.uid}`, //ES6语法，引入组件内的 route object（路由信息对象）
				method: "delete",
			}).then((response => {
				if(response.status === 201){
				this.isFollow = false;
				}
			})).catch(error =>{
				console.log(error);
				window.alert("error code: " + error.response.status);
			})
			},

		},
	    beforeMount() {
    	  this.loading = true
        this.getData();
  	    },
	    computed:{
	   	  topicsLimitBy5(){
	  		return this.userinfo.recent_topics.slice(0,5);
	  	   }
	    },
	    //监控同级路由下复用附件，进行数据更新
		watch:{
			$route(){
				this.getData();
			}
		}
	}
</script>

<style scoped>
	.SideBar {
		float: right;
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
	.SideBar a{
		text-decoration: none;
		color: #a8a3a3;
	}
	.SideBar .authersummay  > a {
		display: inline-block;
	    padding: 12px 0 0 6px;
	    vertical-align: top;
	}
	.SideBar .authersummay  > a[title] {
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
	.recent_topics,.recent_replies {
	    font-size: 0.72rem;
	    border-top: 10px #DDDDDD solid;
	}
	.recent_topics li,
	.recent_replies li {
		padding: 4px 0 0 12px;
		color: #C0CCDA;
		white-space: nowrap;
	}
	.recent_topics ul,
	.recent_replies ul {
		padding: 10px 0 10px 0;
	}
	.recent_topics li a,
	.recent_replies li a {
		text-overflow: ellipsis;
		overflow: hidden;
	    max-width: 95%;
   		display: inline-block;
	}
</style>
