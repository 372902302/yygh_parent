package com.chen.yygh.controller;

import com.chen.yygh.common.result.Result;
import com.chen.yygh.model.hosp.Schedule;
import com.chen.yygh.service.ScheduleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("getRuleSchedule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getRuleSchedule(@PathVariable long page, @PathVariable long limit, @PathVariable String hoscode,@PathVariable String depcode){
        Map<String, Object> ruleSchedule = scheduleService.getRuleSchedule(page, limit, hoscode, depcode);

        return Result.ok(ruleSchedule);
    }

    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail( @PathVariable String hoscode,
                                     @PathVariable String depcode,
                                     @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }

}
