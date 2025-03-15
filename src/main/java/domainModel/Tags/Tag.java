package domainModel.Tags;

import java.util.Objects;

public class Tag {
    protected final String tag;
    protected final String typeOfTag;

    public Tag(String tag, String typeOfTag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be null or empty.");
        }
        if (typeOfTag == null || typeOfTag.trim().isEmpty()) {
            throw new IllegalArgumentException("Type of tag cannot be null or empty.");
        }

        this.tag = tag.trim();
        this.typeOfTag = typeOfTag.trim();
    }

    public String getTypeOfTag() {
        return this.typeOfTag;
    }

    public String getTag() {
        return this.tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tag other = (Tag) obj;
        return Objects.equals(getTypeOfTag(), other.getTypeOfTag())
                && Objects.equals(getTag(), other.getTag());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTypeOfTag(), getTag());
    }

    @Override
    public String toString() {
        return "Tag{" +
                "typeOfTag='" + typeOfTag + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}