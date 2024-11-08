importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-messaging.js');

self.addEventListener("install", () => self.skipWaiting());

const firebaseConfig = {
    apiKey: "AIzaSyAqVv93bD1Po8OHgZhZXfaPBOImUN8ZTNg",
    projectId: "buddies-spring",
    messagingSenderId: "271822946508",
    appId: "1:271822946508:web:3cf3224415f64c2e8b8269",
};

firebase.initializeApp(firebaseConfig);

firebase.messaging();
