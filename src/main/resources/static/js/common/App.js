import Component from '../components/Component.js';
import StartPage from '../pages/common/StartPage.js';

export default class App extends Component {
  setup() {
    this.$state = {
      page: new StartPage(this.$target)
    }
  }

  mounted() {
    this.$state.page.render();
  }

  changePage(targetPage) {
    this.setState({ page: new targetPage(this.$target, this.$state.page.$props) });
  }
}
