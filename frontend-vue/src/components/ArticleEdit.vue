<template>
  <div class="container mt-4" v-if="article">
    <h2>修改公告</h2>

    <div class="mb-3">
      <label>標題</label>
      <input v-model="article.title" class="form-control" />
    </div>

    <div class="mb-3">
      <label>公布者</label>
      <input v-model="article.author" class="form-control" />
    </div>

    <div class="mb-3">
      <label>發布日期</label>
      <input type="date" v-model="article.createdAt" class="form-control" />
    </div>

    <div class="mb-3">
      <label>截止日期</label>
      <input type="date" v-model="article.expireAt" class="form-control" />
    </div>

    <div class="mb-3">
      <label>內容</label>
      <textarea v-model="article.content" rows="10" class="form-control"></textarea>
    </div>

    <button @click="update" class="btn btn-primary">儲存變更</button>
    <router-link to="/list" class="btn btn-secondary ms-2">取消</router-link>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      article: null
    }
  },
  mounted() {
    const id = this.$route.params.id
    axios.get(`/api/announcement/${id}`)
      .then(res => {
        this.article = res.data
      })
      .catch(() => alert('載入失敗'))
  },
  methods: {
    update() {
      axios.put('/api/update', this.article)
        .then(() => {
          alert('更新成功')
          this.$router.push('/list')
        })
        .catch(() => alert('更新失敗'))
    }
  }
}
</script>
