const COMMENT_AREA_BORDER = 4;
const COMMENT_AREA_TOP = 200;
const PAGE_MARGIN = document.querySelector(".left-margin").offsetWidth;

function clickCommentBtn(event) {
    event.preventDefault();

    if (!event.currentTarget.classList.contains("selected")) {
        onClickCommentBtn();
    } else {
        offClickCommentBtn();
    }
}

function onClickCommentBtn() {
    drawCommentProfileImage();
    addBlur();
    commentBtn.classList.add("selected");
}

function offClickCommentBtn() {
    removeBlur();
    commentArea.removeChild(document.querySelector(".comment"));
    commentBtn.classList.remove("selected");
}

function drawCommentProfileImage() {
    fetch(`/api${currentPathName}/comments/verify`)
        .then(async response => {
            if (response.status === 200) {
                return response.json();
            }
            throw await response.json();
        })
        .then(data => {
            const comment = createComment(data.profileImage);
            commentArea.appendChild(comment);
        })
        .catch(data => openNotificationModal("error",  [data.message], 2000));
}

function createComment(profileImage) {
    const comment = document.createElement("div");

    comment.classList.add("comment", "highlight", profileImage);
    comment.addEventListener("touchmove", moveProfileImage);
    comment.addEventListener("touchend", setProfileImage);
    return comment;
}

function addBlur() {
    const blur = document.createElement("div");
    blur.classList.add("blur");
    background.appendChild(blur);

    commentArea.classList.add("highlight");
}

function removeBlur() {
    background.lastChild.remove();
    commentArea.classList.remove("highlight");
}

function moveProfileImage(event) {
    event.preventDefault();

    const COMMENT_WIDTH = event.currentTarget.offsetWidth;
    const COMMENT_HEIGHT = event.currentTarget.offsetHeight;
    const x = getValidProfileImageLeft(event.touches[0].clientX - PAGE_MARGIN,  COMMENT_WIDTH) - COMMENT_WIDTH / 2;
    const y = getValidProfileImageTop(event.touches[0].clientY - COMMENT_AREA_TOP, COMMENT_HEIGHT) - COMMENT_HEIGHT / 2;

    event.currentTarget.style.left = `${x}px`;
    event.currentTarget.style.top = `${y}px`;
}

function getValidProfileImageLeft(left, commentWidth) {
    const COMMENT_AREA_WIDTH = commentArea.offsetWidth - COMMENT_AREA_BORDER * 2;
    const MINIMUM_LEFT = commentWidth / 2;
    const MAXIMUM_LEFT = MINIMUM_LEFT + COMMENT_AREA_WIDTH - commentWidth;

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
    const MINIMUM_TOP = commentHeight / 2;
    const MAXIMUM_TOP = MINIMUM_TOP + COMMENT_AREA_HEIGHT - commentHeight;

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
    }
}
