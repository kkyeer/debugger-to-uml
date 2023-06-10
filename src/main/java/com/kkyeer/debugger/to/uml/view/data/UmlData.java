package com.kkyeer.debugger.to.uml.view.data;

import com.intellij.debugger.engine.JavaStackFrame;
import com.intellij.debugger.ui.impl.watch.StackFrameDescriptorImpl;
import com.kkyeer.debugger.to.uml.helper.ImageType;
import com.kkyeer.debugger.to.uml.helper.InvocationToImage;
import com.kkyeer.debugger.to.uml.model.Invocation;
import com.kkyeer.debugger.to.uml.model.InvokeChain;
import com.kkyeer.debugger.to.uml.model.InvokeType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @Author: kkyeer
 * @Description: data layer
 * @Date:Created in 14:21 2023/1/6
 * @Modified By:
 */
public class UmlData {
    /**
     * svg file
     */
    private File imgFile;

    /**
     * frames
     */
    @Nonnull
    private List<JavaStackFrame> stackFrameList;

    /**
     * selected stack indexes
     */
    @Nullable
    private int[] selectedFrameIndexes;

    private List<Consumer<UmlData>> actions = new ArrayList<>();

    public UmlData(@Nonnull List<JavaStackFrame> stackFrameList) {
        this.stackFrameList = stackFrameList;
        int[] allIndex = new int[this.stackFrameList.size()];
        for (int i = 0; i < this.stackFrameList.size(); i++) {
            allIndex[i] = i;
        }
        this.selectedFrameIndexes = allIndex;
        try {
            this.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribeChange(Consumer<UmlData> action){
        this.actions.add(action);
    }

    /**
     * refresh invoke chain and file
     * @throws IOException when something goes wrong
     */
    public void refresh() throws IOException {
        int[] selectedFrameIndexes = this.selectedFrameIndexes;
        InvokeChain invokeChain = generateInvokeChain(stackFrameList, selectedFrameIndexes);
        File svgFile;
        try {
            svgFile = InvocationToImage.generateRandomFile(invokeChain, ImageType.SVG);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        if (Objects.nonNull(this.imgFile) && this.imgFile.exists()) {
            this.imgFile.delete();
        }
        this.imgFile = svgFile;
        for (Consumer<UmlData> action : this.actions) {
            action.accept(this);
        }
    }

    /**
     * clear file and cache
     */
    public void clear(){
        if (Objects.nonNull(this.imgFile) && this.imgFile.exists()) {
            this.imgFile.deleteOnExit();
        }
    }



    /**
     * generate invoke chain from selected stacks
     * @param items stack frames
     * @param selectedIndexes selected stack indexes
     * @return invoke chain
     */
    private InvokeChain generateInvokeChain(List<JavaStackFrame> items,@Nullable int[] selectedIndexes) {
        InvokeChain invokeChain = new InvokeChain();
        List<Invocation> invocationList = new ArrayList<>();
        String prevClass = null;
        Invocation prevInvocation = null;
        List<JavaStackFrame> selected = new ArrayList<>();
        if (selectedIndexes != null && selectedIndexes.length != 0) {
            for (int selectedIndex : selectedIndexes) {
                selected.add(items.get(selectedIndex));
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

    /**
     * generate short class name from original class name
     * @param className original class name
     * @return shorten class name
     */
    private String getShortClassName(String className) {
        className = className.replaceAll("\\$+", "");
        String[] parts = className.split("\\.");
        return parts[parts.length - 1];
    }

    public File getImgFile() {
        return imgFile;
    }

    public List<JavaStackFrame> getStackFrameList() {
        return stackFrameList;
    }

    public void setStackFrameList(List<JavaStackFrame> stackFrameList) {
        this.stackFrameList = stackFrameList;
    }

    public void setSelectedFrameIndexes(int[] selectedFrameIndexes) {
        this.selectedFrameIndexes = selectedFrameIndexes;
    }
}
