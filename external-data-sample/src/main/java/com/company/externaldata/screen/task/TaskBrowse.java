package com.company.externaldata.screen.task;

import com.company.externaldata.app.TaskService;
import io.jmix.core.LoadContext;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.screen.*;
import com.company.externaldata.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("Task_.browse")
@UiDescriptor("task-browse.xml")
@LookupComponent("tasksTable")
public class TaskBrowse extends StandardLookup<Task> {

    @Autowired
    private TaskService taskService;

    @Autowired
    private GroupTable<Task> tasksTable;

    @Autowired
    private CollectionContainer<Task> tasksDc;

    @Install(to = "tasksDl", target = Target.DATA_LOADER)
    private List<Task> tasksDlLoadDelegate(LoadContext<Task> loadContext) {
        return taskService.loadTasks();
    }

    @Subscribe("tasksTable.remove")
    public void onTasksTableRemove(Action.ActionPerformedEvent event) {
        Task selectedTask = tasksTable.getSingleSelected();
        taskService.deleteTask(selectedTask);
        tasksDc.getMutableItems().remove(selectedTask);
    }
}