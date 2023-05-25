<template>
	<div class="UserInfo">
	    <div class="loading" v-if="loading">
	        Loading...
	    </div>
	    <div class="userInfomation" v-else>
	    	<section>
				<img :src="userinfo.avatar_url" :title="userinfo.loginname"/>
				<span>{{userinfo.loginname}}</span>
				<p>{{userinfo.score}} 积分</p>
				<p>注册时间：{{ userinfo.create_at | formatDate }}</p>
	    	</section>
			<div class="replies">
				<p>回复的主题</p>
				<ul>
					<li v-for="item in userinfo.recent_replies" :key="item">
						<router-link :to="{ name: 'post_content', params: { id: item.id,name:item.author.loginname }}" :title="item.title">
							{{item.title}}
						</router-link>
					</li>
				</ul>
			</div>
			<div class="topics">
				<p>创建的主题</p>
				<ul>
					<li v-for="item in userinfo.recent_topics" :key="item">
						<router-link :to="{ name: 'post_content', params: { id: item.id,name:item.author.loginname }}" :title="item.title">
							{{item.title}}
						</router-link>
					</li>
				</ul>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
	  name: 'UserInfo',
	  data () {
	    return {
	      userinfo: {},
		  isFollow: false,
	      loading:false
	    }
	  },
	  methods: {
	  	getData(){
			this.$http({
	            url: `${this.$route.params.uid}/getFollow/${this.$route.params.author.authorId}`,   //ES6语法，引入组件内的 route object（路由信息对象） 
	            method: 'get',
	          })
			  .then( (response) => {
			  	if( response.data.success === true ){
			  		this.userinfo = this.$route.params.author;
					this.isFollow = response.data;
			  		this.loading = false;
					console.log(response.data);
			  	}
			  })
			  .catch(function (error) {
			  	console.log(error);
			  });
		  	}
		 },
	    beforeMount() {
	    	this.loading = true
	        this.getData();
	    }
	}
</script>

<style>
	.userInfomation {
		background: white;
		width: 75%;
		margin: 10px auto;
	}
	.userInfomation section {
		padding: 12px;
	}
	.userInfomation img {
		width: 5rem;
	}
	.userInfomation li {
		list-style:none;
	}
	.userInfomation .replies,
	.userInfomation .topics {
	    font-size: 0.72rem;
	    border-top: 10px #DDDDDD solid;
	}
	.userInfomation > div > p {
		padding: 12px 0 12px 12px;
	    background-color: rgba(212, 205, 205, 0.17);
  		font-size: 0.75rem;		
	}
	.userInfomation > div >ul > li {
		padding: 4px 0 4px 12px;
		white-space: nowrap;
	    font-size: 0.72rem;
		text-overflow: ellipsis;
		overflow: hidden;
	    line-height: 30px;
        vertical-align: middle;	
	}
	.userInfomation > div >ul > li > a {
		color: #094E99;
		text-decoration: none;
	}
</style>