package com.kkyeer.debugger.to.uml.helper;

import net.sourceforge.plantuml.FileFormat;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 14:02 2022/12/14
 * @Modified By:
 */
public enum ImageType {
    SVG(FileFormat.SVG,"svg"),
    PNG(FileFormat.PNG,"png");
    private FileFormat fileFormat;
    private String suffix;

    ImageType(FileFormat fileFormat, String suffix) {
        this.fileFormat = fileFormat;
        this.suffix = suffix;
    }

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public String getSuffix() {
        return suffix;
    }
}
