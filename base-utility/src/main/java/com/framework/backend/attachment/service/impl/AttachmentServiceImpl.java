package com.framework.backend.attachment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.attachment.entity.Attachment;
import com.framework.backend.attachment.mapper.AttachmentMapper;
import com.framework.backend.attachment.service.AttachmentService;
import org.springframework.stereotype.Service;

/**
 * @author fucong
 * @description To do
 * @since 2025/10/10 16:39
 */
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment>
    implements AttachmentService {}
