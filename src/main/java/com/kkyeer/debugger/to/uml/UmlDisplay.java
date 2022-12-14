package com.kkyeer.debugger.to.uml;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.GridLayoutManager;
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
    private File svgFile;

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

    public UmlDisplay(@Nullable Project project, File svgFile) {
        super(project, false);
        this.svgFile = svgFile;
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
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        panel.setSize(20,20);

        // JLabel imgLabel = new JLabel(new ImageIcon("path_to_image.png"));
        // panel.add(imgLabel, -1);
        // JTextArea jTextArea = new JTextArea();
        // // jTextArea.setSize(30,30);
        // panel.add(jTextArea,BorderLayout.CENTER);
        JLabel imgLabel = new JLabel(new ImageIcon(this.svgFile.getAbsolutePath()));
        imgLabel.setSize(20,20);
        panel.add(imgLabel,BorderLayout.CENTER);
        return panel;
    }
}
