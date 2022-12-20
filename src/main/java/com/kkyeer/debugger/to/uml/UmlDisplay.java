package com.kkyeer.debugger.to.uml;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.kkyeer.stack.to.uml.core.disp.SVGDisplayPanel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @Author: kkyeer
 * @Description: controls display of generated uml
 * @Date:Created in 11:27 2022/12/14
 * @Modified By:
 */
public class UmlDisplay extends DialogWrapper {
    /**
     * svg
     */
    private File imgFile;

    /**
     * Creates modal {@code DialogWrapper}. The currently active window will be the dialog's parent.
     *
     * @param project     parent window for the dialog will be calculated based on focused window for the
     *                    specified {@code project}. This parameter can be {@code null}. In this case parent window
     *                    will be suggested based on current focused window.
     * @param canBeParent specifies whether the dialog can be a parent for other windows. This parameter is used
     *                    by {@code WindowManager}.
     * @throws IllegalStateException if the dialog is invoked not on the event dispatch thread
     */
    protected UmlDisplay(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
    }

    public UmlDisplay(@Nullable Project project, File imgFile) {
        super(project, false);
        this.imgFile = imgFile;
        setTitle("svg");
        init();
    }

    /**
     * Factory method. It creates panel with dialog options. Options panel is located at the
     * center of the dialog's content pane. The implementation can return {@code null}
     * value. In this case there will be no options panel.
     */
    @Override
    protected @Nullable JComponent createCenterPanel() {
        return new SVGDisplayPanel(this.imgFile).createPanel();
        // JPanel panel = new JPanel();
        // panel.setLayout(new BorderLayout(0, 0));
        // panel.setSize(20,20);
        //
        // // JLabel imgLabel = new JLabel(new ImageIcon("path_to_image.png"));
        // // panel.add(imgLabel, -1);
        // // JTextArea jTextArea = new JTextArea();
        // // // jTextArea.setSize(30,30);
        // // panel.add(jTextArea,BorderLayout.CENTER);
        // JLabel imgLabel = new JLabel(new ImageIcon(this.imgFile.getAbsolutePath()));
        // imgLabel.setSize(20,20);
        // panel.add(imgLabel,BorderLayout.CENTER);

        // JSVGCanvas jsvgCanvas = new JSVGCanvas();
        // jsvgCanvas.setURI(imgFile.toURI().toString());
        // panel.add(jsvgCanvas, BorderLayout.CENTER);
        // return panel;
    }
}
