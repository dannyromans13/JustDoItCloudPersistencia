package teccr.justdoitcloud.service;

import org.springframework.stereotype.Service;
import teccr.justdoitcloud.data.Task;
import teccr.justdoitcloud.data.User;
import teccr.justdoitcloud.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByUserId(user.getId());
    }

    public void addTaskToUser(User user, Task task) {
        task.setUserId(user.getId());
        taskRepository.save(task);
    }

    public void advanceStatus(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(); // ✅ taskId, not task

        Task.Status nextStatus = switch (task.getStatus()) {
            case PENDING -> Task.Status.INPROGRESS;
            case INPROGRESS -> Task.Status.DONE;
            case DONE -> Task.Status.DONE;
        };

        Task updated = new Task(
                task.getId(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getDeadline(),
                nextStatus
        );
        updated.setUserId(task.getUserId());

        taskRepository.save(updated);
    }

}