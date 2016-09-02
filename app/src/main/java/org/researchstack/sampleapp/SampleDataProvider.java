package org.researchstack.sampleapp;
import android.content.Context;
import android.content.res.Resources;

import org.apache.commons.lang3.StringUtils;
import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.task.Task;
import org.researchstack.sampleapp.bridge.BridgeDataProvider;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.model.SchedulesAndTasksModel;
import org.researchstack.skin.model.TaskModel;
import org.researchstack.skin.task.SmartSurveyTask;

import java.lang.reflect.Constructor;


public class SampleDataProvider extends BridgeDataProvider
{
    public SampleDataProvider()
    {
        super();
    }

    @Override
    public void processInitialTaskResult(Context context, TaskResult taskResult)
    {
        // handle result from initial task (save profile info to disk, upload to your server, etc)
    }

    @Override
    protected ResourcePathManager.Resource getPublicKeyResId()
    {
        return new SampleResourceManager.PemResource("bridge_key");
    }

    @Override
    protected ResourcePathManager.Resource getTasksAndSchedules()
    {
        return ResourceManager.getInstance().getTasksAndSchedules();
    }

    @Override
    protected String getBaseUrl()
    {
        return BuildConfig.STUDY_BASE_URL;
    }

    @Override
    protected String getStudyId()
    {
        return BuildConfig.STUDY_ID;
    }

    @Override
    protected String getUserAgent()
    {
        return BuildConfig.STUDY_NAME + "/" + BuildConfig.VERSION_CODE;
    }


    @Override
    public Task loadTask(Context context, SchedulesAndTasksModel.TaskScheduleModel task)
    {
        Task newTask = null;
        String taskClassName = task.taskClassName;

        if(!StringUtils.isEmpty(taskClassName))
        {
            Resources res = context.getResources();
            String[] taskClassNamePackages = res.getStringArray(R.array.task_class_name_packages);

            for (String packageName : taskClassNamePackages) {

                try {
                    StringBuilder fullyQualifiedClassNameBuilder = new StringBuilder(packageName).append(".").append(taskClassName);
                    Class taskFactoryClass = Class.forName(fullyQualifiedClassNameBuilder.toString());
                    Constructor constructor = taskFactoryClass.getConstructor();
                    Object factory = constructor.newInstance();
                    if(TaskFactory.class.isAssignableFrom(factory.getClass())) {
                        TaskFactory taskFactory = (TaskFactory)factory;
                        return taskFactory.createTask(context, task, ResourceManager.getInstance(), SampleResourceManager.SURVEY);
                    }
                } catch (ClassNotFoundException e) {
                    //this is handled by calling super
                    e.printStackTrace();
                } catch (Exception e) {
                    //this is a programming error, i.e., we cant construct the object properly
                    e.printStackTrace();
                }
            }


        }

        return super.loadTask(context, task);
    }
}
