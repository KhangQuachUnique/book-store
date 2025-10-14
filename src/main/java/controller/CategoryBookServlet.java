package controller;

import constant.PathConstants;
import model.Book;
import model.Category;
import service.CategoryBookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/categories")
public class CategoryBookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        Integer categoryId = null;
        try {
            String categoryParam = req.getParameter("category");
            if (categoryParam != null && !categoryParam.isEmpty()) {
                categoryId = Integer.parseInt(categoryParam);
            }
        } catch (NumberFormatException e) {
            // Ignore parsing error
        }

        // Lấy các tham số filter từ form
        String title = req.getParameter("title");
        String author = req.getParameter("author");
        String includeCategoriesParam = req.getParameter("includeCategories");
        String excludeCategoriesParam = req.getParameter("excludeCategories");
        String action = req.getParameter("action"); // "title" / "categories" / "filter"
        String sortBy = req.getParameter("sortBy");

        Integer publishYear = parseIntOrNull(req.getParameter("publishYear"));
        Integer yearBefore = parseIntOrNull(req.getParameter("yearBefore"));
        Integer yearAfter = parseIntOrNull(req.getParameter("yearAfter"));
        Long priceFrom = parseLongOrNull(req.getParameter("priceFrom"));
        Long priceUpTo = parseLongOrNull(req.getParameter("priceUpTo"));

        boolean hasSort = sortBy != null && !sortBy.isEmpty();

        List<Long> includeCategories = parseIdList(includeCategoriesParam);
        List<Long> excludeCategories = parseIdList(excludeCategoriesParam);

        // Logic exclusive dựa trên action type để tránh "trộn" filter không mong muốn
        boolean hasTitle = title != null && !title.trim().isEmpty();
        boolean hasAuthor = author != null && !author.trim().isEmpty();
        boolean hasYear = publishYear != null;
        boolean hasYearRange = yearBefore != null || yearAfter != null;
        boolean hasPriceRange = priceFrom != null || priceUpTo != null;
        boolean hasCategories = (includeCategories != null && !includeCategories.isEmpty()) ||
                (excludeCategories != null && !excludeCategories.isEmpty());
        boolean hasAnyFilter = hasTitle || hasAuthor || hasYear || hasYearRange || hasPriceRange || hasCategories;

        if ("title".equals(action)) {
            // Ưu tiên tìm theo title -> clear categories
            includeCategories = null;
            excludeCategories = null;
            includeCategoriesParam = null;
            excludeCategoriesParam = null;
            hasCategories = false;
            hasAnyFilter = hasTitle || hasAuthor || hasYear || hasYearRange || hasPriceRange; // cập nhật lại
        } else if ("categories".equals(action)) {
            // Ưu tiên filter theo categories -> clear title/author/year/price
            title = null;
            author = null;
            publishYear = null;
            yearBefore = null;
            yearAfter = null;
            priceFrom = null;
            priceUpTo = null;
            hasAnyFilter = hasCategories; // chỉ còn categories
        }

        List<Book> books;
        int totalPages;

        try {
            if (hasAnyFilter) {
                // Bất kỳ filter nào -> dùng filterBooks (đã hỗ trợ sort)
                books = CategoryBookService.filterBook(title, author, includeCategories, excludeCategories, page, sortBy, publishYear, yearBefore, yearAfter, priceFrom, priceUpTo);
                totalPages = CategoryBookService.getTotalPage(title, author, includeCategories, excludeCategories, publishYear, yearBefore, yearAfter, priceFrom, priceUpTo);
            } else if (categoryId != null) {
                // Không có filter, chỉ theo Category, có thể có sort riêng
                if (hasSort) {
                    books = CategoryBookService.sortBooksByCategory(categoryId, sortBy, page);
                } else {
                    books = CategoryBookService.getBooksByCategoryId(categoryId, page);
                }
                totalPages = CategoryBookService.getTotalPagesByCategory(categoryId);
            } else {
                // Không filter, không category cụ thể -> All books (có thể có sort)
                if (hasSort) {
                    books = CategoryBookService.sortAllBooks(sortBy, page);
                } else {
                    books = CategoryBookService.getAllBook(page);
                }
                totalPages = CategoryBookService.getTotalPages();
            }
        } catch (Exception e) {
            log("Error when filtering/sorting books", e);
            books = new java.util.ArrayList<>();
            totalPages = 1;
        }

        // Clamp page to totalPages and refetch if necessary to avoid empty page
        if (totalPages < 1) totalPages = 1;
        if (page > totalPages) {
            page = totalPages;
            try {
                if (hasAnyFilter) {
                    books = CategoryBookService.filterBook(title, author, includeCategories, excludeCategories, page, sortBy, publishYear, yearBefore, yearAfter, priceFrom, priceUpTo);
                } else if (categoryId != null) {
                    books = hasSort ? CategoryBookService.sortBooksByCategory(categoryId, sortBy, page)
                                    : CategoryBookService.getBooksByCategoryId(categoryId, page);
                } else {
                    books = hasSort ? CategoryBookService.sortAllBooks(sortBy, page)
                                    : CategoryBookService.getAllBook(page);
                }
            } catch (Exception ex) {
                log("Error when refetching books after clamping page", ex);
            }
        }

        // Lấy danh sách tất cả categories để hiển thị trong bảng category
        List<Category> categories = null;
        try {
            categories = CategoryBookService.getAllCategory();
        } catch (Exception e) {
            log("Error when loading categories", e);
        }

        // Tính toán các trang sẽ hiển thị
        int[] visiblePages = CategoryBookService.calculateVisiblePages(page, totalPages);

        // Build query string preserve filters (exclude page)
        String queryString = buildQueryString(title, author, publishYear, yearBefore, yearAfter, priceFrom, priceUpTo,
                includeCategoriesParam, excludeCategoriesParam, sortBy, categoryId, action);

        // Summary label for category input (optional)
        String categorySummaryLabel = buildCategorySummaryLabel(includeCategoriesParam, excludeCategoriesParam);

        // Set attributes for JSP
        req.setAttribute("books", books);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("categoryId", categoryId);
        req.setAttribute("visiblePages", visiblePages);
        req.setAttribute("showFirstEllipsis", visiblePages.length > 0 && visiblePages[0] > 1);
        req.setAttribute("showLastEllipsis", visiblePages.length > 0 && visiblePages[visiblePages.length - 1] < totalPages);

        // Echo back filters for form
        req.setAttribute("title", title);
        req.setAttribute("author", author);
        req.setAttribute("publishYear", publishYear != null ? publishYear.toString() : "");
        req.setAttribute("yearBefore", yearBefore != null ? yearBefore.toString() : "");
        req.setAttribute("yearAfter", yearAfter != null ? yearAfter.toString() : "");
        req.setAttribute("priceFrom", priceFrom != null ? priceFrom.toString() : "");
        req.setAttribute("priceUpTo", priceUpTo != null ? priceUpTo.toString() : "");
        req.setAttribute("includeCategories", includeCategoriesParam);
        req.setAttribute("excludeCategories", excludeCategoriesParam);
        req.setAttribute("sortBy", sortBy);
        req.setAttribute("action", action);
        req.setAttribute("categorySummaryLabel", categorySummaryLabel);
        req.setAttribute("queryString", queryString);

        req.setAttribute("contentPage", "/WEB-INF/views/categoryBook.jsp");
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }

    private Integer parseIntOrNull(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try { return Integer.parseInt(value.trim()); } catch (NumberFormatException e) { return null; }
    }

    private Long parseLongOrNull(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try { return Long.parseLong(value.trim()); } catch (NumberFormatException e) { return null; }
    }

    private List<Long> parseIdList(String csv) {
        if (csv == null || csv.trim().isEmpty()) return null;
        java.util.ArrayList<Long> list = new java.util.ArrayList<>();
        for (String s : csv.split(",")) {
            try {
                if (!s.trim().isEmpty()) list.add(Long.parseLong(s.trim()));
            } catch (NumberFormatException ignored) {}
        }
        return list.isEmpty() ? null : list;
    }

    private String buildQueryString(String title, String author, Integer publishYear, Integer yearBefore, Integer yearAfter,
                                    Long priceFrom, Long priceUpTo, String includeCategories, String excludeCategories,
                                    String sortBy, Integer categoryId, String action) {
        StringBuilder qs = new StringBuilder();
        java.util.function.BiConsumer<String, String> add = (k, v) -> {
            if (v != null && !v.isEmpty()) {
                if (qs.length() > 0) qs.append('&');
                qs.append(URLEncoder.encode(k, StandardCharsets.UTF_8)).append('=')
                  .append(URLEncoder.encode(v, StandardCharsets.UTF_8));
            }
        };
        add.accept("title", safe(title));
        add.accept("author", safe(author));
        add.accept("publishYear", safeNum(publishYear));
        add.accept("yearBefore", safeNum(yearBefore));
        add.accept("yearAfter", safeNum(yearAfter));
        add.accept("priceFrom", safeNum(priceFrom));
        add.accept("priceUpTo", safeNum(priceUpTo));
        add.accept("includeCategories", safe(includeCategories));
        add.accept("excludeCategories", safe(excludeCategories));
        add.accept("sortBy", safe(sortBy));
        add.accept("action", safe(action));
        if (categoryId != null) add.accept("category", String.valueOf(categoryId));
        return qs.toString();
    }

    private String buildCategorySummaryLabel(String includeCategories, String excludeCategories) {
        int inc = 0, exc = 0;
        if (includeCategories != null && !includeCategories.isEmpty()) {
            inc = (int) java.util.Arrays.stream(includeCategories.split(",")).filter(s -> !s.trim().isEmpty()).count();
        }
        if (excludeCategories != null && !excludeCategories.isEmpty()) {
            exc = (int) java.util.Arrays.stream(excludeCategories.split(",")).filter(s -> !s.trim().isEmpty()).count();
        }
        if (inc == 0 && exc == 0) return "";
        if (inc > 0 && exc > 0) return "Included: " + inc + ", Excluded: " + exc;
        if (inc > 0) return "Include " + inc + " categories";
        return "Exclude " + exc + " categories";
    }

    private String safe(String s) { return s == null ? "" : s; }
    private String safeNum(Number n) { return n == null ? "" : String.valueOf(n); }
}