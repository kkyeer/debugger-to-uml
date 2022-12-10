package com.kkyeer.debugger.to.uml;

import com.intellij.ide.ui.customization.ActionUrl;
import com.intellij.ide.ui.customization.CustomActionsSchema;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.CollectionListModel;
import com.intellij.xdebugger.impl.frame.XDebuggerFramesList;
import com.intellij.xdebugger.impl.ui.tree.actions.XDebuggerTreeActionBase;
import com.intellij.xdebugger.impl.ui.tree.nodes.XValueNodeImpl;
import org.jetbrains.annotations.NotNull;
import com.intellij.debugger.engine.JavaStackFrame;

import java.util.List;

import static com.intellij.xdebugger.impl.actions.XDebuggerActions.TOOL_WINDOW_LEFT_TOOLBAR_GROUP;

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
        // Using the event, create and show a dialog
        Project currentProject = event.getProject();
        StringBuilder dlgMsg = new StringBuilder(event.getPresentation().getText() + " Selected!");
        String dlgTitle = event.getPresentation().getDescription();
        // If an element is selected in the editor, add info about it.
        XDebuggerFramesList framesList = event.getData(FRAMES_LIST);
        CollectionListModel framesListModel = framesList.getModel();
        List items = framesListModel.getItems();
        for (Object item : items) {
            JavaStackFrame frame = (JavaStackFrame) item;
            System.out.println(frame.toString());
        }
        Messages.showMessageDialog(currentProject, items.toString(), dlgTitle, Messages.getInformationIcon());
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}
