package com.zdz.service.impl;

import ch.qos.logback.core.pattern.util.AlmostAsIsEscapeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdz.constants.CommonConstants;
import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddLinkDto;
import com.zdz.domain.dto.LinkPageDto;
import com.zdz.domain.dto.UpdateLinkDto;
import com.zdz.domain.entity.Link;
import com.zdz.domain.vo.LinkPageVo;
import com.zdz.domain.vo.LinkVo;
import com.zdz.domain.vo.PageVo;
import com.zdz.enums.AppHttpCodeEnum;
import com.zdz.exception.SystemException;
import com.zdz.service.LinkService;
import com.zdz.mapper.LinkMapper;
import com.zdz.utils.BeanCopyPropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public ResponseResult<PageVo> linkPageList(Integer pageNum, Integer pageSize, LinkPageDto linkPageDto) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(linkPageDto.getName()),Link::getName,linkPageDto.getName());
        wrapper.eq(StringUtils.hasText(linkPageDto.getStatus()),Link::getStatus,linkPageDto.getStatus());
        Page<Link> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        List<Link> links = page.getRecords();
        List<LinkPageVo> voList = BeanCopyPropertiesUtils.copyBeanList(links,LinkPageVo.class);

        return ResponseResult.okResult(new PageVo(voList,page.getTotal()));
    }

    @Override
    public ResponseResult<?> addLink(AddLinkDto addLinkDto) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Link::getName,addLinkDto.getName());
        Link one = getOne(wrapper);
        if(one!=null)throw new SystemException(AppHttpCodeEnum.LINK_IS_EXIST);
        Link link = BeanCopyPropertiesUtils.copyBean(addLinkDto,Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<LinkPageVo> getLinkById(Long id) {
        Link link = getById(id);
        LinkPageVo linkPageVo = BeanCopyPropertiesUtils.copyBean(link,LinkPageVo.class);
        return ResponseResult.okResult(linkPageVo);
    }

    @Override
    public ResponseResult<?> updateLink(UpdateLinkDto updateLinkDto) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Link::getName,updateLinkDto.getName());
        Link one = getOne(wrapper);
        if(one!=null)throw new SystemException(AppHttpCodeEnum.LINK_IS_EXIST);
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Link::getId,updateLinkDto.getId());
        updateWrapper.set(Link::getName,updateLinkDto.getName());
        updateWrapper.set(Link::getDescription,updateLinkDto.getDescription());
        updateWrapper.set(Link::getStatus,updateLinkDto.getStatus());
        updateWrapper.set(Link::getLogo,updateLinkDto.getLogo());
        updateWrapper.set(Link::getAddress,updateLinkDto.getAddress());
        update(new Link(),updateWrapper);
        return ResponseResult.okResult();
    }

}




