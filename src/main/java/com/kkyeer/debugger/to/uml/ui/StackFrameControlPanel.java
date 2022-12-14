package com.kkyeer.debugger.to.uml.ui;

import com.intellij.debugger.engine.JavaStackFrame;
import com.intellij.debugger.impl.DebuggerUtilsEx;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 17:12 2023/1/1
 * @Modified By:
 */
public class StackFrameControlPanel {
    private UmlData umlData;

    private SFCPSelectionModel selectionModel;


    public StackFrameControlPanel(UmlData umlData) {
        this.umlData = umlData;
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JBList<JavaStackFrame> listUI = new JBList<>();
        JavaStackFrame[] frames = this.umlData.getStackFrameList().toArray(new JavaStackFrame[this.umlData.getStackFrameList().size()]);
        listUI.setListData(frames);
        listUI.setFixedCellHeight(35);
        listUI.setCellRenderer(
                (list, stackFrame, index, isSelected, cellHasFocus) -> {
                    @NlsSafe StringBuilder label = new StringBuilder();
                    label.append(stackFrame.getDescriptor().getMethod().name())
                            .append(':').append(DebuggerUtilsEx.getLineNumber(stackFrame.getDescriptor().getLocation(), false))
                            .append("   [")
                            .append(stackFrame.getDescriptor().getLocation().declaringType().name())
                            .append("]");
                    return new JBLabel(label.toString());
                }
        );
        listUI.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        SFCPSelectionModel selectionModel = new SFCPSelectionModel();
        listUI.setSelectionModel(selectionModel);
        this.selectionModel = selectionModel;
        panel.add(listUI, BorderLayout.CENTER);
        return panel;
    }

    public void refreshSelectedFrames() {
        this.umlData.setSelectedFrameIndexes(this.selectionModel.getSelectedIndices());
    }

    private static class SFCPSelectionModel extends DefaultListSelectionModel {
    }



}
