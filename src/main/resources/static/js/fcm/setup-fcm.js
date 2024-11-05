setupFCM();

function setupFCM() {
    firebase.initializeApp(firebaseConfig);
    const messaging = firebase.messaging();

    registerServiceWorker();
    handleNotificationPermission(messaging);

    messaging.onMessage((payload) => {
        console.log("알림 도착 ", payload);
        const notificationTitle = payload.notification.title;
        const notificationOptions = {
            body: payload.notification.body
        };

        if (Notification.permission === "granted") {
            new Notification(notificationTitle, notificationOptions);
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
                    console.log("푸시 토큰 가져오는 중에 에러 발생")
                })
        }
    }
}

function registerServiceWorker() {
    if ("serviceWorker" in navigator) {
        window.addEventListener("load", function () {
            navigator.serviceWorker
                .register("/firebase-messaging-sw.js")
                .then(() => console.log("Service Worker가 scope에 등록되었습니다."))
                .catch(() =>  console.log("Service Worker 등록 실패"));
        });
    }
}
