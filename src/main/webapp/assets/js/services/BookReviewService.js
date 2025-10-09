import {fetchUtil} from "../fetchUtil.js";

const injected = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : null;
const contextPath = injected ?? (window.location.pathname.split("/")[1] ? `/${window.location.pathname.split("/")[1]}` : "");
const BASE_URL = contextPath;

export const bookReviewApi = {
    likeReview: async (reviewId) => {
        const response = await fetchUtil(BASE_URL + `/user/book-review`, 'POST', {reviewId: reviewId});
        // console.log(response)
        return response;
    },
    unlikeReview: async (reviewId) => {
        const response = await fetchUtil(BASE_URL + `/user/book-review`, 'DELETE', {reviewId: reviewId});
        // console.log(response)
        return response;
    },
}