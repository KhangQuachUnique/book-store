export async function fetchUtil(url, method = 'GET', body = null, headers = {}) {
    const options = {
        method,
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            ...headers,
        },
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        console.log("Request body:", body);
        return await fetch(url, options)
    } catch (error) {
        console.error('Fetch error:', error);
        throw error;
    }
}