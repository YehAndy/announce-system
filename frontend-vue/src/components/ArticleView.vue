<template>
  <div class="container mt-4" v-if="article">
    <h2 class="mb-3">{{ article.title }}</h2>
    <p><strong>發布者：</strong>{{ article.publisher }}</p>
    <p><strong>發布日期：</strong>{{ article.createdAt }}</p>
    <p><strong>截止日期：</strong>{{ article.expireAt }}</p>
    <hr />
    <div v-html="article.content"></div>
    <ul class="list-group mb-3" v-if="article.attachments.length">
      <li v-for="file in article.attachments" :key="file" class="list-group-item">
        <a :href="`/api/announcements/download/${file.split('/').pop()}`">{{ getOriginalFileName(file) }}</a>
      </li>
    </ul>
    <router-link to="/articles" class="btn btn-secondary mt-4">回公告列表</router-link>
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
    axios.get(`/api/announcements/${id}`)
      .then(res => {
        this.article = res.data
      })
      .catch(() => alert('找不到公告'))
  },
  methods: {
    getOriginalFileName(fullPath) {
      if (!fullPath) return ''
      return fullPath.split('/').pop().replace(/^[a-f0-9\-]+_/, '')
    }
  }
}
</script>
