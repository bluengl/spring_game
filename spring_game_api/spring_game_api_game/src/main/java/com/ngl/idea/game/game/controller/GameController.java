package com.ngl.idea.game.game.controller;

import com.ngl.idea.game.common.core.model.TokenUser;
import com.ngl.idea.game.common.core.model.response.ApiResponse;
import com.ngl.idea.game.core.controller.GameBaseController;
import com.ngl.idea.game.game.dto.GameConfigDTO;
import com.ngl.idea.game.game.dto.GameDTO;
import com.ngl.idea.game.game.dto.GameRecordBatchDTO;
import com.ngl.idea.game.game.dto.GmHigeScoreDTO;
import com.ngl.idea.game.game.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@Api(tags = "游戏服务API")
public class GameController extends GameBaseController {

    private final GameService gameService;

    /**
     * 获取游戏列表
     * 
     * @return 游戏列表
     */
    @PostMapping("/gameList")
    @ApiOperation("获取游戏列表")
    public ApiResponse<List<GameDTO>> getGameList() {
        log.info("获取游戏列表请求");
        List<GameDTO> gameList = gameService.getGameList();
        return ApiResponse.success(gameList);
    }

    /**
     * 获取当前用户在指定游戏中的最高分数
     * 
     * @param gameId  游戏ID
     * @param request HTTP请求
     * @return 用户最高分数信息
     */
    @PostMapping("/getUserHighestScore")
    @ApiOperation("获取当前用户最高分数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏ID", required = true, paramType = "query")
    })
    public ApiResponse<List<GmHigeScoreDTO>> getUserHighestScore(
            @RequestParam String gameId) {
        String userId = getUserId();
        log.info("获取用户最高分数请求，游戏ID：{}，用户ID：{}", gameId, userId);
        List<GmHigeScoreDTO> userScore = gameService.getUserHighestScore(gameId, userId);

        if (userScore == null) {
            return ApiResponse.success(new ArrayList<>());
        }

        return ApiResponse.success(userScore);
    }

    /**
     * 获取游戏排行榜
     * 
     * @param gameId 游戏ID
     * @param limit  返回记录数量限制，默认为10
     * @return 用户排行榜列表
     */
    @PostMapping("/gameRanking")
    @ApiOperation("获取游戏排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏ID", required = true, paramType = "query"),
            @ApiImplicitParam(name = "gameLevel", value = "游戏等级", required = true, paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "返回记录数量限制，默认为10", required = false, paramType = "query", defaultValue = "10")
    })
    public ApiResponse<List<GmHigeScoreDTO>> getGameRanking(
            @RequestParam String gameId,
            @RequestParam Integer gameLevel,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {

        log.info("获取游戏排行榜请求，游戏ID：{}，游戏等级：{}，限制数量：{}", gameId, gameLevel, limit);
        TokenUser userInfo = getUserInfo();
        String userId = null;
        if (userInfo != null) {
            userId = userInfo.getUserId();
        }
        List<GmHigeScoreDTO> rankingList = gameService.getGameRanking(gameId, gameLevel, limit, userId);

        return ApiResponse.success(rankingList);
    }

    /**
     * 保存游戏记录
     * 
     * @param gameId    游戏ID
     * @param gameLevel 游戏等级
     * @param gameScore 游戏分数
     * @return 保存结果
     */
    @PostMapping("/saveGameRecord")
    @ApiOperation("保存游戏记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏ID", required = true, paramType = "query"),
            @ApiImplicitParam(name = "gameLevel", value = "游戏等级", required = true, paramType = "query"),
            @ApiImplicitParam(name = "gameScore", value = "游戏分数", required = true, paramType = "query")
    })
    public ApiResponse<GmHigeScoreDTO> saveGameRecord(
            @RequestParam String gameId,
            @RequestParam Integer gameLevel,
            @RequestParam Integer gameScore) {

        TokenUser userInfo = getUserInfo();
        log.info("保存游戏记录请求，游戏ID：{}，用户ID：{}，游戏等级：{}，游戏分数：{}",
                gameId, userInfo.getUserId(), gameLevel, gameScore);

        GmHigeScoreDTO result = gameService.saveGameRecord(gameId, userInfo, gameLevel, gameScore);

        return ApiResponse.success(result);
    }

    /**
     * 批量更新用户最高分（用于离线游戏记录同步）
     * 
     * @param gameId  游戏ID
     * @param records 游戏记录列表
     * @return 更新后的最高分记录
     */
    @PostMapping("/syncOfflineScores")
    @ApiOperation("批量同步离线游戏最高分")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏ID", required = true, paramType = "query")
    })
    public ApiResponse<List<GmHigeScoreDTO>> batchSaveGameRecords(
            @RequestParam String gameId,
            @RequestBody List<GameRecordBatchDTO> records) {

        TokenUser userInfo = getUserInfo();
        log.info("批量同步离线游戏最高分请求，游戏ID：{}，用户ID：{}，记录数量：{}", 
                gameId, userInfo.getUserId(), records != null ? records.size() : 0);

        List<GmHigeScoreDTO> results = gameService.batchSaveGameRecords(gameId, userInfo, records);

        return ApiResponse.success(results);
    }

    /**
     * 查询游戏配置信息
     * 
     * @param gameId 游戏ID
     * @return 游戏配置列表
     */
    @PostMapping("/getGameConfig")
    @ApiOperation("查询游戏配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏ID", required = true, paramType = "query")
    })
    public ApiResponse<List<GameConfigDTO>> getGameConfig(
            @RequestParam String gameId) {
        
        log.info("查询游戏配置信息请求，游戏ID：{}", gameId);
        List<GameConfigDTO> configList = gameService.getGameConfig(gameId);
        
        return ApiResponse.success(configList);
    }
}