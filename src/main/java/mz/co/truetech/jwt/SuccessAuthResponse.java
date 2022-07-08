package mz.co.truetech.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import mz.co.truetech.dto.projection.PUserDTO;
import mz.co.truetech.entity.User;

@Data @AllArgsConstructor
public class SuccessAuthResponse {

	private String accessToken;
	private PUserDTO userData;
	
}
