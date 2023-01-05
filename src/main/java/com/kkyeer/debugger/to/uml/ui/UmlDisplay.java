package com.kkyeer.debugger.to.uml.ui;

import com.intellij.debugger.engine.JavaStackFrame;
import com.intellij.ide.util.TreeFileChooserDialog;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    private List<JavaStackFrame> stackFrameList;

    private Project project;

    private Container parentPanel;

    private StackFrameControlPanel frameControlPanel;

    private JBCefBrowser jbCefBrowser;



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

    public UmlDisplay(@Nullable Project project, List<JavaStackFrame> stackFrameList) {
        super(project, false);
        this.project = project;
        this.stackFrameList = stackFrameList;
        setTitle("Sequence Diagram of Chosen Stack");
        init();
    }


    /**
     * Factory method. It creates panel with dialog options. Options panel is located at the
     * center of the dialog's content pane. The implementation can return {@code null}
     * value. In this case there will be no options panel.
     */
    @Override
    protected @Nullable JComponent createCenterPanel() {
        if (!JBCefApp.isSupported()) {
            JPanel panel = new JPanel();
            panel.setSize(200, 200);
            panel.setLayout(new BorderLayout());
            JLabel label = new JLabel();
            label.setText("JCEF is needed");
            panel.add(label, BorderLayout.CENTER);
            return panel;
        }else {
            return mainPanel();
        }
    }

    private JPanel mainPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        panel.setSize(1000,500);
        this.parentPanel = panel;


        StackFrameControlPanel stackFrameControlPanel = new StackFrameControlPanel(stackFrameList);
        this.frameControlPanel = stackFrameControlPanel;
        panel.add(stackFrameControlPanel.getPanel(), BorderLayout.WEST);


        generateUMLFile();
        this.jbCefBrowser = new JBCefBrowser("file:///" + imgFile.getAbsolutePath());
        JComponent component = jbCefBrowser.getComponent();
        panel.add(component, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        configureMenuBar(menuBar);
        panel.add(menuBar, BorderLayout.NORTH);

        return panel;
    }



    public void configureMenuBar(JMenuBar menuBar) {
        JButton saveBtn = new JButton();
        saveBtn.setText("Save");
        saveBtn.setVisible(true);
        saveBtn.addActionListener(
                e -> configureFileChooser()
        );
        menuBar.add(saveBtn);

        JButton regenerateBtn = new JButton();
        regenerateBtn.setText("Regenerate");
        regenerateBtn.setVisible(true);
        regenerateBtn.addActionListener(
                e -> {
                    generateUMLFile();
                    refreshImgDisplay();
                }
        );
        menuBar.add(regenerateBtn);
    }

    public void generateUMLFile(){
        File generateSvgFile = this.frameControlPanel.generateSvgFile();
        if (this.imgFile != null && this.imgFile.exists()) {
            this.imgFile.deleteOnExit();
        }
        this.imgFile = generateSvgFile;
    }

    private void refreshImgDisplay(){
        this.jbCefBrowser.loadURL("file:///" + imgFile.getAbsolutePath());
    }


    private void configureFileChooser() {
        FileSaverDescriptor fileSaverDescriptor = new FileSaverDescriptor("Export Sequential Diagrams ", "File name:", "svg");
        FileSaverDialog dialog = FileChooserFactory.getInstance().createSaveFileDialog(fileSaverDescriptor, this.project);

        @Nullable VirtualFileWrapper virtualFileWrapper = dialog.save(UUID.randomUUID().toString() + ".svg");
        try {
            FileUtils.copyFile(this.imgFile,virtualFileWrapper.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
