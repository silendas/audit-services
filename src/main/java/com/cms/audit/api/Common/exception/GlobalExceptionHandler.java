package com.cms.audit.api.Common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cms.audit.api.Common.constant.ReturnCode;
import com.cms.audit.api.Common.dto.BaseResponse;
import com.cms.audit.api.Common.dto.ErrorObjectDto;
import com.cms.audit.api.Common.dto.Meta;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        return ResponseEntittyHandler.errorResponse(ex.getMessage(), "Request belum sesuai dengan kriteria", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
        return ResponseEntittyHandler.allHandler(null, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullException(NullPointerException ex, WebRequest request) {
        ErrorObjectDto theErrorObject = new ErrorObjectDto();
        theErrorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        theErrorObject.setMessage(ex.getMessage());
        theErrorObject.setTimeStamp(new Date());
        BaseResponse<ErrorObjectDto> baseResponse = new BaseResponse<>(theErrorObject, new Meta(ReturnCode.FAILED_NOT_FOUND.getStatusCode(), ReturnCode.FAILED_NOT_FOUND.getMessage(), ""));
        return new ResponseEntity<>(baseResponse.getCustomizeResponse("error"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemAlreadyExistException.class)
    public ResponseEntity<Object> handleItemExistsException(ItemAlreadyExistException ex, WebRequest request) {
        ErrorObjectDto theErrorObject = new ErrorObjectDto();
        theErrorObject.setStatusCode(HttpStatus.CONFLICT.value());
        theErrorObject.setMessage(ex.getMessage());
        theErrorObject.setTimeStamp(new Date());
        BaseResponse<ErrorObjectDto> baseResponse = new BaseResponse<>(theErrorObject, new Meta(ReturnCode.FAILED_DATA_ALREADY_EXISTS.getStatusCode(), ReturnCode.FAILED_DATA_ALREADY_EXISTS.getMessage(), ""));
        return new ResponseEntity<>(baseResponse.getCustomizeResponse("error"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        ErrorObjectDto errorObject = new ErrorObjectDto();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimeStamp(new Date());
        BaseResponse<ErrorObjectDto> baseResponse = new BaseResponse<>(errorObject, new Meta(ReturnCode.FAILED_BAD_REQUEST.getStatusCode(), ReturnCode.FAILED_BAD_REQUEST.getMessage(), ""));
        return new ResponseEntity<>(baseResponse.getCustomizeResponse("error"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        ErrorObjectDto errorObject = new ErrorObjectDto();
        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimeStamp(new Date());
        BaseResponse<ErrorObjectDto> baseResponse = new BaseResponse<>(errorObject, new Meta(ReturnCode.FAILED_SERVER_INTERNAL_SERVER_ERROR.getStatusCode(), ex.getMessage(), ""));
        return new ResponseEntity<>(baseResponse.getCustomizeResponse("error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ResourceNotFoundException e, WebRequest request) {
        ErrorObjectDto dto = new ErrorObjectDto();
        dto.setMessage(e.getMessage());
        dto.setStatusCode(HttpStatus.NOT_FOUND.value());
        dto.setTimeStamp(new Date());
        BaseResponse<ErrorObjectDto> baseResponse = new BaseResponse<>(dto, new Meta(ReturnCode.FAILED_NOT_FOUND.getStatusCode(), e.getMessage(), ""));
        return new ResponseEntity<>(baseResponse.getCustomizeResponse("error"), HttpStatus.NOT_FOUND);
    }


    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    //     Map<String, Object> body = new LinkedHashMap<>();
    //     body.put("timestamp", new Date());
    //     body.put("statusCode", HttpStatus.BAD_REQUEST.value());

    //     List<String> errors = ex.getBindingResult()
    //             .getFieldErrors()
    //             .stream()
    //             .map(x -> x.getDefaultMessage())
    //             .collect(Collectors.toList());

    //     body.put("messages", errors);

    //     BaseResponse<Object> baseResponse = new BaseResponse<>(body.get("messages"), new Meta(ReturnCode.FAILED_BAD_REQUEST.getStatusCode(), ex.getMessage(), ""));
    //     return new ResponseEntity<>(baseResponse.getCustomizeResponse("error"), HttpStatus.NOT_FOUND);
    // }

}
