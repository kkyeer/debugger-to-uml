package com.kkyeer.debugger.to.uml.ui;

import com.intellij.debugger.engine.JavaStackFrame;
import com.intellij.debugger.ui.impl.watch.StackFrameDescriptorImpl;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.table.JBListTable;
import com.kkyeer.debugger.to.uml.helper.ImageType;
import com.kkyeer.debugger.to.uml.helper.InvocationToImage;
import com.kkyeer.debugger.to.uml.model.Invocation;
import com.kkyeer.debugger.to.uml.model.InvokeChain;
import com.kkyeer.debugger.to.uml.model.InvokeType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ListUI;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 17:12 2023/1/1
 * @Modified By:
 */
public class StackFrameControlPanel {
    private List<JavaStackFrame> stackFrameList;

    private SFCPSelectionModel selectionModel;


    public StackFrameControlPanel(List<JavaStackFrame> stackFrameList) {
        this.stackFrameList = stackFrameList;
    }

    public JPanel getPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JBList<JavaStackFrame> listUI = new JBList<>();
        JavaStackFrame[] frames = stackFrameList.toArray(new JavaStackFrame[stackFrameList.size()]);
        listUI.setListData(frames);
        listUI.setFixedCellHeight(35);
        listUI.setCellRenderer(
                (list, value, index, isSelected, cellHasFocus) -> {
                    String text = value.getDescriptor().toString();
                    // JBCheckBox checkBox = new JBCheckBox();
                    // checkBox.setText(text);
                    // checkBox.setSelected(isSelected);
                    // checkBox.addItemListener(
                    //         e -> System.out.println(e)
                    // );
                    // checkBox.addChangeListener(
                    //         new ChangeListener() {
                    //             @Override
                    //             public void stateChanged(ChangeEvent e) {
                    //                 System.out.println(e);
                    //             }
                    //         }
                    // );
                    return new JBLabel(text);
                }
        );
        listUI.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        SFCPSelectionModel selectionModel = new SFCPSelectionModel();
        listUI.setSelectionModel(selectionModel);
        this.selectionModel = selectionModel;
        panel.add(listUI, BorderLayout.CENTER);
        return panel;
    }

    private int[] getSelectedFrames(){
        System.out.println("selected" + this.selectionModel.getSelectedItemsCount() + " items");
        return this.selectionModel.getSelectedIndices();
    }

    private static class SFCPSelectionModel extends DefaultListSelectionModel { }

    public InvokeChain generateInvokeChain(List<JavaStackFrame> items, int[] selectedIndexes) {
        InvokeChain invokeChain = new InvokeChain();
        List<Invocation> invocationList = new ArrayList<>();
        String prevClass = null;
        Invocation prevInvocation = null;
        List<JavaStackFrame> selected = new ArrayList<>();
        if (selectedIndexes != null && selectedIndexes.length != 0) {
            for (int i = 0; i < selectedIndexes.length; i++) {
                selected.add(items.get(selectedIndexes[i]));
            }
        } else {
            selected = items;
        }
        for (int i = selected.size() - 1; i >= 0; i--) {
            JavaStackFrame frame = selected.get(i);
            StackFrameDescriptorImpl descriptor = frame.getDescriptor();
            String currentClassName = getShortClassName(descriptor.getLocation().declaringType().name());

            Invocation invocation = Invocation.Builder.builder()
                    .invokerName(prevClass == null ? currentClassName : prevClass)
                    .invokeDesc(descriptor.getName())
                    .invokeType(InvokeType.SIMPLE_INVOKE)
                    .invokedName(currentClassName)
                    .followInvocationList(new ArrayList<>())
                    .build();
            prevClass = currentClassName;
            if (prevInvocation != null) {
                prevInvocation.getFollowInvocationList().add(invocation);
            } else {
                invocationList.add(invocation);
            }
            prevInvocation = invocation;
        }
        invokeChain.setInvokeList(invocationList);
        return invokeChain;
    }

    private String getShortClassName(String className){
        className = className.replaceAll("\\$+", "");
        String[] parts = className.split("\\.");
        return parts[parts.length - 1];
    }

    public File generateSvgFile(){
        int[] selectedFrames = getSelectedFrames();
        InvokeChain invokeChain = generateInvokeChain(stackFrameList, selectedFrames);
        File svgFile = null;
        try {
            svgFile = InvocationToImage.generateRandomFile(invokeChain, ImageType.SVG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return svgFile;
    }

}
