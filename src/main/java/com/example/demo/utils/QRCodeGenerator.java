package com.example.demo.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    public static String generateQRCode(String data, String fileName) {

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 250, 250);

            String folder = "src/main/resources/com/example/demo/qr/";
            File dir = new File(folder);
            if (!dir.exists()) dir.mkdirs();

            String filePath = folder + fileName + ".png";
            Path path = FileSystems.getDefault().getPath(filePath);

            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            return filePath;

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
