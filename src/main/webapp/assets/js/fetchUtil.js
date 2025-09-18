export async function fetchUtil(url, method = 'GET', body = null, headers = {}) {
    const options = {
        method,
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
        const response = await fetch(url, options);
        if (!response.ok) {
            return {
                success: false,
                status: response.status,
                message: response,
            };
        }

        console.log(response)

        return {
            success: true,
            status: response.status,
            message: 'Request successful',
            data: await response.json(),
        };
    } catch (error) {
        console.error('Fetch error:', error);
        throw error;
    }
}