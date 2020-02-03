package com.jeck.tools.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class PdfUtil {

    public static void main(String[] args) throws IOException {

        String inputFileName = "D:\\tmp\\pdf\\深入理解Java虚拟机：JVM高级特性与最佳实践（最新第二版）.pdf";

        String outputFileName = "D:\\tmp\\pdf\\深入理解Java虚拟机：JVM高级特性与最佳实践.pdf";

        PDDocument doc = PDDocument.load(new File(inputFileName));

        int numPages = doc.getNumberOfPages();
        System.out.println("number of pages: " + numPages);

        doc.removePage(0);
        doc.removePage(1);
        doc.removePage(2);

        PDDocument docOutput = new PDDocument();
        for (int i = 2; i < 453; i++) {
            docOutput.addPage(doc.getPage(i));
        }
        docOutput.save(outputFileName);
        docOutput.close();

        doc.close();
    }

}
