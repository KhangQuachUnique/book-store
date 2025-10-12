package service;

import dao.CategoryDao;
import model.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDao categoryDao = new CategoryDao();

    /**
     * Lấy tất cả các danh mục, sắp xếp theo ID giảm dần
     */
    public List<Category> getAll() {
        return categoryDao.findAll();
    }

    /**
     * Lấy một danh mục theo ID
     */
    public Category getById(Long id) {
        return categoryDao.findById(id);
    }

    /**
     * Lấy danh sách danh mục con của một danh mục cha
     */
    public List<Category> getByParentId(Long parentId) {
        return categoryDao.findByParentId(parentId);
    }

    /**
     * Tạo một danh mục mới
     * @return null nếu thành công, chuỗi lỗi nếu thất bại
     */
    public String create(String name, Long parentId, Boolean isLeaf) {
        try {
            // Kiểm tra tính hợp lệ của dữ liệu
            if (name == null || name.trim().isEmpty()) {
                return "Tên danh mục không được để trống";
            }

            // Kiểm tra trùng tên
            if (categoryDao.existsByName(name, null)) {
                return "Tên danh mục đã tồn tại";
            }

            // Gọi DAO để lưu dữ liệu
            categoryDao.createWithParent(name, parentId, isLeaf);
            return null;
        } catch (Exception ex) {
            return "Lỗi khi tạo danh mục: " + ex.getMessage();
        }
    }

    /**
     * Cập nhật một danh mục
     * @return null nếu thành công, chuỗi lỗi nếu thất bại
     */
    public String update(Long id, String name, Long parentId, Boolean isLeaf) {
        try {
            // Kiểm tra tồn tại
            if (categoryDao.findById(id) == null) {
                return "Danh mục không tồn tại";
            }

            // Kiểm tra trùng tên nếu có thay đổi tên
            if (name != null && !name.isEmpty() && categoryDao.existsByName(name, id)) {
                return "Tên danh mục đã tồn tại";
            }

            // Kiểm tra không cho phép đặt cha là chính nó
            if (parentId != null && id.equals(parentId)) {
                return "Không thể đặt danh mục cha là chính nó";
            }

            // Gọi DAO để cập nhật
            categoryDao.updateCategory(id, name, parentId, isLeaf);
            return null;
        } catch (Exception ex) {
            return "Lỗi khi cập nhật danh mục: " + ex.getMessage();
        }
    }

    /**
     * Xóa một danh mục
     * @return null nếu thành công, chuỗi lỗi nếu thất bại
     */
    public String delete(Long id) {
        try {
            // Kiểm tra tồn tại
            Category category = categoryDao.findById(id);
            if (category == null) {
                return "Danh mục không tồn tại";
            }

            // Việc kiểm tra các ràng buộc (danh mục con, sản phẩm) đã được chuyển xuống DAO
            // để đảm bảo chúng được thực hiện trong cùng một transaction
            categoryDao.delete(id);
            return null;
        } catch (Exception ex) {
            return "Lỗi khi xóa danh mục: " + ex.getMessage();
        }
    }
}