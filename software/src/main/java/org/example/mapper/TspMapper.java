package org.example.mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.obj.Arrange;
import org.example.obj.Tsp;

public interface TspMapper {

    @Select("select * from TspShow where email=#{email}")
    Tsp[] select(@Param("email") String email);

    @Update("UPDATE TspShow\n" +
            "SET instance_path = #{instance_path}, solution_path = #{solution_path}, time = #{time}\n" +
            "WHERE email = #{email}")
    void update_path(@Param("email") String email,
                     @Param("instance_path") String instance_path,
                     @Param("solution_path") String solution_path,
                     @Param("time") String time);

    @Insert("insert into TspShow (email,instance_path,solution_path) values (#{email},#{instance_path},#{solution_path}, #{time})")
    void add_path(@Param("email") String email,
                  @Param("instance_path") String instance_path,
                  @Param("solution_path") String solution_path,
                  @Param("time") String time);

    @Insert("insert into TspShow (email) values (#{email})")
    void add(@Param("email") String email);
}
