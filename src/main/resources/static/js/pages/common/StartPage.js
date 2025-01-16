import ImageUtil from "../../common/ImageUtil.js";
import Component from "../../components/Component.js";

export default class StartPage extends Component {
  template() {
    return `
    <div class="background" style="background-image: url('/images/start-page/background.png'); background-size: 100dvw 100dvh;">
        <img class="logo"/>
        <div class="title-box">
            <span class="text spring" >스프링</span>
        </div>
    </div>
    `;
  }

  mounted() {
    loadPages();
  }
}

function loadPages() {
    window.location.hash = "";

    const logo = document.querySelector(".logo");
    const logo_images = [
        "/images/start-page/line.gif",
        "/images/start-page/logo.png"
    ];

    ImageUtil.preLoadImage(logo_images);

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
            window.location.hash = getHash();
        } else {
            logo.classList.add("end");
            isAnimationComplete = true;
        }
    });
}

function getHash() {
    const userData = {};
    if (true) {
        return "/login";
    }
    if (userData.hasGroup) {
        return `/group/${userData.groupId}`;
    }
    return "/";
}
