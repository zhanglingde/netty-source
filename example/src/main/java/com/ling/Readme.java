package com.ling;

/**
 * @author zhangling
 * @date 2022/12/23 2:37 PM
 */
public class Readme {

    /**
     * <ol>
     *     <li> netty Hello world {@link com.ling.basic.demo01.HelloServer}</li>
     *     <li> EventLoop 干活的工人 {@link com.ling.basic.demo02.EventLoopServer}</li>
     *     <li> Channel 及 ChannelFuture 优雅关闭 Netty {@link com.ling.basic.demo03.ChannelClient}</li>
     *     <li> Netty 中的 Future 和 Promise {@link com.ling.basic.demo04.NettyPromiseTest}</li>
     *     <li> Handler 和 Pipeline {@link com.ling.basic.demo05.PipelineTest}</li>
     * </ol>
     *
     * ByteBuf
     * <ol>
     *     <li> 创建 ByteBuf {@link com.ling.basic.demo06.ByteBufTest}</li>
     *     <li> 直接内存 与堆内存 {@link com.ling.basic.demo06.ByteBufMemory}</li>
     *     <li> 读取与写入 ByteBuf {@link com.ling.basic.demo06.ByteBufWrite}</li>
     *     <li> 直接内存 与堆内存 {@link }</li>
     * </ol>
     */
    void basic(){}

    /**
     * <ol>
     *     <li> 粘包与半包问题 {@link com.ling.advance.demo01.HelloWorldClient} </li>
     *     <li> 短连接 解决 粘包问题 {@link com.ling.advance.demo01.HelloWorldClient2} </li>
     *     <li> 定长消息解码器 解决 粘包半包问题 {@link com.ling.advance.demo01.HelloWorldClient3} </li>
     *     <li> 使用固定分隔符（换行） 解决 粘包半包问题 {@link com.ling.advance.demo01.HelloWorldClient4} </li>
     *     <li> 预设长度解决 粘包半包问题 {@link com.ling.advance.demo01.TestLengthFieldDecoder} </li>
     * </ol>
     *
     * netty 实现协议
     * <ol>
     *     <li> Netty 实现 Redis协议与Redis 服务器通信  {@link com.ling.advance.demo02.NettyRedisClient}</li>
     * </ol>
     */
    void advance(){}
}
