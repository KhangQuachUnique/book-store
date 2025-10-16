<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Chart.js CDN -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/orderStatistics.css">

<div class="container">
    <h1 class="page-title">Order Statistics Dashboard</h1>
    <p class="text-muted" style="margin-bottom: 2rem;">Visualize order data with interactive charts and statistics.</p>

    <!-- Statistics Cards -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-icon daily-icon">📅</div>
            <div class="stat-content">
                <h3 class="stat-title">Daily Revenue</h3>
                <div class="stat-value" id="dailyRevenue">Loading...</div>
                <div class="stat-subtitle">Today's total revenue</div>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon order-icon">📦</div>
            <div class="stat-content">
                <h3 class="stat-title">Daily Orders</h3>
                <div class="stat-value" id="dailyOrders">Loading...</div>
                <div class="stat-subtitle">Orders placed today</div>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon monthly-icon">💰</div>
            <div class="stat-content">
                <h3 class="stat-title">Monthly Revenue</h3>
                <div class="stat-value" id="monthlyRevenue">Loading...</div>
                <div class="stat-subtitle">This month's total revenue</div>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon total-icon">📊</div>
            <div class="stat-content">
                <h3 class="stat-title">Monthly Orders</h3>
                <div class="stat-value" id="monthlyOrders">Loading...</div>
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
                    <h3 class="chart-title">Revenue Comparison</h3>
                    <div class="chart-controls">
                        <button class="btn btn-sm btn-primary" onclick="updateRevenueChart()">Refresh</button>
                    </div>
                </div>
                <div class="chart-container">
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>
            
            <!-- Orders Chart -->
            <div class="chart-card">
                <div class="chart-header">
                    <h3 class="chart-title">Orders Comparison</h3>
                    <div class="chart-controls">
                        <button class="btn btn-sm btn-primary" onclick="updateOrdersChart()">Refresh</button>
                    </div>
                </div>
                <div class="chart-container">
                    <canvas id="ordersChart"></canvas>
                </div>
            </div>
        </div>
        
        <div class="chart-row">
            <!-- Top Selling Books -->
            <div class="chart-card full-width">
                <div class="chart-header">
                    <h3 class="chart-title">Top Selling Books</h3>
                    <div class="chart-controls">
                        <select id="topBooksLimit" onchange="updateTopBooksChart()">
                            <option value="5">Top 5</option>
                            <option value="10" selected>Top 10</option>
                            <option value="15">Top 15</option>
                        </select>
                        <button class="btn btn-sm btn-primary" onclick="updateTopBooksChart()">Refresh</button>
                    </div>
                </div>
                <div class="chart-container">
                    <canvas id="topBooksChart"></canvas>
                </div>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="quick-actions">
        <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-secondary">
            <i class="icon">←</i> Back to Orders
        </a>
        <button class="btn btn-primary" onclick="refreshAllCharts()">
            <i class="icon">🔄</i> Refresh All
        </button>
    </div>
</div>

<script>
    const ctx = '${pageContext.request.contextPath}';
    let revenueChart, ordersChart, topBooksChart;

    // Initialize charts when page loads
    document.addEventListener('DOMContentLoaded', function() {
        initializeCharts();
        loadDashboardData();
    });

    function initializeCharts() {
        // Revenue Chart
        const revenueCtx = document.getElementById('revenueChart').getContext('2d');
        revenueChart = new Chart(revenueCtx, {
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
                    legend: {
                        display: false
                    },
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
        const ordersCtx = document.getElementById('ordersChart').getContext('2d');
        ordersChart = new Chart(ordersCtx, {
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
                    legend: {
                        position: 'bottom'
                    },
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
        const topBooksCtx = document.getElementById('topBooksChart').getContext('2d');
        topBooksChart = new Chart(topBooksCtx, {
            type: 'horizontalBar',
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
                        ticks: {
                            stepSize: 1
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    },
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
    }

    async function loadDashboardData() {
        try {
            const response = await fetch(ctx + '/admin/orders/statistics/dashboard');
            const data = await response.json();
            
            if (data.error) {
                showError('Failed to load statistics: ' + data.error);
                return;
            }

            // Update stat cards
            updateStatCard('dailyRevenue', formatCurrency(data.daily.revenue));
            updateStatCard('dailyOrders', data.daily.orderCount);
            updateStatCard('monthlyRevenue', formatCurrency(data.monthly.revenue));
            updateStatCard('monthlyOrders', data.monthly.orderCount);

            // Update charts
            updateRevenueChart(data.daily.revenue, data.monthly.revenue);
            updateOrdersChart(data.daily.orderCount, data.monthly.orderCount);
            updateTopBooksChart(data.topBooks.books);

        } catch (error) {
            console.error('Error loading dashboard data:', error);
            showError('Failed to load statistics');
        }
    }

    function updateStatCard(elementId, value) {
        const element = document.getElementById(elementId);
        if (element) {
            element.textContent = value;
        }
    }

    function updateRevenueChart(dailyRevenue = null, monthlyRevenue = null) {
        if (dailyRevenue === null || monthlyRevenue === null) {
            // Fetch fresh data
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
                    revenueChart.data.datasets[0].data = [dailyRevenue, monthlyRevenue];
                    revenueChart.update();
                })
                .catch(error => console.error('Error updating revenue chart:', error));
        } else {
            revenueChart.data.datasets[0].data = [dailyRevenue, monthlyRevenue];
            revenueChart.update();
        }
    }

    function updateOrdersChart(dailyOrders = null, monthlyOrders = null) {
        if (dailyOrders === null || monthlyOrders === null) {
            // Fetch fresh data
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
                    ordersChart.data.datasets[0].data = [dailyOrders, monthlyOrders];
                    ordersChart.update();
                })
                .catch(error => console.error('Error updating orders chart:', error));
        } else {
            ordersChart.data.datasets[0].data = [dailyOrders, monthlyOrders];
            ordersChart.update();
        }
    }

    function updateTopBooksChart(books = null) {
        if (books === null) {
            const limit = document.getElementById('topBooksLimit').value;
            fetch(ctx + '/admin/orders/statistics/top-books?limit=' + limit)
                .then(response => response.json())
                .then(data => {
                    if (!data.error) {
                        books = data.books;
                    }
                    updateTopBooksChartData(books);
                })
                .catch(error => console.error('Error updating top books chart:', error));
        } else {
            updateTopBooksChartData(books);
        }
    }

    function updateTopBooksChartData(books) {
        const labels = books.map(book => {
            // Truncate long titles
            return book.title.length > 30 ? book.title.substring(0, 30) + '...' : book.title;
        });
        const data = books.map(book => book.totalSold);

        topBooksChart.data.labels = labels;
        topBooksChart.data.datasets[0].data = data;
        topBooksChart.update();
    }

    function refreshAllCharts() {
        loadDashboardData();
    }

    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN').format(amount) + '₫';
    }

    function showError(message) {
        // Simple error display - you can enhance this with a proper modal
        alert(message);
    }
</script>
