package domainModel.Tags;

import java.util.Objects;

public abstract class Tag {
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
        if (!(obj instanceof Tag)) return false;
        Tag other = (Tag) obj;
        return Objects.equals(tag, other.tag) && Objects.equals(typeOfTag, other.typeOfTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, typeOfTag);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "typeOfTag='" + typeOfTag + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}