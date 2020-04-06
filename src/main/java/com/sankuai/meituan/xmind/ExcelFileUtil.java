package com.sankuai.meituan.xmind;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guokaiqiang on 2018/5/23.
 */
public class ExcelFileUtil {

	private static final int EXCEL_CELL_MAX_LENGTH = 32767;

	/**
	 * 读取excel中数据 fileName：完整文件路径 sheetNum：读取excel中第几个表格
	 */
	public static List<List<String>> readExcleFileSheet(File file, String sheetName) {
		Workbook readwb = null;
		List<List<String>> alldataList = null;
		InputStream instream = null;

		try {
			alldataList = new ArrayList<List<String>>();
			instream = new FileInputStream(file);
			readwb = getWorkBook(file, instream);
			Sheet readsheet = readwb.getSheet(sheetName);
			for (Row row : readsheet) {
				List<String> cells = readRowContents(row);
				if (cells != null) {
					alldataList.add(cells);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(readwb);
			IOUtils.closeQuietly(instream);
		}
		return alldataList;
	}

	/**
	 * 读取excel中数据 fileName：完整文件路径 sheetNum：读取excel中第几个表格
	 */
	public static List<List<String>> readExcleFileSheet(File file, int sheetNum) {
		Workbook readwb = null;
		List<List<String>> alldataList = null;
		InputStream instream = null;

		try {
			alldataList = new ArrayList<List<String>>();
			instream = new FileInputStream(file);
			readwb = getWorkBook(file, instream);
			Sheet readsheet = readwb.getSheetAt(sheetNum);
			for (Row row : readsheet) {
				List<String> cells = readRowContents(row);
				if (cells != null && cells.size() > 0) {
					alldataList.add(cells);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(readwb);
			IOUtils.closeQuietly(instream);
		}
		return alldataList;
	}

	public static void writeExcelFile(File file, List<String> header, List<List<String>> data) {
		writeExcelFile(file, null, header, data);
	}

	public static void updateCell(File file, String sheetName, int columnNum, int rowNum, String content) {
		Workbook wwb = null;
		OutputStream os = null;
		InputStream is = null;

		try {
			is = new FileInputStream(file);
			wwb = getWorkBook(file, is);
			Sheet sheet = wwb.getSheet(sheetName);
			setCellValue(sheet, columnNum, rowNum, content);
			os = new FileOutputStream(file);
			wwb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(wwb);
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}
	}

	public static void updateCell(File file, int sheetIndex, int columnNum, int rowNum, String content) {
		Workbook wwb = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			wwb = getWorkBook(file, is);
			Sheet sheet = wwb.getSheetAt(sheetIndex);
			setCellValue(sheet, columnNum, rowNum, content);
			os = new FileOutputStream(file);
			wwb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(wwb);
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}
	}

	public static void writeExcelFile(File file, String sheetName, List<String> header, List<List<String>> data) {
		Workbook wwb = null;
		OutputStream os = null;
		try {
			if (file.getParentFile() != null){
				FileUtils.forceMkdir(file.getParentFile());
			}
			os = new FileOutputStream(file);
			wwb = getWorkBook(file);
			Sheet sheet = null;
			if (StringUtils.isBlank(sheetName)) {
				sheet = wwb.createSheet();
			} else {
				sheet = wwb.createSheet(sheetName);
			}

			if (header != null) {
				Row row = sheet.createRow(0);
				for (int i = 0; i < header.size(); i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(header.get(i));
				}
			}

			if (data != null) {
				for (int i = 1; i <= data.size(); i++) {
					List<String> line = data.get(i - 1);
					Row row = sheet.createRow(i);
					for (int j = 0; j < line.size(); j++) {
						Cell cell = row.createCell(j);
						cell.setCellValue(checkExcelCellLength(line.get(j), row.getRowNum(), cell.getColumnIndex()));
					}
				}
			}
			wwb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(wwb);
			IOUtils.closeQuietly(os);
		}
	}

	private static void setCellValue(Sheet sheet, int columnNum, int rowNum, String content) {
		Row row = sheet.getRow(rowNum);
		Cell cell = row.getCell(columnNum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(content);
	}

	private static List<String> readRowContents(Row row) {
		List<String> cellList = new ArrayList<>();
		int blankCount = 0;
		for (int cn = 0; cn < row.getLastCellNum(); cn++) {
			Cell cell = row.getCell(cn, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			CellType cellType = cell.getCellTypeEnum();
			if (CellType.STRING != cellType) {
				cell.setCellType(CellType.STRING);
			}
			String content = cell.getStringCellValue();
			if (cn == 0 && content.startsWith("#")) {
				return null;
			}
			if (StringUtils.isBlank(content)) {
				blankCount++;
			}
			cellList.add(content);
		}
		if (blankCount == row.getLastCellNum()) {
			return null;
		}
		return cellList;
	}

	private static Workbook getWorkBook(File file, InputStream is) throws IOException {
		Workbook wb = file.getName().endsWith("xls") ? new HSSFWorkbook(is) : new XSSFWorkbook(is);
		return wb;
	}

	private static Workbook getWorkBook(File file) {
		String fileName = file.getName();
		Workbook wb = fileName.endsWith("xls") ? new HSSFWorkbook() : new XSSFWorkbook();
		wb.setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);
		return wb;
	}

	public static String checkExcelCellLength(String content, int rowNum, int colNum) {
		if (content == null) {
			return content;
		}
		if (content.length() > EXCEL_CELL_MAX_LENGTH) {
			System.err.println(String.format("Content of cell [%s][%s] is too long, please fill it manually!",
					rowNum, colNum));
			System.out.println("The content is:");
			System.out.println(content);
			return "Content too long...";
		}
		return content;
	}

}
