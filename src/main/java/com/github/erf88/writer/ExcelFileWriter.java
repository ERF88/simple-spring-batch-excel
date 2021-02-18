package com.github.erf88.writer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;

import com.github.erf88.dto.CustomerDTO;

public class ExcelFileWriter implements ItemStreamWriter<CustomerDTO> {

	private HSSFWorkbook workbook;
	private WritableResource resource;
	private Integer row;

	public ExcelFileWriter(String pathOut) {
		this.resource = new FileSystemResource(pathOut);
	}

	@Override
	public void write(List<? extends CustomerDTO> items) throws Exception {

        HSSFSheet sheet = workbook.getSheetAt(0);

        for (CustomerDTO customer : items) {
            Row r = sheet.createRow(row++);
            Cell cell = r.createCell(0);
            cell.setCellValue(customer.getName());

            cell = r.createCell(1);
            cell.setCellValue(customer.getEmail());
            
            cell = r.createCell(2);
            cell.setCellValue(customer.getProcessedAt());
            
            cell = r.createCell(3);
            cell.setCellValue(customer.getStatus());
        }

	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		workbook = new HSSFWorkbook();
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFSheet sheet = workbook.createSheet();

		row = 0;
		createTitleRow(sheet, palette);
		createHeaderRow(sheet);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws ItemStreamException {
        if (workbook == null) {
            return;
        }

        try (BufferedOutputStream output = new BufferedOutputStream(resource.getOutputStream())) {
            workbook.write(output);
            output.flush();
            workbook.close();
        } catch (IOException ex) {
            throw new ItemStreamException("Error writing to output file", ex);
        }
        row = 0;

	}

	private void createTitleRow(HSSFSheet sheet, HSSFPalette palette) {
		HSSFColor color = palette.findSimilarColor((byte) 0xE6, (byte) 0x50, (byte) 0x32);
		palette.setColorAtIndex(color.getIndex(), (byte) 0xE6, (byte) 0x50, (byte) 0x32);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setWrapText(true);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFillForegroundColor(color.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);

		HSSFRow r = sheet.createRow(row);

		Cell cell = r.createCell(0);
		cell.setCellValue("Customers Processed");
		r.createCell(1).setCellStyle(headerStyle);
		r.createCell(2).setCellStyle(headerStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
		cell.setCellStyle(headerStyle);

		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

		row++;
	}

	private void createHeaderRow(HSSFSheet sheet) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);

		HSSFRow r = sheet.createRow(row);
		r.setRowStyle(cellStyle);

		Cell cell = r.createCell(0);
		cell.setCellValue("Name");
		sheet.setColumnWidth(0, poiWidth(24.0));
		cell = r.createCell(1);
		
		cell.setCellValue("Email");
		sheet.setColumnWidth(1, poiWidth(24.0));
		cell = r.createCell(2);
		
		cell.setCellValue("Processed At");
		sheet.setColumnWidth(2, poiWidth(18.0));
		cell = r.createCell(3);
		
		cell.setCellValue("Status");
		sheet.setColumnWidth(3, poiWidth(18.0));
		cell = r.createCell(4);

		row++;
	}

	private int poiWidth(double width) {
		return (int) Math.round(width * 256 + 200);
	}

}
