const helpIcon = document.querySelector(".help-icon");
const tooltip = document.querySelector(".tooltip");
const helpButton = document.querySelector(".help-btn");
const inquiryButton = document.querySelector(".inquiry-btn");
const helpImageContainer = document.querySelector(".help-image-container");
const helpImage = document.querySelector(".help-image");

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
        helpImagePath = '/images/common/help-menu/help-image/help-monthly.svg';
    } else if (diaryWriteRegexp.test(pathname)) {
        helpImagePath = '/images/common/help-menu/help-image/help-diary-write.svg';
    } else if (diaryViewRegexp.test(pathname)) {
        helpImagePath = '/images/common/help-menu/help-image/help-diary-view.svg';
    }

    helpImage.src = helpImagePath;
    helpImageContainer.classList.remove('hidden');
    tooltip.classList.add('hidden');

});

helpImage.addEventListener('click', () => {
    helpImageContainer.classList.add('hidden');
});
