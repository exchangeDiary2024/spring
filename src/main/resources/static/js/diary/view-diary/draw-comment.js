const STANDARD_TOP = 137;
const STANDARD_LEFT = 76;
const STICKER_BAR_HTML = `
<div class="sticker-bar">
    <table class="stickers">
        <tr>
            <td><a class="sticker" href="#"><img class="sticker-icon" src="/images/diary/view-page/sticker/heart-icon.png"/></a></td>
            <td><a class="sticker" href="#"><img class="sticker-icon" src="/images/diary/view-page/sticker/angry-icon.png"/></a></td>
            <td><a class="sticker" href="#"><img class="sticker-icon v-sign" src="/images/diary/view-page/sticker/v-sign-icon.png"/></a></td>
            <td><a class="sticker" href="#"><img class="sticker-icon troubled" src="/images/diary/view-page/sticker/troubled-icon.png"/></a></td>
            <td><a class="sticker" href="#"><img class="sticker-icon sad" src="/images/diary/view-page/sticker/sad-icon.png"/></a></td>
            <td><a class="sticker" href="#"><img class="sticker-icon eating" src="/images/diary/view-page/sticker/eating-icon.png"/></a></td>
            <td><a class="sticker" href="#"><img class="sticker-icon question" src="/images/diary/view-page/sticker/question-icon.png"/></a></td>
        </tr>
    </table>    
    <div class="sticker-partition"></div>
</div>
`;
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
const REPLY_BAR_HTML = `
<div class="reply-box" style="height: 44px;">
    ${STICKER_BAR_HTML}
    <div class="reply-bar">
        <div class="reply-character"></div>
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


function drawComment(type, characterColor, commentParent) {
    const comment = createComment(type);
    const character = document.querySelector(`.comment-character.${characterColor}`);

    commentParent.appendChild(comment);

    processByCommentVertical(character, comment);
    processByCommentHorizontal(character, comment);

    addEventInCommentBox();
}

function addEventInCommentBox() {
    document.querySelector(".write-comment-btn").addEventListener("click", writeComment);
    document.querySelector(".sticker-btn").addEventListener("click", clickStickerBtn);
    addEventToStickers();
}

function createComment(type) {
    const comment = document.createElement("div");

    comment.classList.add("comment");
    comment.appendChild(createCommentBox(type));
    return comment;
}

function createCommentBox(type) {
    const commentBox = document.createElement("div");

    commentBox.classList.add("comment-box");
    if (type === "write") {
        commentBox.style.height = "50px";
        commentBox.innerHTML = STICKER_BAR_HTML + COMMENT_BAR_HTML;
    } else {
        commentBox.style.height = "100px";
        commentBox.innerHTML = REPLY_BAR_HTML;
    }
    return commentBox;
}

function makeWrittenCommentBoxHtml() {
    const READ_COMMENT_BOX_HTML = `
        <div class="comment-bar">
            <div class="comment-textarea">
                <textarea class="comment-text" placeholder="댓글을 입력해주세요." spellcheck="false"></textarea>
            </div>
            <a class="sticker-btn" href="#">
                <img class="sticker-icon" src="/images/diary/view-page/sticker-icon.png">            
            </a>
            <a class="write-comment-btn" href="#">
                <img class="bar-icon" src="/images/diary/write-page/write_icon.svg"/>
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
    const commentBox = document.querySelector(".comment-box");

    if (character.offsetTop < STANDARD_TOP) { // 위
        comment.style.top = `${character.offsetTop + character.offsetHeight - 4}px`;
        comment.classList.add("top");
        comment.prepend(commentArrow);
    } else {
        comment.style.top = `${character.offsetTop - comment.offsetHeight + 4}px`;
        comment.children[0].style.marginTop = `${225 - parseInt(commentBox.style.height) + 50}px`;
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

async function writeComment() {
    const character = document.querySelector(".write .comment-character");
    const commentText = document.querySelector(".comment-text");

    fetch(`/api${currentPathName}/comments`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "xCoordinate": parseFloat(character.style.left),
            "yCoordinate": parseFloat(character.style.top),
            "page": currentPage.index,
            "content": commentText.value
        })
    })
        .then(async response => {
            if (response.status !== 201) {
                throw await response.json();
            }
            commentArea.classList.remove("write");
            window.location.reload();
            return response.json();
        })
        .catch(data => {
            openNotificationModal("error", [data.message], 2000);
        });
}
