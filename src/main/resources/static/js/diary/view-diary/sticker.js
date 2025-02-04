const STICKER_BAR_HTML = `
<div class="sticker-bar">
    <table class="stickers">
        <tr>
            <td><a class="sticker heart" href="#"><img class="sticker-icon" src="/images/diary/sticker/heart-icon.png"/></a></td>
            <td><a class="sticker angry" href="#"><img class="sticker-icon" src="/images/diary/sticker/angry-icon.png"/></a></td>
            <td><a class="sticker v-sign" href="#"><img class="sticker-icon" src="/images/diary/sticker/v-sign-icon.png"/></a></td>
            <td><a class="sticker troubled" href="#"><img class="sticker-icon" src="/images/diary/sticker/troubled-icon.png"/></a></td>
            <td><a class="sticker sad" href="#"><img class="sticker-icon" src="/images/diary/sticker/sad-icon.png"/></a></td>
            <td><a class="sticker eating" href="#"><img class="sticker-icon" src="/images/diary/sticker/eating-icon.png"/></a></td>
            <td><a class="sticker question" href="#"><img class="sticker-icon" src="/images/diary/sticker/question-icon.png"/></a></td>
        </tr>
    </table>
</div>
`;

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

function addEventToStickerIcons() {
    const stickerBar = document.querySelectorAll(".sticker-bar .sticker");

    stickerBar.forEach(sticker => sticker.addEventListener("click", clickStickerIcon));
}

function clickStickerIcon(event) {
    event.preventDefault();

    const stickerType = event.currentTarget.classList[1];

    addStickerCharacter(stickerType);
    adjustHeightByTextarea();
}

function addStickerCharacter(stickerType) {
    const selection = window.getSelection();
    const range = selection.getRangeAt(0);

    const template = document.createElement("template");
    template.innerHTML = makeStickerCharacterHTML(stickerType);

    const stickerElement = template.content.firstChild;
    range.insertNode(stickerElement);

    const space = document.createTextNode("\u200B");
    stickerElement.parentNode.insertBefore(space, stickerElement.nextSibling);

    range.setStartAfter(stickerElement);
    range.setEndAfter(stickerElement);
}

function makeStickerCharacterHTML(stickerType) {
    const character = getCharacter();

    return `<div class="sticker-character ${stickerType}" contenteditable="false">
                <img class="character-icon ${character}">
                <img class="sticker">
            </div>`;
}

function getCharacter() {
    if (commentBtn.classList.contains("selected")) {
        return document.querySelector(".comment-area .comment-character").classList[1];
    }
    return document.querySelector(".reply-bar .reply-character-icon").classList[1];
}
