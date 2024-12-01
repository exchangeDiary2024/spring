const notificationModal = document.querySelector(".notification-modal");

function clickCommentBlur(event) {
    event.preventDefault();

    if (commentBtn.classList.contains("selected") && event.target.classList.contains("comment-blur")) {
        offClickCommentBtn();
        commentBtn.classList.remove("selected");
    }
}

// todo: 댓글 외부 영역 클릭 이벤트 갈아엎기
// function clickWriteCommentOutside(event) {
//     event.preventDefault();
//
//     if (!notificationModal.contains(event.target)
//         && (commentBtn.classList.contains("selected") && !isWriteComment(event.target))
//     ) {
//             document.querySelector(".write .comment-character").remove();
//             commentArea.classList.remove("write");
//             commentBtn.classList.remove("selected");
//             document.querySelector(".comment").remove();
//             document.removeEventListener("click", clickWriteCommentOutside);
//     }
// }
//
// function isWriteComment(target) {
//     const commentBox = document.querySelector(".comment-box");
//
//     return (commentBox.contains(target) && target !== commentBox)
//         || (target.classList.contains("comment-character") && target.parentElement.classList.contains("write"))
//         || target.classList.contains("comment-arrow");
// }
//
// function clickViewCommentOutside(event) {
//     const viewCommentCharacter = document.querySelector(".note-content .comment-character:not(.view)");
//
//     if (!notificationModal.contains(event.target)
//         && (viewCommentCharacter && !isViewComment(event.target, viewCommentCharacter))
//     ) {
//         viewCommentCharacter.classList.add("view");
//         document.querySelector(".comment").remove();
//         document.removeEventListener("click", clickViewCommentOutside);
//     }
// }
//
// function isViewComment(target, viewCommentCharacter) {
//     const commentBox = document.querySelector(".comment-box");
//
//     return commentBox.contains(target)
//         || target === viewCommentCharacter
//         || target.classList.contains("comment-arrow");
// }
