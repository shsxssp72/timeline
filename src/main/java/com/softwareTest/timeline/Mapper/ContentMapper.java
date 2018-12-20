package com.softwareTest.timeline.Mapper;

import com.softwareTest.timeline.Entity.Content;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ContentMapper {
    int deleteByPrimaryKey(Integer contentId);

    int insert(Content record);

    int insertSelective(Content record);

    Content selectByPrimaryKey(Integer contentId);

    int updateByPrimaryKeySelective(Content record);

    int updateByPrimaryKeyWithBLOBs(Content record);

    int updateByPrimaryKey(Content record);

    List<Content> selectByParams(Map<String,Object> params);

    int getAvailableContentId();

    List<Content> selectByTimeRange(Timestamp start,Timestamp end);

    List<Content> selectByIdRange(Integer startId,Integer endId);
}