function clickStickerBtn(event) {
    event.preventDefault();

    const stickerBtn = event.currentTarget;
    const stickerBar = document.querySelector(".sticker-bar");

    if (stickerBar.style.display === "block") {
        closeStickerBar();
        stickerBar.style.display = "none";
        stickerBtn.classList.remove("selected");
    } else {
        openStickerBar();
        stickerBar.style.display = "block";
        stickerBtn.classList.add("selected");
    }
}

function closeStickerBar() {
    const comment = document.querySelector(".comment");
    const commentBox = document.querySelector(".comment-box");
    const selectedSticker = document.querySelector(".sticker.selected");

    commentBox.style.height = `${parseInt(commentBox.style.height) - 50}px`;

    if (comment.classList.contains("bottom")) {
        commentBox.style.marginTop = `${parseInt(commentBox.style.marginTop) + 50}px`;
    }
    if (!commentBtn.classList.contains("selected")) {
        const replyBox = document.querySelector(".reply-box");

        replyBox.style.height = `${parseInt(replyBox.style.height) - 44}px`;
    }
    removeSelectedSticker(selectedSticker);
}

function openStickerBar() {
    const comment = document.querySelector(".comment");
    const commentBox = document.querySelector(".comment-box");

    commentBox.style.height = `${parseInt(commentBox.style.height) + 50}px`;
    if (comment.classList.contains("bottom")) {
        commentBox.style.marginTop = `${parseInt(commentBox.style.marginTop) - 50}px`;
    }
    if (!commentBtn.classList.contains("selected")) {
        const replyBox = document.querySelector(".reply-box");

        replyBox.style.height = `${parseInt(replyBox.style.height) + 44}px`;
    }
}

function addEventToStickers() {
    const stickerBar = document.querySelectorAll(".sticker");

    stickerBar.forEach(sticker => sticker.addEventListener("click", clickStickerIcon));
}

function clickStickerIcon(event) {
    event.preventDefault();

    const selectedSticker = document.querySelector(".sticker.selected");

    removeSelectedSticker(selectedSticker);
    if (selectedSticker !== event.currentTarget) {
        event.currentTarget.classList.add("selected");
    }
}

function removeSelectedSticker(selectedSticker) {
    if (selectedSticker) {
        selectedSticker.classList.remove("selected");
    }
}
