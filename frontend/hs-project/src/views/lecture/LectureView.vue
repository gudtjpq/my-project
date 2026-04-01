<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'

const route = useRoute()
const categoryMap = {
  L100001: '프로그래밍',
  L100002: '데이터베이스',
  L100003: '웹 프로그래밍',
  L100004: '데이터분석 및 AI',
  L100005: '클라우드 및 기타'
}

const categoryName = computed(() => {
  return categoryMap[route.params.category] || '알 수 없음'
})
</script>
<template>
	<div class="page-title">
		<div class="container align-items-center" data-aos="fade-up">
			<h1>{{ categoryName }}</h1>
			<div class="page-title-underline-accent"></div>
		</div>
	</div>

  	<div class="section">
		<div class="container" data-aos="fade-up" data-aos-delay="100">
			<div class="row justify-content-center">
				<div class="col-md-10 board-section my-4 p-5">
					강좌 페이지 입니다.
				</div>
			</div>
		</div>
	</div>
</template>
