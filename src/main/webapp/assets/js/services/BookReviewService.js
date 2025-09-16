import {fetchUtil} from "../fetchUtil";


export const bookReviewService = {
    likeReview: async (reviewId) => {
        const response = await fetchUtil(`/user/bookReview`, 'POST', {reviewId: reviewId});
        console.log(response)
        return response;
    },
    unlikeReview: async (reviewId) => {
        const response = await fetchUtil(`/user/bookReview`, 'DELETE', {reviewId: reviewId});
        console.log(response)
        return response;
    },
}