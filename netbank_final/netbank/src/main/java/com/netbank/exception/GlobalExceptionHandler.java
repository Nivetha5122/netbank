package com.netbank.exception;

import com.netbank.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    public Object handleInsufficientFunds(InsufficientFundsException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(ex.getMessage()));
        }
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorTitle", "Insufficient Funds");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public Object handleAccountNotFound(AccountNotFoundException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(ex.getMessage()));
        }
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorTitle", "Account Not Found");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(NetBankException.class)
    public Object handleNetBankException(NetBankException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(ex.getMessage()));
        }
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorTitle", "Error");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName, error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneral(Exception ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An unexpected error occurred"));
        }
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorTitle", "Unexpected Error");
        mav.addObject("errorMessage", "Something went wrong. Please try again.");
        return mav;
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("application/json");
    }
}
