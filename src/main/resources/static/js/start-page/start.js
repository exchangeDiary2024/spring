const logo = document.querySelector(".logo");
const logo_images = [
    "/images/start-page/line.gif",
    "/images/start-page/logo.png"
];

preLoadImgage(logo_images);

let isAnimationComplete = false;

setTimeout(() => {
    logo.src = "/images/start-page/line.gif";
}, 10);

setTimeout(() => {
    logo.classList.add("end");
    isAnimationComplete = true;
}, 2400);

document.addEventListener("click", () => {
    if (isAnimationComplete) {
        window.location.href = '/login';
    }
    else {
        logo.classList.add("end");
        isAnimationComplete = true;
    }
});
