package com.company.scheduler.meeting_scheduler.domain.model;

import java.util.List;

public class MeetingRequestDTO {

    public String id;
    public String organizerId;
    public Long durationMinutes;
    public Integer priority; // o String si usas enum
    public Boolean preferEarlier;
    public List<String> participants;
    public List<String> requiredEquipment;
    public List<TimeWindowDTO> timeWindows;

    public static class TimeWindowDTO {
        public String start;
        public String end;
    }
}
