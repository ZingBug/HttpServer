package com.zingbug.httpserver.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZingBug on 2019/5/14.
 */
@Getter
@AllArgsConstructor
@Slf4j
public class NettyHttpServer {

    private int inetPort;

    public void init() throws Exception{
        EventLoopGroup parentGroup=new NioEventLoopGroup();
        EventLoopGroup childGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap server=new ServerBootstrap();
            //1、绑定两个线程组分别用来处理客户端通道的accept和读写时间
            server.group(parentGroup,childGroup)
                    //2、绑定服务端通道NioSctpServerChannel
                    .channel(NioServerSocketChannel.class)
                    //3、给读写事件的线程通道绑定handler去真正处理读写
                    //ChannelInitializer初始化通道SocketChannel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //请求解码器
                            ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            //将HTTP消息的多个部分合并成一条完成的HTTP消息
                            ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));
                            //响应转码器
                            ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                            //解决大码流的问题，ChunkedWriteHandler:向客户端发送HTML5文件
                            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            //自定义处理handler
                            ch.pipeline().addLast("http-server",new NettyHttpServerHandler());
                        }
                    });

            //4、监听端口（服务器host和port端口），同步返回
            ChannelFuture future=server.bind(this.inetPort).sync();
            log.info("[http server] opening in "+this.inetPort);
            // 当通道关闭时继续向后执行，这是一个阻塞方法
            future.channel().closeFuture().sync();
        }
        finally {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }
    }

}
