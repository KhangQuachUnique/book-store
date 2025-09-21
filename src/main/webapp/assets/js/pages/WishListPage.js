import { wishListApi } from "../services/WishListService.js";

function showResult(message, success = true) {
    showToast(message, success ? 'success' : 'error', 3000);
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.heart-icon').forEach(svg => {
        svg.addEventListener('click', async (e) => {
            svg.classList.toggle('selected');
            const bookId = svg.getAttribute('data-book-id');
            if (svg.classList.contains('selected')) {
                // Add to wishlist (do not remove DOM)
                const response = await wishListApi.addToWishList({ itemId: bookId });
                if (response.success) {
                    showResult("Added to wishlist", true);
                } else {
                    showResult("Failed to add to wishlist", false);
                    // Revert UI change
                    svg.classList.remove('selected');
                }
            } else {
                // Remove from wishlist (do not remove DOM)
                const response = await wishListApi.removeFromWishList({ itemId: bookId });
                if (response.success) {
                    showResult(response.message || "Removed from wishlist", true);
                } else {
                    showResult(response.error || "Failed to remove from wishlist", false);
                    // Revert UI change
                    svg.classList.add('selected');
                }
            }
        });
    });
});
