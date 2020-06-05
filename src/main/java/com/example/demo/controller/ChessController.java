package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.constant.CommonResult;
import com.example.demo.constant.ResultCode;
import com.example.demo.model.User;
import com.example.demo.model.dto.MessageObject;
import com.example.demo.model.dto.OnlineUser;
import com.example.demo.model.dto.PositionObject;
import com.example.demo.model.dto.ResultUser;
import com.example.demo.service.UserService;
import com.example.demo.util.RedisUtil;
import com.example.demo.websocket.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Semaphore;

@Slf4j
@RestController
@RequestMapping("/chess")
public class ChessController {

    @Autowired
    private ServerWebSocket webSocket;
    @Autowired
    private RedisUtil redisUtil;

//    private RedisUtil redisUtil=new RedisUtil();
    @Autowired
    private UserService userService;
    //定义资源的总数量,控制匹配时的线程安全
    Semaphore semaphore=new Semaphore(1);
    //向对手发起投降
    @PostMapping("/surrender")
    public void surrenderAction(@RequestParam("enemyId") String enemyId){
        //获得一个随机数
        String identCode= RandomStringUtils.randomAlphanumeric(10);
        PositionObject positionObject=new PositionObject();
        MessageObject message=new MessageObject(1,positionObject,identCode);
        webSocket.AppointSending(enemyId,message);
    }
    //向对手发送自己刚走的此步棋的初始位置和目的位置
    //棋子信息封装在positionsStr中
    @PostMapping("/sendMessage")
    // todo positions 可不可以通过RequestParam获取，还是通过RequestBody获取
    public CommonResult sendMessage(@RequestParam("enemyId") String enemyId, @RequestParam("positions") String positionsStr){
//    public CommonResult sendMessage(@RequestParam("enemyId") String enemyId, @RequestBody PositionObject positions){

            PositionObject positions= JSON.parseObject(positionsStr,PositionObject.class);
            log.info(positions.getType()+" "+positions.getX()+" "+positions.getY());
            //次处应该是显示bug，只要用户传入的参数不为空，则position也不为空
            if(enemyId==null||positions==null){
                //返回客户端错误的状态码
                return CommonResult.failed(ResultCode.VALIDATE_FAILED);
            }
            //获得一个随机数
            String identCode= RandomStringUtils.randomAlphanumeric(10);
            MessageObject message= new MessageObject(0,positions,identCode);
            webSocket.AppointSending(enemyId,message);
            //返回发送成功的状态码
            return CommonResult.success("success");
    }
    //取消匹配
    @PostMapping("/pauseMatching")
    public void pauseMatching(@RequestParam("userId") String userId){
        //把玩家在缓存中的匹配信息移除
        if(userId!=null&&redisUtil.hasKey(userId)){

            redisUtil.remove(userId);
        }

    }
    //匹配正在匹配敌方玩家
    @GetMapping("/matching")
    public CommonResult matchingPlayer(@RequestParam("userId")String userId){

        log.info(userId+"玩家开始匹配");

        //生成在线匹配玩家，并设置其状态信息
        OnlineUser loginUser=new OnlineUser(userId,false,null);

        //将在线匹配玩家存入缓存中
        log.info("id为"+userId);
        log.info("用户"+loginUser);
        redisUtil.set(userId,loginUser);

        //设置玩家信息在缓存中的过期时间，单位SECOND
        redisUtil.expire(userId,80);
        log.info(userId+"已经进入缓存");

        //获取开始匹配时的时间
        long startTime=System.currentTimeMillis();

        //设置匹配时间，单位毫秒
        long goOutTime=30000;

        String result=null;
        OnlineUser currentUser;

        while(true){
            //此处主要时完成取消匹配的功能，若缓存中不存在用户信息
            if(!redisUtil.hasKey(userId)){
                break;
            }

            //如果匹配超过60s将会取消匹配
            if((System.currentTimeMillis()-startTime) >= goOutTime){
                result =null;
                break;
            }
            //todo 当redis缓存中没有数据的时候会不会抛出异常？

//            int availablePermits=semaphore.availablePermits();//可用资源数
//            if(availablePermits>0){
//                System.out.println("抢到资源");
//
//            }
//            else{
//                System.out.println("资源已被占用，稍后再试");
//            }

//                semaphore.acquire(1);  //请求占用一个资源
            if(semaphore.tryAcquire()){
//                System.out.println(userId+"资源正在被使用");

                /**
                 *
                 * 抢到资源后，首先要判断自己是否被上个资源使用者匹配到
                 */
                //通过查询自己的enemyId属性判断自己是否被别人匹配到
                currentUser = (OnlineUser) redisUtil.get(userId);
                /**
                 *
                 * 必须加currentUser!=null
                 * 避免用户取消匹配后，查询到的currentUser为空
                 */
                if(currentUser!=null && currentUser.getEnemyId()!=null){
                    //获得匹配到自己的玩家的id
                    result = currentUser.getEnemyId();
                    semaphore.release(1);//释放一个资源
                    break;
                }
                /**
                 *
                 *如果自己没被上一个资源使用者匹配到，则自己进行匹配
                 */
                String enemyId=redisUtil.getRandomKey();
                log.info("获取到的随机key"+enemyId);
                if(enemyId!=null){
                    OnlineUser onlineUser= (OnlineUser) redisUtil.get(enemyId);
                    /**
                     *
                     * 判断从缓存中获得玩家是否符合逻辑,
                     * 必须加redisUtil.hasKey(userId)条件，以判断用户是否取消了匹配
                     *
                     */
                    if(redisUtil.hasKey(userId) && !onlineUser.isWaring() && !enemyId.equals(userId)){
//               if(redisUtil.hasKey(userId) && !enemyId.equals(userId) && onlineUser.getEnemyId()!=null){

                        result=enemyId;
                        //更新缓存中用户自己和对手的状态信息
                        OnlineUser refreshUser=new OnlineUser(userId,true,enemyId);
                        redisUtil.set(userId,refreshUser);
                        redisUtil.expire(userId,80);

                        OnlineUser enemyUser=new OnlineUser(enemyId,true,userId);
                        redisUtil.set(enemyId,enemyUser);
                        redisUtil.expire(enemyId,80);
                        semaphore.release(1);//释放一个资源
                        break;
                    }
//                       semaphore.release(1);//释放一个资源
                }
                semaphore.release(1);//释放一个资源
                /**
                 *
                 * 执行到这里，说明当前的用户进程没有匹配到对手，让该用户进程暂停1s，
                 * 以把redisUtil等临界资源拿给其它用户进程使用
                 */
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

//                semaphore.release(1);//释放一个资源

        }
//        }

        //清除玩家的匹配状态
        if(redisUtil.hasKey(userId)){
            redisUtil.remove(userId);
        }
        if(result==null){
            log.info("匹配了"+(System.currentTimeMillis() - startTime) +" 毫秒，匹配失败");
            return CommonResult.failed("没有匹配到玩家");
        }else{
            log.info("匹配了"+(System.currentTimeMillis() - startTime) +" 毫秒，匹配成功");
            //从数据中获得对手数据
            User enemyUser = userService.selectUserByOpenid(result);
            int flag=0;
            //根据当前玩家的id和对手的id设置标志flag的值，以判断谁先走棋
//            if(expireTimeOfCurrentUser >= expireTimeOfEnemyUser){
            if(userId.compareTo(result)<0){
//                log.info("当前用户的过期时间"+String.valueOf(expireTimeOfCurrentUser));
//                log.info("对手的过期时间"+String.valueOf(expireTimeOfEnemyUser));
                flag=1;
                ResultUser resultUser=new ResultUser(enemyUser);
                resultUser.setFlag(flag);
                return CommonResult.success(resultUser,"匹配成功");
            }else{
                ResultUser resultUser=new ResultUser(enemyUser);
                resultUser.setFlag(flag);
                return CommonResult.success(resultUser,"匹配成功");
            }

        }
    }
}
