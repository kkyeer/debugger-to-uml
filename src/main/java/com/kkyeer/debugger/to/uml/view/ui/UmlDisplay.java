package com.kkyeer.debugger.to.uml.view.ui;

import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import com.kkyeer.debugger.to.uml.view.data.UmlData;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: kkyeer
 * @Description: controls display of generated uml
 * @Date:Created in 11:27 2022/12/14
 * @Modified By:
 */
public class UmlDisplay extends DialogWrapper {

    private Project project;

    private StackFrameControlPanel frameControlPanel;

    private JBCefBrowser jbCefBrowser;

    private UmlData umlData;


    public UmlDisplay(@Nullable Project project, UmlData umlData) {
        super(project, false);
        this.project = project;
        this.umlData = umlData;
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
        BorderLayout borderLayout = new BorderLayout(0, 0);
        panel.setLayout(borderLayout);
        panel.setPreferredSize(new Dimension(1000, 500));

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerSize(10);
        StackFrameControlPanel stackFrameControlPanel = new StackFrameControlPanel(this.umlData);
        this.frameControlPanel = stackFrameControlPanel;
        // panel.add(stackFrameControlPanel.getPanel(), BorderLayout.WEST);
        splitPane.setLeftComponent(stackFrameControlPanel.getPanel());

        this.jbCefBrowser = new JBCefBrowser("file:///" + this.umlData.getImgFile().getAbsolutePath());
        JComponent component = jbCefBrowser.getComponent();
        // panel.add(component, BorderLayout.CENTER);
        splitPane.setRightComponent(component);
        panel.add(splitPane, BorderLayout.CENTER);
        JMenuBar menuBar = new UmlPluginMenuBar(this.umlData, this.project).getMenuBar();
        panel.add(menuBar, BorderLayout.NORTH);
        this.umlData.subscribeChange(
                umlData1 -> this.refreshImgDisplay()
        );
        return panel;
    }

    private void refreshImgDisplay(){
        this.jbCefBrowser.loadURL("file:///" + this.umlData.getImgFile().getAbsolutePath());
    }

}
