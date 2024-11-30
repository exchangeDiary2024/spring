const commentBtn = document.querySelector(".comment-btn");
const commentArea = document.querySelector(".comment-area");
const currentPathName = window.location.pathname;

init();

function init() {
    commentBtn.addEventListener("click", clickCommentBtn);
    document.addEventListener("click", clickCommentBlur);
}

function clickCommentBlur(event) {
    event.preventDefault();

    if (commentArea.classList.contains("write") && event.target.classList.contains("comment-blur")) {
        offClickCommentBtn();
    }
}
