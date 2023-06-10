package com.kkyeer.debugger.to.uml.view.ui;

import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.kkyeer.debugger.to.uml.view.data.UmlData;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: kkyeer
 * @Description: MenuBar
 * @Date:Created in 11:41 2023/6/10
 * @Modified By:
 */
public class UmlPluginMenuBar extends JMenuBar {
    private JMenuBar menuBar;

    private UmlData umlData;

    private Project project;

    public UmlPluginMenuBar(UmlData umlData, Project project) {
        this.umlData = umlData;
        this.project = project;

        JMenuBar menuBar = new JMenuBar();
        this.menuBar = menuBar;
        JButton saveBtn = new JButton();
        saveBtn.setText("Save");
        saveBtn.setVisible(true);
        saveBtn.addActionListener(
                this::configureFileChooser
        );
        menuBar.add(saveBtn);

        JButton regenerateBtn = new JButton();
        regenerateBtn.setText("Regenerate");
        regenerateBtn.setVisible(true);
        regenerateBtn.addActionListener(
                e -> {
                    try {
                        this.umlData.refresh();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        menuBar.add(regenerateBtn);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private void configureFileChooser(ActionEvent event) {
        FileSaverDescriptor fileSaverDescriptor = new FileSaverDescriptor("Export Sequential Diagrams ", "File name:", "svg");
        FileSaverDialog dialog = FileChooserFactory.getInstance().createSaveFileDialog(fileSaverDescriptor, this.project);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String defaultFileName = "Sequence_" + simpleDateFormat.format(new Date());
        @Nullable VirtualFileWrapper virtualFileWrapper = dialog.save(defaultFileName);
        try {
            FileUtils.copyFile(this.umlData.getImgFile(), virtualFileWrapper.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
