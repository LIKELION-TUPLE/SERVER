package likelion.tupl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 존재하지 않은 객체에 접근하려고 할 때 발생하는 exception
// 404 error: 요청한 URI를 찾을 수 없음
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final Integer serialVersionUID = 1;
    public ResourceNotFoundException(String message) {
        super(message);
    }
}