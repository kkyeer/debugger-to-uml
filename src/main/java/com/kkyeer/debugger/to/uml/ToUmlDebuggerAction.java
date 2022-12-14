package com.kkyeer.debugger.to.uml;

import com.intellij.debugger.engine.JavaStackFrame;
import com.intellij.debugger.ui.impl.watch.StackFrameDescriptorImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.CollectionListModel;
import com.intellij.xdebugger.impl.frame.XDebuggerFramesList;
import com.kkyeer.stack.to.uml.core.helper.InvocationToImage;
import com.kkyeer.stack.to.uml.core.model.Invocation;
import com.kkyeer.stack.to.uml.core.model.InvokeChain;
import com.kkyeer.stack.to.uml.core.model.InvokeType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 下午6:41 2022/12/10
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
        // // Using the event, create and show a dialog
        // Project currentProject = event.getProject();
        // StringBuilder dlgMsg = new StringBuilder(event.getPresentation().getText() + " Selected!");
        // String dlgTitle = event.getPresentation().getDescription();
        // If an element is selected in the editor, add info about it.
        XDebuggerFramesList framesList = event.getData(FRAMES_LIST);
        List items = framesList.getModel().getItems();
        InvokeChain invokeChain = generateInvokeChain(items);
        File svgFile = null;
        try {
            svgFile = InvocationToImage.generateRandomFile(invokeChain);
        } catch (IOException e) {
            e.printStackTrace();
        }
        displayFile(svgFile, event);
        // System.out.println(svgFile.getAbsolutePath());
        // Messages.showMessageDialog(event.getProject(), items.toString(), dlgTitle, Messages.getInformationIcon());
    }



    public static void displayFile(File file, AnActionEvent event) {
        UmlDisplay umlDisplay = new UmlDisplay(event.getProject(), file);
        umlDisplay.show();
    }


    public InvokeChain generateInvokeChain(List items) {
        InvokeChain invokeChain = new InvokeChain();
        List<Invocation> invocationList = new ArrayList<>();
        String prevClass = null;
        Invocation prevInvocation = null;
        for (int i = items.size() - 1; i >= 0; i--) {
            JavaStackFrame frame = (JavaStackFrame) items.get(i);
            StackFrameDescriptorImpl descriptor = frame.getDescriptor();
            String currentClassName = descriptor.getLocation().declaringType().classObject().reflectedType().name();
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

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}
