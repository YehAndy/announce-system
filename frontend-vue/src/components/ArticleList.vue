<template>
  <div class="mt-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
          <h2>最新公告</h2>
          <router-link to="/articles/new" class="btn btn-primary">＋ 新增公告</router-link>
        </div>
    <table class="table table-bordered mt-3">
      <thead class="table-light">
        <tr>
          <th>標題</th>
          <th>發布日期</th>
          <th>截止日期</th>
          <th>查看</th>
          <th>修改</th>
          <th>刪除</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="article in articles" :key="article.id">
          <td>{{ article.title }}</td>
          <td>{{ formatDate(article.createdAt) }}</td>
          <td>{{ formatDate(article.expireAt) }}</td>
          <td><router-link :to="`/view/${article.id}`" class="btn btn-sm btn-info me-1">查看</router-link></td>
          <td><router-link :to="`/edit/${article.id}`" class="btn btn-sm btn-warning">修改</router-link></td>
          <td><button @click="remove(article.id)" class="btn btn-sm btn-danger">刪除</button></td>
        </tr>
        <tr v-if="articles.length === 0">
          <td colspan="5" class="text-center text-danger">尚無公告資料</td>
        </tr>
      </tbody>
    </table>


    <!-- 分頁控制 -->
    <div class="d-flex justify-content-between align-items-center mt-3">
      <button class="btn btn-outline-secondary" :disabled="page === 0" @click="prevPage">上一頁</button>
      <span>第 {{ page + 1 }} 頁</span>
      <button class="btn btn-outline-secondary" :disabled="!hasNext" @click="nextPage">下一頁</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      articles: [],
      page: 0,
      size: 5,
      hasNext: false
    }
  },
  mounted() {
    this.loadArticles()
  },
  methods: {
    loadArticles() {
      axios.get('/api/announcements/list', {
        params: {
          page: this.page,
          size: this.size
        }
      }).then(res => {
        this.articles = res.data.content
        this.hasNext = !res.data.last
      }).catch(() => alert('載入失敗'))
    },
    nextPage() {
      this.page++
      this.loadArticles()
    },
    prevPage() {
      this.page--
      this.loadArticles()
    },
    formatDate(iso) {
      if (!iso) return ''
      return new Date(iso).toLocaleDateString()
    },
    remove(id) {
      if (!confirm('確定要刪除這筆公告嗎？')) return

      axios.delete(`/api/announcements/${id}`)
        .then(() => {
          alert('刪除成功')
          this.loadArticles()  // 重新載入列表
        })
        .catch(() => alert('刪除失敗'))
    }
  }
}
</script>
