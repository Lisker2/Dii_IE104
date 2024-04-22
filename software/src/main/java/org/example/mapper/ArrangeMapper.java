package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.obj.Arrange;

public interface ArrangeMapper {

    @Select("select * from ArrangeShow where email=#{email}")
    Arrange select(@Param("email") String email);

    @Update("UPDATE ArrangeShow\n" +
            "SET instance_path = #{instance_path}, solution_path = #{solution_path}, time = #{time}\n" +
            "WHERE email = #{email}")
    void update_path(@Param("email") String email,
                     @Param("instance_path") String instance_path,
                     @Param("solution_path") String solution_path,
                     @Param("time") String time);

    @Insert("insert into ArrangeShow (email) values (#{email})")
    void add(@Param("email") String email);

}
