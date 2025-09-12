<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>White List Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/whiteList.css">
</head>
<body>

<section class="white-list">
    <h1>Wish List</h1>

    <div class="white-list-item-display">
        <div class="white-list-item">
            <img src="https://salt.tikicdn.com/cache/280x280/ts/product/aa/5e/bd/ec108d4cb00072386fa1a879be926b98.jpg" alt="temp">
            <div class="white-list-item-part-2">
                <h3>On Writing Well, 30th Anniversary Edition: The Classic Guide to Writing Nonfiction</h3>
                <div class="white-list-item-price-section">
                    <span class="white-list-item-price">192.000</span>
                    <del class="white-list-item-price-old">240.000</del>
                </div>
                <span class="white-list-item-rating">rating</span>
                <span class="white-list-item-sold">Đã bán 1923</span>
            </div>
            <p>Everyone knows that high IQ is no guarantee of success, happiness, or virtue, but until Emotional Intelligence, we could only guess why. Daniel Goleman's brilliant report from the frontiers of psychology and neuroscience offers startling new insight into our "two minds"—the rational and the emotional—and how they together shape our destiny.<br /><br />
                Through vivid examples, Goleman delineates the five crucial skills of emotional intelligence, and shows how they determine our success in relationships, work, and even our physical well-being. What emerges is an entirely new way to talk about being smart. <br /><br />
                The best news is that "emotional literacy" is not fixed early in life. Every parent, every teacher, every business leader, and everyone interested in a more civil society, has a stake in this compelling vision of human possibility.</p>
            <div class="actions">
                <svg id="heart" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.0.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M305 151.1L320 171.8L335 151.1C360 116.5 400.2 96 442.9 96C516.4 96 576 155.6 576 229.1L576 231.7C576 343.9 436.1 474.2 363.1 529.9C350.7 539.3 335.5 544 320 544C304.5 544 289.2 539.4 276.9 529.9C203.9 474.2 64 343.9 64 231.7L64 229.1C64 155.6 123.6 96 197.1 96C239.8 96 280 116.5 305 151.1z"/></svg>
                <button class="add-to-cart-button">Add to cart</button>
                <button class="buy-button">Buy now</button>
            </div>
        </div>
    </div>
</section>
<script>
    // src/main/webapp/WEB-INF/views/whiteList/whiteList.jsp
    const heartIcon = document.getElementById('heart');
    heartIcon.addEventListener('click', () => {
        heartIcon.classList.toggle('selected');
    });
</script>
</body>
</html>
