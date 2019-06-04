package com.hfp.util.common;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
	
	private final static String excel2003L =".xls";		//2003- 版本的excel 
	private final static String excel2007U =".xlsx";	//2007+ 版本的excel 
	
	/**
	 * 生成Excel文件
	 */
    public static Workbook doExcel(String[] titles,
                                   String[] keys,
                                   List<Map<String,Object>> datas){
        Workbook wb=new HSSFWorkbook();
        Sheet sheet= wb.createSheet();
        
        //标题行
        Row row=sheet.createRow(0);
        for (int i=0;i<titles.length;i++){
            Cell cell=row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        
        //开始遍历内容
        for (int j=0;j<datas.size();j++){
            Row datarow=sheet.createRow(j+1);
            Map<String,Object> data=datas.get(j);
            for (int k=0;k<keys.length;k++) {
                Cell datacell=datarow.createCell(k);
                datacell.setCellValue(data.get(keys[k])==null?"":data.get(keys[k]).toString());
            }
        }
        return wb;
    }
    
    public static byte[] doExcel(List<Map<String, Object>> datas, String[] title, String[] key, String fileName)
			throws UnsupportedEncodingException, IOException {
		String titles[] = title;
		String keys[] = key;
		Workbook wb = ExcelUtils.doExcel(titles, keys, datas);
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		wb.write(outByteStream);
		return outByteStream.toByteArray();
		//String datetime = DateUtil.getSdfTimes();
		//String fullFileName = fileName + datetime + ".xls";// 中文文件名乱码
		//HttpHeaders headers = new HttpHeaders();
		//headers.setContentDispositionFormData("attachment", new String(fullFileName.getBytes("utf-8"), "ISO8859-1"));
		//headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		//return new ResponseEntity<byte[]>(outByteStream.toByteArray(), headers, HttpStatus.OK);
	}
    
    public static void genExcelFile(String file, String[] titles, String[] keys, List<Map<String, Object>> datas) throws IOException{
    	Workbook workbook = doExcel(titles, keys, datas);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
    }
    
    private static Workbook getWorkbook(String fileName, InputStream is) throws Exception{

   	 	Workbook wb = null;
   	 	String fileType = fileName.substring(fileName.lastIndexOf("."));

		if(excel2003L.equals(fileType)){
			wb = new HSSFWorkbook(is); //2003-
		}else if(excel2007U.equals(fileType)){
			wb = new XSSFWorkbook(is); //2007+
		}else{
			throw new Exception("解析的文件格式有误！");
		}

   	 	return wb;
	}
    
    private static Object getCellValue(Cell cell){
    	Object value = null;
    	DecimalFormat df = new DecimalFormat("0");                //格式化number String字符
    	SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); //日期格式化
    	DecimalFormat df2 = new DecimalFormat("0.00");            //格式化数字

    	CellType cellType = cell.getCellType();
    	if(cellType == CellType.STRING){
    		value = cell.getRichStringCellValue().getString();
    	}else if(cellType == CellType.NUMERIC){
	    	 if("General".equals(cell.getCellStyle().getDataFormatString())){
	    		 value = df.format(cell.getNumericCellValue());
	    	 }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
	    		 value = sdf.format(cell.getDateCellValue());
	    	 }else{
	    		 value = df2.format(cell.getNumericCellValue());
	    	 }
    	}else if(cellType == CellType.BOOLEAN){
    		value = cell.getBooleanCellValue();
    	}else if(cellType == CellType.BLANK){
    		value = "";
    	}

    	return value;
	}
    
    public static List<Map<String, Object>> readExcel(String fileName) throws Exception{
   	 	FileInputStream fileInputStream = new FileInputStream(fileName);
   	 	Workbook wb = getWorkbook( fileName, fileInputStream);
	   	if (null == wb) {
	   		throw new Exception("创建Excel工作薄为空！");
	    }
        List<Map<String,Object>> sheetList = new ArrayList<>();
        

        //遍历Excel中所有的sheet
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
        	Sheet sheet = wb.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            
            Map<String,Object> sheetMap = new HashMap<>();
            sheetMap.put("sheetName", sheet.getSheetName());
            sheetMap.put("sheetIndex", i);

            //遍历当前sheet中的所有行
            for (int j =0; j <= sheet.getLastRowNum(); j++) {
            	Row row = sheet.getRow(j);
                if (row == null ) {
                    continue;
                }
                
                Map<String,String> rowMap = new HashMap<>();

                Iterator<Cell> iter = row.iterator();
                int index=0;
                while(iter.hasNext()){
                	Cell cell = iter.next();
                	rowMap.put(String.valueOf(index), String.valueOf(getCellValue(cell)));
                	index++;
                }
                sheetMap.put(String.valueOf(j), rowMap);
            }
            
            sheetList.add(sheetMap);
        }

   	 	fileInputStream.close();
   	 	return sheetList;
    }

    
    public static void main(String[] args) throws Exception{
    	/*
    	List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
    	Map<String,Object> map1 = new HashMap<>();
    	map1.put("field1", "单号1");
    	map1.put("field2", "结算方式1");
    	datas.add(map1);
    	String titles[]={ "结算订单号", "结算方式"};
		String keys[]={ "field1", "field2"};
		genExcelFile("G://tmp.xls", titles, keys, datas);
		*/
    	
    	/*
    	List<Map<String, Object>> list = readExcel("G://tmp.xls");
    	for(int i=0; i<list.size();i++){
    		Map<String, Object> sheetMap = list.get(i);
    		System.out.println("sheetIndex: "+sheetMap.get("sheetIndex").toString());
    		System.out.println("sheetName: "+sheetMap.get("sheetName").toString());
    		if(sheetMap.size()>=3){
    			System.out.println("rowTitle: "+sheetMap.get("0").toString());
    		}
    		if(sheetMap.size()>=4){
    			for(int j=1;j<=sheetMap.size()-3;j++){
    				System.out.println("row "+j+": "+sheetMap.get(String.valueOf(j)).toString());
    			}
    		}
    	}
    	*/
    }
}
