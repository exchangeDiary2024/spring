export default class Router {
    constructor(app, defaultHash) {
        this.app = app;
        this.defaultHash = defaultHash;
        this.routes = {};

        window.addEventListener("hashchange", () => {
            this.push(window.location.hash);
        });
    }

    add(hash, component) {
        this.routes[`${this.defaultHash}${hash}`] = component;
    }

    push(hash) {
        if (this.routes.hasOwnProperty(hash)) {
            this.app.changePage(this.routes[hash]);
        }
    }
}
