package com.zdz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.entity.Link;
import com.zdz.domain.vo.LinkVo;
import com.zdz.service.LinkService;
import com.zdz.mapper.LinkMapper;
import com.zdz.utils.BeanCopyPropertiesUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zdz
* @description 针对表【sg_link(友链)】的数据库操作Service实现
* @createDate 2023-01-09 22:35:49
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService {

    @Override
    public ResponseResult<List<LinkVo>> getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, CommonConstants.LINK_STATUS_PASSED);
        List<Link> links = list(queryWrapper);
        List<LinkVo> linkVos = BeanCopyPropertiesUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }
}




