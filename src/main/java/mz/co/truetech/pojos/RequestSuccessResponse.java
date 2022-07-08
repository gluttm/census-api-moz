package mz.co.truetech.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor @Data
public class RequestSuccessResponse {
    private final String msg;
    private final HttpStatus httpStatus;
}
