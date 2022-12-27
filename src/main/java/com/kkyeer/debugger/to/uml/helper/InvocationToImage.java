package com.kkyeer.debugger.to.uml.helper;

import com.kkyeer.debugger.to.uml.helper.ImageType;
import com.kkyeer.debugger.to.uml.helper.InvocationChainToPlantUmlHelper;
import com.kkyeer.debugger.to.uml.model.InvokeChain;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: kkyeer
 * @Description:
 * @Date:Created in 22:57 2022/12/12
 * @Modified By:
 */
public class InvocationToImage {
    private static final ImageType imageType = ImageType.PNG;
    /**
     * @param invokeChain  invokeChain
     * @throws IOException if error occurs while write to outputStream
     * @return
     */
    public static File generateRandomFile(InvokeChain invokeChain,ImageType imageType) throws IOException {
        String plantUMLText = InvocationChainToPlantUmlHelper.generatePlantUmlText(invokeChain);
        // plantUMLText = "@startuml\n" +
        //         "skinparam responseMessageBelowArrow true\n" +
        //         "RestartLauncher -> RestartLauncher: run\n" +
        //         "activate RestartLauncher\n" +
        //         "RestartLauncher -> Method: invoke\n" +
        //         "activate Method\n" +
        //         "Method -> DelegatingMethodAccessorImpl: invoke\n" +
        //         "activate DelegatingMethodAccessorImpl\n" +
        //         "DelegatingMethodAccessorImpl -> NativeMethodAccessorImpl: invoke\n" +
        //         "activate NativeMethodAccessorImpl\n" +
        //         "NativeMethodAccessorImpl -> NativeMethodAccessorImpl: invoke0\n" +
        //         "activate NativeMethodAccessorImpl\n" +
        //         "NativeMethodAccessorImpl -> SpringExperimentApplication: main\n" +
        //         "activate SpringExperimentApplication\n" +
        //         "SpringExperimentApplication -> SpringApplication: run\n" +
        //         "activate SpringApplication\n" +
        //         "SpringApplication -> SpringApplication: run\n" +
        //         "activate SpringApplication\n" +
        //         "SpringApplication -> SpringApplication: run\n" +
        //         "activate SpringApplication\n" +
        //         "SpringApplication -> SpringApplication: refreshContext\n" +
        //         "activate SpringApplication\n" +
        //         "SpringApplication -> SpringApplication: refresh\n" +
        //         "activate SpringApplication\n" +
        //         "SpringApplication -> ServletWebServerApplicationContext: refresh\n" +
        //         "activate ServletWebServerApplicationContext\n" +
        //         "ServletWebServerApplicationContext -> AbstractApplicationContext: refresh\n" +
        //         "activate AbstractApplicationContext\n" +
        //         "AbstractApplicationContext -> AbstractApplicationContext: registerBeanPostProcessors\n" +
        //         "activate AbstractApplicationContext\n" +
        //         "AbstractApplicationContext -> PostProcessorRegistrationDelegate: registerBeanPostProcessors\n" +
        //         "activate PostProcessorRegistrationDelegate\n" +
        //         "PostProcessorRegistrationDelegate -> AbstractBeanFactory: getBean\n" +
        //         "activate AbstractBeanFactory\n" +
        //         "AbstractBeanFactory -> AbstractBeanFactory: doGetBean\n" +
        //         "activate AbstractBeanFactory\n" +
        //         "AbstractBeanFactory -> DefaultSingletonBeanRegistry: getSingleton\n" +
        //         "activate DefaultSingletonBeanRegistry\n" +
        //         "DefaultSingletonBeanRegistry -> 1973640293: getObject\n" +
        //         "activate 1973640293\n" +
        //         "1973640293 -> AbstractBeanFactory: lambda$doGetBean$0\n" +
        //         "activate AbstractBeanFactory\n" +
        //         "AbstractBeanFactory -> AbstractAutowireCapableBeanFactory: createBean\n" +
        //         "activate AbstractAutowireCapableBeanFactory\n" +
        //         "AbstractAutowireCapableBeanFactory -> AbstractAutowireCapableBeanFactory: doCreateBean\n" +
        //         "activate AbstractAutowireCapableBeanFactory\n" +
        //         "AbstractAutowireCapableBeanFactory -> AbstractAutowireCapableBeanFactory: initializeBean\n" +
        //         "activate AbstractAutowireCapableBeanFactory\n" +
        //         "AbstractAutowireCapableBeanFactory -> AbstractAutowireCapableBeanFactory: applyBeanPostProcessorsAfterInitialization\n" +
        //         "activate AbstractAutowireCapableBeanFactory\n" +
        //         "AbstractAutowireCapableBeanFactory -> AbstractAutoProxyCreator: postProcessAfterInitialization\n" +
        //         "activate AbstractAutoProxyCreator\n" +
        //         "AbstractAutoProxyCreator -> AbstractAutoProxyCreator: wrapIfNecessary\n" +
        //         "activate AbstractAutoProxyCreator\n" +
        //         "AbstractAutoProxyCreator -> AbstractAdvisorAutoProxyCreator: getAdvicesAndAdvisorsForBean\n" +
        //         "activate AbstractAdvisorAutoProxyCreator\n" +
        //         "AbstractAdvisorAutoProxyCreator -> AbstractAdvisorAutoProxyCreator: findEligibleAdvisors\n" +
        //         "activate AbstractAdvisorAutoProxyCreator\n" +
        //         "AbstractAdvisorAutoProxyCreator -> AbstractAdvisorAutoProxyCreator: findAdvisorsThatCanApply\n" +
        //         "activate AbstractAdvisorAutoProxyCreator\n" +
        //         "AbstractAdvisorAutoProxyCreator -> AopUtils: findAdvisorsThatCanApply\n" +
        //         "activate AopUtils\n" +
        //         "AopUtils -> AopUtils: canApply\n" +
        //         "activate AopUtils\n" +
        //         "AopUtils -> AopUtils: canApply\n" +
        //         "activate AopUtils\n" +
        //         "AopUtils -> TransactionAttributeSourcePointcut: matches\n" +
        //         "activate TransactionAttributeSourcePointcut\n" +
        //         "TransactionAttributeSourcePointcut -> AbstractFallbackTransactionAttributeSource: getTransactionAttribute\n" +
        //         "activate AbstractFallbackTransactionAttributeSource\n" +
        //         "AbstractFallbackTransactionAttributeSource -> AbstractFallbackTransactionAttributeSource: computeTransactionAttribute\n" +
        //         "activate AbstractFallbackTransactionAttributeSource\n" +
        //         "deactivate AbstractFallbackTransactionAttributeSource\n" +
        //         "AbstractFallbackTransactionAttributeSource --> TransactionAttributeSourcePointcut: getTransactionAttribute\n" +
        //         "deactivate AbstractFallbackTransactionAttributeSource\n" +
        //         "TransactionAttributeSourcePointcut --> AopUtils: matches\n" +
        //         "deactivate TransactionAttributeSourcePointcut\n" +
        //         "deactivate AopUtils\n" +
        //         "deactivate AopUtils\n" +
        //         "AopUtils --> AbstractAdvisorAutoProxyCreator: findAdvisorsThatCanApply\n" +
        //         "deactivate AopUtils\n" +
        //         "deactivate AbstractAdvisorAutoProxyCreator\n" +
        //         "deactivate AbstractAdvisorAutoProxyCreator\n" +
        //         "AbstractAdvisorAutoProxyCreator --> AbstractAutoProxyCreator: getAdvicesAndAdvisorsForBean\n" +
        //         "deactivate AbstractAdvisorAutoProxyCreator\n" +
        //         "deactivate AbstractAutoProxyCreator\n" +
        //         "AbstractAutoProxyCreator --> AbstractAutowireCapableBeanFactory: postProcessAfterInitialization\n" +
        //         "deactivate AbstractAutoProxyCreator\n" +
        //         "deactivate AbstractAutowireCapableBeanFactory\n" +
        //         "deactivate AbstractAutowireCapableBeanFactory\n" +
        //         "deactivate AbstractAutowireCapableBeanFactory\n" +
        //         "AbstractAutowireCapableBeanFactory --> AbstractBeanFactory: createBean\n" +
        //         "deactivate AbstractAutowireCapableBeanFactory\n" +
        //         "AbstractBeanFactory --> 1973640293: lambda$doGetBean$0\n" +
        //         "deactivate AbstractBeanFactory\n" +
        //         "1973640293 --> DefaultSingletonBeanRegistry: getObject\n" +
        //         "deactivate 1973640293\n" +
        //         "DefaultSingletonBeanRegistry --> AbstractBeanFactory: getSingleton\n" +
        //         "deactivate DefaultSingletonBeanRegistry\n" +
        //         "deactivate AbstractBeanFactory\n" +
        //         "AbstractBeanFactory --> PostProcessorRegistrationDelegate: getBean\n" +
        //         "deactivate AbstractBeanFactory\n" +
        //         "PostProcessorRegistrationDelegate --> AbstractApplicationContext: registerBeanPostProcessors\n" +
        //         "deactivate PostProcessorRegistrationDelegate\n" +
        //         "deactivate AbstractApplicationContext\n" +
        //         "AbstractApplicationContext --> ServletWebServerApplicationContext: refresh\n" +
        //         "deactivate AbstractApplicationContext\n" +
        //         "ServletWebServerApplicationContext --> SpringApplication: refresh\n" +
        //         "deactivate ServletWebServerApplicationContext\n" +
        //         "deactivate SpringApplication\n" +
        //         "deactivate SpringApplication\n" +
        //         "deactivate SpringApplication\n" +
        //         "deactivate SpringApplication\n" +
        //         "SpringApplication --> SpringExperimentApplication: run\n" +
        //         "deactivate SpringApplication\n" +
        //         "SpringExperimentApplication --> NativeMethodAccessorImpl: main\n" +
        //         "deactivate SpringExperimentApplication\n" +
        //         "deactivate NativeMethodAccessorImpl\n" +
        //         "NativeMethodAccessorImpl --> DelegatingMethodAccessorImpl: invoke\n" +
        //         "deactivate NativeMethodAccessorImpl\n" +
        //         "DelegatingMethodAccessorImpl --> Method: invoke\n" +
        //         "deactivate DelegatingMethodAccessorImpl\n" +
        //         "Method --> RestartLauncher: invoke\n" +
        //         "deactivate Method\n" +
        //         "deactivate RestartLauncher\n" +
        //         "@enduml";
        SourceStringReader reader = new SourceStringReader(plantUMLText);
        File imgFile = new File(UUID.randomUUID()+"."+imageType.getSuffix());
        FileOutputStream fos = new FileOutputStream(imgFile);
        // Write the first image to "os"
        DiagramDescription desc = reader.outputImage(fos, new FileFormatOption(imageType.getFileFormat()));

        fos.close();
        return imgFile;
    }
}
