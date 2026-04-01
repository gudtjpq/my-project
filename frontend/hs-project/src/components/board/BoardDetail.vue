<script setup>
import { ref, onMounted, watch } from 'vue'
import { boardApi } from '@/util/api/boardApi'
import { downloadFileFromServer } from '@/util/fileDownloader' 

const props = defineProps({
  postId: Number,
  searchType: String,
  searchValue: String
})

const emit = defineEmits(['list', 'edit', 'detail'])

const post = ref({})
const prevPost = ref(null)
const nextPost = ref(null)
const isUserLiked = ref(false)

const fetchPost = async (id) => {
  if (!id) return
  try {
    const res = await boardApi.getBoardDetail(id, props.searchType, props.searchValue)
    post.value = res.dto
    prevPost.value = res.prevDto
    nextPost.value = res.nextDto
    isUserLiked.value = res.isUserLiked
  } catch (err) {
    console.error("데이터 로딩 실패:", err)
  }
}

onMounted(() => fetchPost(props.postId))
watch(() => props.postId, (newId) => fetchPost(newId))

const removePost = async () => {
  if (!confirm('게시글을 삭제하시겠습니까?')) return
  try {
    await boardApi.deleteBoard(post.value.num)
    emit('list')
  } catch (error) {
    alert('삭제에 실패했습니다.')
  }
}

const sendBoardLike = async () => {
	const msg = isUserLiked.value ? '게시글 공감을 취소하시겠습니까 ?' : '게시글에 공감하십니까 ?';
	if(! confirm(msg)) {
		return;
	}
			
	try {
		const method = isUserLiked.value ? 'delete' : 'post'; // 메소드는 소문자로
		const res = await boardApi.likeBoard(post.value.num, method);

		post.value.boardLikeCount = res.boardLikeCount;
		isUserLiked.value = !isUserLiked.value;
				
	} catch (error) {
		console.log(error);
		alert("오류가 발생했습니다.");
	}	
}
</script>

<template>
    <div class="pb-2">
        <span class="small-title">상세정보</span>
    </div>
                    
    <table class="table board-article">
        <thead>
            <tr>
                <td colspan="2" class="text-center">
                    {{ post.subject }}
                </td>
            </tr>
        </thead>

        <tbody>
            <tr>
                <td width="50%">
                    작성자 : {{ post.name }}
                </td>
                <td width="50%" class="text-end">
                    작성일 : {{ post.reg_date }} | 조회 {{ post.hitCount }}
                </td>
            </tr>
            
            <tr>
                <td colspan="2" valign="top" class="article-content"
                    style="border-bottom: none;">
                    <div v-html="post.content" class="ql-editor" style="min-height: 200px;"></div>
                </td>
            </tr>

			<tr>
				<td colspan="2" class="text-center p-3" style="border-bottom: none;">
					<button class="btn-default" title="좋아요"
						@click="sendBoardLike"><i class="bi" 
						:class="{
							'bi-heart-fill text-danger': isUserLiked,
							'bi-heart': !isUserLiked
						}"></i>&nbsp;&nbsp;<span>{{ post.boardLikeCount }}</span></button>
				</td>
			</tr>

			<tr>
				<td colspan="2">
					<p v-if="post.saveFilename" class="border text-secondary my-1 p-2">
						<i class="bi bi-folder2-open"></i>
						<a href="#" @click.prevent="downloadFileFromServer(post.num, boardApi)" class="text-reset ms-1">{{ post.originalFilename }}</a>
					</p>
				</td>
			</tr>

            <tr>
                <td colspan="2">
                    이전글 : 
                    <a v-if="prevPost" href="#" 
                        @click.prevent="emit('detail', {id: prevPost.num})">{{ prevPost.subject }}</a>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    다음글 : 
                    <a v-if="nextPost" href="#" 
                        @click.prevent="emit('detail', {id: nextPost.num})">{{ nextPost.subject }}</a>
                </td>
            </tr>
        </tbody>
    </table>

    <div class="row mb-2">
        <div class="col-md-6 align-self-center">
            <button v-if="post.canEdit" @click="emit('edit', post.num)" class="btn-default me-1">수정</button>
            <button v-else class="btn-default me-1">신고</button>

            <button v-if="post.canDelete" @click="removePost" class="btn-default">삭제</button>
            <button v-else class="btn-default" disabled>삭제</button>
        </div>
        <div class="col-md-6 align-self-center text-end">
            <button @click="emit('list')" class="btn-default">리스트</button>
        </div>
    </div>
</template>

<style scoped>
.ql-resize-style-left { float: left; margin: 0 1em 1em 0; }
.ql-resize-style-center { display: block; margin: auto; }
.ql-resize-style-right { float: right; margin: 0 0 1em 1em; }
.ql-resize-style-full { width: 100% !important; }
</style>