const COMMENT_AREA_BORDER = 4;
const COMMENT_AREA_TOP = 200;
const PAGE_MARGIN = document.querySelector(".left-margin").offsetWidth;

function clickCommentBtn(event) {
    event.preventDefault();

    if (commentBtn.classList.contains("selected")) {
        offClickCommentBtn();
        commentBtn.classList.remove("selected");
    } else {
        onClickCommentBtn();
        commentBtn.classList.add("selected");
    }
}

async function onClickCommentBtn() {
    const result = await drawCommentCharacter();

    if (result) {
        addBlur();
        commentArea.classList.add("write");
    }
}

function offClickCommentBtn() {
    removeBlur();
    commentArea.removeChild(document.querySelector(".write .comment-character"));
    commentArea.classList.remove("write");
}

async function drawCommentCharacter() {
    return await fetch(`/api${currentPathName}/comments/verify`)
        .then(async response => {
            if (response.status === 200) {
                return response.json();
            }
            throw await response.json();
        })
        .then(data => {
            const character = createCharacter(data.profileImage);
            commentArea.appendChild(character);
            return true;
        })
        .catch(data => {
            openNotificationModal("error",  [data.message], 2000);
            return false;
        });
}

function createCharacter(character) {
    const comment = document.createElement("div");

    comment.classList.add("comment-character", "highlight", character);
    comment.addEventListener("touchmove", moveCharacter);
    comment.addEventListener("touchend", setCharacter);
    return comment;
}

function addBlur() {
    commentArea.parentElement.classList.add("comment-blur");
    commentArea.classList.add("highlight");
}

function removeBlur() {
    commentArea.parentElement.classList.remove("comment-blur");
    commentArea.classList.remove("highlight");
}

function moveCharacter(event) {
    event.preventDefault();

    const COMMENT_WIDTH = event.currentTarget.offsetWidth;
    const COMMENT_HEIGHT = event.currentTarget.offsetHeight;
    const x = getValidCharacterLeft(event.touches[0].clientX - PAGE_MARGIN,  COMMENT_WIDTH) - COMMENT_WIDTH / 2;
    const y = getValidCharacterTop(event.touches[0].clientY - COMMENT_AREA_TOP, COMMENT_HEIGHT) - COMMENT_HEIGHT / 2;

    event.currentTarget.style.left = `${x}px`;
    event.currentTarget.style.top = `${y}px`;
}

function getValidCharacterLeft(left, commentWidth) {
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

function getValidCharacterTop(top, commentHeight) {
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

function setCharacter(event) {
    event.preventDefault();

    confirmCharacterPosition();
}

async function confirmCharacterPosition() {
    const result = await openConfirmModal("여기에 댓글을 쓸까요?");

    if (result) {
        const character = document.querySelector(".write .comment-character");

        character.classList.remove("highlight");
        character.removeEventListener("touchmove", moveCharacter);
        character.removeEventListener("touchend", setCharacter);
        removeBlur();

        document.addEventListener("click", clickCommentOutside);

        drawComment("write", character.classList[1], commentArea);
    }
}

function clickCommentOutside(event) {
    event.preventDefault();

    // todo: notification modal 생겼을 때, 처리
    // todo: 빈 comment box 부분 클릭

    if (!commentArea.contains(event.target) || event.target.classList.contains("comment-area")) {
        document.querySelector(".comment").remove();
        document.querySelector(".write .comment-character").remove();
        commentArea.classList.remove("write");
        commentBtn.classList.remove("selected");
        document.removeEventListener("click", clickCommentOutside);
    }
}
