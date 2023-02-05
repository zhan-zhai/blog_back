package com.zdz.service;

import com.zdz.domain.ResponseResult;
import com.zdz.domain.dto.AddLinkDto;
import com.zdz.domain.dto.LinkPageDto;
import com.zdz.domain.dto.UpdateLinkDto;
import com.zdz.domain.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdz.domain.vo.LinkPageVo;
import com.zdz.domain.vo.LinkVo;
import com.zdz.domain.vo.PageVo;

import java.util.List;

/**
* @author zdz
* @description 针对表【sg_link(友链)】的数据库操作Service
* @createDate 2023-01-09 22:35:49
*/
public interface LinkService extends IService<Link> {

    ResponseResult<List<LinkVo>> getAllLink();

    ResponseResult<PageVo> linkPageList(Integer pageNum, Integer pageSize, LinkPageDto linkPageDto);

    ResponseResult<?> addLink(AddLinkDto addLinkDto);

    ResponseResult<LinkPageVo> getLinkById(Long id);

    ResponseResult<?> updateLink(UpdateLinkDto updateLinkDto);
}
