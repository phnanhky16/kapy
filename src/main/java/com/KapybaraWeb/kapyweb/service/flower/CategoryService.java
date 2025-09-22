package com.KapybaraWeb.kapyweb.service.flower;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.KapybaraWeb.kapyweb.dto.request.flower.CategoryRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.CategoryResponse;
import com.KapybaraWeb.kapyweb.entity.Category;
import com.KapybaraWeb.kapyweb.entity.Flower;
import com.KapybaraWeb.kapyweb.entity.User;
import com.KapybaraWeb.kapyweb.exception.AppException;
import com.KapybaraWeb.kapyweb.exception.ErrorCode;
import com.KapybaraWeb.kapyweb.repository.CategoryRepository;
import com.KapybaraWeb.kapyweb.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    UserRepository userRepository;

    public CategoryResponse createCategory(CategoryRequest request) {
        validateBeginEndDate(request.getBeginDate(), request.getEndDate());
        Category category = new Category();
        category.setName(request.getName());
        category.setBeginDate(request.getBeginDate());
        category.setEndDate(request.getEndDate());
        category.setCreatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        categoryRepository.save(category);
        return CategoryResponse.from(category);
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return CategoryResponse.from(category);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryResponse::from).toList();
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        validateBeginEndDate(request.getBeginDate(), request.getEndDate());

        category.setName(request.getName());
        category.setBeginDate(request.getBeginDate());
        category.setEndDate(request.getEndDate());
        category.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        category.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        categoryRepository.save(category);
        return CategoryResponse.from(category);
    }

    public void deleteCategory(Long id) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        List<Flower> flowers = category.getFlowers();
        if (!flowers.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_HAS_FLOWERS);
        }
        categoryRepository.delete(category);
    }

    public void validateBeginEndDate(String beginDateStr, String endDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");

        MonthDay begin = MonthDay.parse(beginDateStr, formatter);
        MonthDay end = MonthDay.parse(endDateStr, formatter);

        if (end.isBefore(begin)) {
            throw new AppException(ErrorCode.CATEGORY_CHECK_DATE_BEGIN_END);
        }
    }
}
