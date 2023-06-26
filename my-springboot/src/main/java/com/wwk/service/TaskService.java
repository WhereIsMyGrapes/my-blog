package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.Task;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.StatusDTO;
import com.wwk.model.dto.TaskDTO;
import com.wwk.model.dto.TaskRunDTO;
import com.wwk.model.vo.PageResult;
import com.wwk.model.vo.Result;
import com.wwk.model.vo.TaskBackVO;

import java.util.List;

/**
 * 定时任务业务接口
 *
 * @author makejava
 * @since 2023-05-31 00:10:43
 */
public interface TaskService extends IService<Task> {

    /**
     * 查看定时任务列表
     *
     * @param condition 条件
     * @return 定时任务列表
     */
    PageResult<TaskBackVO> listTaskBackVO(ConditionDTO condition);

    /**
     * 添加定时任务
     *
     * @param task 定时任务信息
     */
    void addTask(TaskDTO task);

    /**
     * 修改定时任务
     *
     * @param task 定时任务信息
     * @return {@link Result <>}
     */
    void updateTask(TaskDTO task);

    /**
     * 删除定时任务
     *
     * @param taskIdList 定时任务id集合
     * @return {@link Result<>}
     */
    void deleteTask(List<Integer> taskIdList);

    /**
     * 修改定时任务状态
     *
     * @param taskStatus 定时任务状态
     * @return {@link Result<>}
     */
    void updateTaskStatus(StatusDTO taskStatus);

    /**
     * 执行定时任务
     *
     * @param taskRun 运行定时任务
     * @return {@link Result<>}
     */
    void runTask(TaskRunDTO taskRun);
}

