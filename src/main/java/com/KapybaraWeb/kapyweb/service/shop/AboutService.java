package com.KapybaraWeb.kapyweb.service.shop;

import com.KapybaraWeb.kapyweb.dto.request.shop.AboutRequest;
import com.KapybaraWeb.kapyweb.dto.response.shop.AboutResponse;
import com.KapybaraWeb.kapyweb.entity.About;
import com.KapybaraWeb.kapyweb.entity.User;
import com.KapybaraWeb.kapyweb.exception.AppException;
import com.KapybaraWeb.kapyweb.exception.ErrorCode;
import com.KapybaraWeb.kapyweb.repository.AboutRepository;
import com.KapybaraWeb.kapyweb.repository.UserRepository;
import com.KapybaraWeb.kapyweb.service.config.ImagesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AboutService {
    AboutRepository aboutRepository;
    UserRepository userRepository;
    ImagesService imagesService;
    public AboutResponse getAboutById(Long id){
        return AboutResponse.from(aboutRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ABOUT_NOT_FOUND)));
    }
    public List<AboutResponse> getAllAbouts(){
        return aboutRepository.findAll().stream().map(AboutResponse::from).collect(Collectors.toList());
    }
    public AboutResponse createAbout(AboutRequest request, MultipartFile aboutImage){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String imageUrl;
        try {
            imageUrl = (aboutImage != null && !aboutImage.isEmpty()) ? imagesService.uploadImage(aboutImage) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        About about = About.builder()
                .aboutText(request.getAboutText())
                .aboutImage(imageUrl)
                .aboutStatus(request.getAboutStatus())
                .createdAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"))).createdBy(user.getRole() + "-" + user.getFirstName())
                .build();
        return AboutResponse.from(aboutRepository.save(about));
    }
    public AboutResponse updateAbout(Long id, AboutRequest request, MultipartFile aboutImage){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        About about = aboutRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ABOUT_NOT_FOUND));
        String imageUrl;
        try {
            imageUrl = (aboutImage != null && !aboutImage.isEmpty()) ? imagesService.uploadImage(aboutImage) : about.getAboutImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        about.setAboutText(request.getAboutText());
        about.setAboutImage(imageUrl);
        about.setAboutStatus(request.getAboutStatus());
        about.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        about.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        return AboutResponse.from(aboutRepository.save(about));
    }
    public String activeAbout(Long id){
        About about = aboutRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ABOUT_NOT_FOUND));
        about.setAboutStatus(true);
        aboutRepository.save(about);
        return "Activated";
    }

    public String deActiveAbout(Long id){
        About about = aboutRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ABOUT_NOT_FOUND));
        about.setAboutStatus(false);
        aboutRepository.save(about);
        return "Deactivated";
    }
}
