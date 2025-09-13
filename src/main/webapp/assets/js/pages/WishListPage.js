import { wishListApi } from "../services/wishListService.js";

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.heart-icon').forEach(svg => {
        svg.addEventListener('click', async (e) => {
            svg.classList.toggle('selected');
            const bookId = svg.getAttribute('data-book-id');
            if (svg.classList.contains('selected')) {
                // Add to wishlist (do not remove DOM)
                await wishListApi.addToWishList({ id: bookId });
            } else {
                // Remove from wishlist (do not remove DOM)
                await wishListApi.removeFromWishList(bookId);
            }
        });
    });
});
