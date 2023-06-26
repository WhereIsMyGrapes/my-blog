package com.wwk.validator;

import com.wwk.model.dto.CommentDTO;
import com.wwk.validator.groups.ArticleTalk;
import com.wwk.validator.groups.Link;
import com.wwk.validator.groups.ParentIdNotNull;
import com.wwk.validator.groups.ParentIdNull;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wwk.enums.CommentTypeEnum.*;

/**
 * 评论分组校验器
 *
 * @author WWK
 * @program: my-springboot
 */
public class CommentProvider implements DefaultGroupSequenceProvider<CommentDTO> {
    @Override
    public List<Class<?>> getValidationGroups(CommentDTO commentDTO) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(CommentDTO.class);
        if (commentDTO != null) {
            if (commentDTO.getCommentType().equals(ARTICLE.getType()) || commentDTO.getCommentType().equals(TALK.getType())) {
                defaultGroupSequence.add(ArticleTalk.class);
            }
            if (commentDTO.getCommentType().equals(LINK.getType())) {
                defaultGroupSequence.add(Link.class);
            }
            if (Objects.isNull(commentDTO.getParentId())) {
                defaultGroupSequence.add(ParentIdNull.class);
            }
            if (Objects.nonNull(commentDTO.getParentId())) {
                defaultGroupSequence.add(ParentIdNotNull.class);
            }
        }
        return defaultGroupSequence;
    }
}
