import { bookReviewApi } from "../services/BookReviewService.js";

// BookReviewPage.js
document.addEventListener("DOMContentLoaded", function () {
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
            let success = false;
            let count = parseInt(countSpan.textContent) || 0;

            if (btn.classList.contains('liked')) {
                success = await bookReviewApi.unlikeReview(reviewId);
                if (success) {
                    btn.classList.remove("liked");
                    countSpan.textContent = `${count - 1} Like`;
                }
            } else {
                success = await bookReviewApi.likeReview(reviewId);
                if (success) {
                    btn.classList.add("liked");
                    countSpan.textContent = `${count + 1} Like`;
                }
            }


            btn.classList.remove("loading");
            btn.querySelector(".spinner").remove();
            svg.style.display = "";
            countSpan.style.display = "";
        });
    });
});
