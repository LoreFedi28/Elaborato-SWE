package domainModel;

import domainModel.State.*;
import domainModel.Tags.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private final List<Tag> tags = new ArrayList<>();

    public Visit(int idVisit, String title, String description, LocalDateTime startTime, LocalDateTime endTime, double price, String doctorCF) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (doctorCF == null || doctorCF.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor CF cannot be null or empty.");
        }
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }

        this.idVisit = idVisit;
        this.title = title.trim();
        this.description = description.trim();
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.doctorCF = doctorCF.trim();
        this.state = new Available();
    }

    public int getIdVisit() { return idVisit; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public LocalDateTime getStartTime() { return startTime; }

    public LocalDateTime getEndTime() { return endTime; }

    public double getPrice() { return price; }

    public String getDoctorCF() { return doctorCF; }

    public String getState() { return state.getState(); }

    public String getStateExtraInfo() { return state.getExtraInfo(); }

    public List<Tag> getTags() { return new ArrayList<>(tags); }

    public void setState(State state) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null.");
        }
        this.state = state;
    }

    public void addTag(Tag newTag) {
        if (newTag == null) {
            throw new IllegalArgumentException("Tag cannot be null.");
        }
        this.tags.add(newTag);
    }

    public boolean removeTag(String tagType, String tag) {
        if (!tagType.equals("UrgencyLevel") &&
                !tagType.equals("Specialty") &&
                !tagType.equals("Zone") &&
                !tagType.equals("IsOnline")) {
            throw new IllegalArgumentException("Invalid tagType");
        }
        // removeIf restituisce true se almeno un elemento è stato rimosso, false altrimenti
        return tags.removeIf(t -> t.getTypeOfTag().equals(tagType) && t.getTag().equals(tag));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Visit{")
                .append("title='").append(title).append("', ")
                .append("description='").append(description).append("', ")
                .append("startTime='").append(startTime).append("', ")
                .append("endTime='").append(endTime).append("', ")
                .append("doctor='").append(doctorCF).append("', ")
                .append("price=").append(price);

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Visit)) return false;
        Visit visit = (Visit) obj;
        return idVisit == visit.idVisit &&
                Double.compare(visit.price, price) == 0 &&
                Objects.equals(title, visit.title) &&
                Objects.equals(description, visit.description) &&
                Objects.equals(startTime, visit.startTime) &&
                Objects.equals(endTime, visit.endTime) &&
                Objects.equals(doctorCF, visit.doctorCF) &&
                Objects.equals(state.getState(), visit.state.getState()) &&
                Objects.equals(tags, visit.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVisit, title, description, startTime, endTime, price, doctorCF, state.getState(), tags);
    }
}