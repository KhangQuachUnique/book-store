document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll(".like-btn").forEach((btn) => {
        btn.addEventListener("click", async () => {
            const svg = btn.querySelector("svg");
            // Select the like-count span next to the button
            const countSpan = btn.parentElement.querySelector(".like-count");

            btn.classList.add("loading");
            svg.style.display = "none";
            countSpan.style.display = "none";
            btn.insertAdjacentHTML("beforeend", '<div class="spinner"></div>');

            await new Promise(resolve => setTimeout(resolve, 1500));

            btn.classList.remove("loading");
            btn.querySelector(".spinner").remove();
            svg.style.display = "";
            countSpan.style.display = "";

            btn.classList.toggle("liked");
            let count = parseInt(countSpan.textContent) || 0;
            if (btn.classList.contains("liked")) {
                countSpan.textContent = `${count + 1} Like`;
            } else {
                countSpan.textContent = `${count - 1} Like`;
            }
        });
    });
});
