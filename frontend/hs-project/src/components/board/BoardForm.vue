<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { boardApi } from '@/util/api/boardApi'
import QuillEditor from '@/components/editor/QuillEditor.vue';

const authStore = useAuthStore();

const props = defineProps({
  postId: Number
})

// const emit = defineEmits(['list', 'edit'])
const emit = defineEmits(['list'])

const form = ref({
  name: authStore.auth.username,
  subject: '',
  content: '',
  saveFilename: '',
  originalFilename: ''
})

const uploadFile = ref(null);
const fileInput = ref(null);

const isEdit = props.postId ? true : false

onMounted(async () => {
    if(isEdit) {
        try{
            const res = await boardApi.getBoardForEdit(props.postId)
            form.value.subject = res.subject
            form.value.content = res.content
            form.value.saveFilename = res.saveFilename ?? ''
            form.value.originalFilename = res.originalFilename ?? ''
        }catch(err) {
            console.error("데이터 로딩 실패 :", err)
        }
    }
})

const handleFileChange = event => {
    uploadFile.value = event.target.files[0];
}

const savePost = async () => {
    try {
        const formData = new FormData();

        formData.append('subject', form.value.subject);
        formData.append('content', form.value.content);
        if (uploadFile.value) {
			formData.append('selectFile', uploadFile.value);
		}
        
        if (isEdit) {
            formData.append('saveFilename', form.value.saveFilename);
            formData.append('originalFilename', form.value.originalFilename);

            formData.append('num', props.postId);
            await boardApi.updateBoard(props.postId, formData);
        } else {
            await boardApi.createBoard(formData);
        }        

        uploadFile.value = null;
        fileInput.value.value = '';

        emit('list')
    } catch(err) {
      console.log(err)
    }
}

const deleteFile = async () => {
    try{
        if(! confirm('파일을 삭제 하시겠습니까 ? ')) {
            return;
        }

        await boardApi.deleteBoardFile(props.postId);

        form.value.saveFilename = '';
        form.value.originalFilename = ''
    } catch(err) {
      console.log(err)
    }
}
</script>

<template>
    <div class="pb-2">
        <span class="small-title">{{ isEdit ? '게시글 수정' : '새 글 작성' }}</span>
    </div>
    
    <form @submit.prevent="savePost">
        <table class="table write-form">
            <tbody>
                <tr>
                    <td class="col-md-2 bg-light">제 목</td>
                    <td>
                        <input type="text" v-model="form.subject"
                            class="form-control" maxlength="100" placeholder="Subject"
                            required>
                    </td>
                </tr>

                <tr>
                    <td class="col-md-2 bg-light">이 름</td>
                    <td>
                        <div class="row">
                            <div class="col-md-6">
                                <input type="text" v-model="form.name"
                                    class="form-control" readonly tabindex="-1">
                            </div>
                        </div>
                    </td>
                </tr>

                <tr>
                    <td class="col-md-2 bg-light">내 용</td>
                    <td>
                        <QuillEditor v-model="form.content" />                         
                    </td>
                </tr>

                <tr>
                    <td class="col-md-2 bg-light">파일</td>
                    <td>
                        <input type="file" ref="fileInput" class="form-control" @change="handleFileChange">
                    </td>
                </tr>

                <tr v-if="isEdit">
                    <td class="col-md-2 bg-light">첨부된파일</td>
                    <td> 
                        <p class="form-control-plaintext">
                            <template v-if="form.saveFilename">
                                <a href="#" @click.prevent="deleteFile"><i class="bi bi-trash"></i></a>
							{{ form.originalFilename }}
                            </template>
                            &nbsp;
                        </p>
                    </td>
                </tr>
	        </tbody>
        </table>

        <div class="text-center">
            <button type="submit" class="btn-accent btn-md me-1">{{ isEdit ? '수정 완료' : '등록 완료' }}</button>
            <button type="reset" class="btn-default btn-md me-1">다시입력</button>
            <button type="button" class="btn-default btn-md" @click="emit('list')">{{ isEdit ? '수정 취소' : '등록 취소' }}</button>
        </div>
    </form>

</template>

<style scoped>

</style>