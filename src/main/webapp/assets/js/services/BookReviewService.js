import {fetchUtil} from "../fetchUtil";


export const BookReviewService = {
    getReviews: async (bookId) => {
        const response = await fetchUtil("/api/book-reviews?bookId=" + bookId, "GET");
    }
}