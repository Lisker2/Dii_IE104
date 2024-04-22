package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.obj.Arrange;

public interface ArrangeHisMapper {
    @Select("select * from ArrangeHis where email=#{email}")
    Arrange[] select(@Param("email") String email);

    @Select("select * from ArrangeHis where time=#{time}")
    Arrange select_by_time(@Param("time") String time);

    @Update("UPDATE ArrangeHis\n" +
            "SET instance_path = #{instance_path}, solution_path = #{solution_path}, time = #{time}\n" +
            "WHERE email = #{email}")
    void update_path(@Param("email") String email,
                     @Param("instance_path") String instance_path,
                     @Param("solution_path") String solution_path,
                     @Param("time") String time);

    @Delete("DELETE FROM ArrangeHis\n" +
            "WHERE time = #{time} AND email = #{email}")
    void delete(@Param("email") String email, @Param("time") String time);

    @Insert("insert into ArrangeHis (email,instance_path,solution_path,time) values (#{email},#{instance_path},#{solution_path}, #{time})")
    void add_path(@Param("email") String email,
                  @Param("instance_path") String instance_path,
                  @Param("solution_path") String solution_path,
                  @Param("time") String time);

    @Insert("insert into ArrangeHis (email) values (#{email})")
    void add(@Param("email") String email);
}
