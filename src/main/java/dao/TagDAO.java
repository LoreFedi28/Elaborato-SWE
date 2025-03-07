package dao;

import domainModel.Tags.*;

import java.util.List;

public interface TagDAO {
    public Tag getTag(String tag, String tagType) throws Exception;

    public List<Tag> getAllTags() throws Exception;

    public void addTag(Tag tag) throws Exception;

    public void attachTag(Integer idVisit, Tag tagToAttach) throws Exception;

    public boolean removeTag(String tag, String tagType) throws Exception;

    public boolean detachTag(Integer idVisit, Tag tagToDetach) throws Exception;

    public List<Tag> getTagsByVisit(Integer idVisit) throws Exception;
}
