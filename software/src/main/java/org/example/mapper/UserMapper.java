package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.obj.User;

public interface UserMapper {
    @Select("select * from User where email=#{email} and password=#{password}")
    User select(@Param("email") String email, @Param("password") String password);

    @Select("select * from User where email=#{email}")
    User selectByEmail(@Param("email") String email);

    @Insert("insert into User (name, email, password) values (#{name},#{email},#{password})")
    void add(User user);
}
