import { createRouter, createWebHistory } from 'vue-router'
import ArticleList from './components/ArticleList.vue'
import ArticleEditor from './components/ArticleEditor.vue'
import ArticleView from './components/ArticleView.vue'
import ArticleForm from './components/ArticleForm.vue'

const routes = [
  { path: '/', redirect: '/articles' },
  { path: '/articles', component: ArticleList },
  { path: '/articles/new', component: ArticleForm  },
  { path: '/view/:id', component: ArticleView },
  { path: '/edit/:id', component: ArticleForm  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
