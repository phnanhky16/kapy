package com.KapybaraWeb.kapyweb.service.user;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.KapybaraWeb.kapyweb.dto.request.user.UserChangePasswordRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.UserRegisterRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.UserResetPasswordRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.UserUpdateRequest;
import com.KapybaraWeb.kapyweb.dto.response.user.UserResponse;
import com.KapybaraWeb.kapyweb.entity.User;
import com.KapybaraWeb.kapyweb.enums.Role;
import com.KapybaraWeb.kapyweb.exception.AppException;
import com.KapybaraWeb.kapyweb.exception.ErrorCode;
import com.KapybaraWeb.kapyweb.repository.UserRepository;
import com.KapybaraWeb.kapyweb.service.config.MailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Qualifier("realEmailService")
    MailService mailService;
    //    RoleRepository roleRepository;

    public UserResponse getUserById(Long userId) {
        return UserResponse.fromUser(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public List<UserResponse> getAllUsers() {
        return UserResponse.fromUsers(userRepository.findAllByRole(Role.USER));
    }

    public List<UserResponse> getAllStaffs() {
        return UserResponse.fromUsers(userRepository.findAllByRole(Role.STAFF));
    }

    public List<UserResponse> getAllOrders() {
        return UserResponse.fromUsers(userRepository.findAllByRole(Role.ORDER));
    }

    public List<UserResponse> getAllFlorists() {
        return UserResponse.fromUsers(userRepository.findAllByRole(Role.FLORIST));
    }

    public List<UserResponse> getAllHrs() {
        return UserResponse.fromUsers(userRepository.findAllByRole(Role.HR));
    }

    public UserResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return UserResponse.fromUser(userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    private UserResponse registerUserWithRole(UserRegisterRequest user, Role roleName) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        //        Role roleEntity = roleRepository.findById(roleName)
        //                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User newUser = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthday(user.getBirthday())
                .status(true)
                .role(roleName)
                .gender(user.getGender())
                .createdAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .build();

        try {
            User savedUser = userRepository.save(newUser);
            mailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
            return UserResponse.fromUser(savedUser);
        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_CREATION_FAILED);
        }
    }

    public UserResponse registerUser(UserRegisterRequest user) {
        return registerUserWithRole(user, Role.USER);
    }

    public UserResponse registerStaff(UserRegisterRequest user) {
        return registerUserWithRole(user, Role.STAFF);
    }

    public UserResponse registerOrder(UserRegisterRequest user) {
        return registerUserWithRole(user, Role.ORDER);
    }

    public UserResponse registerFlorist(UserRegisterRequest user) {
        return registerUserWithRole(user, Role.FLORIST);
    }

    public UserResponse registerHr(UserRegisterRequest user) {
        return registerUserWithRole(user, Role.HR);
    }

    public UserResponse updateUser(UserUpdateRequest user) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User u =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setEmail(user.getEmail());
        u.setPhoneNumber(user.getPhoneNumber());
        u.setBirthday(user.getBirthday());
        u.setGender(user.getGender());
        u.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        u.setUpdatedBy(u.getRole() + "-" + u.getFirstName());
        User savedUser = userRepository.save(u);
        return UserResponse.fromUser(savedUser);
    }

    public UserResponse updateUserById(Long id, UserUpdateRequest user) {
        User u = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userUpdate =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setEmail(user.getEmail());
        u.setPhoneNumber(user.getPhoneNumber());
        u.setBirthday(user.getBirthday());
        u.setGender(user.getGender());
        u.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        u.setUpdatedBy(userUpdate.getRole() + "-" + userUpdate.getFirstName());
        User savedUser = userRepository.save(u);
        return UserResponse.fromUser(savedUser);
    }

    public String ActiveUser(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userUpdate =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatus(true);
        user.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        user.setUpdatedBy(userUpdate.getRole() + "-" + userUpdate.getFirstName());
        userRepository.save(user);
        return "Active user successfully";
    }

    public String ActiveUserMyself() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatus(true);
        user.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        user.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        userRepository.save(user);
        return "Active user successfully";
    }

    public String DeactiveUser(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userUpdate =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatus(false);
        user.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        user.setUpdatedBy(userUpdate.getRole() + "-" + userUpdate.getFirstName());
        userRepository.save(user);
        return "DeActive user successfully";
    }

    public String DeactiveUserMyself() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatus(false);
        user.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        user.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        userRepository.save(user);
        return "DeActive user successfully";
    }

    public String changePassword(UserChangePasswordRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (passwordEncoder.matches(user.getPassword(), request.getOldPassword())) {
            throw new AppException(ErrorCode.PASSWORD_WRONG);
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        user.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        userRepository.save(user);
        return "Change password successfully";
    }

    public String resetPassword(Long id, UserResetPasswordRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userUpdate =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        user.setUpdatedBy(userUpdate.getRole() + "-" + userUpdate.getFirstName());
        userRepository.save(user);
        return "Reset password successfully";
    }
}
