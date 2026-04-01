import { imageHandler } from "./imageUploader";
import Quill from "quill"
import QuillResize from "quill-resize-module"

Quill.register("modules/resize", QuillResize)

export function initQuill(selector, options = {}) {
	const uploadUrl = options.uploadUrl || '/editor/upload';
	
	const quill = new Quill(selector, {
		modules: {
			toolbar: [
				[{ 'header': [1, 2, 3, 4, 5, 6, false] }],
				['bold', 'italic', 'underline', 'strike'],
				[{ 'align': [] }],
				[{ 'color': [] }, { 'background': [] }],
				[{ list: 'ordered' }, { list: 'bullet' }, { 'indent': '-1'}, { 'indent': '+1' }],
				['blockquote', 'code-block'],
				['link', 'image'],
				['clean'],
			],
			resize: {
			},
		},
		
		placeholder: options.placeholder || 'Content',
		theme: 'snow',
	});
	
	const toolbar = quill.getModule('toolbar');
	toolbar.addHandler('image', () => imageHandler(quill, uploadUrl));
	
	addSourceViewButton(quill);

	return quill;
}

// 소스 보기(HTML) 버튼
function addSourceViewButton(quill) {
    const toolbarContainer = quill.getModule('toolbar').container;
    const lastGroup = toolbarContainer.querySelector('.ql-formats:last-child') 
                      || toolbarContainer.appendChild(document.createElement('span'));
    
    if (!lastGroup.classList.contains('ql-formats')) lastGroup.classList.add('ql-formats');

    const button = document.createElement('button');
    button.innerHTML = '<span style="font-weight:bold; font-size:11px;">HTML</span>';
    button.title = 'Source';
    button.type = 'button';
    button.className = 'ql-custom-button';
    button.style.width = '45px';

    let isSourceView = false;
    
    button.addEventListener('click', () => {
        const editorEl = quill.container.firstChild; // .ql-editor
        const container = quill.container;
        
        if (!isSourceView) {
            const html = editorEl.innerHTML;
            quill.disable();
            editorEl.style.display = 'none';

            const textarea = document.createElement('textarea');
            textarea.id = 'html-view';
            textarea.className = 'default-control';
            textarea.style.cssText = "width:100%; height:100%; min-height:200px; border:1px solid #ccc; padding:10px; box-sizing:border-box;";
            textarea.value = html;
            container.appendChild(textarea);
            
            button.innerHTML = '<span style="font-weight:bold; font-size:11px;">View</span>';
            isSourceView = true;
        } else {
            const textarea = container.querySelector('#html-view');
            quill.root.innerHTML = textarea.value;
            quill.enable();
            textarea.remove();
            editorEl.style.display = '';
            
            button.innerHTML = '<span style="font-weight:bold; font-size:11px;">HTML</span>';
            isSourceView = false;
        }
    });

    lastGroup.appendChild(button);
}
