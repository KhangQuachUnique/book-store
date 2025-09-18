


import {fetchUtil} from "../fetchUtil.js";

const CONTEXT_PATH = '/bookiecake';

export const bookReviewApi = {
    likeReview: async (reviewId) => {
        const response = await fetchUtil(CONTEXT_PATH + `/user/book-review`, 'POST', {reviewId: reviewId});
        console.log(response)
        return response;
    },
    unlikeReview: async (reviewId) => {
        const response = await fetchUtil(CONTEXT_PATH + `/user/book-review`, 'DELETE', {reviewId: reviewId});
        console.log(response)
        return response;
    },
}