const modal = document.querySelector("#modal");
const modalBody = modal.querySelector(".modal-body");
const moodBtn = document.querySelector(".mood-btn");
const emojiDiv = modalBody.querySelector(".emojies");

var currentModal = null

modal.style.display = "none";

backgroundImage = new Image();
backgroundImage.addEventListener("load", () => modal.style.backgroundImage = `url('${backgroundImage.src}')`)
backgroundImage.src = "/images/diary/write-page/modal/background.svg";

moodBtn.addEventListener("click", (event) => {
    clickModalBtn(event, emojiDiv);
});

Array.from(emojiDiv.querySelectorAll("a")).forEach(mood => {
    mood.addEventListener("click", changeMood);
});

function clickModalBtn(event, displayElement) {
    if (modal.style.display === "block") {
        closeModal();

        if (event.target !== currentModal && currentModal != null) {
            setTimeout(() => openModal(displayElement), 300);
            currentModal = event.target;
        }
    } else {
        openModal(displayElement);
        currentModal = event.target;
    }
}

function closeModal() {
    modal.style.transform = "translateY(100%)";
    setTimeout(() => {
        modal.style.display = "none";
        undisplayModalBody();
    }, 300);
}

function openModal(displayElement) {
    modal.style.display = "block";
    displayElement.style.display = "block";
    setTimeout(() => modal.style.transform = "translateY(0)", 10);
}

function changeMood(event) {
    const moodBtn = document.querySelector(".mood-btn");
    moodBtn.children[0].src = event.target.src;
    closeModal();
}

function undisplayModalBody() {
    emojiDiv.style.display = "none";
}
