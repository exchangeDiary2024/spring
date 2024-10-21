const image = document.querySelector(".image");

if (uploadImage) {
    image.className = "image";
    image.children[0].src = `data:image/png;base64,${uploadImage}`;
}
