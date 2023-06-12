package com.kkyeer.debugger.to.uml.view.ui;

import com.intellij.debugger.engine.JavaStackFrame;
import com.intellij.debugger.impl.DebuggerUtilsEx;
import com.intellij.debugger.ui.impl.watch.StackFrameDescriptorImpl;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.kkyeer.debugger.to.uml.view.data.UmlData;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 17:12 2023/1/1
 * @Modified By:
 */
public class StackFrameControlPanel {
    private UmlData umlData;

    private StackFrameSelectionModel selectionModel;

    private Map<JComponent, List<Integer>> labelToFrameIndices;

    private Map<JComponent, Boolean> filterButtonSelected = new HashMap<>();
    private JBList<JavaStackFrame> listUI;


    public StackFrameControlPanel(UmlData umlData) {
        this.umlData = umlData;
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel packageFilterPanel = getPackageFilterPanel();
        panel.add(packageFilterPanel, BorderLayout.NORTH);
        // StackList
        JBList<JavaStackFrame> listUI = getJavaStackFrameJBList();
        this.listUI = listUI;
        JBScrollPane scrollPane = new JBScrollPane(listUI);
        // scrollPane.add(listUI);
        // scrollPane.setSize(100,100);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getPackageFilterPanel() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new GridLayout());
        buttonPane.setSize(100,100);
        List<JComponent> components = extractPackagesFromUmlData(umlData);
        for (JComponent button : components) {
            buttonPane.add(button);
        }
        return buttonPane;
    }

    private List<JComponent> extractPackagesFromUmlData(UmlData umlData) {
        // Spring -> org.springframework
        Map<String, String> commonPackagePrefixToName = new HashMap<>();
        File commonPackagePrefixPropertyFile = new File("common_package_prefix.properties");
        if (commonPackagePrefixPropertyFile.exists()) {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(commonPackagePrefixPropertyFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            commonPackagePrefixToName = properties.entrySet().stream().collect(
                    Collectors.toMap(
                            entry -> entry.getKey().toString(),
                            entry -> entry.getValue().toString()
                    )
            );
        }
        // if not common prefix,get first 2 part as package prefix as this may be the domain
        Map<String, JComponent> commonNameToJLabel = new HashMap<>();
        Map<JComponent, List<Integer>> labelToFrameIndices = new HashMap<>();
        for (int i = 0; i < umlData.getStackFrameList().size(); i++) {
            JavaStackFrame javaStackFrame = umlData.getStackFrameList().get(i);
            String packageFullName = getClassNameFromJavaStackFrame(javaStackFrame);
            boolean foundInCommon = false;
            for (Map.Entry<String, String> nameToPrefixPattern : commonPackagePrefixToName.entrySet()) {
                // match common package
                if (packageFullName.startsWith(nameToPrefixPattern.getValue())) {
                    String packageName = nameToPrefixPattern.getKey();
                    handlePackageName(commonNameToJLabel, labelToFrameIndices, i, packageName);
                    foundInCommon = true;
                    break;
                }
            }
            if (!foundInCommon) {
                //     guess domain
                String[] parts = packageFullName.split("\\.");
                Optional<String> guessedDomain = Optional.empty();
                if (parts.length > 0) {
                    if (parts.length == 1) {
                        guessedDomain = Optional.of(parts[0]);
                    }else {
                        guessedDomain = Optional.of(parts[0] + "." + parts[1]);
                    }
                }
                if (guessedDomain.isPresent()) {
                    String packageName = guessedDomain.get();
                    handlePackageName(commonNameToJLabel, labelToFrameIndices, i,packageName);
                }
            }
        }
        this.labelToFrameIndices = labelToFrameIndices;
        return new ArrayList<>(labelToFrameIndices.keySet());
    }
    private void handlePackageName(Map<String, JComponent> commonNameToButton, Map<JComponent, List<Integer>> buttonToFrameIndices, int i, String packageName) {
        if (commonNameToButton.containsKey(packageName)) {
            JComponent button = commonNameToButton.get(packageName);
            List<Integer> indices = buttonToFrameIndices.get(button);
            indices.add(i);
        }else {
            JButton button = new JButton(packageName);
            button.setSelected(true);
            button.setText(packageName);
            this.filterButtonSelected.put(button, true);

            class OnClickMouseListener implements MouseListener {
                private final StackFrameControlPanel stackFrameControlPanel;

                private final JButton source;

                private OnClickMouseListener(StackFrameControlPanel stackFrameControlPanel, JButton button) {
                    this.stackFrameControlPanel = stackFrameControlPanel;
                    this.source = button;
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    Boolean selected = this.stackFrameControlPanel.filterButtonSelected.get(source);
                    if (selected) {
                    //     deselect action
                        List<Integer> relative = labelToFrameIndices.get(source);
                        int[] selectedIndices = this.stackFrameControlPanel.listUI.getSelectedIndices();
                        System.out.println(selectedIndices);
                        List<Integer> shouldSelect = new ArrayList<>();
                        for (int j = 0; j < selectedIndices.length; j++) {
                            int selectedIndex = selectedIndices[j];
                            if (!relative.contains(selectedIndex)) {
                                shouldSelect.add(selectedIndex);
                            }
                        }
                        int[] result = new int[shouldSelect.size()];
                        for (int j = 0; j < result.length; j++) {
                            result[j] = shouldSelect.get(j);
                        }
                        listUI.setSelectedIndices(result);
                    }else {
                    //     select package
                        List<Integer> relative = labelToFrameIndices.get(source);
                        int[] selectedIndices = this.stackFrameControlPanel.listUI.getSelectedIndices();
                        System.out.println(selectedIndices);
                        List<Integer> shouldSelect = new ArrayList<>();
                        for (int j = 0; j < selectedIndices.length; j++) {
                            int selectedIndex = selectedIndices[j];
                            shouldSelect.add(selectedIndex);
                        }
                        shouldSelect.addAll(relative);
                        int[] result = new int[shouldSelect.size()];
                        for (int j = 0; j < result.length; j++) {
                            result[j] = shouldSelect.get(j);
                        }
                        listUI.setSelectedIndices(result);
                    }
                    this.stackFrameControlPanel.filterButtonSelected.put(source, !selected);
                    this.source.setSelected(!selected);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            }
            button.addMouseListener(new OnClickMouseListener(this,button));
            commonNameToButton.put(packageName, button);
            List<Integer> indices = new ArrayList<>();
            indices.add(i);
            buttonToFrameIndices.put(button, indices);
        }
    }

    private String getClassNameFromJavaStackFrame(JavaStackFrame javaStackFrame) {
        StackFrameDescriptorImpl descriptor = javaStackFrame.getDescriptor();
        return descriptor.getLocation().declaringType().name();
    }


    @NotNull
    private JBList<JavaStackFrame> getJavaStackFrameJBList() {
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
        int[] allIndices = new int[frames.length];
        for (int i = 0; i < allIndices.length; i++) {
            allIndices[i] = i;
        }
        listUI.setSelectedIndices(allIndices);
        listUI.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listUI.addListSelectionListener(
                e -> {
                    this.umlData.setSelectedFrameIndexes(this.selectionModel.getSelectedIndices());
                }
        );
        StackFrameSelectionModel selectionModel = new StackFrameSelectionModel();
        listUI.setSelectionModel(selectionModel);
        this.selectionModel = selectionModel;
        return listUI;
    }

    private static class StackFrameSelectionModel extends DefaultListSelectionModel {

    }



}
