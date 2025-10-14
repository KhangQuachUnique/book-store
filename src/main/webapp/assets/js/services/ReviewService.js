import {fetchUtil} from "../fetchUtil.js";

const injected = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : null;
const contextPath = injected ?? (window.location.pathname.split("/")[1] ? `/${window.location.pathname.split("/")[1]}` : "");
const BASE_URL = "";

export const bookReviewApi = {
    likeReview: async (reviewId) => {
        const response = await fetchUtil(BASE_URL + `/review`, 'POST', {reviewId: reviewId});
        // console.log(response)
        return response;
    },
    unlikeReview: async (reviewId) => {
        const response = await fetchUtil(BASE_URL + `/review`, 'DELETE', {reviewId: reviewId});
        // console.log(response)
        return response;
    },
}