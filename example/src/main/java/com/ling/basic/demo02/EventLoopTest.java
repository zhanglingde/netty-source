package com.ling.basic.demo02;

import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * NioEventLoopGroup 为线程池，且可以执行定时任务
 *
 * @author zhangling 2021/5/17 13:30
 */
//@Slf4j
public class EventLoopTest {

    private static final Logger logger = LoggerFactory.getLogger(EventLoopTest.class);


    public static void main(String[] args) {
        // 1. 创建一个事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup(2); // io 事件，普通任务，定时任务
        //DefaultEventLoopGroup group1 = new DefaultEventLoopGroup(); // 普通任务，定时任务

        // 2. 获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        // 3. 执行普通任务
        group.next().execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.debug("ok");
        });

        // 4. 定时任务(固定时间间隔的定时任务)
        group.next().scheduleAtFixedRate(() -> {
            logger.info("delay");
        }, 0, 1, TimeUnit.SECONDS);

        logger.debug("main");

    }
}
