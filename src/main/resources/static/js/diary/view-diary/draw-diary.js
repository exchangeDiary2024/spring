const mood = document.querySelector(".mood");
const image = document.querySelector(".image");

mood.children[0].src = moodLocation;

if (uploadImage) {
    image.className = "image";
    image.children[0].src = `data:image/png;base64,${uploadImage}`;
}
