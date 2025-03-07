package businessLogic;

import dao.TagDAO;
import domainModel.Tags.*;

public class TagsController {
    public final TagDAO tagDAO;

    public TagsController(TagDAO tagDAO){
        this.tagDAO = tagDAO;
    }

    public Tag createTag(String tag, String tagType) throws Exception {
        Tag t = this.tagDAO.getTag(tag, tagType);
        if ( t == null ){
            switch (tagType){
                case "IsOnline":
                    TagIsOnline tagIsOnline = new TagIsOnline(tag);
                    this.tagDAO.addTag(tagIsOnline);
                    return this.tagDAO.getTag(tagIsOnline.getTag(), tagIsOnline.getTypeOfTag());
                case "Level":
                    TagLevel tagLevel = new TagLevel(tag);
                    this.tagDAO.addTag(tagLevel);
                    return this.tagDAO.getTag(tagLevel.getTag(), tagLevel.getTypeOfTag());
                case "Subject":
                    TagSubject tagSubject = new TagSubject(tag);
                    this.tagDAO.addTag(tagSubject);
                    return this.tagDAO.getTag(tagSubject.getTag(), tagSubject.getTypeOfTag());
                case "Zone":
                    TagZone tagZone = new TagZone(tag);
                    this.tagDAO.addTag(tagZone);
                    return this.tagDAO.getTag(tagZone.getTag(), tagZone.getTypeOfTag());
                default: throw new IllegalArgumentException("Invalid tag type");
            }
        }

        return this.tagDAO.getTag(t.getTag(), t.getTypeOfTag());
    }

    public boolean deleteTag(Tag tagToRemove) throws Exception{
        return this.tagDAO.removeTag(tagToRemove.getTag(), tagToRemove.getTypeOfTag());
    }
}
