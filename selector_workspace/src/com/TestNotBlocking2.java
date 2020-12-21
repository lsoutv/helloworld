package com;

import org.junit.Test;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 *  UDP
 */
public class TestNotBlocking2 {

    //发送端
    @Test
    public void send() throws IOException {
        //创建通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        //切换成非阻塞模式
        datagramChannel.configureBlocking(false);
        //创建一个缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //将数据发送到接受端
        buf.put("HelloWorld".getBytes());
        buf.flip();
        datagramChannel.send(buf,new InetSocketAddress("127.0.0.1",9898));
        buf.clear();
        //关闭通道
        datagramChannel.close();
    }

    //接受端
    @Test
    public void receive() throws IOException {
        //创建通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        //切换为非阻塞模式
        datagramChannel.configureBlocking(false);
        //绑定连接,绑定端口号
        datagramChannel.bind(new InetSocketAddress(9898));
        //获取选择器
        Selector selector = Selector.open();
        //将通道注册到选择器上
        datagramChannel.register(selector, SelectionKey.OP_READ);
        //轮询式的获取选择器上“准备就绪的事件”
        while(selector.select()>0){
            //获取当前选择器上所有已经“准备就绪的选择器”（已准备就绪的监听事件）
            Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
            //获取准备就绪的事件
            while(selectionKeys.hasNext()){
                SelectionKey selectionKey = selectionKeys.next();
                //判断具体是什么事件
                if (selectionKey.isReadable()){
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    datagramChannel.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(),0,buf.limit()));
                    buf.clear();
                }
            }
            selectionKeys.remove();
        }
    }
}
