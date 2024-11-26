let isPress = false;
let x = 0;
let y = 0;
let deltaX = 0;
let deltaY = 0;

function touchProfileImage(event) {
    x = event.touches[0].clientX;
    y = event.touches[0].clientY;
    isPress = true;
}

function moveProfileImage(event) {
    const target = event.currentTarget;

    event.preventDefault();

    if (isPress) {
        deltaX = event.touches[0].clientX - x;
        deltaY = event.touches[0].clientY - y;
        event.currentTarget.style.left = getValidProfileImageLeft(target.offsetLeft + deltaX, target.offsetWidth) + "px";
        event.currentTarget.style.top = getValidProfileImageTop(target.offsetTop + deltaY, target.offsetHeight) + "px";
        x = event.touches[0].clientX;
        y = event.touches[0].clientY;
    }
}

function getValidProfileImageLeft(left, width) {
    const minimumLeft = 0;
    const maximumLeft = document.querySelector(".comment-area").offsetWidth - width;

    if (left < minimumLeft) {
        return minimumLeft;
    }
    if (left > maximumLeft) {
        return maximumLeft;
    }
    return left;
}

function getValidProfileImageTop(top, height) {
    const minimumTop = 0;
    const maximumTop = document.querySelector(".comment-area").offsetHeight - height;

    if (top < minimumTop) {
        return minimumTop;
    }
    if (top > maximumTop) {
        return maximumTop;
    }
    return top;
}

function setProfileImage(event) {
    event.preventDefault();

    confirmProfileImagePosition();

    deltaX = 0;
    deltaY = 0;
    isPress = false;
}

async function confirmProfileImagePosition() {
    // todo: 0.5초 delay
    const result = await openConfirmModal("여기에 댓글을 쓸까요?");

    if (result) {
        removeBlur();
        comment.classList.remove("highlight");
        comment.removeEventListener("touchstart", touchProfileImage);
        comment.removeEventListener("touchmove", moveProfileImage);
        comment.removeEventListener("touchend", setProfileImage);
        commentBtn.classList.remove("selected");

        //todo
        document.addEventListener("click", handleClickOutside);

        writeComment();
    }
}

function handleClickOutside(event) {
    if (!(event.target.classList.contains("comment") || event.target.classList.contains("comment-character-icon"))) {
        note.removeChild(document.querySelector(".note .comment"));
        document.removeEventListener("click", handleClickOutside);
    }
}

function writeComment() {

}
