<template>
  <div class="container mt-4">
    <h2>發布公告</h2>

    <div class="mb-3">
      <label>標題</label>
      <input v-model="title" class="form-control" placeholder="請輸入標題" />
    </div>

    <div class="mb-3">
      <label>公布者</label>
      <input v-model="publisher" class="form-control" placeholder="請輸入公布者" />
    </div>

    <div class="mb-3">
      <label>發布日期</label>
      <input type="date" v-model="createdAt" class="form-control" />
    </div>

    <div class="mb-3">
      <label>截止日期</label>
      <input type="date" v-model="expireAt" class="form-control" />
    </div>

    <div class="mb-3">
      <label>公告內容</label>
      <textarea v-model="content" rows="10" class="form-control" placeholder="請輸入公告內容"></textarea>
    </div>

    <button @click="submit" class="btn btn-primary">發布公告</button>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    const today = new Date().toISOString().split('T')[0]
    return {
      title: '',
      publisher: '',
      createdAt: today,
      expireAt: today,
      content: ''
    }
  },
  methods: {
    submit() {
      axios.post('/api/announcements/publish', {
        title: this.title,
        publisher: this.publisher,
        createdAt: this.createdAt,
        expireAt: this.expireAt,
        content: this.content
      })
        .then(() => {
            alert('發布成功')
            this.$router.push('/articles')
            })
        .catch(() => alert('發布失敗'))
    }
  }
}
</script>
