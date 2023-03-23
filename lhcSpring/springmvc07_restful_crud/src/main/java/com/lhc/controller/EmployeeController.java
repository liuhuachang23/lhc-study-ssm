package com.lhc.controller;

import com.lhc.dao.EmployeeDao;
import com.lhc.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/10 21:32
 * @ClassName:
 * @Description: 查询所有的有员工信息 --> /employee --> get
 * 跳转到添加页面 --> /to/add --> get
 * 添加员工信息 --> /employee --> post
 * 跳转到修改页面 --> /employee/1 -->get
 * 修改员工信息 --> /employee --> put
 * 删除员工信息 --> /employee/1 --> delete
 */
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;

    @GetMapping("/employee")
    public String getAllEmployee(Model model) {
        //获取所有员工信息
        Collection<Employee> allEmp = employeeDao.getAll();
        //将所有的员工信息在请求域中共享数据
        model.addAttribute("allEmployee", allEmp);
        //跳转到列表页面
        return "employee_list";
    }

    @PostMapping("/employee")
    public String addEmployee(Employee employee) {
        //保存员工信息
        employeeDao.save(employee);
        //重定向到列表功能 /employee  (服务器查询发送请求，默认为get请求，即被getAllEmployee处理)
        return "redirect:/employee";
    }

    @GetMapping("/employee/{id}")
    public String updateEmployee(@PathVariable("id") Integer id, Model model) {
        //根据id查询员工信息
        Employee employee = employeeDao.get(id);
        //将员工信息共享到请求域中
        model.addAttribute("employee", employee);
        //跳转到employee_update
        return "employee_update";
    }

    @PutMapping(value = "/employee")
    public String updateEmployee(Employee employee) {
        //执行修改
        employeeDao.save(employee);
        //重定向到列表功能 /employee  (服务器查询发送请求，默认为get请求，即被getAllEmployee处理)
        return "redirect:/employee";
    }

    @DeleteMapping (value = "/employee/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id){
        employeeDao.delete(id);
        return "redirect:/employee";
    }

}
