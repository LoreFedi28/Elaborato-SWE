package businessLogic;

import dao.TagDAO;
import domainModel.Tags.*;

import java.util.logging.Logger;

public class TagsController {
    private static final Logger logger = Logger.getLogger(TagsController.class.getName());

    private final TagDAO tagDAO;

    public TagsController(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public Tag createTag(String tag, String tagType) throws Exception {
        Tag existingTag = this.tagDAO.getTag(tag, tagType);
        if (existingTag != null) {
            return existingTag;
        }

        Tag newTag = switch (tagType) {
            case "Online" -> new TagIsOnline(tag);
            case "UrgencyLevel" -> new TagUrgencyLevel(tag);
            case "Specialty" -> new TagSpecialty(tag);
            case "Zone" -> new TagZone(tag);
            default -> throw new IllegalArgumentException("Tipo di tag non valido: " + tagType);
        };

        this.tagDAO.addTag(newTag);
        logger.info("Creato nuovo tag: " + newTag.getTag() + " (Tipo: " + newTag.getTypeOfTag() + ")");
        return this.tagDAO.getTag(newTag.getTag(), newTag.getTypeOfTag());
    }

    public boolean deleteTag(Tag tagToRemove) throws Exception {
        boolean success = this.tagDAO.removeTag(tagToRemove.getTag(), tagToRemove.getTypeOfTag());
        if (success) {
            logger.info("Tag eliminato: " + tagToRemove.getTag() + " (Tipo: " + tagToRemove.getTypeOfTag() + ")");
        } else {
            logger.warning("Impossibile eliminare il tag: " + tagToRemove.getTag() + " (Tipo: " + tagToRemove.getTypeOfTag() + ")");
        }
        return success;
    }
}