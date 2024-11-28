const COMMENT_AREA_BORDER = 4;
const COMMENT_AREA_TOP = 200;

function moveProfileImage(event) {
    event.preventDefault();

    const commentWidth = event.currentTarget.offsetWidth;
    const commentHeight = event.currentTarget.offsetHeight;

    console.log(`x: ${event.touches[0].clientX}, y: ${event.touches[0].clientY}`);

    const x = getValidProfileImageLeft(event.touches[0].clientX - commentWidth / 2,  commentWidth) + commentWidth / 2;
    const y = getValidProfileImageTop(event.touches[0].clientY - COMMENT_AREA_TOP - commentHeight / 2, commentHeight) + commentHeight / 2;

    event.currentTarget.style.left = `${x}px`;
    event.currentTarget.style.top = `${y}px`;
}

function getValidProfileImageLeft(left, commentWidth) {
    const COMMENT_AREA_WIDTH = commentArea.offsetWidth - COMMENT_AREA_BORDER * 2;
    const MINIMUM_LEFT = 0;
    const MAXIMUM_LEFT = COMMENT_AREA_WIDTH - commentWidth;

    if (left <= MINIMUM_LEFT) {
        return MINIMUM_LEFT;
    }
    if (left >= MAXIMUM_LEFT) {
        return MAXIMUM_LEFT;
    }
    return left;
}

function getValidProfileImageTop(top, commentHeight) {
    const COMMENT_AREA_HEIGHT = commentArea.offsetHeight - COMMENT_AREA_BORDER * 2;
    const MINIMUM_TOP = 0;
    const MAXIMUM_TOP = COMMENT_AREA_HEIGHT - commentHeight;

    if (top <= MINIMUM_TOP) {
        return MINIMUM_TOP;
    }
    if (top >= MAXIMUM_TOP) {
        return MAXIMUM_TOP;
    }
    return top;
}

function setProfileImage(event) {
    event.preventDefault();

    confirmProfileImagePosition();
}

async function confirmProfileImagePosition() {
    const result = await openConfirmModal("여기에 댓글을 쓸까요?");

    if (result) {
        const comment = document.querySelector(".comment");

        comment.classList.remove("highlight");
        comment.removeEventListener("touchmove", moveProfileImage);
        comment.removeEventListener("touchend", setProfileImage);
        removeBlur();
        commentBtn.classList.remove("selected");

        //todo
        document.addEventListener("click", handleClickOutside);

        writeComment();
    }
}

function handleClickOutside(event) {
    if (!(event.target.classList.contains("comment") || event.target.classList.contains("comment-character-icon"))) {
        commentArea.removeChild(document.querySelector(".comment"));
        document.removeEventListener("click", handleClickOutside);
    }
}

function writeComment() {

}
