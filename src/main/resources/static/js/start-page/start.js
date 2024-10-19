const logo = document.querySelector(".logo");
const logo_images = [
    "/images/start-page/line.gif",
    "/images/start-page/logo.png"
];

preLoadImgage(logo_images);

setTimeout(() => {
    logo.src = "/images/start-page/line.gif";
}, 10);

setTimeout(() => {
    logo.classList.add("end");
}, 2400);

setTimeout(() => {
    window.location.href = '/login';
}, 3000);
