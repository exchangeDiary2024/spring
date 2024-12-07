const STANDARD_TOP = 137;
const STANDARD_LEFT = 76;
const COMMENT_BAR_HTML = `
<div class="comment-bar">
    <div class="comment-textarea">
        <textarea class="comment-text" placeholder="댓글을 입력해주세요." spellcheck="false"></textarea>
    </div>
    <a class="sticker-btn" href="#">
        <img class="sticker-icon">
    </a>
    <a class="write-comment-btn" href="#">
        <img class="bar-icon" src="/images/diary/write-page/write_icon.svg"/>
    </a>
</div>
`;

async function drawComment(characterColor, commentParent) {
    const comment = await createComment();
    const character = document.querySelector(`.comment-character.${characterColor}`);

    commentParent.appendChild(comment);

    processByCommentVertical(character, comment);
    processByCommentHorizontal(character, comment);

    if (!commentBtn.classList.contains("selected")) {
        adjustCommentBoxHeight();
    }
    comment.innerHTML += STICKER_BAR_HTML;

    addEventInCommentBox();
}

function addEventInCommentBox() {
    if (commentBtn.classList.contains("selected")) {
        document.querySelector(".comment-bar .write-comment-btn").addEventListener("click", clickWriteCommentBtn);
        document.addEventListener("click", clickWriteCommentOutside);
    } else {
        document.querySelector(".reply-bar .write-comment-btn").addEventListener("click", clickWriteReplyBtn);
        document.addEventListener("click", clickWrittenCommentOutside);
    }

    document.querySelector(".sticker-btn").addEventListener("click", clickStickerBtn);
    addEventToStickers();
}

async function createComment() {
    const comment = document.createElement("div");

    comment.classList.add("comment");
    if (commentBtn.classList.contains("selected")) {
        comment.style.height = "70px";
    } else {
        comment.style.height = "125px";
    }
    comment.appendChild(await createCommentBox());
    return comment;
}

async function createCommentBox() {
    const commentBox = document.createElement("div");

    commentBox.classList.add("comment-box");
    if (commentBtn.classList.contains("selected")) {
        commentBox.style.height = "50px";
        commentBox.innerHTML =  COMMENT_BAR_HTML;
    } else {
        commentBox.style.height = "100px";
        commentBox.innerHTML = await makeViewCommentBoxHTML();
    }
    return commentBox;
}

async function makeViewCommentBoxHTML() {
    const commentId = document.querySelector(".note-content .comment-character:not(.written)").classList[2];
    const comment =  await getCommentById(commentId);

    return `
    <div class="written-comment" style="height: 50px;">
        <p class="written-comment-text">${comment.content}</p>
    </div>
     <div class="reply-box" style="height: 44px;">
        <div class="reply-bar">
            <div class="reply-character ${comment.profileImage}"></div>
            <div class="comment-textarea">
                <textarea class="comment-text" placeholder="답글을 입력해주세요." spellcheck="false"></textarea>
            </div>
            <a class="sticker-btn" href="#">
                <img class="sticker-icon" src="/images/diary/view-page/sticker-icon.png">            
            </a>
            <a class="write-comment-btn" href="#">
                <img class="bar-icon" src="/images/diary/write-page/write_icon.svg"/>
            </a>
        </div>
    </div>
    `;
}

async function getCommentById(commentId) {
    return await fetch(`/api${currentPathName}/comments/${commentId}`)
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else {
                openNotificationModal("error", "댓글 조회 실패", 2000);
            }
        });
}

function processByCommentVertical(character, comment) {
    if (character.offsetLeft <= STANDARD_LEFT) {
        comment.classList.add("left");
    }
    if (character.offsetLeft >= commentArea.offsetWidth - STANDARD_LEFT - character.offsetWidth) {
        comment.classList.add("right");
    }
}

function processByCommentHorizontal(character, comment) {
    const commentArrow = createCommentArrow(character, comment);

    if (character.offsetTop < STANDARD_TOP) {
        comment.style.top = `${character.offsetTop + character.offsetHeight}px`;
        comment.classList.add("top");
        comment.prepend(commentArrow);
    } else {
        if (commentBtn.classList.contains("selected")) {
            comment.style.bottom = `${comment.parentElement.offsetHeight - character.offsetTop}px`;
        } else {
            comment.style.bottom = `${comment.parentElement.offsetHeight - character.offsetTop - 5}px`;
        }
        comment.classList.add("bottom");
        comment.appendChild(commentArrow);
    }
}

function createCommentArrow(character, comment) {
    const commentArrow = document.createElement("div");

    commentArrow.classList.add("comment-arrow");
    commentArrow.style.left = `${character.offsetLeft - comment.offsetLeft + 6}px`;
    return commentArrow;
}

function adjustCommentBoxHeight() {
    const commentText = document.querySelector(".written-comment-text");
    const comment = document.querySelector(".comment");
    const commentBox = document.querySelector(".comment-box");
    const writtenComment = document.querySelector(".written-comment");

    if (commentText.offsetHeight > 60) {
        commentText.style.height = "60px";
        commentText.style.overflow = "scroll";
    }
    comment.style.height = `${parseInt(comment.style.height) + (commentText.offsetHeight - 25)}px`;
    commentBox.style.height = `${parseInt(commentBox.style.height) + (commentText.offsetHeight - 25)}px`;
    writtenComment.style.height = `${parseInt(writtenComment.style.height) + (commentText.offsetHeight - 25)}px`;
}

async function clickWriteCommentBtn(event) {
    event.preventDefault();

    const result = await openConfirmModal("댓글을 작성할까요?", "댓글은 수정, 삭제가 불가하니 신중하게 결정해 주세요.");

    if (result) {
        writeComment();
    }
}

function writeComment() {
    const commentCharacter = document.querySelector(".write .comment-character");
    const commentText = document.querySelector(".comment-text");

    fetch(`/api${currentPathName}/comments`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "xCoordinate": parseFloat(commentCharacter.style.left) + 5,
            "yCoordinate": parseFloat(commentCharacter.style.top) + 26,
            "page": currentPage.index,
            "content": commentText.value
        })
    })
        .then(async response => {
            if (response.status !== 201) {
                throw await response.json();
            }
            window.location.reload();
        })
        .catch(data => {
            openNotificationModal("error", [data.message], 2000);
        });
}

async function clickWriteReplyBtn(event) {
    event.preventDefault();

    const result = await openConfirmModal("답글을 작성할까요?", "답글은 수정, 삭제가 불가하니 신중하게 결정해 주세요.");

    if (result) {
        writeReply();
    }
}

function writeReply() {
    const commentCharacter = document.querySelector(".note-content .comment-character:not(.written)");
    const commentText = document.querySelector(".reply-bar .comment-text");
    const commentId = commentCharacter.classList[2];

    fetch(`/api${currentPathName}/comments/${commentId}/replies`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "content": commentText.value
        })
    })
        .then(async response => {
            if (response.status !== 201) {
                throw await response.json();
            }
            window.location.reload();
        })
        .catch(data => {
            openNotificationModal("error", [data.message], 2000);
        });
}
