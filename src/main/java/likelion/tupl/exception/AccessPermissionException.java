package likelion.tupl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 사용자가 해당 URI에 접근할 권한이 없을 때 발생하는 exception
// 403 error: 클라이언트가 요청한 컨텐츠에 대해 접근할 권리가 없음 (신원 인증은 되었지만 권한은 없음)
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AccessPermissionException extends  RuntimeException {
    private static final Integer serialVersionUID = 2;

    public AccessPermissionException(String message) {
        super(message);
    }
}
