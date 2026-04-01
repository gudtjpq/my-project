import { useHostStore } from '@/stores/hostStore' 
import http from "@/util/http";

// 이미지 업로드
export function imageHandler(quill, uploadUrl) {
    const input = document.createElement("input")
    input.type = "file"
    input.accept = "image/*"
    input.click()

    input.onchange = () => {
        const file = input.files[0]
        uploadImage(quill, file, uploadUrl)
    }
}

async function uploadImage(quill, file, uploadUrl){
    if(!file) return

    const formData = new FormData()
    formData.append("imageFile", file)

    const hostStore = useHostStore()
    // const url = hostStore.host + uploadUrl

    try{
        const res = await http.post(uploadUrl, formData,
                { headers: { 'Content-Type': 'multipart/form-data' } }  )

        const imageUrl = res.imageUrl
        if(! imageUrl){
            alert("이미지 업로드 실패")
            return
        }

        const range = quill.getSelection()
        const index = range ? range.index : quill.getLength()

        quill.insertEmbed(index,"image",imageUrl)
        quill.setSelection(index + 1)

    } catch(err) {
        console.error(err)
    }
}

export function handleDrop(quill, uploadUrl, e) {
  e.preventDefault()

  const file = e.dataTransfer.files[0]

  if (file && file.type.startsWith("image/")) {
    uploadImage(quill, file, uploadUrl)
  }
}

export function handlePaste(quill, uploadUrl, e) {
  const items = e.clipboardData?.items

  if (!items) return

  for (const item of items) {

    if (item.type.indexOf("image") !== -1) {

      e.preventDefault()

      const file = item.getAsFile()
      uploadImage(quill, file, uploadUrl)

    }
  }
}
