import {fetchUtil} from "../fetchUtil.js";

export const bookReviewApi = {
    likeReview: async (reviewId) => {
        const response = await fetchUtil(`/review`, 'POST', {reviewId: reviewId});
        // console.log(response)
        return response;
    },
    unlikeReview: async (reviewId) => {
        const response = await fetchUtil(`/review`, 'DELETE', {reviewId: reviewId});
        // console.log(response)
        return response;
    },
}