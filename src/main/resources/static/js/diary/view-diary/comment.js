const commentBtn = document.querySelector(".comment-btn");
const commentArea = document.querySelector(".comment-area");
const currentPathName = window.location.pathname;

init();

function init() {
    commentBtn.addEventListener("click", clickCommentBtn);
    document.addEventListener("click", clickCommentBlur);
}
