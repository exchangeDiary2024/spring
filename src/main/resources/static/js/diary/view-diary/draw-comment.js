const STANDARD_TOP = 137;
const STANDARD_LEFT = 76;
const MAXIMUM_REPLY_BOX_HEIGHT = 100;
const NEWLINE_REGEX = /^[\r\n]*$/;
const WHITESPACE_REGEX = /^\s+$/;
const COMMENT_BAR_HTML = `
<div class="comment-bar">
    <div class="comment-textarea" contenteditable="true" spellcheck="false" style="height: 20px;"></div>
    <a class="write-comment-btn" href="#">
        <img class="bar-icon" src="/images/diary/write-page/write_icon.svg"/>
    </a>
    <a class="sticker-btn" href="#">
        <img class="sticker-icon">
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
        adjustHeightByWrittenComment();
        adjustHeightByWrittenReplies();
    }
    comment.innerHTML += STICKER_BAR_HTML;

    addEventInCommentBox();
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
        commentBox.innerHTML = await makeWrittenCommentBoxHTML();
    }
    return commentBox;
}

async function makeWrittenCommentBoxHTML() {
    const commentId = document.querySelector(".note-content .comment-character:not(.written)").classList[2];
    const comment =  await getCommentById(commentId);
    const repliesHTML = makeRepliesHTML(comment.replies);
    const replyBarHTML = makeReplyBoxHTML(comment.profileImage)

    return `
    <div class="written-comment" style="height: 50px;">
        <div class="written-comment-text">${comment.content}</div>
    </div>
     <div class="reply-box" style="height: 44px;">
        ${repliesHTML}
        ${replyBarHTML}
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

function makeRepliesHTML(replies) {
    var repliesHTML = "";

    if (replies.length === 0) {
        return "";
    }

    replies.forEach(reply => {
        repliesHTML += `
        <div class="reply">
            <div class="reply-character">
                <img class="reply-character-icon ${reply.profileImage}">
            </div>
            <div class="reply-text">${reply.content}</div>
        </div>
        `;
    })
    return `
    <div class="replies">
        ${repliesHTML}
    </div>
    <div class="reply-partition"></div>
    `;
}

