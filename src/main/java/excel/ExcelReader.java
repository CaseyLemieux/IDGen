package excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import student.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelReader {

    private File excelFile;
    private FileInputStream inputStream;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public boolean openFile(File path){
        this.excelFile = path;
        try{
            inputStream = new FileInputStream(excelFile);
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public ArrayList<Student> getStudents(){
        ArrayList<Student> students = new ArrayList<>();
        Iterator<Row> iterator = sheet.iterator();
        //Iterate through rows
        while(iterator.hasNext()){
            Row row = iterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            if(row.getRowNum() == 0){
                //Do nothing these are column headers
            }
            else{
                //Iterate through columns
                Student student = new Student();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    CellAddress address = new CellAddress(cell);
                    switch (cell.getCellType()){
                        case STRING:
                            int col = address.getColumn();
                            switch (col){
                                case 1:
                                    student.setUserName(cell.getStringCellValue());
                                    break;
                                case 2:
                                    student.setEmail(cell.getStringCellValue());
                                    break;
                                case 3:
                                    student.setDisplayName(cell.getStringCellValue());
                                    break;
                                case 4:
                                    student.setQrCode(cell.getStringCellValue());
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }
                students.add(student);

            }


        }
        return students;
    }
}
