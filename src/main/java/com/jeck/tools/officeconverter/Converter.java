package com.jeck.tools.officeconverter;

import org.jodconverter.JodConverter;
import org.jodconverter.LocalConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Converter {


    public static void converterWithFilter(File inputFile, File outputFile) throws OfficeException {
        // File inputFile = new File("document.rtf");
        // File outputFile = new File("document.pdf");
        final LocalOfficeManager officeManager =
                LocalOfficeManager.builder().processTimeout(20 * 60 * 1000L).taskExecutionTimeout(20 * 60 * 1000L).install().build();
        officeManager.start();

        Map<String, Object> filterData = new HashMap<>();
        filterData.put("PageRange", "2000");
        Map<String, Object> customProperties = new HashMap<>();
        customProperties.put("FilterData", filterData);

        LocalConverter
                .builder()
                .storeProperties(customProperties)
                .build()
                .convert(inputFile)
                .to(outputFile)
                .execute();

    }

    public static void converterWithTimeout(File inputFile, File outputFile) {
        // File inputFile = new File("C:\\Users\\yuanjk\\Downloads\\文本_临空经济区修改0614最终2 - 副本 (2).pptx");
        // File outputFile = new File("G:\\tmp\\doc_convertor\\文本_临空经济区修改0614最终2 - 副本 (2).pdf");

// Create an office manager using the default configuration.
// The default port is 2002. Note that when an office manager
// is installed, it will be the one used by default when
// a converter is created.
//         final LocalOfficeManager officeManager = LocalOfficeManager.install();
        final LocalOfficeManager officeManager =
                LocalOfficeManager.builder().processTimeout(20 * 60 * 1000L).taskExecutionTimeout(20 * 60 * 1000L).install().build();
        try {

            // Start an office process and connect to the started instance (on port 2002).
            officeManager.start();

            // Convert
            JodConverter
                    .convert(inputFile)
                    .to(outputFile)
                    .execute();
        } catch (OfficeException e) {
            e.printStackTrace();
        } finally {
            // Stop the office process
            OfficeUtils.stopQuietly(officeManager);
        }
    }

    public static void main(String[] args) throws OfficeException {
        File inputFile = new File("C:\\Users\\yuanjk\\Downloads\\文本_临空经济区修改0614最终2 - 副本 (2).pptx");
        File outputFile = new File("G:\\tmp\\doc_convertor\\文本_临空经济区修改0614最终2 - 副本 (2).pdf");
        converterWithFilter(inputFile, outputFile);
    }

}