function makeReplyBoxHTML(profileImage) {
    return `
    <div class="reply-bar">
        <div class="reply-character">
            <img class="reply-character-icon ${profileImage}">
        </div>
        <div class="comment-textarea" contenteditable="true" spellcheck="false" style="height: 20px;"></div>
        <a class="write-comment-btn" href="#">
            <img class="bar-icon" src="/images/diary/write-page/write_icon.svg"/>
        </a>
        <a class="sticker-btn" href="#">
            <img class="sticker-icon">
        </a>
    </div>
    `;
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
    var standard = STANDARD_TOP;

    if (!commentBtn.classList.contains("selected")) {
        standard -= 18;
    }
    if (character.offsetTop < standard) {
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

function adjustHeightByWrittenComment() {
    const commentText = document.querySelector(".written-comment-text");
    const comment = document.querySelector(".comment");
    const commentBox = document.querySelector(".comment-box");
    const writtenComment = document.querySelector(".written-comment");

    if (commentText.offsetHeight > 40) {
        commentText.style.height = "40px";
        commentText.style.overflow = "scroll";
    }
    comment.style.height = `${parseInt(comment.style.height) + (commentText.offsetHeight - 26)}px`;
    commentBox.style.height = `${parseInt(commentBox.style.height) + (commentText.offsetHeight - 26)}px`;
    writtenComment.style.height = `${parseInt(writtenComment.style.height) + (commentText.offsetHeight - 26)}px`;
}

function adjustHeightByWrittenReplies() {
    const repliesText = document.querySelectorAll(".reply-text");

    if (repliesText.length > 0) {
        const replyBox = document.querySelector(".reply-box");
        const comment = document.querySelector(".comment");
        const commentBox = document.querySelector(".comment-box");
        var repliesHeight = 0;
        var lastReplyHeight;

        repliesText.forEach(replyText => {
            repliesHeight += replyText.offsetHeight;

            if (parseInt(replyText.offsetHeight / 20) === 1) {
                repliesHeight += (27 - replyText.offsetHeight);

                replyText.parentElement.style.height = `${replyText.offsetHeight + 7}px`;
            } else {
                replyText.style.marginTop = "-3px";

                const reply = replyText.parentElement;

                reply.style.height = `${replyText.offsetHeight}px`;
                reply.style.marginBottom = "2px";
            }
            lastReplyHeight = replyText.offsetHeight;
        });

        changeRepliesStyle(repliesHeight, lastReplyHeight);
        const gap = getIncreaseOfHeight(repliesHeight, lastReplyHeight);

        replyBox.style.height = `${parseInt(replyBox.style.height) + gap}px`;
        comment.style.height = `${parseInt(comment.style.height) + gap}px`;
        commentBox.style.height = `${parseInt(commentBox.style.height) + gap}px`;
    }
}

function changeRepliesStyle(repliesHeight, lastReplyHeight) {
    const replies = document.querySelector(".replies");

    if (lastReplyHeight === 20 && repliesHeight < MAXIMUM_REPLY_BOX_HEIGHT) {
        replies.style.height = `${repliesHeight + 7}px`;
    } else {
        replies.style.height = `${repliesHeight}px`;
    }
    if (repliesHeight > MAXIMUM_REPLY_BOX_HEIGHT) {
        replies.style.overflow = "scroll";
        replies.style.height = `${MAXIMUM_REPLY_BOX_HEIGHT}px`;
    }
    if (lastReplyHeight > 20) {
        replies.style.marginBottom = "6px";
    }
}

function getIncreaseOfHeight(repliesHeight, lastReplyHeight) {
    if (repliesHeight >= MAXIMUM_REPLY_BOX_HEIGHT) {
         return MAXIMUM_REPLY_BOX_HEIGHT + 24;
    }
    if (lastReplyHeight > 20) {
        return repliesHeight + 23;
    }
    return repliesHeight + 30;
}

async function clickWriteCommentBtn(event) {
    event.preventDefault();

    const commentText = document.querySelector(".comment-textarea");

    if (NEWLINE_REGEX.test(commentText.value) || WHITESPACE_REGEX.test(commentText.value)) {
        openNotificationModal("error", ["댓글 내용을 입력해주세요."], 2000);
    } else {
        const result = await openConfirmModal("댓글을 작성할까요?", "댓글은 수정, 삭제가 불가하니 신중하게 결정해 주세요.");

        if (result) {
            writeComment(commentText.innerHTML);
        }
    }
}

function writeComment(commentContent) {
    const commentCharacter = document.querySelector(".write .comment-character");

    fetch(`/api${currentPathName}/comments`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "xCoordinate": parseFloat(commentCharacter.style.left) + 5,
            "yCoordinate": parseFloat(commentCharacter.style.top) - 18,
            "page": currentPage.index,
            "content": commentContent
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

    const commentText = document.querySelector(".reply-bar .comment-textarea");

    if (NEWLINE_REGEX.test(commentText.value) || WHITESPACE_REGEX.test(commentText.value)) {
        openNotificationModal("error", ["답글 내용을 입력해주세요."], 2000);
    } else {
        const result = await openConfirmModal("답글을 작성할까요?", "답글은 수정, 삭제가 불가하니 신중하게 결정해 주세요.");

        if (result) {
            writeReply(commentText.innerHTML);
        }
    }
}

function writeReply(replyContent) {
    const commentCharacter = document.querySelector(".note-content .comment-character:not(.written)");
    const commentId = commentCharacter.classList[2];

    fetch(`/api${currentPathName}/comments/${commentId}/replies`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "content": replyContent
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
