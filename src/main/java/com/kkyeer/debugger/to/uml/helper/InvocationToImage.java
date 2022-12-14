package com.kkyeer.debugger.to.uml.helper;

import com.kkyeer.debugger.to.uml.model.InvokeChain;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 22:57 2022/12/12
 * @Modified By:
 */
public class InvocationToImage {
    private static final ImageType imageType = ImageType.PNG;
    /**
     * @param invokeChain  invokeChain
     * @throws IOException if error occurs while write to outputStream
     * @return
     */
    public static File generateRandomFile(InvokeChain invokeChain,ImageType imageType) throws IOException {
        String plantUMLText = InvocationChainToPlantUmlHelper.generatePlantUmlText(invokeChain);
        SourceStringReader reader = new SourceStringReader(plantUMLText);
        Path tmpdir = Path.of(System.getProperty("java.io.tmpdir"));
        File imgFile = Files.createFile(tmpdir.resolve(UUID.randomUUID() + "." + imageType.getSuffix())).toFile();
        FileOutputStream fos = new FileOutputStream(imgFile);
        // Write the first image to "os"
        DiagramDescription desc = reader.outputImage(fos, new FileFormatOption(imageType.getFileFormat()));

        fos.close();
        return imgFile;
    }
}
