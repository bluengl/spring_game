package com.ngl.idea.game.game.service;

import com.ngl.idea.game.game.dto.GameDTO;
import com.ngl.idea.game.game.dto.GameRecordBatchDTO;
import com.ngl.idea.game.common.core.model.TokenUser;
import com.ngl.idea.game.game.dto.GameConfigDTO;
import com.ngl.idea.game.game.dto.GmHigeScoreDTO;
import java.util.List;

/**
 * 游戏服务接口
 */
public interface GameService {
    
    /**
     * 获取游戏列表，按照ORDER_NUM排序
     * @return 游戏列表
     */
    List<GameDTO> getGameList();
    
    /**
     * 获取当前用户在指定游戏中的最高分数
     * @param gameId 游戏ID
     * @param userId 用户ID
     * @return 用户最高分数信息
     */
    List<GmHigeScoreDTO> getUserHighestScore(String gameId, String userId);
    
    /**
     * 获取游戏排行榜
     * @param gameId 游戏ID
     * @param gameLevel 游戏等级
     * @param limit 返回记录数量限制，默认为10
     * @param userId 用户ID
     * @return 用户排行榜列表
     */
    List<GmHigeScoreDTO> getGameRanking(String gameId, Integer gameLevel, Integer limit, String userId);
    
    /**
     * 保存游戏记录并更新最高分
     * @param gameId 游戏ID
     * @param userInfo 用户信息
     * @param gameLevel 游戏等级
     * @param gameScore 游戏分数
     * @return 最高分信息
     */
    GmHigeScoreDTO saveGameRecord(String gameId, TokenUser userInfo, Integer gameLevel, Integer gameScore);
    
    /**
     * 批量同步离线游戏最高分记录（不保存每条游戏记录）
     * @param gameId 游戏ID
     * @param userInfo 用户信息
     * @param records 离线游戏记录列表
     * @return 各个等级的最高分记录
     */
    List<GmHigeScoreDTO> batchSaveGameRecords(String gameId, TokenUser userInfo, List<GameRecordBatchDTO> records);
    
    /**
     * 获取游戏配置信息
     * @param gameId 游戏ID
     * @return 游戏配置列表
     */
    List<GameConfigDTO> getGameConfig(String gameId);
} 