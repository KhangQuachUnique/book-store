<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Chart.js CDN -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
    /* Inline styles for statistics tab */
    .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 16px;
        margin-bottom: 30px;
    }

    .stat-card {
        background: white;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        border: 1px solid #e2e8f0;
        display: flex;
        align-items: center;
        gap: 12px;
        transition: all 0.3s ease;
    }

    .stat-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    }

    .stat-icon {
        font-size: 2rem;
        width: 50px;
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 8px;
        flex-shrink: 0;
    }

    .daily-icon { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
    .order-icon { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
    .monthly-icon { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
    .total-icon { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }

    .stat-content { flex: 1; }
    .stat-title {
        font-size: 0.75rem;
        font-weight: 600;
        color: #718096;
        margin: 0 0 6px 0;
        text-transform: uppercase;
        letter-spacing: 0.05em;
    }
    .stat-value {
        font-size: 1.5rem;
        font-weight: 700;
        color: #1a202c;
        margin: 0 0 4px 0;
        line-height: 1.2;
    }
    .stat-subtitle {
        font-size: 0.7rem;
        color: #a0aec0;
        margin: 0;
    }

    .charts-section { margin-bottom: 30px; }
    .chart-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 16px;
        margin-bottom: 16px;
    }
    .chart-card {
        background: white;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        border: 1px solid #e2e8f0;
    }
    .chart-card.full-width { grid-column: 1 / -1; }
    .chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;
        padding-bottom: 12px;
        border-bottom: 1px solid #e2e8f0;
    }
    .chart-title {
        font-size: 1.1rem;
        font-weight: 600;
        color: #1a202c;
        margin: 0;
    }
    .chart-controls {
        display: flex;
        gap: 6px;
        align-items: center;
    }
    .chart-container {
        position: relative;
        height: 250px;
        width: 100%;
    }

    .btn {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        padding: 6px 12px;
        border-radius: 6px;
        font-weight: 600;
        text-decoration: none;
        border: none;
        cursor: pointer;
        transition: all 0.2s ease;
        font-size: 0.75rem;
    }
    .btn-primary {
        background: #3182ce;
        color: white;
    }
    .btn-primary:hover { background: #2c5282; }
    .btn-sm { padding: 4px 8px; font-size: 0.7rem; }

    select {
        padding: 4px 8px;
        border: 1px solid #d1d5db;
        border-radius: 4px;
        background: white;
        font-size: 0.75rem;
        color: #374151;
        cursor: pointer;
    }

    @media (max-width: 768px) {
        .chart-row { grid-template-columns: 1fr; }
        .chart-card.full-width { grid-column: 1; }
        .stats-grid { grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); }
        .chart-container { height: 200px; }
    }
</style>

<div class="statistics-dashboard">
    <h3 style="margin-bottom: 20px; color: #1a202c;">Order Statistics Dashboard</h3>
    
    <!-- Statistics Cards -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-icon daily-icon">📅</div>
            <div class="stat-content">
                <h4 class="stat-title">Daily Revenue</h4>
                <div class="stat-value" id="dailyRevenueInline">Loading...</div>
                <div class="stat-subtitle">Today's total revenue</div>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon order-icon">📦</div>
            <div class="stat-content">
                <h4 class="stat-title">Daily Orders</h4>
                <div class="stat-value" id="dailyOrdersInline">Loading...</div>
                <div class="stat-subtitle">Orders placed today</div>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon monthly-icon">💰</div>
            <div class="stat-content">
                <h4 class="stat-title">Monthly Revenue</h4>
                <div class="stat-value" id="monthlyRevenueInline">Loading...</div>
                <div class="stat-subtitle">This month's total revenue</div>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon total-icon">📊</div>
            <div class="stat-content">
                <h4 class="stat-title">Monthly Orders</h4>
                <div class="stat-value" id="monthlyOrdersInline">Loading...</div>
                <div class="stat-subtitle">Orders this month</div>
            </div>
        </div>
    </div>

    <!-- Charts Section -->
    <div class="charts-section">
        <div class="chart-row">
            <!-- Revenue Chart -->
            <div class="chart-card">
                <div class="chart-header">
                    <h4 class="chart-title">Revenue Comparison</h4>
                    <div class="chart-controls">
                        <button class="btn btn-primary btn-sm" onclick="updateRevenueChartInline()">Refresh</button>
                    </div>
                </div>
                <div class="chart-container">
                    <canvas id="revenueChartInline"></canvas>
                </div>
            </div>
            
            <!-- Orders Chart -->
            <div class="chart-card">
                <div class="chart-header">
                    <h4 class="chart-title">Orders Comparison</h4>
                    <div class="chart-controls">
                        <button class="btn btn-primary btn-sm" onclick="updateOrdersChartInline()">Refresh</button>
                    </div>
                </div>
                <div class="chart-container">
                    <canvas id="ordersChartInline"></canvas>
                </div>
            </div>
        </div>
        
        <div class="chart-row">
            <!-- Top Selling Books -->
            <div class="chart-card full-width">
                <div class="chart-header">
                    <h4 class="chart-title">Top Selling Books</h4>
                    <div class="chart-controls">
                        <select id="topBooksLimitInline" onchange="updateTopBooksChartInline()">
                            <option value="5">Top 5</option>
                            <option value="10" selected>Top 10</option>
                            <option value="15">Top 15</option>
                        </select>
                        <button class="btn btn-primary btn-sm" onclick="updateTopBooksChartInline()">Refresh</button>
                    </div>
                </div>
                <div class="chart-container">
                    <canvas id="topBooksChartInline"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    let revenueChartInline, ordersChartInline, topBooksChartInline;
    let statisticsInitialized = false;

    function initializeStatisticsCharts() {
        if (statisticsInitialized) return;
        
        // Revenue Chart
        const revenueCtx = document.getElementById('revenueChartInline').getContext('2d');
        revenueChartInline = new Chart(revenueCtx, {
            type: 'bar',
            data: {
                labels: ['Daily', 'Monthly'],
                datasets: [{
                    label: 'Revenue (₫)',
                    data: [0, 0],
                    backgroundColor: [
                        'rgba(54, 162, 235, 0.8)',
                        'rgba(255, 99, 132, 0.8)'
                    ],
                    borderColor: [
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 99, 132, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return new Intl.NumberFormat('vi-VN').format(value) + '₫';
                            }
                        }
                    }
                },
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return 'Revenue: ' + new Intl.NumberFormat('vi-VN').format(context.parsed.y) + '₫';
                            }
                        }
                    }
                }
            }
        });

        // Orders Chart
        const ordersCtx = document.getElementById('ordersChartInline').getContext('2d');
        ordersChartInline = new Chart(ordersCtx, {
            type: 'doughnut',
            data: {
                labels: ['Daily', 'Monthly'],
                datasets: [{
                    data: [0, 0],
                    backgroundColor: [
                        'rgba(75, 192, 192, 0.8)',
                        'rgba(255, 206, 86, 0.8)'
                    ],
                    borderColor: [
                        'rgba(75, 192, 192, 1)',
                        'rgba(255, 206, 86, 1)'
                    ],
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'bottom' },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.label + ': ' + context.parsed + ' orders';
                            }
                        }
                    }
                }
            }
        });

        // Top Books Chart
        const topBooksCtx = document.getElementById('topBooksChartInline').getContext('2d');
        topBooksChartInline = new Chart(topBooksCtx, {
            type: 'bar',
            data: {
                labels: [],
                datasets: [{
                    label: 'Quantity Sold',
                    data: [],
                    backgroundColor: 'rgba(153, 102, 255, 0.8)',
                    borderColor: 'rgba(153, 102, 255, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                indexAxis: 'y',
                scales: {
                    x: {
                        beginAtZero: true,
                        ticks: { stepSize: 1 }
                    }
                },
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return 'Sold: ' + context.parsed.x + ' copies';
                            }
                        }
                    }
                }
            }
        });

        statisticsInitialized = true;
    }

    async function loadStatisticsData() {
        if (!statisticsInitialized) {
            initializeStatisticsCharts();
        }

        try {
            const response = await fetch(ctx + '/admin/orders/statistics/dashboard');
            const data = await response.json();
            
            if (data.error) {
                console.error('Failed to load statistics:', data.error);
                return;
            }

            // Update stat cards
            updateStatCardInline('dailyRevenueInline', formatCurrencyInline(data.daily.revenue));
            updateStatCardInline('dailyOrdersInline', data.daily.orderCount);
            updateStatCardInline('monthlyRevenueInline', formatCurrencyInline(data.monthly.revenue));
            updateStatCardInline('monthlyOrdersInline', data.monthly.orderCount);

            // Update charts
            updateRevenueChartInline(data.daily.revenue, data.monthly.revenue);
            updateOrdersChartInline(data.daily.orderCount, data.monthly.orderCount);
            updateTopBooksChartInline(data.topBooks.books);

        } catch (error) {
            console.error('Error loading statistics data:', error);
        }
    }

    function updateStatCardInline(elementId, value) {
        const element = document.getElementById(elementId);
        if (element) {
            element.textContent = value;
        }
    }

    function updateRevenueChartInline(dailyRevenue = null, monthlyRevenue = null) {
        if (dailyRevenue === null || monthlyRevenue === null) {
            fetch(ctx + '/admin/orders/statistics/daily')
                .then(response => response.json())
                .then(data => {
                    if (!data.error) {
                        dailyRevenue = data.revenue;
                    }
                    return fetch(ctx + '/admin/orders/statistics/monthly');
                })
                .then(response => response.json())
                .then(data => {
                    if (!data.error) {
                        monthlyRevenue = data.revenue;
                    }
                    revenueChartInline.data.datasets[0].data = [dailyRevenue, monthlyRevenue];
                    revenueChartInline.update();
                })
                .catch(error => console.error('Error updating revenue chart:', error));
        } else {
            revenueChartInline.data.datasets[0].data = [dailyRevenue, monthlyRevenue];
            revenueChartInline.update();
        }
    }

    function updateOrdersChartInline(dailyOrders = null, monthlyOrders = null) {
        if (dailyOrders === null || monthlyOrders === null) {
            fetch(ctx + '/admin/orders/statistics/daily')
                .then(response => response.json())
                .then(data => {
                    if (!data.error) {
                        dailyOrders = data.orderCount;
                    }
                    return fetch(ctx + '/admin/orders/statistics/monthly');
                })
                .then(response => response.json())
                .then(data => {
                    if (!data.error) {
                        monthlyOrders = data.orderCount;
                    }
                    ordersChartInline.data.datasets[0].data = [dailyOrders, monthlyOrders];
                    ordersChartInline.update();
                })
                .catch(error => console.error('Error updating orders chart:', error));
        } else {
            ordersChartInline.data.datasets[0].data = [dailyOrders, monthlyOrders];
            ordersChartInline.update();
        }
    }

    function updateTopBooksChartInline(books = null) {
        if (books === null) {
            const limit = document.getElementById('topBooksLimitInline').value;
            fetch(ctx + '/admin/orders/statistics/top-books?limit=' + limit)
                .then(response => response.json())
                .then(data => {
                    if (!data.error) {
                        books = data.books;
                    }
                    updateTopBooksChartDataInline(books);
                })
                .catch(error => console.error('Error updating top books chart:', error));
        } else {
            updateTopBooksChartDataInline(books);
        }
    }

    function updateTopBooksChartDataInline(books) {
        const labels = books.map(book => {
            return book.title.length > 25 ? book.title.substring(0, 25) + '...' : book.title;
        });
        const data = books.map(book => book.totalSold);

        topBooksChartInline.data.labels = labels;
        topBooksChartInline.data.datasets[0].data = data;
        topBooksChartInline.update();
    }

    function formatCurrencyInline(amount) {
        return new Intl.NumberFormat('vi-VN').format(amount) + '₫';
    }
</script>
