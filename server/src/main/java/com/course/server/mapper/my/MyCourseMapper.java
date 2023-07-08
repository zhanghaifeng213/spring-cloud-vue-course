package com.course.server.mapper.my;

import com.course.server.domain.Course;
import com.course.server.domain.CourseExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyCourseMapper {
    int updateTime(@Param("courseId") String courseId);
}