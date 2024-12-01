const helpIcon = document.querySelector(".help-icon");
const tooltip = document.querySelector(".tooltip");

helpIcon.addEventListener('click', () => {
    tooltip.classList.toggle('hidden');
});

document.addEventListener('click', (event) => {
    if (!helpIcon.contains(event.target) && !tooltip.contains(event.target)) {
        tooltip.classList.add('hidden');
    }
});
