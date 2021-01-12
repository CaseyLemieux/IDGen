package pdf;

import com.google.zxing.WriterException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import qrcode.QRGenerator;
import student.Student;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PDFAdapter {

    //Default Constructor
    public PDFAdapter(){

    }

    public void exportStudentPDF(Student student){
        String outFile = "C:\\Users\\clemieux\\Desktop\\ID GEN Program\\" + student.getLastName() + student.getFirstName() + "ID.pdf";
         try {
             QRGenerator generator = new QRGenerator();
             byte[] data = student.getIdPic();
             PDDocument document = PDDocument.load(data);
             byte[] qrData = generator.createQR(student.getQrCode(), 150, 150);
             ByteArrayInputStream inputStream = new ByteArrayInputStream(qrData);
             BufferedImage bufferedImage = ImageIO.read(inputStream);
             PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, bufferedImage);
             float width = bufferedImage.getWidth();
             float height = bufferedImage.getHeight();
             PDPage qrPage = new PDPage(new PDRectangle(width, height));
             document.addPage(qrPage);
             PDPageContentStream contentStream = new PDPageContentStream(document, qrPage);
             contentStream.drawImage(pdImageXObject, 0, 0);
             contentStream.close();
             document.save(outFile);
             document.close();
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }

    }

    public void exportGradeLevelPDF(ArrayList<Student> gradeLevel){

    }

    public PDDocument getStudentPDF(Student student){
        PDDocument document = null;
        try {
            byte[] idData = student.getIdPic();
            document = PDDocument.load(idData);
            if(!student.getQrCode().isEmpty()){
                QRGenerator generator = new QRGenerator();
                byte[] qrData = generator.createQR(student.getQrCode(), 150,150);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(qrData);
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, bufferedImage);
                float width = bufferedImage.getWidth();
                float height = bufferedImage.getHeight();
                PDPage qrPage = new PDPage(new PDRectangle(width, height));
                document.addPage(qrPage);
                PDPageContentStream contentStream = new PDPageContentStream(document, qrPage);
                contentStream.drawImage(pdImageXObject, 0, 0);
                contentStream.close();
            }
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
        return document;
    }

    public PDDocument getGradeLevelPDF(ArrayList<Student> students){
        PDDocument document = new PDDocument();
        return document;
    }

}
