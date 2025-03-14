package com.ngl.idea.game.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ngl.idea.game.core.entity.GmUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface GmUserMapper extends BaseMapper<GmUser> {
} 