package mz.co.truetech.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class FileUploadService {

	public Map<Integer, List<String>> UploadFile(String path) throws IOException {
		FileInputStream file = new FileInputStream(path);
		Workbook workbook = new XSSFWorkbook(file);
		
		Sheet sheet = workbook.getSheetAt(0);
		
		Map<Integer, List<String>> data = new HashMap<>();
		
		Integer i = 0;
		for (Row row : sheet) {
			if (i > 0) {
				data.put(i, new ArrayList<String>());
				for (Cell cell: row) {
					switch (cell.getCellType()) {
					case STRING:
						data.get(i).add(cell.getRichStringCellValue().getString());
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							data.get(i).add(cell.getDateCellValue() + "");
						} else {
							data.get(i).add(cell.getNumericCellValue() + "");
						}
					default:
						//data.get(i).add(" ");
						break;
					}
				}
			}
			i++;
		}
		workbook.close();
		System.out.println(data.toString());
		return data;
	}
}
