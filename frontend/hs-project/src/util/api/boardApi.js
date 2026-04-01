import http from "@/util/http";

export const boardApi = {
  // GET
  //  { params }는 쿼리 스트링(Query String) 을 붙여서 GET 요청을 보낼 때 사용
  getBoardList(params = {}) {
    return http.get('/board', { params });
  },
  
  getBoardDetail(id, searchType, searchValue) {
    return http.get(`/board/${id}`, {
      params: {
        schType: searchType,
        kwd: searchValue
      }
    });
  },

  getBoardForEdit (id) {
    return http.get(`/board/${id}/edit`);
  },

  // POST
  createBoard(formData) {
    return http.post('/board', formData,
       { headers: { 'Content-Type': 'multipart/form-data' }}
    );
  },

  // PUT
  updateBoard(id, formData) {
    return http.put(`/board/${id}`,formData,
      { headers: { 'Content-Type': 'multipart/form-data' }}
    );
  },

  deleteBoardFile(id) {
    return http.delete(`/board/file/${id}`);
  },

  // DELETE
  deleteBoard(id) {
    return http.delete(`/board/${id}`);
  },

  downloadFile(id) {
    return http.get(`/board/download/${id}`, {
        responseType: 'blob'
    });
  },

  likeBoard(id, method) {
    return http[method](`/board/like/${id}`);  // 대괄호 표기법
  }  
};
