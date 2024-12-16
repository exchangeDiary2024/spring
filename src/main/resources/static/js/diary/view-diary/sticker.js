const STICKER_BAR_HTML = `
<div class="sticker-bar">
    <table class="stickers">
        <tr>
            <td><a class="sticker heart" href="#"><img class="sticker-icon" src="/images/diary/view-page/sticker/heart-icon.png"/></a></td>
            <td><a class="sticker angry" href="#"><img class="sticker-icon" src="/images/diary/view-page/sticker/angry-icon.png"/></a></td>
            <td><a class="sticker v-sign" href="#"><img class="sticker-icon" src="/images/diary/view-page/sticker/v-sign-icon.png"/></a></td>
            <td><a class="sticker troubled" href="#"><img class="sticker-icon" src="/images/diary/view-page/sticker/troubled-icon.png"/></a></td>
            <td><a class="sticker sad" href="#"><img class="sticker-icon" src="/images/diary/view-page/sticker/sad-icon.png"/></a></td>
            <td><a class="sticker eating" href="#"><img class="sticker-icon" src="/images/diary/view-page/sticker/eating-icon.png"/></a></td>
            <td><a class="sticker question" href="#"><img class="sticker-icon" src="/images/diary/view-page/sticker/question-icon.png"/></a></td>
        </tr>
    </table>
</div>
`;
const IMAGE_FROM_STICKER_TYPE = {
    "heart": "/images/diary/view-page/sticker/heart-purple-character.png",
};

function clickStickerBtn(event) {
    event.preventDefault();

    const stickerBtn = event.currentTarget;
    const stickerBar = document.querySelector(".sticker-bar");
    const comment = document.querySelector(".comment");

    if (stickerBar.style.display === "block") {
        if (comment.classList.contains("bottom")) {
            comment.children[1].style.zIndex = 5;
        }
        stickerBar.style.display = "none";
        stickerBtn.classList.remove("selected");
    } else {
        if (comment.classList.contains("bottom")) {
            comment.children[1].style.zIndex = -1;
        }
        stickerBar.style.display = "block";
        stickerBtn.classList.add("selected");
    }
}

function addEventToStickers() {
    const stickerBar = document.querySelectorAll(".sticker");

    stickerBar.forEach(sticker => sticker.addEventListener("click", clickStickerIcon));
}

function clickStickerIcon(event) {
    event.preventDefault();

    const textarea = document.querySelector(".comment-textarea");
    const stickerType = event.currentTarget.classList[1];

    textarea.innerHTML += makeStickerCharacterHTML(stickerType);
    textarea.appendChild(document.createTextNode("\u200B"));

    moveCursorToEnd(textarea);
    adjustCommentBoxHeightByTextarea();
}

function makeStickerCharacterHTML(stickerType) {
    return `<div class="sticker-character">
                <img src="${IMAGE_FROM_STICKER_TYPE[stickerType]}" class="character-icon">
            </div>`;
}
