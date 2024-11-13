setupFCM();

function setupFCM() {
    firebase.initializeApp(firebaseConfig);
    const messaging = firebase.messaging();

    registerServiceWorker();
    handleNotificationPermission(messaging);

    messaging.onMessage(payload => {
        if (Notification.permission === "granted") {
            const title = payload.notification.title;
            const options = {
                body: payload.notification.body,
                data: {
                    url: ""
                }
            }

            const notification = new Notification(title, options);

            notification.addEventListener("click", (event) => {
                const url = `https://buddies-spring.site/${event.target.data.url}`
                window.open(url);
            });
        }
    })
}

async function handleNotificationPermission(messaging) {
    const permission = await Notification.requestPermission();

    if (permission === "granted") {
        if (sessionStorage.getItem("token") === null) {
            messaging.getToken(messaging, {
                vapidKey: vapidKey
            })
                .then(token => {
                    fetch("/api/members/notifications/token", {
                        method: "PATCH",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            "token": token
                        })
                    });
                    sessionStorage.setItem("token", token);
                })
                .catch(() => {
                    console.log("Fail to issue fcm token")
                })
        }
    }
}

function registerServiceWorker() {
    if ("serviceWorker" in navigator) {
        window.addEventListener("load", function () {
            navigator.serviceWorker
                .register("/firebase-messaging-sw.js")
                .catch(() =>  console.log("Fail to register service worker"));
        });
    }
}
