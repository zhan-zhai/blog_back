package com.zdz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.domain.entity.Tag;
import com.zdz.service.TagService;
import com.zdz.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author zdz
* @description 针对表【sg_tag(标签)】的数据库操作Service实现
* @createDate 2023-01-09 22:35:49
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




