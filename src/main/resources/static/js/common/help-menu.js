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

helpImage.addEventListener('click', () => {
    if (currentImageIndex === 1) {
        showHelpImages(1);
    }
    else {
        helpImageContainer.classList.add('hidden');
    }
});
