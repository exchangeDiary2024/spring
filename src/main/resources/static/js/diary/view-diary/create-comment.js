const STANDARD_TOP = 137;
const STANDARD_LEFT = 76;

function drawComment() {
    const comment = createComment();
    const character = document.querySelector(".comment-character");

    addEventToStickerIcon();
    processByCommentVertical(character, comment);
    processByCommentHorizontal(character, comment);

    document.querySelector(".sticker-btn").addEventListener("click", clickStickerBtn);
}

function createComment() {
    const comment = document.createElement("div");

    comment.classList.add("comment");
    comment.appendChild(createCommentBox());
    commentArea.appendChild(comment);
    return comment;
}

function createCommentBox() {
    const commentBox = document.createElement("div");

    commentBox.classList.add("comment-box");
    commentBox.style.height = "50px";
    commentBox.innerHTML = `<div class="sticker-bar">
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
                            <div class="comment-bar">
                                <div class="comment-textarea">
                                    <textarea class="comment-text" placeholder="댓글을 입력해주세요." spellcheck="false"></textarea>
                                </div>
                                <a class="sticker-btn" href="#">
                                    <img class="sticker-icon" src="/images/diary/view-page/sticker-icon.png">            
                                </a>
                                <a class="upload-comment-btn" href="#">
                                    <img class="bar-icon" src="/images/diary/write-page/write_icon.svg"/>
                                </a>
                            </div>`;
    return commentBox;
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

    if (character.offsetTop < STANDARD_TOP) { // 위
        comment.style.top = `${character.offsetTop + character.offsetHeight - 4}px`;
        comment.classList.add("top");
        comment.prepend(commentArrow);
    } else {
        comment.style.top = `${character.offsetTop - comment.offsetHeight + 4}px`;
        comment.children[0].style.marginTop = "225px";
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
