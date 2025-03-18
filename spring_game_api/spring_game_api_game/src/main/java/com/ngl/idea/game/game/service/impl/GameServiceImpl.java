package com.ngl.idea.game.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ngl.idea.game.common.core.exception.BusinessException;
import com.ngl.idea.game.common.core.model.TokenUser;
import com.ngl.idea.game.core.entity.GmGameConfig;
import com.ngl.idea.game.core.entity.GmGameList;
import com.ngl.idea.game.core.entity.GmGameRecord;
import com.ngl.idea.game.core.entity.GmHigeScore;
import com.ngl.idea.game.core.mapper.GmGameConfigMapper;
import com.ngl.idea.game.core.mapper.GmGameListMapper;
import com.ngl.idea.game.core.mapper.GmGameRecordMapper;
import com.ngl.idea.game.core.mapper.GmHigeScoreMapper;
import com.ngl.idea.game.game.dto.GameConfigDTO;
import com.ngl.idea.game.game.dto.GameDTO;
import com.ngl.idea.game.game.dto.GameRecordBatchDTO;
import com.ngl.idea.game.game.dto.GmHigeScoreDTO;
import com.ngl.idea.game.game.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 游戏服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GmGameListMapper gmGameListMapper;
    private final GmHigeScoreMapper gmHigeScoreMapper;
    private final GmGameRecordMapper gmGameRecordMapper;
    private final GmGameConfigMapper gmGameConfigMapper;

    /**
     * 获取游戏列表，按照ORDER_NUM排序
     * 
     * @return 游戏列表
     */
    @Override
    public List<GameDTO> getGameList() {
        log.info("获取游戏列表");
        // 创建查询条件，查询未销毁的游戏，按ORDER_NUM排序
        LambdaQueryWrapper<GmGameList> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GmGameList::getDestroy, "0")
                .eq(GmGameList::getEffective, "1")
                .orderByAsc(GmGameList::getOrderNum);
        
        // 查询数据库
        List<GmGameList> gameList = gmGameListMapper.selectList(queryWrapper);
        // 将实体转换为DTO
        return gameList.stream().map(game -> {
            GameDTO dto = new GameDTO();
            BeanUtils.copyProperties(game, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取当前用户在指定游戏中的最高分数
     * 
     * @param gameId 游戏ID
     * @param userId 用户ID
     * @return 用户最高分数信息
     */
    @Override
    public List<GmHigeScoreDTO> getUserHighestScore(String gameId, String userId) {
        log.info("获取用户最高分数，游戏ID：{}，用户ID：{}", gameId, userId);

        // 查询用户最高分数记录
        LambdaQueryWrapper<GmHigeScore> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GmHigeScore::getGameId, gameId)
                .eq(GmHigeScore::getUserId, userId)
                .eq(GmHigeScore::getDestroy, "0")
                .orderByAsc(GmHigeScore::getGameLevel);

        List<GmHigeScore> higeScoreList = gmHigeScoreMapper.selectList(queryWrapper);

        if (higeScoreList.isEmpty()) {
            log.info("未找到用户最高分数记录");
            return new ArrayList<>();
        }
        List<GmHigeScoreDTO> scoreDTOList = higeScoreList.stream()
                .map(score -> {
                    GmHigeScoreDTO dto = new GmHigeScoreDTO();
                    BeanUtils.copyProperties(score, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        return scoreDTOList;
    }

    /**
     * 获取用户在指定游戏中的排名
     * 
     * @param gameId 游戏ID
     * @param gameLevel 游戏等级
     * @param userId 用户ID
     * @return 排名（从1开始）
     */
    private GmHigeScoreDTO getUserRank(String gameId, Integer gameLevel, String userId) {
        // 查询用户最高分数
        LambdaQueryWrapper<GmHigeScore> userScoreQuery = Wrappers.lambdaQuery();
        userScoreQuery.eq(GmHigeScore::getGameId, gameId)
                .eq(GmHigeScore::getUserId, userId)
                .eq(GmHigeScore::getGameLevel, gameLevel)
                .eq(GmHigeScore::getDestroy, "0")
                .orderByDesc(GmHigeScore::getGameScore)
                .last("LIMIT 1");
        
        GmHigeScore userScore = gmHigeScoreMapper.selectOne(userScoreQuery);
        if (userScore == null) {
            return null;
        }

        GmGameList gameList = getGameDetail(gameId);
        // 查询比该用户分数高的记录数量
        LambdaQueryWrapper<GmHigeScore> rankQuery = Wrappers.lambdaQuery();
        rankQuery.eq(GmHigeScore::getGameId, gameId)
                .eq(GmHigeScore::getGameLevel, gameLevel)
                .eq(GmHigeScore::getDestroy, "0");
        if("-1".equals(gameList.getGameType())) {
            rankQuery.lt(GmHigeScore::getGameScore, userScore.getGameScore());
        } else {
            rankQuery.gt(GmHigeScore::getGameScore, userScore.getGameScore());
        }
        
        Long higherScores = gmHigeScoreMapper.selectCount(rankQuery);
        GmHigeScoreDTO result = new GmHigeScoreDTO();
        BeanUtils.copyProperties(userScore, result);
        result.setRank(higherScores.intValue() + 1);
        return result;
    }

    /**
     * 获取游戏排行榜
     * 
     * @param gameId 游戏ID
     * @param gameLevel 游戏等级
     * @param limit  返回记录数量限制，默认为10
     * @param userId 用户ID
     * @return 用户排行榜列表
     */
    @Override
    public List<GmHigeScoreDTO> getGameRanking(String gameId, Integer gameLevel, Integer limit, String userId) {
        log.info("获取游戏排行榜，游戏ID：{}，游戏等级：{}，限制数量：{}", gameId, gameLevel, limit);
        
        // 如果limit为空，设置默认值为10
        if (limit == null) {
            limit = 10;
        }
        // 查询游戏信息
        GmGameList gameList = getGameDetail(gameId);
        // 查询分数最高的记录
        LambdaQueryWrapper<GmHigeScore> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GmHigeScore::getGameId, gameId)
                .eq(GmHigeScore::getGameLevel, gameLevel)
                .eq(GmHigeScore::getDestroy, "0");
        // 根据gameType判断使用正序还是倒序
        if("-1".equals(gameList.getGameType())) {
            queryWrapper.orderByAsc(GmHigeScore::getGameScore);
        } else {
            queryWrapper.orderByDesc(GmHigeScore::getGameScore);
        }
        queryWrapper.last("LIMIT " + limit);
        List<GmHigeScore> scoreList = gmHigeScoreMapper.selectList(queryWrapper);
        
        if (scoreList.isEmpty()) {
            log.info("未找到游戏排行榜记录");
            return new ArrayList<>();
        }
        List<GmHigeScoreDTO> resultList = new ArrayList<>();
        for (int i = 0; i < scoreList.size(); i++) {
            GmHigeScoreDTO dto = new GmHigeScoreDTO();
            BeanUtils.copyProperties(scoreList.get(i), dto);
            dto.setRank(i + 1);
            resultList.add(dto);
        }
        if (userId != null) {
            boolean hasUser = false;
            for (GmHigeScore score : scoreList) {
                if (score.getUserId().equals(userId)) {
                    hasUser = true;
                    break;
                }
            }
            if (!hasUser) {
                GmHigeScoreDTO userRank = getUserRank(gameId, gameLevel, userId);
                if (userRank != null) {
                    resultList.add(userRank);
                }
            }
        }
        return resultList;
    }
    
    /**
     * 保存游戏记录并更新最高分
     * 
     * @param gameId    游戏ID
     * @param userInfo  用户信息
     * @param gameLevel 游戏等级
     * @param gameScore 游戏分数
     * @return 保存结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GmHigeScoreDTO saveGameRecord(String gameId, TokenUser userInfo, Integer gameLevel, Integer gameScore) {
        log.info("保存游戏记录，游戏ID：{}，用户ID：{}，游戏等级：{}，游戏分数：{}", gameId, userInfo.getUserId(), gameLevel, gameScore);
        
        try {
            // 1. 保存游戏记录到GM_GAME_RECORD表
            GmGameRecord gameRecord = new GmGameRecord();
            gameRecord.setGameId(gameId);
            gameRecord.setUserId(userInfo.getUserId());
            gameRecord.setUsername(userInfo.getUsername());
            gameRecord.setGameLevel(gameLevel);
            gameRecord.setGameScore(gameScore);
            gameRecord.setCreateTime(LocalDateTime.now());
            gameRecord.setDestroy("0"); // 未销毁
            
            int insertResult = gmGameRecordMapper.insert(gameRecord);
            log.info("游戏记录保存结果：{}", insertResult > 0 ? "成功" : "失败");
            
            // 2. 查询GM_HIGE_SCORE表中该用户在该游戏该等级的最高分记录
            LambdaQueryWrapper<GmHigeScore> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(GmHigeScore::getGameId, gameId)
                    .eq(GmHigeScore::getUserId, userInfo.getUserId())
                    .eq(GmHigeScore::getGameLevel, gameLevel)
                    .eq(GmHigeScore::getDestroy, "0");
            
            GmHigeScore higeScore = gmHigeScoreMapper.selectOne(queryWrapper);
            GmHigeScoreDTO result = new GmHigeScoreDTO();
            
            // 3. 如果没有记录或者记录的分数低于当前分数，则更新最高分记录
            if (higeScore == null) {
                // 插入新记录
                log.info("未找到最高分记录，创建新记录");
                GmHigeScore newScore = new GmHigeScore();
                newScore.setGameId(gameId);
                newScore.setUserId(userInfo.getUserId());
                newScore.setUsername(userInfo.getUsername());
                newScore.setGameLevel(gameLevel);
                newScore.setGameScore(gameScore);
                newScore.setCreateTime(LocalDateTime.now());
                newScore.setDestroy("0"); // 未销毁
                
                gmHigeScoreMapper.insert(newScore);
                BeanUtils.copyProperties(newScore, result);
            } else {
                // 查询游戏详情
                GmGameList gameList = getGameDetail(gameId);
                // 已有记录，比较分数
                int existingScore = higeScore.getGameScore();
                int existingLevel = higeScore.getGameLevel();
                if (gameLevel.equals(existingLevel) && ("-1".equals(gameList.getGameType()) ? gameScore < existingScore : gameScore > existingScore)) {
                    log.info("更新最高分记录，原分数：{}，新分数：{}", existingScore, gameScore);
                    higeScore.setGameScore(gameScore);
                    higeScore.setGameLevel(gameLevel);
                    higeScore.setUpdateTime(LocalDateTime.now());
                    
                    gmHigeScoreMapper.updateById(higeScore);
                    BeanUtils.copyProperties(higeScore, result);
                } else {
                    log.info("当前分数不是最高分，无需更新记录");
                    BeanUtils.copyProperties(gameRecord, result);
                }
            }
            
            return result;
        } catch (Exception e) {
            log.error("保存游戏记录发生错误", e);
            throw new BusinessException("保存游戏记录发生错误"); // 抛出异常以触发事务回滚
        }
    }

    /**
     * 批量更新游戏离线最高分
     * 
     * @param gameId  游戏ID
     * @param userInfo  用户信息
     * @param records 游戏记录列表
     * @return 保存结果，包含各个等级的最高分信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GmHigeScoreDTO> batchSaveGameRecords(String gameId, TokenUser userInfo, List<GameRecordBatchDTO> records) {
        log.info("批量保存用户离线游戏最高分，游戏ID：{}，用户ID：{}，记录数量：{}", gameId, userInfo.getUserId(), records.size());
        
        if (CollectionUtils.isEmpty(records)) {
            log.warn("游戏记录列表为空，不进行保存");
            return new ArrayList<>();
        }
        
        try {
            // 查询游戏详情
            GmGameList gameList = getGameDetail(gameId);
            
            // 使用较低分数为更好成绩的标志
            boolean isLowerBetter = "-1".equals(gameList.getGameType());
            
            List<GmHigeScoreDTO> resultList = new ArrayList<>();
            
            // 遍历处理每条记录
            for (GameRecordBatchDTO record : records) {
                // 查询已有最高分记录
                LambdaQueryWrapper<GmHigeScore> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(GmHigeScore::getGameId, gameId)
                        .eq(GmHigeScore::getUserId, userInfo.getUserId())
                        .eq(GmHigeScore::getGameLevel, record.getGameLevel())
                        .eq(GmHigeScore::getDestroy, "0");
                
                GmHigeScore higeScore = gmHigeScoreMapper.selectOne(queryWrapper);
                GmHigeScoreDTO resultDTO = new GmHigeScoreDTO();
                
                if (higeScore == null) {
                    // 如果没有记录，创建新记录
                    GmHigeScore newScore = new GmHigeScore();
                    newScore.setGameId(gameId);
                    newScore.setUserId(userInfo.getUserId());
                    newScore.setUsername(userInfo.getUsername());
                    newScore.setGameLevel(record.getGameLevel());
                    newScore.setGameScore(record.getGameScore());
                    newScore.setCreateTime(LocalDateTime.now());
                    newScore.setDestroy("0");
                    
                    gmHigeScoreMapper.insert(newScore);
                    BeanUtils.copyProperties(newScore, resultDTO);
                    log.info("创建新的最高分记录，游戏ID：{}，用户ID：{}，等级：{}，分数：{}", 
                            gameId, userInfo.getUserId(), record.getGameLevel(), record.getGameScore());
                } else {
                    // 已有记录，根据游戏类型比较分数
                    int existingScore = higeScore.getGameScore();
                    boolean shouldUpdate = false;
                    
                    if (isLowerBetter) {
                        // 较低分数更好
                        shouldUpdate = record.getGameScore() < existingScore;
                    } else {
                        // 较高分数更好
                        shouldUpdate = record.getGameScore() > existingScore;
                    }
                    
                    if (shouldUpdate) {
                        log.info("更新最高分记录，级别：{}，原分数：{}，新分数：{}", 
                                record.getGameLevel(), existingScore, record.getGameScore());
                        higeScore.setGameScore(record.getGameScore());
                        higeScore.setUpdateTime(LocalDateTime.now());
                        
                        gmHigeScoreMapper.updateById(higeScore);
                        BeanUtils.copyProperties(higeScore, resultDTO);
                    } else {
                        log.info("当前分数不是最高分，无需更新记录");
                        // 使用现有的最高分记录
                        BeanUtils.copyProperties(higeScore, resultDTO);
                    }
                }
                
                resultList.add(resultDTO);
            }
            
            return resultList;
        } catch (Exception e) {
            log.error("批量保存游戏最高分发生错误", e);
            throw new BusinessException("批量保存游戏最高分发生错误");
        }
    }

    /**
     * 获取游戏配置信息
     * 
     * @param gameId 游戏ID
     * @return 游戏配置列表
     */
    @Override
    public List<GameConfigDTO> getGameConfig(String gameId) {
        log.info("获取游戏配置信息，游戏ID：{}", gameId);
        
        // 创建查询条件
        LambdaQueryWrapper<GmGameConfig> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GmGameConfig::getGameId, gameId)
                .eq(GmGameConfig::getDestroy, "0")
                .eq(GmGameConfig::getEffective, "1")
                .orderByAsc(GmGameConfig::getConfigKey);
        
        // 查询数据库
        List<GmGameConfig> configList = gmGameConfigMapper.selectList(queryWrapper);
        
        if (configList.isEmpty()) {
            log.info("未找到游戏配置信息");
            return new ArrayList<>();
        }
        
        // 将实体转换为DTO
        return configList.stream()
                .map(config -> {
                    GameConfigDTO dto = new GameConfigDTO();
                    BeanUtils.copyProperties(config, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询游戏详情
     * 
     * @param gameId 游戏ID
     * @return 游戏详情
     */
    private GmGameList getGameDetail(String gameId) {
        LambdaQueryWrapper<GmGameList> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GmGameList::getGameId, gameId)
                .eq(GmGameList::getDestroy, "0");
        GmGameList gameList = gmGameListMapper.selectOne(queryWrapper);
        if (gameList == null) {
            log.error("游戏不存在");
            throw new BusinessException("游戏不存在");
        }
        return gameList;
    }
} 