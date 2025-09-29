package com.marathicoder.service;

import com.marathicoder.model.Schedule;
import com.marathicoder.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public Schedule addSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedulesByTrainer(String trainerId) {
        return scheduleRepository.findByTrainerId(trainerId);
    }

    public List<Schedule> getSchedulesByStudent(String studentId) {
        return scheduleRepository.findByStudentId(studentId);
    }
    public List<Schedule> getSchedulesByStudentEmail(String studentEmail) {
        return scheduleRepository.findByStudentEmail(studentEmail);
    }

    public List<Schedule> getSchedulesByDate(LocalDate date) {
        return scheduleRepository.findByDate(date);
    }
}
