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

function addEventInCommentBox() {
    if (commentBtn.classList.contains("selected")) {
        document.querySelector(".comment-bar .write-comment-btn").addEventListener("click", clickWriteCommentBtn);
        document.addEventListener("click", clickWriteCommentOutside);
    } else {
        isActive = false;
        document.querySelector(".reply-bar .write-comment-btn").addEventListener("click", clickWriteReplyBtn);
        document.addEventListener("click", clickWrittenCommentOutside);
    }
    document.querySelector(".sticker-btn").addEventListener("click", clickStickerBtn);

    addEventInCommentTextarea();
    addEventToStickers();
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
        isActive = true;
        document.removeEventListener("click", clickWrittenCommentOutside);
    }
}

function isInModal(target) {
    return notificationModal.contains(target) || confirmModal.contains(target);
}

function addEventInCommentTextarea() {
    const textarea = document.querySelector(".comment-textarea");

    textarea.addEventListener("input", (event) => {
        // if (textarea.lastChild.nodeType !== Node.TEXT_NODE) {
        //     textarea.appendChild(document.createTextNode("\u200B"));
        // }
        adjustCommentBoxHeightByTextarea();
        moveCursorToEnd(textarea);
    });

    textarea.addEventListener("keydown", (event) => {
        if (event.key === "Backspace"
            && (textarea.lastChild && textarea.lastChild.nodeType === Node.ELEMENT_NODE)
        ) {
            event.preventDefault();
            textarea.lastChild.remove();
        }
        if (event.key === "Enter") {
            event.preventDefault();
            textarea.appendChild(document.createElement("br"));
        }
        if (textarea.innerHTML === "<br>") {
            textarea.innerHTML = "";
        }
        adjustCommentBoxHeightByTextarea();
        moveCursorToEnd(textarea);
    });
    textarea.addEventListener("keyup", () => {
        console.log(`innerHTML: ${textarea.innerHTML}`)
    })
}

function moveCursorToEnd(element) {
    const range = document.createRange();
    const selection = window.getSelection();

    range.selectNodeContents(element);
    range.collapse(false);
    selection.removeAllRanges();
    selection.addRange(range);
}

function adjustCommentBoxHeightByTextarea() {
    const commentText = document.querySelector(".comment-textarea");
    commentText.style.height = "auto";
    const commentTextHeight = parseInt(commentText.scrollHeight / 20) * 20 + 4;
    const maximumHeight = commentBtn.classList.contains("selected") ? 104 : 44;

    if (commentTextHeight > maximumHeight) {
        commentText.style.overflow = "scroll";
        commentText.style.height = `${maximumHeight}px`;
        previousCommentTextHeight = maximumHeight;
        return ;
    }

    commentText.style.height = `${commentTextHeight}px`;

    if (commentTextHeight > 0 && commentTextHeight !== previousCommentTextHeight) {
        const gap = commentTextHeight > previousCommentTextHeight ? 20 : -20;

        commentText.style.overflow = "none";
        previousCommentTextHeight = commentTextHeight;

        const comment = document.querySelector(".comment");
        const commentBox = document.querySelector(".comment-box");

        comment.style.height = `${parseInt(comment.style.height) + gap}px`;
        commentBox.style.height = `${parseInt(commentBox.style.height) + gap}px`;

        if (!commentBtn.classList.contains("selected")) {
            const replyBox = document.querySelector(".reply-box");
            replyBox.style.height = `${parseInt(replyBox.style.height) + gap}px`;
        }
    }
}
