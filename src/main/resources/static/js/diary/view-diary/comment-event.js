const notificationModal = document.querySelector(".notification-modal");
const confirmModal = document.querySelector(".confirm-modal");
let previousCommentTextHeight = 24;

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

    if (!isInModal(event.target)
        && event.target !== commentCharacter
        && !comment.contains(event.target)
    ) {
        commentCharacter.remove();
        commentArea.classList.remove("write");
        commentBtn.classList.remove("selected");
        comment.remove();
        previousCommentTextHeight = 24;
        document.removeEventListener("click", clickWriteCommentOutside);
    }
}

function clickWrittenCommentOutside(event) {
    event.preventDefault();

    const viewCommentCharacter = document.querySelector(".note-content .comment-character:not(.written)");
    const comment = document.querySelector(".comment");

    if (!isInModal(event.target)
        && event.target !== viewCommentCharacter
        && !comment.contains(event.target)
    ) {
        viewCommentCharacter.classList.add("written");
        document.querySelector(".comment").remove();
        previousCommentTextHeight = 24;
        document.removeEventListener("click", clickWrittenCommentOutside);
    }
}

function isInModal(target) {
    return notificationModal.contains(target) || confirmModal.contains(target);
}

function adjustCommentBoxHeightByTextarea() {
    const commentText = document.querySelector(".comment-text");
    var maximumHeight = 44;
    if (commentBtn.classList.contains("selected")) {
        maximumHeight = 104;
    }

    if (commentText.scrollHeight > maximumHeight) {
        commentText.style.overflow = "scroll";
        commentText.style.height = `${maximumHeight - 4}px`;
        previousCommentTextHeight = maximumHeight;
        return ;
    }

    commentText.style.height = "auto";
    commentText.style.height = `${commentText.scrollHeight}px`;

    if (commentText.scrollHeight !== previousCommentTextHeight) {
        const gap = commentText.scrollHeight > previousCommentTextHeight ? 20 : -20;

        commentText.style.overflow = "none";
        previousCommentTextHeight = commentText.scrollHeight;

        const comment = document.querySelector(".comment");
        const commentBox = document.querySelector(".comment-box");
        const commentTextarea = document.querySelector(".comment-textarea");

        comment.style.height = `${parseInt(comment.style.height) + gap}px`;
        commentBox.style.height = `${parseInt(commentBox.style.height) + gap}px`;
        commentTextarea.style.height = `${parseInt(commentTextarea.style.height) + gap}px`;

        if (!commentBtn.classList.contains("selected")) {
            const replyBox = document.querySelector(".reply-box");
            replyBox.style.height = `${parseInt(replyBox.style.height) + gap}px`;
        }
    }
}
