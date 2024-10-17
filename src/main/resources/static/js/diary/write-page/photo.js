const photoBtn = document.querySelector(".photo-btn");
const photoInput = document.querySelector("#photo-input");

photoBtn.addEventListener("click", (event) => clickInput(event, photoInput));
photoInput.addEventListener("change", uploadImage);

function clickInput(event, input) {
    event.preventDefault();
    input.click();
}

function uploadImage(event) {
    const file = event.target.files[0];

    const reader = new FileReader();

    reader.onloadend = function () {
        photoBtn.classList.remove("photo-btn");
        photoBtn.children[0].src = reader.result;
        photoBtn.children[0].classList = ["image"];
        closeModal();
    };

    if (file) {
        reader.readAsDataURL(file);
    }
}
