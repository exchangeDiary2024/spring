import App from "./common/App.js";
import Router from "./common/Router.js";
import HomePage from "./pages/common/HomePage.js";
import LoginPage from "./pages/common/LoginPage.js";

const app = new App(document.querySelector(".main-screen"));
const router = new Router(app, "#");

router.add("/", HomePage);
router.add("/login", LoginPage);
