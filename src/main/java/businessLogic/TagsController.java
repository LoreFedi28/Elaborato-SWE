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
                    TagUrgencyLevel tagLevel = new TagUrgencyLevel(tag);
                    this.tagDAO.addTag(tagLevel);
                    return this.tagDAO.getTag(tagLevel.getTag(), tagLevel.getTypeOfTag());
                case "Subject":
                    TagSpecialty tagSpecialty = new TagSpecialty(tag);
                    this.tagDAO.addTag(tagSpecialty);
                    return this.tagDAO.getTag(tagSpecialty.getTag(), tagSpecialty.getTypeOfTag());
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
