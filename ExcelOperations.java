package utilities;


import genericConstants.GenericApplicationConstants;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

public class ExcelDataOperations {
	public  String path = System.getProperty("user.dir")+"\\TestData\\"+ GenericApplicationConstants.EXCEL_DATA_INPUT_FILE+GenericApplicationConstants.EXCEL_FILE_EXTENSION;
	public  FileInputStream fis = null;
	public  FileOutputStream fileOut =null;

	/*for 2007-2011 workbooks
	private HSSFWorkbook workbook = null;
	private HSSFSheet sheet = null;
	private HSSFRow row   =null;
	private HSSFCell cell = null;*/

	//for 2003-2007 workbooks
	private HSSFWorkbook workbook = null;
	private HSSFSheet sheet = null;
	private HSSFRow row   =null;
	private HSSFCell cell = null;


	/**************************************************************************
	 * Function to Read Excel Data 
	 **************************************************************************/

	public void excel_Reader(String path) {		

		this.path = path;		

		try {
			fis = new FileInputStream(path);
			workbook = new HSSFWorkbook(fis);
			//sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

	/**************************************************************************
	 * Function to Read entire Row Data and put this into an ArrayList
	 **************************************************************************/

	
	public Map retrieveRowData(String sheetName, String columnName, String cellValue){		

		Map<String, Map<String,String>> DetailsMap  = new HashMap<String, Map<String,String>>();		


		try{					
			excel_Reader(path);

			int index = workbook.getSheetIndex(sheetName);

			if(!(index==-1))
			{
				sheet = workbook.getSheetAt(index);

				int colCount = getColumnNumber(columnName);				

				int rowHeaderCount = sheet.getFirstRowNum();				

				HSSFRow headerRowData = sheet.getRow(rowHeaderCount);

				List<String> rowCount = getRowNumber(colCount,cellValue);					

				if(rowCount.size()>0)
				{	
					for(int i=0; i<rowCount.size(); i++)
					{	
						Map<String, String> myDataMap = new HashMap<String,String>();

						HSSFRow rowData = sheet.getRow(Integer.parseInt(rowCount.get(i)));

						Iterator<Cell> cellIterator = rowData.cellIterator();

						Iterator<Cell> headerCellIterator = headerRowData.cellIterator();						

						while(cellIterator.hasNext())	
						{	
							HSSFCell excelCell = (HSSFCell) cellIterator.next();	

							HSSFCell headerCell = (HSSFCell) headerCellIterator.next();					

							if(excelCell.getCellType()==excelCell.CELL_TYPE_BLANK)
							{
								String cellText = " ";
								if(headerCell.getCellType()==headerCell.CELL_TYPE_NUMERIC)
								{
									
									myDataMap.put(String.valueOf(headerCell.getNumericCellValue()).replace(".0", "").trim(), cellText);
								}	else if(headerCell.getCellType()==headerCell.CELL_TYPE_FORMULA)
								{
									
									myDataMap.put(String.valueOf(headerCell.getCellFormula()), cellText);
								}else if(headerCell.getCellType()==headerCell.CELL_TYPE_STRING)
								{
									
									myDataMap.put(String.valueOf(headerCell.getStringCellValue()), cellText);
								}
							}

							if(excelCell.getCellType()==excelCell.CELL_TYPE_NUMERIC)
							{								
								String cellText;							
								if (HSSFDateUtil.isCellDateFormatted(excelCell)) {
									/**** format in form of M/D/YY *****/
									double d = excelCell.getNumericCellValue();

									Calendar cal =Calendar.getInstance();

									cal.setTime(HSSFDateUtil.getJavaDate(d));



									cellText = (String.valueOf(cal.get(Calendar.YEAR)));

									int value = Integer.parseInt(String.valueOf(cal.get(Calendar.MONTH))) +1;



									cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" +
											String.valueOf(Integer.parseInt(String.valueOf(cal.get(Calendar.MONTH))) +1)+ "/" + 
											cellText;								
								}else{
									cellText =String.valueOf(excelCell.getNumericCellValue()).replace(".0", "").trim();
								}



								if(headerCell.getCellType()==headerCell.CELL_TYPE_NUMERIC)
								{
									
									myDataMap.put(String.valueOf(headerCell.getNumericCellValue()).replace(".0", "").trim(), cellText);
								}	else if(headerCell.getCellType()==headerCell.CELL_TYPE_FORMULA)
								{
									
									myDataMap.put(String.valueOf(headerCell.getCellFormula()), cellText);
								}else if(headerCell.getCellType()==headerCell.CELL_TYPE_STRING)
								{
									
									myDataMap.put(headerCell.getStringCellValue(), cellText);
								}			    
							}	



							if(excelCell.getCellType()==excelCell.CELL_TYPE_STRING)
							{
								if(headerCell.getCellType()==headerCell.CELL_TYPE_NUMERIC)
								{
									
									myDataMap.put(String.valueOf(headerCell.getNumericCellValue()).replace(".0", "").trim(), excelCell.getStringCellValue());
								}	else if(headerCell.getCellType()==headerCell.CELL_TYPE_FORMULA)
								{
									
									myDataMap.put(String.valueOf(headerCell.getCellFormula()), excelCell.getStringCellValue());
								}else if(headerCell.getCellType()==headerCell.CELL_TYPE_STRING)
								{
									
									myDataMap.put(String.valueOf(headerCell.getStringCellValue()), excelCell.getStringCellValue());
								}
							}

							if( excelCell.getCellType()==excelCell.CELL_TYPE_FORMULA){

								if(headerCell.getCellType()==headerCell.CELL_TYPE_NUMERIC)
								{
									
									myDataMap.put(String.valueOf(headerCell.getNumericCellValue()).replace(".0", "").trim(), String.valueOf(excelCell.getCellFormula()));
								}	else if(headerCell.getCellType()==excelCell.CELL_TYPE_FORMULA)
								{
									
									myDataMap.put(String.valueOf(headerCell.getCellFormula()), String.valueOf(excelCell.getCellFormula()));
								}else if(headerCell.getCellType()==headerCell.CELL_TYPE_STRING)
								{
									
									myDataMap.put(String.valueOf(headerCell.getStringCellValue()), String.valueOf(excelCell.getCellFormula()));
								}
							}						
						}

						DetailsMap.put(String.valueOf(i+1),myDataMap);
					}
				}	
			}			
		}catch(Exception e)
		{
			e.printStackTrace();
		}			
		return DetailsMap;		
	}

	/**************************************************************************
	 * Function to Find Row Number 
	 **************************************************************************/

	@SuppressWarnings("null")
	private ArrayList<String> getRowNumber(int columnNumber, String cellValue) {

		ArrayList<String> rowNumber = new ArrayList<String>();
		try{		
			int lastrow = sheet.getLastRowNum();	
			int i=0;

			for(int rowIndex=1; rowIndex<=lastrow; rowIndex++)
			{
				HSSFCell rowDataCell = sheet.getRow(rowIndex).getCell(columnNumber);				

				if(!(rowDataCell.getCellType()==rowDataCell.CELL_TYPE_BLANK))
				{		

					if(rowDataCell.getCellType()==rowDataCell.CELL_TYPE_NUMERIC || rowDataCell.getCellType()==rowDataCell.CELL_TYPE_FORMULA)
					{							
						if(String.valueOf(rowDataCell.getNumericCellValue()).replace(".0", "").trim().equalsIgnoreCase(cellValue))
						{						
							rowNumber.add(i, String.valueOf(rowIndex));
							i=i+1;					
						}
					}			

					if(rowDataCell.getCellType()==rowDataCell.CELL_TYPE_STRING)
					{
						if(rowDataCell.getStringCellValue().equalsIgnoreCase(cellValue)){					
							rowNumber.add(i,String.valueOf(rowIndex));
							i=i+1;					
						}
					}
				}			
			}

		}catch(Exception e){
			e.printStackTrace();			
		}			

		return rowNumber;
	}

	/**************************************************************************
	 * Function to Find Column Number 
	 **************************************************************************/
	private int getColumnNumber(String columnName) {

		int columnCount = 0;
		try{
			int rowNum = sheet.getTopRow();

			HSSFRow headerRow = sheet.getRow(rowNum);

			short firstCol =  headerRow.getFirstCellNum();

			short lastCol = headerRow.getLastCellNum();

			for(int i=Integer.parseInt(Short.toString(firstCol)); i<Integer.parseInt(Short.toString(lastCol)); i++)
			{
				@SuppressWarnings("deprecation")
				HSSFCell excelCellData = headerRow.getCell(i);						

				if(excelCellData.getStringCellValue().equalsIgnoreCase(columnName))
				{
					columnCount=i;
					break;
				}
			}		

		}catch(Exception e){

			e.printStackTrace();			
		}
		return columnCount;
	}	

	/**************************************************************************
	 * Function to Read entire Header Row Data and put this into an ArrayList
	 **************************************************************************/

	@SuppressWarnings("unchecked")
	public ArrayList retrieveHeaderRowData(String sheetName)
	{
		ArrayList excelHeaderRowData = new ArrayList();

		try{
			excel_Reader(path);

			int index = workbook.getSheetIndex(sheetName);

			if(!(index==-1))
			{
				sheet = workbook.getSheetAt(index);			

				int rowCount = sheet.getFirstRowNum();			

				HSSFRow rowData = sheet.getRow(rowCount);

				Iterator<Cell> cellIterator = rowData.cellIterator();

				while(cellIterator.hasNext())
				{	
					Cell excelCell = cellIterator.next();	

					excelHeaderRowData.add(String.valueOf(excelCell.getStringCellValue()));				
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return excelHeaderRowData;		
	}
}
