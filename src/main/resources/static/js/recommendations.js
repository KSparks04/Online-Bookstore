document.addEventListener('DOMContentLoaded', () => {

    // Get ISBN from HTML data attribute
    const container = document.getElementById('recommendations');
    const isbn = Number(document.getElementById('recommendations').dataset.isbn);


    const slider = document.getElementById('bookSlider');
    const scrollAmount = 200;

    // Fetch top similar books
    fetch(`/similar/${isbn}`)
        .then(response => response.json())
        .then(data => {
            data.forEach(entry => {
                const card = document.createElement('div');
                card.className = 'card';
                card.style.minWidth = '150px';
                card.style.flex = '0 0 auto';
                const imgRef = document.createElement('a');
                imgRef.href =`/book/${entry.similarBookIsbn}`;
                const img = document.createElement('img');
                img.className = 'card-img-top';
                img.src = `/book-image/${entry.similarBookIsbn}`;
                img.alt = "No Image Found";

                const body = document.createElement('div');
                body.className = 'card-body p-2';

                const title = document.createElement('a');
                title.className = 'card-title text-decoration-none';
                title.href = `/book/${entry.similarBookIsbn}`;
                title.textContent = entry.similarBookTitle;
                imgRef.appendChild(img);
                body.appendChild(title);
                card.appendChild(imgRef);
                card.appendChild(body);

                card.addEventListener("click", ()=>{
                    window.location.href = `/book/${entry.similarBookIsbn}`;
                });
                slider.appendChild(card);
            });
        })
        .catch(err => console.error('Error fetching similar books:', err));

    // Drag-to-scroll
    let isDown = false;
    let startX;
    let scrollLeftStart;

    slider.addEventListener('mousedown', (e) => {
        isDown = true;
        slider.classList.add('active');
        startX = e.pageX - slider.offsetLeft;
        scrollLeftStart = slider.scrollLeft;
    });

    slider.addEventListener('mouseleave', () => { isDown = false; slider.classList.remove('active'); });
    slider.addEventListener('mouseup', () => { isDown = false; slider.classList.remove('active'); });
    slider.addEventListener('mousemove', (e) => {
        if(!isDown) return;
        e.preventDefault();
        const x = e.pageX - slider.offsetLeft;
        const walk = (x - startX) * 1;
        slider.scrollLeft = scrollLeftStart - walk;
    });
});
