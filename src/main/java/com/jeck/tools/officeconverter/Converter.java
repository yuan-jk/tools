package com.jeck.tools.officeconverter;

import org.jodconverter.JodConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeUtils;

import java.io.File;

public class Converter {

    public static void main(String[] args) throws OfficeException {
        File inputFile = new File("G:\\tmp\\设备类型对应关.xlsx");
        File outputFile = new File("G:\\tmp\\设备类型对应关.pdf");

// Create an office manager using the default configuration.
// The default port is 2002. Note that when an office manager
// is installed, it will be the one used by default when
// a converter is created.
        final LocalOfficeManager officeManager = LocalOfficeManager.install();
        try {

            // Start an office process and connect to the started instance (on port 2002).
            officeManager.start();

            // Convert
            JodConverter
                    .convert(inputFile)
                    .to(outputFile)
                    .execute();
        } finally {
            // Stop the office process
            OfficeUtils.stopQuietly(officeManager);
        }
    }

}
