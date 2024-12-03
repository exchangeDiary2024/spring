const notificationModal = document.querySelector(".notification-modal");
const confirmModal = document.querySelector(".confirm-modal");

function clickCommentBlur(event) {
    if (commentBtn.classList.contains("selected") && event.target.classList.contains("comment-blur")) {
        event.preventDefault();
        offClickCommentBtn();
        commentBtn.classList.remove("selected");
    }
}

function clickWriteCommentOutside(event) {
    event.preventDefault();

    const commentCharacter = document.querySelector(".comment-area .comment-character");
    const comment = document.querySelector(".comment");

    console.log(commentCharacter);
    if (!isInModal(event.target)
        && event.target !== commentCharacter
        && !comment.contains(event.target)
    ) {
        commentCharacter.remove();
        commentArea.classList.remove("write");
        commentBtn.classList.remove("selected");
        comment.remove();
        document.removeEventListener("click", clickWriteCommentOutside);
    }
}

function clickWrittenCommentOutside(event) {
    const viewCommentCharacter = document.querySelector(".note-content .comment-character:not(.written)");
    const comment = document.querySelector(".comment");

    if (!isInModal(event.target)
        && event.target !== viewCommentCharacter
        && !comment.contains(event.target)
    ) {
        viewCommentCharacter.classList.add("written");
        document.querySelector(".comment").remove();
        document.removeEventListener("click", clickWrittenCommentOutside);
    }
}

function isInModal(target) {
    return notificationModal.contains(target) || confirmModal.contains(target);
}
