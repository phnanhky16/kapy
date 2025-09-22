package com.KapybaraWeb.kapyweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Exception codes
    UNCATEGORIZED_EXCEPTION("Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("You do not have permission", HttpStatus.FORBIDDEN),
    KEY_INVALID("Key is invalid", HttpStatus.BAD_REQUEST),
    // Field validated user
    USERNAME_NOTBLANK("Username must not be blank", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID("Username must be between 4 and 30 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_NOTBLANK("Password must not be blank", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID("Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    FIRST_NAME_NOTBLANK("First name must not be blank", HttpStatus.BAD_REQUEST),
    LAST_NAME_NOTBLANK("Last name must not be blank", HttpStatus.BAD_REQUEST),
    EMAIL_NOTBLANK("Email must not be blank", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID("Email is not valid", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_NOTBLANK("Phone number must not be blank", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_INVALID("Phone number is not valid", HttpStatus.BAD_REQUEST),
    BIRTHDAY_INVALID("Birthday is not valid", HttpStatus.BAD_REQUEST),
    GENDER_NOTBLANK("Gender must not be blank", HttpStatus.BAD_REQUEST),
    // CHECK USER
    USER_EXISTED("User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED("User not found", HttpStatus.NOT_FOUND),
    USERNAME_EXISTED("Username already exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED("Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED("Phone number already exists", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED("Wrong username or password", HttpStatus.UNAUTHORIZED),
    UNAUTHENTICATION("Unauthenticated", HttpStatus.UNAUTHORIZED),
    PASSWORD_WRONG("Wrong password", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH("Passwords do not match", HttpStatus.BAD_REQUEST),

    // Field validated QuantityInStock
    QUANTITY_NOTNULL("Quantity must not be null", HttpStatus.BAD_REQUEST),
    QUANTITY_MIN("Quantity must be at least 0", HttpStatus.BAD_REQUEST),
    NAME_NOTBLANK("name must not be blank", HttpStatus.BAD_REQUEST),
    QUANTITY_NOT_FOUND("Quantity in stock not found", HttpStatus.NOT_FOUND),
    QUANTITY_HAS_FLOWERS("Quantity in stock has flowers, cannot delete", HttpStatus.BAD_REQUEST),
    // Field validated Category
    CATEGORY_CHECK_DATE_BEGIN_END("Category start date must be before end date", HttpStatus.BAD_REQUEST),
    DATE_MUST_BE_IN_dd_MM("Date must be in dd-MM format", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND("Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_HAS_FLOWERS("Category has flowers, cannot delete", HttpStatus.BAD_REQUEST),
    // Field validated Price
    PRICE_NOTNULL("Price must not be null", HttpStatus.BAD_REQUEST),
    PRICE_MIN("Price must be at least 0", HttpStatus.BAD_REQUEST),
    PRICE_NOT_FOUND("Price not found", HttpStatus.NOT_FOUND),
    PRICE_HAS_FLOWERS("Price has flowers, cannot delete", HttpStatus.BAD_REQUEST),
    // Field validated Flower
    FLOWER_NOT_FOUND("Flower not found", HttpStatus.NOT_FOUND),
    // Field validated Ribbon
    RIBBON_NOT_FOUND("Ribbon not found", HttpStatus.NOT_FOUND),
    // Field validated Wrap
    WRAP_NOT_FOUND("Wrap not found", HttpStatus.NOT_FOUND),
    // Field validated Note
    NOTE_NOT_FOUND("Note not found", HttpStatus.NOT_FOUND),
    // Field validated Coupon
    COUPON_CODE_EXISTED("Coupon code existed", HttpStatus.BAD_REQUEST),
    COUPON_NOT_FOUND("Coupon not found", HttpStatus.NOT_FOUND),
    PERMISSION_EXISTED("Permission already exists", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND("Permission not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND("Role not found", HttpStatus.NOT_FOUND),
    USER_CREATION_FAILED("User creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    ABOUT_NOT_FOUND("About not found", HttpStatus.NOT_FOUND)
    ;

    private final String message;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(String message, HttpStatusCode httpStatusCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
