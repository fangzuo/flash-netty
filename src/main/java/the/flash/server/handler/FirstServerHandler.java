package the.flash.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chao.yu
 * chao.yu@dianping.com
 * @date 2018/08/04 06:21.
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    private static final AtomicInteger onlineNum = new AtomicInteger(0);

    private static final ExecutorService ex =  Executors.newFixedThreadPool(5);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        byteBuf.skipBytes(8);
        //System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        super.channelReadComplete(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        onlineNum.incrementAndGet();
        ex.submit(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("当前在线人数:"+onlineNum.get());
                }
            }
        });
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        onlineNum.decrementAndGet();
        super.channelInactive(ctx);
    }
}
