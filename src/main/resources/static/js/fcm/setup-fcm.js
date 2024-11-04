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
        // Todo: API 요청 - 사용자가 토큰 가지고 있는지.
        if (localStorage.getItem("token") !== null) {
            return ;
        }

        messaging.getToken(messaging, {
            vapidKey: vapidKey
        })
            .then(token => {
                console.log(`푸시 토큰 발급 완료 : ${token}`);
                localStorage.setItem("token", token);
            })
            .catch(() => {
                console.log("푸시 토큰 가져오는 중에 에러 발생")
            })
    } else {
        console.log("푸시 권한 차단")
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
