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

function addEventToStickers() {
    const stickerBar = document.querySelectorAll(".sticker");

    stickerBar.forEach(sticker => sticker.addEventListener("click", clickStickerIcon));
}

function clickStickerIcon(event) {
    event.preventDefault();

    // todo: 클릭시 스티커 생성됨
}
