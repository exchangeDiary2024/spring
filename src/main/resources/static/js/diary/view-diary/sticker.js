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
    const sticker = createStickerDiv(stickerType);

    range.insertNode(sticker);

    const space = document.createTextNode("\u200B");
    sticker.parentNode.insertBefore(space, sticker.nextSibling);

    range.setStartAfter(sticker);
    range.setEndAfter(sticker);
}

function createStickerDiv(stickerType) {
    const stickerDiv = document.createElement("div");
    const characterImage = document.createElement("img");
    const stickerImage = document.createElement("img");

    stickerDiv.classList.add("sticker-character", `${stickerType}`);
    stickerDiv.contentEditable = "false";

    characterImage.classList.add("character-icon", `${getCharacter()}`);
    stickerImage.classList.add("sticker");

    stickerDiv.append(characterImage, stickerImage);
    return stickerDiv;
}

function getCharacter() {
    if (commentBtn.classList.contains("selected")) {
        return document.querySelector(".comment-area .comment-character").classList[1];
    }
    return document.querySelector(".reply-bar .reply-character-icon").classList[1];
}
