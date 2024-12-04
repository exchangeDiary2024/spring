const helpIcon = document.querySelector(".help-icon");
const tooltip = document.querySelector(".tooltip");
const helpButton = document.querySelector(".help-btn");
const inquiryButton = document.querySelector(".inquiry-btn");
const helpImageContainer = document.querySelector(".help-image-container");
const helpImage = document.querySelector(".help-image");

let currentImageIndex = 0;
const monthlyImages = [
    '/images/common/help-menu/help-image/help-monthly.svg',
    '/images/common/help-menu/help-image/help-group-menu.svg'
];

helpIcon.addEventListener('click', () => {
    tooltip.classList.toggle('hidden');
});

document.addEventListener('click', (event) => {
    if (!helpIcon.contains(event.target) && !tooltip.contains(event.target)) {
        tooltip.classList.add('hidden');
    }
});

helpButton.addEventListener('click', () => {
    const pathname = window.location.pathname;
    const stepContent = document.querySelector(".step-content");

    const monthlyRegexp = /^\/groups\/[a-zA-Z0-9]+$/;
    const diaryWriteRegexp = /^\/groups\/[a-zA-Z0-9]+\/diaries$/;
    const diaryViewRegexp = /^\/groups\/[a-zA-Z0-9]+\/diaries\/\d+$/;
    let helpImagePath = '';

    if (monthlyRegexp.test(pathname)) {
        currentImageIndex = 0;
        showHelpImages(0);
        return;
    } else if (diaryWriteRegexp.test(pathname)) {
        helpImagePath = '/images/common/help-menu/help-image/help-diary-write.svg';
    } else if (diaryViewRegexp.test(pathname)) {
        helpImagePath = '/images/common/help-menu/help-image/help-diary-view.svg';
    } else if (stepContent.classList.contains("step1")) {
        helpImagePath = '/images/common/help-menu/help-image/help-group-step1.svg';
    } else if (stepContent.classList.contains("step2-create")) {
        helpImagePath = '/images/common/help-menu/help-image/help-group-create-step2.svg';
    } else if (stepContent.classList.contains("step2-join")) {
        helpImagePath = '/images/common/help-menu/help-image/help-group-join-step2.svg';
    } else if (stepContent.classList.contains("step3")) {
        helpImagePath = '/images/common/help-menu/help-image/help-group-step3.svg';
    } else if (stepContent.classList.contains("step4")) {
        helpImagePath = '/images/common/help-menu/help-image/help-group-step4.svg';
    } else if (stepContent.classList.contains("step5-create")) {
        helpImagePath = '/images/common/help-menu/help-image/help-group-create-step5.svg';
    } else if (stepContent.classList.contains("step5-join")) {
        helpImagePath = '/images/common/help-menu/help-image/help-group-join-step5.svg';
    }
    showHelpImage(helpImagePath);
});

function showHelpImages(imageIndex) {
    currentImageIndex = imageIndex;
    helpImage.src = monthlyImages[currentImageIndex++];
    helpImageContainer.classList.remove('hidden');
    tooltip.classList.add('hidden');
}

function showHelpImage(helpImagePath) {
    helpImage.src = helpImagePath;
    helpImageContainer.classList.remove('hidden');
    tooltip.classList.add('hidden');
}

helpImageContainer.addEventListener('click', () => {
    if (currentImageIndex === 1) {
        showHelpImages(1);
    }
    else {
        helpImageContainer.classList.add('hidden');
    }
});
