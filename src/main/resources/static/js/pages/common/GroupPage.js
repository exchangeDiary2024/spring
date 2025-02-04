import Component from "../../components/Component.js";

export default class GroupPage extends Component {
  template() {
    return `
    <div class="background" style="background-image: url('/images/group/create-join-page/background.svg');">
        <div class="top-bar">
            <div class="logo">
                <img class="logo-icon" src="/images/group/create-join-page/logo.svg">
            </div>
        </div>
        <div class="content">
            <div class="left-margin"></div>
            <div class="right-margin"></div>
            <div class="note">
                <div class="note-header">
                    <div class="step-bar">
                        <div class="step-icon step1 fill"></div>
                        <div class="step-icon step2"></div>
                        <div class="step-icon step3"></div>
                        <div class="step-icon step4"></div>
                        <div class="step-icon step5"></div>
                    </div>
                </div>
                <div class="note-body">
                </div>
                <div class="note-footer">
                    <a href="javascript:void(0);" class="confirm-btn">
                        <span class="confirm-text">확인</span>
                    </a>
                </div>
            </div>
          </div>
      </div>
    `;
  }

  mounted() {
  }
}
