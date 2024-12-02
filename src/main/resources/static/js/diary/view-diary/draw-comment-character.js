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
    }
}

async function onClickCommentBtn() {
    closeSelectedCommentCharacter();

    const result = await drawCommentCharacter();

    if (result) {
        addBlur();
        commentArea.classList.add("write");
        commentBtn.classList.add("selected");
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

function createCharacter(profileImage) {
    const commentCharacter = document.createElement("div");

    commentCharacter.classList.add("comment-character", "highlight", profileImage);
    commentCharacter.style.left = "158px";
    commentCharacter.style.top = "190px";
    commentCharacter.addEventListener("touchmove", moveCommentCharacter);
    commentCharacter.addEventListener("touchend", setCommentCharacter);
    return commentCharacter;
}

function addBlur() {
    commentArea.parentElement.classList.add("comment-blur");
    commentArea.classList.add("highlight");
}

function removeBlur() {
    commentArea.parentElement.classList.remove("comment-blur");
    commentArea.classList.remove("highlight");
}

function moveCommentCharacter(event) {
    event.preventDefault();

    const COMMENT_WIDTH = event.currentTarget.offsetWidth;
    const COMMENT_HEIGHT = event.currentTarget.offsetHeight;
    const x = getValidCommentCharacterLeft(event.touches[0].clientX - PAGE_MARGIN,  COMMENT_WIDTH) - COMMENT_WIDTH / 2;
    const y = getValidCommentCharacterTop(event.touches[0].clientY - COMMENT_AREA_TOP, COMMENT_HEIGHT) - COMMENT_HEIGHT / 2;

    event.currentTarget.style.left = `${x}px`;
    event.currentTarget.style.top = `${y}px`;
}

function getValidCommentCharacterLeft(left, commentWidth) {
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

function getValidCommentCharacterTop(top, commentHeight) {
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

function setCommentCharacter(event) {
    event.preventDefault();

    confirmCommentCharacterPosition();
}

async function confirmCommentCharacterPosition() {
    const result = await openConfirmModal("여기에 댓글을 쓸까요?");

    if (result) {
        const character = document.querySelector(".write .comment-character");

        character.classList.remove("highlight");
        character.removeEventListener("touchmove", moveCommentCharacter);
        character.removeEventListener("touchend", setCommentCharacter);
        removeBlur();

        drawComment(character.classList[1], commentArea);
    }
}
