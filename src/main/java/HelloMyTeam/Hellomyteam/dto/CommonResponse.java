package HelloMyTeam.Hellomyteam.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    private String status;
    private T data;
    private String message;

    public static <T> CommonResponse<T> createSuccess(T data) {
        return new CommonResponse<>(SUCCESS_STATUS, data, null);
    }

    public static <T> CommonResponse<T> createSuccess(T data, String message) {
        return new CommonResponse<>(SUCCESS_STATUS, data, message);
    }

//    public static CommonResponse<?> createSuccessWithNoContent() {
//        return new CommonResponse<>(SUCCESS_STATUS, null, null);
//    }

//    public static CommonResponse<?> createFail(BindingResult bindingResult) {
//        Map<String, String> errors = new HashMap<>();
//
//        List<ObjectError> allErrors = bindingResult.getAllErrors();
//        for (ObjectError error : allErrors) {
//            if (error instanceof FieldError) {
//                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
//            } else {
//                errors.put( error.getObjectName(), error.getDefaultMessage());
//            }
//        }
//        return new CommonResponse<>(FAIL_STATUS, errors, null);
//    }

    // 예외 발생으로 API 호출 실패시 반환
    public static CommonResponse<?> createError(String message) {
        return new CommonResponse<>(ERROR_STATUS, null, message);
    }

    private CommonResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
