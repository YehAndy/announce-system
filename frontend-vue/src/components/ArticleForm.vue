<template>
  <div class="container mt-4" v-if="article">
    <h2>{{ isEditMode ? '修改公告' : '新增公告' }}</h2>

    <div class="mb-3">
      <label>標題</label>
      <input v-model="article.title" type="text" class="form-control"
               :class="{'is-invalid': errors.title, 'is-valid': !errors.title && article.title}" />
        <div class="invalid-feedback">{{ errors.title }}</div>
    </div>

    <div class="mb-3">
      <label>公布者</label>
      <input v-model="article.publisher" type="text" class="form-control"
               :class="{'is-invalid': errors.publisher, 'is-valid': !errors.publisher && article.publisher}" />
        <div class="invalid-feedback">{{ errors.publisher }}</div>
    </div>

    <div class="mb-3">
      <label>發布日期</label>
      <input v-model="article.createdAt" type="date" class="form-control"
               :class="{'is-invalid': errors.createdAt}" />
        <div class="invalid-feedback">{{ errors.createdAt }}</div>
    </div>

    <div class="mb-3">
      <label>截止日期</label>
      <input type="date" v-model="article.expireAt" class="form-control" />
    </div>

    <div class="mb-3">
      <label>內容</label>
      <textarea v-model="article.content" rows="6" class="form-control"
                  :class="{'is-invalid': errors.content, 'is-valid': !errors.content && article.content}">
        </textarea>
        <div class="invalid-feedback">{{ errors.content }}</div>
    </div>

    <div class="mb-3">
      <label>上傳附件</label>
      <input type="file" @change="uploadFile" class="form-control" />
    </div>

    <ul class="list-group mb-3" v-if="article.attachments.length">
      <li v-for="file in article.attachments" :key="file" class="list-group-item">
        <a :href="`/api/announcements/download/${file.split('/').pop()}`">{{ getOriginalFileName(file) }}</a>
      </li>
    </ul>

    <button @click="submit" class="btn btn-primary">
      {{ isEditMode ? '儲存變更' : '發布公告' }}
    </button>
    <router-link to="/articles" class="btn btn-secondary ms-2">取消</router-link>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      article: {
        id: null,
        title: '',
        publisher: '',
        createdAt: this.today(),
        expireAt: this.today(),
        content: '',
        attachments: []
      },
      isEditMode: false,
      errors: {}  // 驗證錯誤狀態
    }
  },
  mounted() {
    const id = this.$route.params.id
    if (id) {
      this.isEditMode = true
      axios.get(`/api/announcements/${id}`)
        .then(res => {
          this.article = res.data
        })
        .catch(() => alert('載入失敗'))
    }
  },
  methods: {
    today() {
      return new Date().toISOString().split('T')[0]
    },
    submit() {
      this.errors = {}

      if (!this.article.title.trim()) this.errors.title = '標題不得為空'
      if (!this.article.publisher.trim()) this.errors.publisher = '公布者不得為空'
      if (!this.article.createdAt) this.errors.createdAt = '請選擇發布日期'
      if (!this.article.expireAt) this.errors.expireAt = '請選擇截止日期'
      if (new Date(this.article.expireAt) < new Date(this.article.createdAt)) {
        this.errors.expireAt = '截止日期不能早於發布日期'
      }
      if (!this.article.content.trim()) this.errors.content = '內容不得為空'

      if (Object.keys(this.errors).length > 0) return // 有錯就不提交

      const url = this.isEditMode ? '/api/announcements/update' : '/api/announcements/publish'
      const method = this.isEditMode ? axios.put : axios.post

      method(url, this.article)
        .then(() => {
          alert(this.isEditMode ? '更新成功' : '發布成功')
          this.$router.push('/articles')
        })
        .catch(() => alert(this.isEditMode ? '更新失敗' : '發布失敗'))
    },
    uploadFile(event) {
        const file = event.target.files[0]
        if (!file) return

        const formData = new FormData()
        formData.append('file', file)

        axios.post('/api/announcements/uploadAttachment', formData)
          .then(res => {
            this.article.attachments.push(res.data.url)
          })
          .catch(() => alert('上傳失敗'))
      },
      getFileName(fullUrl) {
        return fullUrl.split('/').pop()
      },
      getOriginalFileName(fullPath) {
        if (!fullPath) return ''
        return fullPath.split('/').pop().replace(/^[a-f0-9\-]+_/, '')
      }
  }
}
</script>
