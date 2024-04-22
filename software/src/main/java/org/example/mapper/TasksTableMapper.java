package org.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.obj.Task;

public interface TasksTableMapper {

    @Select("select * from TasksTable limit 1")
    Task select_to_process();

}
