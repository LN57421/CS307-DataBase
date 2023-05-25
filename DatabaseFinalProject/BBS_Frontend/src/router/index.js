import Vue from 'vue'
import Router from 'vue-router'
import PostList from '@/components/PostList'
import Article from '@/components/Article'
import SideBar from '@/components/SideBar'
import UserInfo from '@/components/UserInfo'
import Header from '@/components/Header'
import Login from '@/components/Login'
import SideColumn from '@/components/SideColumn'
import CreatePost from '@/components/CreatePost'
import FollowAuthor from '@/components/FollowAuthor'

Vue.use(Router)

export default new Router({
	mode: 'history',
	routes: [
		{
			path: '/',
			name: 'root',
			components: {
				header: Header,
				sideColumn: SideColumn
			}
		},
		{
			path: '/postList',
			name: 'postList',
			components: {
				main: PostList,
				header: Header,
				sideColumn: SideColumn,
			}
		},
		{
			path: '/likeList',
			name: 'likeList',
			components: {
				main: PostList,
				header: Header,
				sideColumn: SideColumn
			}
		},
		{
			path: '/favorList',
			name: 'favorList',
			components: {
				main: PostList,
				header: Header,
				sideColumn: SideColumn
			}
		},
		{
			path: '/shareList',
			name: 'shareList',
			components: {
				main: PostList,
				header: Header,
				sideColumn: SideColumn
			}
		},
    {
      path: '/followingList',
      name: 'followingList',
      components: {
        main: FollowAuthor,
        header: Header,
        sideColumn: SideColumn
      }
    },
    {
      path: '/createdPost',
      name: 'createdPost',
      components: {
        main: PostList,
        header: Header,
        sideColumn: SideColumn
      }
    },
    {
      path: '/repliedPost',
      name: 'repliedPost',
      components: {
        main: PostList,
        header: Header,
        sideColumn: SideColumn
      }
    },
		{
			path: '/create',
			name: 'create',
			components: {
				main: CreatePost,
				header: Header,
				sideColumn: SideColumn
			}
		},
		{
			path: '/post_detail',
			name: 'post_content',
			components: {
				main: Article,
				header: Header,
				sideColumn: SideColumn,
				sidebar: SideBar
			},
		},
		{
			path: '/user/:name',
			name: 'user_info',
			components: {
				main: UserInfo,
				header: Header
			}
		},
		{
			path: '/login',
			name: 'login',
			components: {
				Login: Login
			}
		}
	]
})
