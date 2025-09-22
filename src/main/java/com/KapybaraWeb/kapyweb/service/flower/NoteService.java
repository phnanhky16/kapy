package com.KapybaraWeb.kapyweb.service.flower;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.KapybaraWeb.kapyweb.dto.request.flower.NoteCreateRequest;
import com.KapybaraWeb.kapyweb.dto.request.flower.NoteUpdateRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.NoteResponse;
import com.KapybaraWeb.kapyweb.entity.*;
import com.KapybaraWeb.kapyweb.exception.AppException;
import com.KapybaraWeb.kapyweb.exception.ErrorCode;
import com.KapybaraWeb.kapyweb.repository.*;
import com.KapybaraWeb.kapyweb.service.config.ImagesService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class NoteService {
    NoteRepository noteRepository;
    ImagesService imagesService;
    PriceRepository priceRepository;
    QuantityInStockRepository quantityInStockRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;

    public NoteResponse createNote(NoteCreateRequest request, MultipartFile noteImage) {
        String imageUrl;
        try {
            imageUrl = (noteImage != null && !noteImage.isEmpty()) ? imagesService.uploadImage(noteImage) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Price p = priceRepository
                .findById(request.getPriceId())
                .orElseThrow(() -> new AppException(ErrorCode.PRICE_NOT_FOUND));

        QuantityInStock quantityInStock = quantityInStockRepository
                .findById(request.getQuantityInStockId())
                .orElseThrow(() -> new AppException(ErrorCode.QUANTITY_NOT_FOUND));

        List<Long> categoryIds = request.getCategoryId();
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Note n = Note.builder()
                .noteName(request.getNoteName())
                .notePrice(request.getNoteQuantity() * p.getPrice())
                .noteText(request.getNoteText())
                .noteImage(imageUrl)
                .noteQuantity(request.getNoteQuantity())
                .noteStatus(request.getNoteStatus())
                .price(p)
                .quantityInStock(quantityInStock)
                .categories(categories)
                .createdAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .build();
        Note saved = noteRepository.save(n);

        p.getNotes().add(saved);
        priceRepository.save(p);

        quantityInStock.getNotes().add(saved);
        quantityInStockRepository.save(quantityInStock);

        for (Category category : categories) {
            category.getNotes().add(saved);
            categoryRepository.save(category);
        }
        return NoteResponse.from(saved);
    }

    public NoteResponse getNoteById(Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOTE_NOT_FOUND));
        return NoteResponse.from(note);
    }

    public List<NoteResponse> getAllNotes() {
        return noteRepository.findAll().stream().map(NoteResponse::from).collect(Collectors.toList());
    }

    public NoteResponse updateNote(Long id, NoteUpdateRequest request, MultipartFile noteImage) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Note note = noteRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOTE_NOT_FOUND));

        // Upload image if provided
        if (noteImage != null && !noteImage.isEmpty()) {
            try {
                String imageUrl = imagesService.uploadImage(noteImage);
                note.setNoteImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Update QuantityInStock if changed
        if (request.getQuantityInStockId() != null
                && !request.getQuantityInStockId()
                        .equals(note.getQuantityInStock().getQuantityInStockId())) {

            QuantityInStock oldStock = note.getQuantityInStock();
            QuantityInStock newStock = quantityInStockRepository
                    .findById(request.getQuantityInStockId())
                    .orElseThrow(() -> new AppException(ErrorCode.QUANTITY_NOT_FOUND));

            oldStock.getNotes().remove(note);
            newStock.getNotes().add(note);

            note.setQuantityInStock(newStock);

            quantityInStockRepository.save(oldStock);
            quantityInStockRepository.save(newStock);
        }

        // Update Price if changed
        if (request.getPriceId() != null
                && !request.getPriceId().equals(note.getPrice().getPriceId())) {

            Price oldPrice = note.getPrice();
            Price newPrice = priceRepository
                    .findById(request.getPriceId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRICE_NOT_FOUND));

            oldPrice.getNotes().remove(note);
            newPrice.getNotes().add(note);

            note.setPrice(newPrice);

            priceRepository.save(oldPrice);
            priceRepository.save(newPrice);
        }

        // Update Categories if new list is not empty
        if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
            List<Long> categoryIds = request.getCategoryId();
            List<Category> newCategories = categoryRepository.findAllById(categoryIds);

            if (newCategories.size() != categoryIds.size()) {
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }

            // Remove note from old categories
            for (Category oldCategory : note.getCategories()) {
                oldCategory.getNotes().remove(note);
                categoryRepository.save(oldCategory);
            }

            // Add note to new categories
            for (Category newCategory : newCategories) {
                newCategory.getNotes().add(note);
                categoryRepository.save(newCategory);
            }

            note.setCategories(newCategories);
        }
        note.setNoteName(request.getNoteName());
        note.setNoteText(request.getNoteText());
        note.setNoteQuantity(request.getNoteQuantity());
        note.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        note.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        if (!request.getNoteQuantity().equals(note.getNoteQuantity())) {
            note.setNotePrice(request.getNoteQuantity() * note.getPrice().getPrice());
        }
        noteRepository.save(note);

        return NoteResponse.from(note);
    }

    public String deActiveNote(Long id) {
        User user = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Note note = noteRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOTE_NOT_FOUND));
        note.setNoteStatus(false);
        note.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        note.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        noteRepository.save(note);
        return "DeActive note successfully";
    }

    public String activeNote(Long id) {
        User user = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Note note = noteRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOTE_NOT_FOUND));
        note.setNoteStatus(true);
        note.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        note.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        noteRepository.save(note);
        return "Active note successfully";
    }
}
