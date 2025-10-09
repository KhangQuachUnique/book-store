import { bookReviewApi } from "../services/BookReviewService.js";

// BookReviewPage.js
document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll(".like-btn").forEach((btn) => {
        btn.addEventListener("click", async () => {
            const svg = btn.querySelector("svg");
            const countSpan = btn.parentElement.querySelector(".like-count");
            console.log("clicked");
            btn.classList.add("loading");
            svg.style.display = "none";
            countSpan.style.display = "none";
            btn.insertAdjacentHTML("beforeend", '<div class="spinner"></div>');

            const reviewId = svg.getAttribute('review-id');
            let response;
            let count = parseInt(countSpan.textContent) || 0;

            if (btn.classList.contains('liked')) {
                response = await bookReviewApi.unlikeReview(reviewId);
                const data = await response.json();
                console.log(data)
                if (data.success) {
                    btn.classList.remove("liked");
                    countSpan.textContent = `${count - 1} Like`;
                }
                showToast(data.message, "success", 3000);
            } else {
                response = await bookReviewApi.likeReview(reviewId);
                const data = await response.json();
                console.log(data)
                if (data.success) {
                    btn.classList.add("liked");
                    countSpan.textContent = `${count + 1} Like`;
                }
                showToast(data.message, "success", 3000);
            }


            btn.classList.remove("loading");
            btn.querySelector(".spinner").remove();
            svg.style.display = "";
            countSpan.style.display = "";
        });
    });
});

