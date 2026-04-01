<script setup>
import '@/assets/css/board.css'

import { ref } from 'vue'

import BoardList from '@/components/board/BoardList.vue';
import BoardDetail from '@/components/board/BoardDetail.vue';
import BoardForm from '@/components/board/BoardForm.vue';

const mode = ref('list') // list | detail | create | edit
const selectedId = ref(null)

// 상태는 부모에서 관리
const page = ref(1)
const searchType = ref('all')
const searchValue = ref('')

const goList = () => { mode.value = 'list' }
const goDetail = (payload) => { 
	selectedId.value = payload.id; 
	mode.value = 'detail';
	window.scrollTo(0, 0);
}
const goCreate = () => { mode.value = 'create' }
const goEdit = (id) => { selectedId.value = id; mode.value = 'edit' }
</script>

<template>
	<div class="page-title">
		<div class="container align-items-center" data-aos="fade-up">
			<h1>자유 게시판</h1>
			<div class="page-title-underline-accent"></div>
		</div>
	</div>

  	<div class="section">
		<div class="container" data-aos="fade-up" data-aos-delay="100">
			<div class="row justify-content-center">
				<div class="col-md-10 board-section my-4 p-5">
					<BoardList
      					v-if="mode === 'list'"
      					v-model:page="page"
      					v-model:searchType="searchType"
      					v-model:searchValue="searchValue"
      					@detail="goDetail"
      					@create="goCreate"
    				/>

    				<BoardDetail
      					v-if="mode === 'detail'"
      						:postId="selectedId"
      						:searchType="searchType"
      						:searchValue="searchValue"
      						@list="goList"
      						@edit="goEdit"
      						@detail="goDetail"
    				/>

    				<BoardForm
      					v-if="mode === 'create'"
    					@list="goList"
    				/>

    				<BoardForm
      					v-if="mode === 'edit'"
      					:postId="selectedId"
      					@list="goList"
    				/>					
				</div>
			</div>
		</div>
	</div>
</template>
