const notificationModal = document.querySelector(".notification-modal");
const confirmModal = document.querySelector(".confirm-modal");

function clickCommentBlur(event) {
    event.preventDefault();

    if (commentBtn.classList.contains("selected") && event.target.classList.contains("comment-blur")) {
        offClickCommentBtn();
        commentBtn.classList.remove("selected");
    }
}

function clickWriteCommentOutside(event) {
    event.preventDefault();

    if (!isInModal(event.target)
        && (commentBtn.classList.contains("selected") && !isWriteComment(event.target))
    ) {
            document.querySelector(".write .comment-character").remove();
            commentArea.classList.remove("write");
            commentBtn.classList.remove("selected");
            document.querySelector(".comment").remove();
            document.removeEventListener("click", clickWriteCommentOutside);
    }
}

function isWriteComment(target) {
    const commentBox = document.querySelector(".comment-box");

    return (commentBox.contains(target) && target !== commentBox)
        || (target.classList.contains("comment-character") && target.parentElement.classList.contains("write"))
        || target.classList.contains("comment-arrow");
}

function clickWrittenCommentOutside(event) {
    const viewCommentCharacter = document.querySelector(".note-content .comment-character:not(.written)");

    if (!isInModal(event.target)
        && (viewCommentCharacter && !isWrittenComment(event.target, viewCommentCharacter))
    ) {
        viewCommentCharacter.classList.add("written");
        document.querySelector(".comment").remove();
        document.removeEventListener("click", clickWrittenCommentOutside);
    }
}

function isWrittenComment(target, viewCommentCharacter) {
    const commentBox = document.querySelector(".comment-box");

    return commentBox.contains(target)
        || target === viewCommentCharacter
        || target.classList.contains("comment-arrow");
}

function isInModal(target) {
    return notificationModal.contains(target) || confirmModal.contains(target);
}
