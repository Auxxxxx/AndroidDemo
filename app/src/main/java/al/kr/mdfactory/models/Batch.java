package al.kr.mdfactory.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Duration;
import java.time.LocalTime;

public final class Batch {
    private LocalTime startTime;
    private LocalTime finishTime;
    @JsonFormat(pattern="SECONDS")
    private Duration breakDuration;
    @JsonFormat(pattern="SECONDS")
    private Duration duration;

    public Batch() {
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    public Duration getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(Duration breakDuration) {
        this.breakDuration = breakDuration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
