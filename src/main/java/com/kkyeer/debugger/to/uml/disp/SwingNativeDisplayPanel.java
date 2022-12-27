package com.kkyeer.debugger.to.uml.disp;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 18:25 2022/12/25
 * @Modified By:
 */
public class SwingNativeDisplayPanel extends SVGDisplayPanel{
    private Container parent;
    public SwingNativeDisplayPanel(File imgFile,Container parent) {
        super(imgFile);
        this.parent = parent;
    }

    @Override
    public void configureMenuBar(JMenuBar menuBar) {
        JButton saveBtn = new JButton();
        saveBtn.setText("Save");
        saveBtn.setVisible(true);
        saveBtn.addActionListener(
                e -> configureFileChooser(parent)
        );
        menuBar.add(saveBtn);

        JButton resetZoomBtn = new JButton();
        resetZoomBtn.setText("ResetZoom");
        resetZoomBtn.setVisible(true);
        AtomicReference<JSVGCanvas> canvasRef = new AtomicReference<>(getCanvas());
        resetZoomBtn.addActionListener(
                e ->{
                    AffineTransform initialTransform = canvasRef.get().getInitialTransform();
                    canvasRef.get().setPaintingTransform(initialTransform);
                }
        );
        menuBar.add(resetZoomBtn);
    }


    private void configureFileChooser(Component parent) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser.setVisible(true);
        jFileChooser.setCurrentDirectory(new File("./"));
        int code = jFileChooser.showSaveDialog(parent);
        if (JFileChooser.APPROVE_OPTION == code) {
            File selectedFile = jFileChooser.getSelectedFile();
            try {
                FileUtils.copyFile(getImgFile(), selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
