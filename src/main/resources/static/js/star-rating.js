const stars = document.querySelectorAll('.star-rating .star');
const ratingInput = document.getElementById('ratingValue');

stars.forEach((star, index) => {
    star.addEventListener('click', () => {
        const value = index + 1;
        ratingInput.value = value;

        stars.forEach(s => s.classList.remove('filled'));

        for (let i = 0; i < value; i++) {
            stars[i].classList.add('filled');
        }
    });
});