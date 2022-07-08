package mz.co.truetech.resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.enums.Zone;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.service.CensusService;
import mz.co.truetech.service.FileUploadService;
import mz.co.truetech.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/api/v1/census")
@RequiredArgsConstructor
@ApiIgnore
public class UploadsController {
	
	private final FileUploadService fileUploadService;

	private final CensusService censusService;

	
	@PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<Integer, List<String>>>
			uploadFile(
					@RequestParam("file")MultipartFile file,
					@RequestParam("zone")Zone zone,
					@RequestParam("gender")Gender gender,
					@RequestParam("districtId")Long districtId,
					@RequestParam("year")Integer year
			) throws ApiRequestException {
		try {
			new FileUtil().createFolderIfDoenstExists();
			System.out.println(zone+" - "+gender+" - "+districtId+" - "+year);
			byte[] bytes = new byte[0];
			bytes = file.getBytes();
			Path path = Files.write(Paths.get(FileUtil.folderPath + file.getOriginalFilename()), bytes);
			Map<Integer, List<String>> data = fileUploadService.UploadFile(path.toString());

			censusService.saveMass(data, districtId, year, zone, gender);
			return ResponseEntity.status(HttpStatus.CREATED).body(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	

}
