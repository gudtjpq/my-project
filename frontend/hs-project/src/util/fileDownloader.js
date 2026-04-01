export async function downloadFileFromServer(fileId, apiInstance, fileName = 'download') {
  try {
    // API 호출(axios 인스턴스 전달)
    const res = await apiInstance.downloadFile(fileId);

    // Blob으로 변환
    const blob = new Blob([res.data]);

    // URL 생성
    const url = window.URL.createObjectURL(blob);

    // 파일 이름 추출(서버에서 content-disposition 헤더를 제공 할 경우)
    console.log(res.headers)

    const disposition = res.headers['content-disposition'];
    if (disposition && disposition.includes('filename=')) {
      fileName = decodeURIComponent(
        disposition.split('filename=')[1].replace(/"/g, '')
      );
    }

    // a 태그를 만들어 다운로드 실행
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', fileName);
    document.body.appendChild(link);
    link.click();

    link.remove();
    window.URL.revokeObjectURL(url);

  } catch (error) {
    // 서버 에러 처리
    if (error.response?.data instanceof Blob) {
      const text = await error.response.data.text();
      try {
        const json = JSON.parse(text);
        alert(json.message || "파일 다운로드 중 오류가 발생했습니다.");
      } catch {
        alert(text);
      }
    } else {
      alert("서버 연결 중 오류가 발생했습니다.");
    }
  }
}
