package main.java.domainModel;

import domainModel.State.*;
import domainModel.Tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Objects;

public class Visit {
    private final int idVisit;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double price;
    private final String doctorCF;
    private State state;
    List<Tag> tags = new ArrayList<>();

    public Visit(int idVisit, String title, String description, LocalDateTime startTime, LocalDateTime endTime, double price, String doctorCF) {
        this.idVisit = idVisit;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.doctorCF = doctorCF;

        this.state = new Available();

        if (endTime.equals(startTime) || endTime.isBefore(startTime))
            throw new IllegalArgumentException("End time must be before start time");
    }

    public int getIdVisit() {
        return idVisit;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getPrice() {
        return price;
    }

    public String getDoctorCF() {
        return doctorCF;
    }

    public State getState() {
        return state.getState();
    }

    public String getStateExtraInfo() {
        return state.getStateExtraInfo();
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(State state) {
        this.state = state;
    }

    public void addTag(Tag newTag) {
        this.tags.add(newTag);
    }

    public boolean removeTag(String tagType, String tag) {
        if (!Objects.equals(tagType, "Level") && !Objects.equals(tagType, "Subject") && !Objects.equals(tagType, "Zone") && !Objects.equals(tagType, "IsOnline")) {
            throw new IllegalArgumentException("Invalid tagType");
        }
        boolean removed = false;
        for (Tag t : tags) {
            if (Objects.equals(t.getTypeOfTag(), tagType) && Objects.equals(t.getTag(), tag)) {  // tag esempi: Firenze, Matematica, True, Elementari ecc...
                this.tags.remove(t);
                removed = true;
            }
        }

        return removed;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Visit{");
        stringBuilder.append("title='").append(getTitle()).append('\'');
        stringBuilder.append(", description='").append(getDescription()).append('\'');
        stringBuilder.append(", start time='").append(getStartTime().toString()).append('\'');
        stringBuilder.append(", end time='").append(getEndTime().toString()).append('\'');
        stringBuilder.append(", doctor='").append(getDoctorCF()).append('\'');
        stringBuilder.append(", price='").append(getPrice()).append('\'');

        if (!tags.isEmpty()) {
            stringBuilder.append(", tags=[");
            for (Tag tag : tags) {
                stringBuilder.append(tag.getTypeOfTag()).append(":").append(tag.getTag()).append(", ");
            }
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append("]");
        }

        stringBuilder.append('}');

        return stringBuilder.toString();
    }
}
