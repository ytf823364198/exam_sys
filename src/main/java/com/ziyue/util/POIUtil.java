package com.ziyue.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 
public class POIUtil {
    private int totalRows = 0; 
    private int totalCells = 0; 

    public List<ArrayList<String>> read(String fileName) { 
        List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>(); 
        if (fileName == null || !fileName.matches("^.+\\.(?i)((xls)|(xlsx))$")) { 
            return dataLst; 
        } 
        
        boolean isExcel2003 = true; 
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) { 
            isExcel2003 = false; 
        } 

        File file = new File(fileName); 
        if (file == null || !file.exists()) { 
            return dataLst; 
        } 
        
        try { 
            dataLst = read(new FileInputStream(file), isExcel2003); 
        }catch (Exception ex) { 
            ex.printStackTrace(); 
        } 
        return dataLst; 
    } 
    
    
    public List<ArrayList<String>> read(InputStream inputStream, boolean isExcel2003) { 
        List<ArrayList<String>> dataLst = null; 
        try {      
            Workbook wb = isExcel2003 ? new HSSFWorkbook(inputStream)   : new XSSFWorkbook(inputStream); 
            dataLst = read(wb); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return dataLst; 
    } 
    
    
    public int getTotalRows() { 
        return totalRows; 
    } 

    public int getTotalCells() { 
        return totalCells; 
    } 

    @SuppressWarnings("deprecation")
	private List<ArrayList<String>> read(Workbook wb) { 
        List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>(); 
        Sheet sheet = wb.getSheetAt(0); 
        this.totalRows = sheet.getPhysicalNumberOfRows(); 
        if (this.totalRows >= 1 && sheet.getRow(0) != null)  { 
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells(); 
        } 
        
        for (int r = 1; r < this.totalRows; r++) { 
            Row row = sheet.getRow(r); 
            if (row == null)  { 
                continue; 
            } 
            ArrayList<String> rowLst = new ArrayList<String>(); 
            for (short c = 0; c < this.getTotalCells(); c++) { 
                Cell cell = row.getCell(c); 
                String cellValue = ""; 
                if (cell == null) { 
                    rowLst.add(cellValue); 
                    continue; 
                } 
                switch (cell.getCellType()) {  
	                case 0 : //case Cell.CELL_TYPE_NUMERIC: // 数字
		                DecimalFormat df = new DecimalFormat("#0.##");  
	                    cellValue = df.format(cell.getNumericCellValue()); 
	                break;  
	                case 1 : //Cell.CELL_TYPE_STRING: // 字符串  
	                    cellValue = cell.getStringCellValue();  
	                break;  
	
	                case 4: // Cell.CELL_TYPE_BOOLEAN: // Boolean  
	                    cellValue = cell.getBooleanCellValue() + "";  
	                break;  
	
	                case 2: //Cell.CELL_TYPE_FORMULA: // 公式  
	                    cellValue = cell.getCellFormula() + "";  
	                break;  
	
	                case 3 ://Cell.CELL_TYPE_BLANK: // 空值  
	                    cellValue = "";  
	                break;  
	
	                case 5 :// Cell.CELL_TYPE_ERROR: // 故障  
	                    cellValue = "非法字符";  
	                break;  
	
	                default:  
	                    cellValue = "未知类型";  
	                break;   
	            }
                rowLst.add(cellValue.trim()); 
            } 
            dataLst.add(rowLst); 
        } 
        return dataLst; 
    } 
    
    public static  CellStyle BaseStyle(Workbook workbook) {
    	CellStyle cellStyle = workbook.createCellStyle(); 
    	//水平居中  
    	cellStyle.setAlignment(HorizontalAlignment.CENTER); 
    	//垂直居中
    	cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); 
    	cellStyle.setBorderBottom(BorderStyle.THIN);
    	cellStyle.setBorderLeft(BorderStyle.THIN);
    	cellStyle.setBorderRight(BorderStyle.THIN);
    	cellStyle.setBorderTop(BorderStyle.THIN);
    	//自动换行
    	cellStyle.setWrapText(true);
    	
    	return cellStyle;
    }
    
    public static CellStyle HeadStyle(Workbook workbook) {
    	CellStyle cellStyle = BaseStyle(workbook);
    	cellStyle.setFillForegroundColor((short) 70);
    	cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    	return cellStyle;
    }
    
    public static CellStyle RightStyle(Workbook workbook) {
    	CellStyle cellStyle = BaseStyle(workbook);
    	cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
    	return cellStyle;
    }
    
//    public static CellStyle [] getStyles(Workbook workbook) {
//    	return new CellStyle []{ 
//    		HeadStyle(workbook),
//    		BaseStyle(workbook),
//    		RightStyle(workbook)
//    	}; 
//    }
//    
    public static Map<String,CellStyle> getStyles(Workbook workbook) {
    	Map<String ,CellStyle > styles = new HashMap<String ,CellStyle >();
    	styles.put("base", BaseStyle(workbook));
    	styles.put("right", RightStyle(workbook));
    	return styles;
    }
  
	public static void setCell(XSSFRow row , int rowNo , String value ,CellStyle style) {
		Cell cell = row.createCell(rowNo, CellType.STRING);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static void setCell(XSSFRow row , int rowNo , int value ,CellStyle style) {
		Cell cell = row.createCell(rowNo, CellType.STRING);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static void setCell(XSSFRow row , int rowNo , Double value ,CellStyle style) {
		Cell cell = row.createCell(rowNo, CellType.STRING);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static void setCell(XSSFRow row , int rowNo , Date value ,CellStyle style) {
		Cell cell = row.createCell(rowNo, CellType.STRING);
		cell.setCellValue(DateUtil.toDateStr(value));
		cell.setCellStyle(style);
	}
	
	public static void closeStream(XSSFWorkbook workBook, FileOutputStream outputStream) {
	    try {
			workBook.close();
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void creatHead(XSSFWorkbook workBook,XSSFRow row,XSSFSheet sheet,String[] titles  ) {
		row = sheet.createRow(0);
		row.setHeight((short) (25 * 20));
		
		for(int i = 0; i< titles.length; i++ ) {
			String[] head = titles[i].split(":");
			//设置列宽
			sheet.setColumnWidth(i,256 * Integer.valueOf(head[1]) );
			setCell(row, i, head[0], HeadStyle(workBook));
		}	
	}
	
	public static void creatHead(XSSFWorkbook workBook,XSSFRow row,XSSFSheet sheet,String[] titles , int width ) {
		row = sheet.createRow(0);
		row.setHeight((short) (25 * 20));
		
		for(int i = 0; i< titles.length; i++ ) {
			//设置列宽
			sheet.setColumnWidth(i,256 * width);
			setCell(row, i, titles[i], HeadStyle(workBook));
		}	
	}

}
