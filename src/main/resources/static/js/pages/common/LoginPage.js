import Component from "../../components/Component.js";
import { requestNotificationPermission } from "../../fcm/setup.js";

export default class LoginPage extends Component {
  template() {
    return `
    <div class="background" style="background-image: url('/images/login-page/background.svg')">
        <div class="content">
            <div class="logo">
                <img class="logo-img" src="/images/login-page/spring.svg">
            </div>
            <div class="spring">
                <span class="text main-text">우리만의 스프링을 만들어 볼까요?</span>
            </div>
            <div class="signup">
                <span class="text signup-text">SNS 계정으로 간편 가입하기</span>
            </div>
            <a class="login-btn" href="https://kauth.kakao.com/oauth/authorize(response_type='code', redirect_uri={}, client_id=$})">
                <div class="kakao-login">
                    <div class="kakao">
                        <img src="/images/login-page/kakao.svg">
                    </div>
                    <div class="login">
                        <span class="text login-text">카카오 로그인</span>
                    </div>
                </div>
            </a>
            <div class="test">알림 테스트</div>
        </div>
    </div>
    `;
  }

  mounted() {

    document.querySelector(".test").addEventListener("click", requestNotificationPermission);
  }
}
