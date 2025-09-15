import {fetchUtil} from "../fetchUtil";


export const BookReviewService = {
    likeReview: async (reviewId) => {
        return await fetchUtil(`/b`, 'POST');
    },
    dislikeReview: async (reviewId) => {
        return await fetchUtil(`/comments/${reviewId}/dislike`, 'DELETE');
    },
}