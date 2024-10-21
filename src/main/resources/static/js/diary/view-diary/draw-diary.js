function drawDiary() {
    adjustCharacterSize();
    uploadImage();
}

function uploadImage() {
    const image = document.querySelector(".image");

    if (photo) {
        image.className = "image";
        image.children[0].src = `data:image/png;base64,${photo}`;
    }
}

function adjustCharacterSize() {
    const characters = document.querySelectorAll(".character-icon");

    if (characters[0].classList.contains("green") || characters[0].classList.contains("blue")) {
        characters.forEach(character => character.classList.add(`${characters[0].classList[0]}-icon`))
    }
}

drawDiary();
