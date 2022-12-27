package com.kkyeer.debugger.to.uml.disp;

import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 21:21 2022/12/18
 * @Modified By:
 */
public abstract class SVGDisplayPanel {
    private File imgFile;

    private JSVGCanvas canvas;


    public SVGDisplayPanel(File imgFile) {
        this.imgFile = imgFile;
    }


    public JComponent createPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        panel.setSize(1000,500);
        JSVGCanvas jsvgCanvas = new JSVGCanvas();
        jsvgCanvas.setSize(200,200);

        jsvgCanvas.setURI(imgFile.toURI().toString());
        this.canvas = jsvgCanvas;
        panel.add(jsvgCanvas, BorderLayout.CENTER);



        JMenuBar menuBar = new JMenuBar();
        configureMenuBar(menuBar);
        panel.add(menuBar, BorderLayout.NORTH);
        return panel;
    }

    public abstract void configureMenuBar(JMenuBar menuBar);

    public File getImgFile() {
        return imgFile;
    }

    public JSVGCanvas getCanvas() {
        return canvas;
    }
}
