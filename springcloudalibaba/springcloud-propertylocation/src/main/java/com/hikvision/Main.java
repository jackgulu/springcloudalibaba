package com.hikvision;

import com.hikvision.student.Student;
import com.hikvision.student.StudentConfig;
import com.hikvision.teacher.Teacher;
import com.hikvision.teacher.TeacherConfig;
import org.springframework.cloud.bootstrap.BootstrapConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext studentContext=new AnnotationConfigApplicationContext(StudentConfig.class);
        AnnotationConfigApplicationContext teacherContext=new AnnotationConfigApplicationContext(TeacherConfig.class);
        teacherContext.setParent(studentContext);
        Student student = teacherContext.getBean(Student.class);
        /*List<String> s = Arrays.asList(StringUtils.commaDelimitedListToStringArray("ddd,aaa,cc"));
        s.stream().forEach(name->System.out.println(name));*/
        String property = teacherContext.getEnvironment().getProperty("spring.cloud.bootstrap.sources");
        System.out.println("property 为"+property);
        System.out.println("获取student对象的name属性:" + student.getName());
        System.out.println(studentContext.getEnvironment().getProperty("jdbc.user"));
        List<String> names = SpringFactoriesLoader
                .loadFactoryNames(BootstrapConfiguration.class, Class.class.getClassLoader());
        names.stream().forEach(name->System.out.println(name));
    }
}
