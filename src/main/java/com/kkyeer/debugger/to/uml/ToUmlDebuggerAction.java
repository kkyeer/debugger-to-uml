package com.kkyeer.debugger.to.uml;

import com.intellij.debugger.engine.JavaStackFrame;
import com.intellij.debugger.ui.impl.watch.StackFrameDescriptorImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.impl.frame.XDebuggerFramesList;
import com.kkyeer.debugger.to.uml.ui.UmlDisplay;
import com.kkyeer.debugger.to.uml.helper.ImageType;
import com.kkyeer.debugger.to.uml.helper.InvocationToImage;
import com.kkyeer.debugger.to.uml.model.Invocation;
import com.kkyeer.debugger.to.uml.model.InvokeChain;
import com.kkyeer.debugger.to.uml.model.InvokeType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 18:41 2022/12/10
 * @Modified By:
 */
public class ToUmlDebuggerAction extends AnAction {
    private static final DataKey<XDebuggerFramesList> FRAMES_LIST = DataKey.create("FRAMES_LIST");

    /**
     * Implement this method to provide your action handler.
     *
     * @param event Carries information on the invocation place
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        // If an element is selected in the editor, add info about it.
        XDebuggerFramesList framesList = event.getData(FRAMES_LIST);
        // no type in framesList.getModel() response
        List items = framesList.getModel().getItems();
        List<JavaStackFrame> list = (List<JavaStackFrame>) items.stream().map(item->{
            Object ori = item;
            JavaStackFrame obj = (JavaStackFrame) ori;
            return obj;
        }).collect(Collectors.toList());
        displayFile(event, list);
    }



    public static void displayFile(AnActionEvent event, List<JavaStackFrame> stackFrameList) {
        UmlDisplay umlDisplay = new UmlDisplay(event.getProject(), stackFrameList);
        umlDisplay.show();
    }




    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}
