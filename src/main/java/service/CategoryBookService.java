package service;

import dao.CategoryBookDao;
import model.Book;
import model.Category;

import java.util.List;

public class CategoryBookService {
    private static final int BOOKS_PER_PAGE = 40;
    private static final int MAX_PAGE_DISPLAY = 5;

    /**
     * Lấy sách theo category ID với phân trang
     */
    public static List<Book> getBooksByCategoryId(int categoryId, int page) {
        return CategoryBookDao.getBooksByCategoryId(categoryId, page);
    }

    /**
     * Lấy tất cả sách với phân trang
     */
    public static List<Book> getAllBook(int page) {
        return CategoryBookDao.getAllBook(page);
    }

    /**
     * Tính tổng số trang cho tất cả sách
     */
    public static int getTotalPages() {
        long totalBooks = CategoryBookDao.getTotalBooks();
        int totalPages = (int) Math.ceil((double) totalBooks / BOOKS_PER_PAGE);
        return Math.max(1, totalPages); // Đảm bảo luôn có ít nhất 1 trang
    }

    /**
     * Tính tổng số trang theo category
     */
    public static int getTotalPagesByCategory(int categoryId) {
        long totalBooks = CategoryBookDao.getTotalBooksByCategory(categoryId);
        int totalPages = (int) Math.ceil((double) totalBooks / BOOKS_PER_PAGE);
        return Math.max(1, totalPages); // Đảm bảo luôn có ít nhất 1 trang
    }

    /**
     * Tính toán các trang hiển thị trong pagination
     */
    public static int[] calculateVisiblePages(int currentPage, int totalPages) {
        // Xử lý trường hợp không có trang nào hoặc totalPages không hợp lệ
        if (totalPages <= 0) {
            return new int[]{1}; // Trả về array với page 1 thay vì empty array
        }

        if (totalPages <= MAX_PAGE_DISPLAY) {
            int[] pages = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                pages[i] = i + 1;
            }
            return pages;
        }

        if (currentPage <= 3) {
            // Đầu: 1 2 3 4 5 ... totalPages
            return new int[]{1, 2, 3, 4, 5};
        } else if (currentPage >= totalPages - 2) {
            // Cuối: 1 ... totalPages-4 totalPages-3 totalPages-2 totalPages-1 totalPages
            return new int[]{totalPages - 4, totalPages - 3, totalPages - 2, totalPages - 1, totalPages};
        } else {
            // Giữa: 1 ... currentPage-1 currentPage currentPage+1 ... totalPages
            return new int[]{currentPage - 2, currentPage - 1, currentPage, currentPage + 1, currentPage + 2};
        }
    }

    /**
     * Lọc sách theo tiêu đề, tác giả, và categories
     */
    public static List<Book> filterBook(String title, String author, 
                                        List<Long> includeCategories,
                                        List<Long> excludeCategories, 
                                        int page) {
        return CategoryBookDao.filterBooks(title, author, includeCategories, excludeCategories, page);
    }

    /**
     * Tính tổng số trang cho kết quả filter
     */
    public static int getTotalPage(String title, String author, 
                                   List<Long> includeCategories,
                                   List<Long> excludeCategories) {
        long totalBooks = CategoryBookDao.countBooks(title, author, includeCategories, excludeCategories);
        int totalPages = (int) Math.ceil((double) totalBooks / BOOKS_PER_PAGE);
        return Math.max(1, totalPages); // Đảm bảo luôn có ít nhất 1 trang
    }

    /**
     * Lấy tất cả categories
     */
    public static List<Category> getAllCategory() {
        return CategoryBookDao.getAllCategories();
    }
}
