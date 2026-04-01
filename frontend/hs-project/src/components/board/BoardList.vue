<script setup>
import '@/assets/css/paginate.css'

import { ref, onMounted, watch, nextTick, computed } from 'vue'
import { boardApi } from '@/util/api/boardApi'
import { getPagination } from '@/util/paginate'
import { downloadFileFromServer } from '@/util/fileDownloader' 

const props = defineProps({
    page: { type: Number, default: 1 },
    searchType: { type: String, default: 'all' },
    searchValue: { type: String, default: '' }
})

const emit = defineEmits([
    'update:page',
    'update:searchType',
    'update:searchValue',
    'detail',
    'create'
])

const posts = ref([])
const totalCount = ref(0)

const pageSize = 10
const pageBlockSize = 10  // 하단에 표시할 페이지 번호 개수

// 화면의 input/select 와 연결
const localSearchType = ref(props.searchType)
const localSearchValue = ref(props.searchValue)

// 전체 페이지 수 계산
const totalPage = computed(() => 
    Math.max(0, Math.ceil(totalCount.value / pageSize))
)

watch(() => [props.page, totalPage], ([page, total]) => {
  if (page > total) {
      emit('update:page', total)
  }
})

// 페이징
const pagination = computed(() =>
    getPagination(props.page, totalPage.value, pageBlockSize)
);

// 부모 props 변경 감시
watch(() => [props.searchType, props.searchValue], ([newType, newVal]) => {
  localSearchType.value = newType
  localSearchValue.value = newVal
})

const fetchList = async () => {
  try {
    const res = await boardApi.getBoardList({
      pageNo: props.page,
      pageSize: pageSize,
      schType: props.searchType,
      kwd: props.searchValue
    })
    posts.value = res.list
    totalCount.value = res.totalCount
  } catch (err) {
    console.error("Data fetch error:", err)
  }
}

// 페이징 이동은 즉시 props를 변경하여 watch가 감지하게 함
const changePage = (p) => { 
  if (p < 1 || p > totalPage.value) return

  emit('update:page', p) 
}

// 검색
const search = async () => {
  // 부모에게 새로운 값 알림
  emit('update:searchType', localSearchType.value)
  emit('update:searchValue', localSearchValue.value)
  emit('update:page', 1)

  await nextTick(); // Vue가 모든 데이터 변경을 마칠 때까지 기다림
}

const resetSearch = async () => {
  localSearchType.value = 'all'
  localSearchValue.value = ''

  emit('update:searchType', 'all')
  emit('update:searchValue', '')
  emit('update:page', 1)

  await nextTick()  // Vue가 모든 데이터 변경을 마칠 때까지 기다림
}

// 부모 데이터가 최종적으로 변했을 때만 서버 통신
watch(() => [props.page, props.searchType, props.searchValue], fetchList)

onMounted(fetchList)

const handleDownload = async (id) => {
    downloadFileFromServer(id, boardApi);
};
</script>

<template>
    <div class="row py-1 mb-2">
        <div class="col-md-6 align-self-center">
            <span class="small-title">글목록</span> <span class="dataCount">{{totalCount}}개({{props.page}}/{{totalPage}} 페이지)</span>
        </div>	
        <div class="col-md-6 align-self-center text-end">
        </div>
    </div>				

    <table class="table table-hover board-list">
        <thead>
            <tr>
                <th class="num">번호</th>
                <th class="subject">제목</th>
                <th class="name">글쓴이</th>
                <th class="date">작성일</th>
                <th class="hit">조회수</th>
                <th class="file">파일</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(item, index) in posts" :key="item.num">
                <td>{{ totalCount - (page - 1) * pageSize - index }}</td>
                <td class="left">
                    <div class="text-wrap">
                        <a href="javascript:void(0);" @click.prevent="emit('detail', { id: item.num })">{{ item.subject }}</a>
                    </div>
                </td>
                <td>{{ item.name }}</td>
                <td>{{ item.reg_date }}</td>
                <td>{{ item.hitCount }}</td>
                <td>
                    <template v-if="item.saveFilename">
                        <a href="javascript:void(0);"
                            @click.prevent="handleDownload(item.num)" 
                            class="text-reset"><i class="bi bi-file-arrow-down"></i></a>
                    </template>
                </td>
            </tr>
        </tbody>					
    </table>

    <div class="page-navigation" v-if="pagination">
		<div class="paginate">
			<a v-if="pagination.showPrev" @click.prevent="changePage(pagination.firstPage)">≪</a>
			<a v-if="pagination.showPrev" @click.prevent="changePage(pagination.prevBlockPage)">&lt;</a>
			
			<a v-for="p in pagination.pages"
				:key="p" 
				@click.prevent="changePage(p)"
				:class="{ active: props.page === p }">
				{{ p }}
			</a>

			<a v-if="pagination.showNext" @click.prevent="changePage(pagination.nextBlockPage)">&gt;</a>
			<a v-if="pagination.showNext" @click.prevent="changePage(pagination.lastPage)">≫</a>
		</div>
    </div>

    <div class="row mt-3">
        <div class="col-md-3">
            <button @click="resetSearch" class="btn-default" title="새로고침"><i class="bi bi-arrow-clockwise"></i></button>
        </div>
        <div class="col-md-6 text-center">
            <div class="form-search">
                <select v-model="localSearchType" class="me-1">
                    <option value="all">제목+내용</option>
                    <option value="name">글쓴이</option>
                    <option value="reg_date">작성일</option>
                    <option value="subject">제목</option>
                    <option value="content">내용</option>
                </select>
                <input type="text" v-model="localSearchValue" 
                       @keyup.enter="search" class="me-1">
                <button @click="search" class="btn-default"><i class="bi bi-search"></i></button>
            </div>
        </div>
        <div class="col-md-3 text-end">
            <button @click="emit('create')" class="btn-accent btn-md">글쓰기</button>
        </div>
    </div>

</template>

<style scoped>

</style>